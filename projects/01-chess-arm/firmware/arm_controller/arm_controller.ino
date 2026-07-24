#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>
#include <math.h>
#include <WiFi.h>
#include <WebServer.h>

Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver(0x40);

// ---------------- WIFI WEB CONTROL ----------------
// ESP32 will create its own WiFi network.
// Connect your phone to this WiFi, then open: http://192.168.4.1
const char* AP_SSID = "ChessArm-Calib";
const char* AP_PASS = "12345678";
WebServer server(80);

// ---------------- CHANNELS ----------------
#define BASE        0
#define SHOULDER    1
#define ELBOW       2
#define WRIST       3
#define WRIST_ROT   4
#define GRIPPER     5

// ---------------- CALIBRATION VALUES ----------------

// CH0 Base - positional servo
#define BASE_MIN_ANGLE 0
#define BASE_MAX_ANGLE 180
#define BASE_MIN_PWM   75
#define BASE_MAX_PWM   550
#define BASE_CENTER_ANGLE 90
#define BASE_STEP_ANGLE 10
#define BASE_SMOOTH_STEP 1
#define BASE_SMOOTH_DELAY_MS 45
// CH1 Shoulder
#define SHOULDER_MIN_ANGLE 0
#define SHOULDER_MAX_ANGLE 180
#define SHOULDER_MIN_PWM   75
#define SHOULDER_MAX_PWM   550

// CH2 Elbow
#define ELBOW_MIN_ANGLE 0
#define ELBOW_MAX_ANGLE 180
#define ELBOW_MIN_PWM   600
#define ELBOW_MAX_PWM   75

// CH3 Wrist
#define WRIST_STATIC_PWM 320
int currentWristPulse = WRIST_STATIC_PWM;

// CH4 Wrist rotation
#define WRIST_ROT_STATIC_PWM 290
int currentWristRotPulse = WRIST_ROT_STATIC_PWM;

// CH5 Gripper
#define GRIPPER_MIN_ANGLE 0
#define GRIPPER_MAX_ANGLE 180
#define GRIPPER_OPEN_PWM  150
#define GRIPPER_CLOSE_PWM 500

int lastGripperPulse = GRIPPER_OPEN_PWM;

// ---------------- ARM MEASUREMENTS ----------------
#define L1 10.0
#define L2 14.0
#define L3 10.0
#define L4 13.0

// ---------------- FRAME CALIBRATION ----------------
#define USE_MEASURED_FRAME true
#define X_MEASURED_OFFSET 8.2
#define Z_MEASURED_OFFSET 2.0

// ---------------- IK SETTINGS ----------------
#define USE_X_MIRROR false
#define X_MIRROR_TOTAL_CM 24.0

// ---------------- SAFETY LIMITS ----------------
#define MIN_SAFE_SHOULDER_ANGLE 35
#define MIN_SAFE_ELBOW_ANGLE    45

#define MIN_COMMAND_X 16.0
#define MAX_COMMAND_X 24.0

#define MIN_COMMAND_Z 10.5
#define MAX_COMMAND_Z 22.0

#define MIN_INTERNAL_X 4.0
#define MAX_INTERNAL_X 16.0

#define MIN_INTERNAL_Z 10.0
#define MAX_INTERNAL_Z 26.0

// ---------------- PICK / PLACE SETTINGS ----------------
#define HOVER_Z 18.0
#define PICK_Z  12.5
#define PLACE_Z 12.5

// Gripper tuned to avoid power drop/stall
#define GRIPPER_OPEN_ANGLE  120
#define GRIPPER_CLOSE_ANGLE 60

// ---------------- SIMPLE X POINTS ----------------
// Practical marks on your table
#define POINT_A_X 18.5
#define POINT_B_X 20.0
#define POINT_C_X 22.0

// ---------------- MINI BOARD 3x3 ----------------
// Rows = base angles
// Columns = X positions
//
//        X=18.5   X=20.0   X=22.0
// B60      A1       A2       A3
// B90      B1       B2       B3
// B120     C1       C2       C3

#define ROW_A_BASE_ANGLE 60
#define ROW_B_BASE_ANGLE 90
#define ROW_C_BASE_ANGLE 120

#define COL_1_X 18.5
#define COL_2_X 20.0
#define COL_3_X 22.0

struct BoardPoint {
  int baseAngle;
  float x;
};

// Wrist manual test limits
#define WRIST_MIN_PWM 150
#define WRIST_MAX_PWM 500

// Slower movement reduces current spikes
#define MOTION_STEP_DELAY_MS 60

int currentBaseAngle = BASE_CENTER_ANGLE;
int currentShoulderAngle = 90;
int currentElbowAngle = 90;

// ---------------- GENERAL FUNCTIONS ----------------

int angleToPulse(
  int angle,
  int minAngle,
  int maxAngle,
  int minPulse,
  int maxPulse
) {
  angle = constrain(angle, minAngle, maxAngle);
  return map(angle, minAngle, maxAngle, minPulse, maxPulse);
}

float radToDeg(float rad) {
  return rad * 180.0 / PI;
}

float clampValue(float value, float minValue, float maxValue) {
  if (value < minValue) return minValue;
  if (value > maxValue) return maxValue;
  return value;
}

// ---------------- BASE FUNCTIONS ----------------

void writeBaseAngleFast(int angle) {
  angle = constrain(angle, BASE_MIN_ANGLE, BASE_MAX_ANGLE);

  int pulse = angleToPulse(
    angle,
    BASE_MIN_ANGLE,
    BASE_MAX_ANGLE,
    BASE_MIN_PWM,
    BASE_MAX_PWM
  );

  pwm.setPWM(BASE, 0, pulse);
  currentBaseAngle = angle;

  Serial.print("BASE | Angle = ");
  Serial.print(angle);
  Serial.print(" | PWM = ");
  Serial.println(pulse);
}

void moveBaseAngle(int targetAngle) {
  targetAngle = constrain(targetAngle, BASE_MIN_ANGLE, BASE_MAX_ANGLE);

  Serial.println("BASE SMOOTH MOVE START");
  Serial.print("From = ");
  Serial.print(currentBaseAngle);
  Serial.print(" To = ");
  Serial.println(targetAngle);

  if (currentBaseAngle == targetAngle) {
    writeBaseAngleFast(targetAngle);
    Serial.println("BASE already at target.");
    return;
  }

  int direction = (targetAngle > currentBaseAngle) ? BASE_SMOOTH_STEP : -BASE_SMOOTH_STEP;

  while (currentBaseAngle != targetAngle) {
    int nextAngle = currentBaseAngle + direction;

    if ((direction > 0 && nextAngle > targetAngle) ||
        (direction < 0 && nextAngle < targetAngle)) {
      nextAngle = targetAngle;
    }

    writeBaseAngleFast(nextAngle);
    delay(BASE_SMOOTH_DELAY_MS);
  }

  Serial.println("BASE SMOOTH MOVE DONE");
}

void baseCenter() {
  moveBaseAngle(BASE_CENTER_ANGLE);
  Serial.println("BASE CENTER");
}

void baseStepLeft() {
  moveBaseAngle(currentBaseAngle - BASE_STEP_ANGLE);
  Serial.println("BASE STEP LEFT");
}

void baseStepRight() {
  moveBaseAngle(currentBaseAngle + BASE_STEP_ANGLE);
  Serial.println("BASE STEP RIGHT");
}

void baseHold() {
  writeBaseAngleFast(currentBaseAngle);
  Serial.println("BASE HOLD CURRENT ANGLE");
}

void baseDetach() {
  pwm.setPWM(BASE, 0, 0);
  Serial.println("BASE DETACHED");
}

// ---------------- SHOULDER ----------------

void moveShoulder(int angle) {
  angle = constrain(angle, SHOULDER_MIN_ANGLE, SHOULDER_MAX_ANGLE);

  int pulse = angleToPulse(
    angle,
    SHOULDER_MIN_ANGLE,
    SHOULDER_MAX_ANGLE,
    SHOULDER_MIN_PWM,
    SHOULDER_MAX_PWM
  );

  pwm.setPWM(SHOULDER, 0, pulse);
  currentShoulderAngle = angle;

  Serial.print("SHOULDER | Angle = ");
  Serial.print(angle);
  Serial.print(" | PWM = ");
  Serial.println(pulse);
}

// ---------------- ELBOW ----------------

void moveElbow(int angle) {
  angle = constrain(angle, ELBOW_MIN_ANGLE, ELBOW_MAX_ANGLE);

  int pulse = angleToPulse(
    angle,
    ELBOW_MIN_ANGLE,
    ELBOW_MAX_ANGLE,
    ELBOW_MIN_PWM,
    ELBOW_MAX_PWM
  );

  pwm.setPWM(ELBOW, 0, pulse);
  currentElbowAngle = angle;

  Serial.print("ELBOW | Angle = ");
  Serial.print(angle);
  Serial.print(" | PWM = ");
  Serial.println(pulse);
}

// ---------------- WRIST ----------------

void moveWristStatic() {
  pwm.setPWM(WRIST, 0, WRIST_STATIC_PWM);
  currentWristPulse = WRIST_STATIC_PWM;

  Serial.print("WRIST STATIC | PWM = ");
  Serial.println(WRIST_STATIC_PWM);
}

void holdWristStaticSilent() {
  pwm.setPWM(WRIST, 0, WRIST_STATIC_PWM);
}

void moveWristPWM(int pulse) {
  pulse = constrain(pulse, WRIST_MIN_PWM, WRIST_MAX_PWM);

  pwm.setPWM(WRIST, 0, pulse);
  currentWristPulse = pulse;

  Serial.print("WRIST MANUAL PWM = ");
  Serial.println(pulse);
}

// ---------------- WRIST ROTATION ----------------

void moveWristRotationStatic() {
  pwm.setPWM(WRIST_ROT, 0, WRIST_ROT_STATIC_PWM);
  currentWristRotPulse = WRIST_ROT_STATIC_PWM;

  Serial.print("WRIST ROTATION STATIC | PWM = ");
  Serial.println(WRIST_ROT_STATIC_PWM);
}

void holdWristRotationStaticSilent() {
  pwm.setPWM(WRIST_ROT, 0, WRIST_ROT_STATIC_PWM);
}

void moveWristRotationPWM(int pulse) {
  pulse = constrain(pulse, 150, 500);

  pwm.setPWM(WRIST_ROT, 0, pulse);
  currentWristRotPulse = pulse;

  Serial.print("WRIST ROTATION MANUAL PWM = ");
  Serial.println(pulse);
}

// ---------------- GRIPPER ----------------

void moveGripper(int angle) {
  angle = constrain(angle, GRIPPER_MIN_ANGLE, GRIPPER_MAX_ANGLE);

  int pulse = angleToPulse(
    angle,
    GRIPPER_MIN_ANGLE,
    GRIPPER_MAX_ANGLE,
    GRIPPER_OPEN_PWM,
    GRIPPER_CLOSE_PWM
  );

  lastGripperPulse = pulse;
  pwm.setPWM(GRIPPER, 0, pulse);

  Serial.print("GRIPPER | Angle = ");
  Serial.print(angle);
  Serial.print(" | PWM = ");
  Serial.print(pulse);
  Serial.println(" | HOLDING POSITION");
}

void holdGripper() {
  pwm.setPWM(GRIPPER, 0, lastGripperPulse);

  Serial.print("GRIPPER HOLD | PWM = ");
  Serial.println(lastGripperPulse);
}

void detachGripper() {
  pwm.setPWM(GRIPPER, 0, 0);

  Serial.println("GRIPPER PWM DETACHED");
  Serial.println("Warning: gripper will stop holding torque.");
}

void openGripper() {
  Serial.println("OPEN GRIPPER SLOW");

  int startAngle = GRIPPER_CLOSE_ANGLE;
  int endAngle = GRIPPER_OPEN_ANGLE;

  if (startAngle < endAngle) {
    for (int a = startAngle; a <= endAngle; a += 5) {
      moveGripper(a);
      delay(40);
    }
  } else {
    for (int a = startAngle; a >= endAngle; a -= 5) {
      moveGripper(a);
      delay(40);
    }
  }

  delay(300);
}

void closeGripper() {
  Serial.println("CLOSE GRIPPER SLOW");

  int startAngle = GRIPPER_OPEN_ANGLE;
  int endAngle = GRIPPER_CLOSE_ANGLE;

  if (startAngle < endAngle) {
    for (int a = startAngle; a <= endAngle; a += 5) {
      moveGripper(a);
      delay(40);
    }
  } else {
    for (int a = startAngle; a >= endAngle; a -= 5) {
      moveGripper(a);
      delay(40);
    }
  }

  delay(500);
}

// ---------------- DETACH ALL ----------------

void detachAllServos() {
  for (int ch = 0; ch <= 15; ch++) {
    pwm.setPWM(ch, 0, 0);
  }

  Serial.println("ALL SERVOS DETACHED");
}

// ---------------- SMOOTH SHOULDER + ELBOW MOVE ----------------

void smoothMoveArm(int targetShoulder, int targetElbow) {
  targetShoulder = constrain(targetShoulder, 0, 180);
  targetElbow = constrain(targetElbow, 0, 180);

  int startShoulder = currentShoulderAngle;
  int startElbow = currentElbowAngle;

  int shoulderDiff = targetShoulder - startShoulder;
  int elbowDiff = targetElbow - startElbow;

  int steps = max(abs(shoulderDiff), abs(elbowDiff));

  if (steps == 0) {
    Serial.println("Already at target angles.");
    moveWristStatic();
    moveWristRotationStatic();
    return;
  }

  Serial.println("SMOOTH ARM MOVE START");

  for (int i = 1; i <= steps; i++) {
    int newShoulder = startShoulder + (shoulderDiff * i) / steps;
    int newElbow = startElbow + (elbowDiff * i) / steps;

    int shoulderPulse = angleToPulse(
      newShoulder,
      SHOULDER_MIN_ANGLE,
      SHOULDER_MAX_ANGLE,
      SHOULDER_MIN_PWM,
      SHOULDER_MAX_PWM
    );

    int elbowPulse = angleToPulse(
      newElbow,
      ELBOW_MIN_ANGLE,
      ELBOW_MAX_ANGLE,
      ELBOW_MIN_PWM,
      ELBOW_MAX_PWM
    );

    pwm.setPWM(SHOULDER, 0, shoulderPulse);
    pwm.setPWM(ELBOW, 0, elbowPulse);

    holdWristStaticSilent();
    holdWristRotationStaticSilent();

    currentShoulderAngle = newShoulder;
    currentElbowAngle = newElbow;

    delay(MOTION_STEP_DELAY_MS);
  }

  moveWristStatic();
  moveWristRotationStatic();

  Serial.print("SMOOTH MOVE DONE | Shoulder = ");
  Serial.print(currentShoulderAngle);
  Serial.print(" | Elbow = ");
  Serial.println(currentElbowAngle);
}

// ---------------- HOME POSITION ----------------

void moveHomePosition() {
  Serial.println("SAFE HOME START");

  baseCenter();
  delay(500);

  // Do NOT detach gripper here.
  // Use GD manually only when you want to release gripper torque.

  moveWristStatic();
  delay(300);

  moveWristRotationStatic();
  delay(300);

  smoothMoveArm(90, 90);
  delay(500);

  Serial.println("SAFE HOME DONE");
}

// ---------------- INVERSE KINEMATICS 2D ----------------

bool calculateIK2D(float x, float z, float &shoulderDeg, float &elbowDeg) {
  float targetX = x;
  float zFromShoulder = z - L1;

  float d = sqrt((targetX * targetX) + (zFromShoulder * zFromShoulder));

  Serial.println("===== IK INPUT =====");
  Serial.print("IK X used = ");
  Serial.println(targetX);
  Serial.print("Z from table = ");
  Serial.println(z);
  Serial.print("Z from shoulder = ");
  Serial.println(zFromShoulder);
  Serial.print("Distance d = ");
  Serial.println(d);
  Serial.println("====================");

  if (d > (L2 + L3)) {
    Serial.println("Target unreachable: too far");
    return false;
  }

  if (d < abs(L2 - L3)) {
    Serial.println("Target unreachable: too close");
    return false;
  }

  float cosElbow = ((d * d) - (L2 * L2) - (L3 * L3)) / (2 * L2 * L3);
  cosElbow = clampValue(cosElbow, -1.0, 1.0);

  float elbowRad = acos(cosElbow);

  float shoulderRad =
    atan2(zFromShoulder, targetX) +
    atan2(L3 * sin(elbowRad), L2 + L3 * cos(elbowRad));

  shoulderDeg = radToDeg(shoulderRad);
  elbowDeg = radToDeg(elbowRad);

  Serial.println("===== IK RAW ANGLES =====");
  Serial.print("Shoulder IK raw = ");
  Serial.println(shoulderDeg);
  Serial.print("Elbow IK raw = ");
  Serial.println(elbowDeg);
  Serial.println("=========================");

  return true;
}

// ---------------- IK ANGLE TO SERVO ANGLE ----------------

int shoulderIKToServo(float ikAngle) {
  int servoAngle = (int)ikAngle;
  return constrain(servoAngle, 0, 180);
}

int elbowIKToServo(float ikAngle) {
  int servoAngle = 180 - (int)ikAngle;
  return constrain(servoAngle, 0, 180);
}

// ---------------- X CORRECTION ----------------

float applyXCorrection(float x) {
  if (USE_X_MIRROR) {
    return X_MIRROR_TOTAL_CM - x;
  }

  return x;
}

// ---------------- FRAME CALIBRATION ----------------

void applyMeasuredFrameCalibration(
  float commandX,
  float commandZ,
  float &ikX,
  float &ikZ
) {
  if (USE_MEASURED_FRAME) {
    ikX = commandX - X_MEASURED_OFFSET;
    ikZ = commandZ + Z_MEASURED_OFFSET;
  } else {
    ikX = commandX;
    ikZ = commandZ;
  }
}

// ---------------- MOVE WRIST JOINT TO XZ ----------------

bool moveToXZ(float commandX, float commandZ) {
  Serial.println("!!!!!!!! XZ CALIBRATED WRIST-JOINT MOVE !!!!!!!!");

  if (commandX < MIN_COMMAND_X || commandX > MAX_COMMAND_X) {
    Serial.println("ERROR: Command X out of safe range.");
    Serial.println("Use X between 16 and 24 for now.");
    return false;
  }

  if (commandZ < MIN_COMMAND_Z || commandZ > MAX_COMMAND_Z) {
    Serial.println("ERROR: Command Z out of safe range.");
    Serial.println("Use Z between 10.5 and 22 for now.");
    return false;
  }

  float calibratedX;
  float calibratedZ;

  applyMeasuredFrameCalibration(commandX, commandZ, calibratedX, calibratedZ);

  Serial.println("===== FRAME CALIBRATION =====");
  Serial.print("COMMAND X = ");
  Serial.println(commandX);
  Serial.print("COMMAND Z = ");
  Serial.println(commandZ);
  Serial.print("X OFFSET = ");
  Serial.println(X_MEASURED_OFFSET);
  Serial.print("Z OFFSET = ");
  Serial.println(Z_MEASURED_OFFSET);
  Serial.print("CALIBRATED IK X = ");
  Serial.println(calibratedX);
  Serial.print("CALIBRATED IK Z = ");
  Serial.println(calibratedZ);
  Serial.println("=============================");

  if (calibratedX < MIN_INTERNAL_X || calibratedX > MAX_INTERNAL_X) {
    Serial.println("ERROR: Calibrated IK X out of internal safe range.");
    return false;
  }

  if (calibratedZ < MIN_INTERNAL_Z || calibratedZ > MAX_INTERNAL_Z) {
    Serial.println("ERROR: Calibrated IK Z out of internal safe range.");
    return false;
  }

  float correctedX = applyXCorrection(calibratedX);

  Serial.println("===== X CORRECTION =====");
  Serial.print("CALIBRATED X = ");
  Serial.println(calibratedX);
  Serial.print("REAL IK X USED = ");
  Serial.println(correctedX);
  Serial.println("========================");

  float shoulderIK;
  float elbowIK;

  bool ok = calculateIK2D(correctedX, calibratedZ, shoulderIK, elbowIK);

  if (!ok) {
    Serial.println("IK failed.");
    return false;
  }

  int shoulderServo = shoulderIKToServo(shoulderIK);
  int elbowServo = elbowIKToServo(elbowIK);

  Serial.println("===== IK RESULT =====");
  Serial.print("Shoulder IK angle = ");
  Serial.println(shoulderIK);
  Serial.print("Shoulder servo angle = ");
  Serial.println(shoulderServo);

  Serial.print("Elbow IK angle = ");
  Serial.println(elbowIK);
  Serial.print("Elbow servo angle = ");
  Serial.println(elbowServo);
  Serial.println("=====================");

  if (shoulderServo < MIN_SAFE_SHOULDER_ANGLE || elbowServo < MIN_SAFE_ELBOW_ANGLE) {
    Serial.println("UNSAFE LOW ANGLE. Movement cancelled.");
    Serial.print("Shoulder servo = ");
    Serial.println(shoulderServo);
    Serial.print("Elbow servo = ");
    Serial.println(elbowServo);
    return false;
  }

  baseHold();

  smoothMoveArm(shoulderServo, elbowServo);

  Serial.println("moveToXZ done.");
  return true;
}

// ---------------- PICK / PLACE SEQUENCES ----------------

void pickAtX(float x) {
  Serial.println("===== PICK SEQUENCE START =====");
  Serial.print("Pick X = ");
  Serial.println(x);

  if (!moveToXZ(x, HOVER_Z)) return;
  delay(700);

  openGripper();

  if (!moveToXZ(x, 15.5)) return;
  delay(400);

  if (!moveToXZ(x, 13.5)) return;
  delay(400);

  if (!moveToXZ(x, PICK_Z)) return;
  delay(500);

  closeGripper();

  if (!moveToXZ(x, 13.5)) return;
  delay(400);

  if (!moveToXZ(x, HOVER_Z)) return;
  delay(700);

  Serial.println("===== PICK SEQUENCE DONE =====");
}

void placeAtX(float x) {
  Serial.println("===== PLACE SEQUENCE START =====");
  Serial.print("Place X = ");
  Serial.println(x);

  if (!moveToXZ(x, HOVER_Z)) return;
  delay(700);

  if (!moveToXZ(x, 15.5)) return;
  delay(400);

  if (!moveToXZ(x, 13.5)) return;
  delay(400);

  if (!moveToXZ(x, PLACE_Z)) return;
  delay(500);

  openGripper();

  if (!moveToXZ(x, 13.5)) return;
  delay(400);

  if (!moveToXZ(x, HOVER_Z)) return;
  delay(700);

  Serial.println("===== PLACE SEQUENCE DONE");
}

// ---------------- MOVE PIECE BY X ----------------

void movePieceX(float fromX, float toX) {
  Serial.println("===== MOVE X SEQUENCE START =====");

  Serial.print("From X = ");
  Serial.println(fromX);

  Serial.print("To X = ");
  Serial.println(toX);

  pickAtX(fromX);
  delay(700);

  placeAtX(toX);
  delay(700);

  Serial.println("===== MOVE X SEQUENCE DONE =====");
}

// ---------------- SIMPLE POINT NAME TO X ----------------

bool pointNameToX(String name, float &x) {
  name.trim();
  name.toUpperCase();

  if (name == "A") {
    x = POINT_A_X;
    return true;
  }

  if (name == "B") {
    x = POINT_B_X;
    return true;
  }

  if (name == "C") {
    x = POINT_C_X;
    return true;
  }

  return false;
}

void movePointToPoint(String fromName, String toName) {
  float fromX;
  float toX;

  if (!pointNameToX(fromName, fromX)) {
    Serial.print("Invalid from point: ");
    Serial.println(fromName);
    Serial.println("Use A, B, or C");
    return;
  }

  if (!pointNameToX(toName, toX)) {
    Serial.print("Invalid to point: ");
    Serial.println(toName);
    Serial.println("Use A, B, or C");
    return;
  }

  Serial.println("===== MOVE POINT SEQUENCE =====");
  Serial.print("From point ");
  Serial.print(fromName);
  Serial.print(" = X ");
  Serial.println(fromX);

  Serial.print("To point ");
  Serial.print(toName);
  Serial.print(" = X ");
  Serial.println(toX);

  movePieceX(fromX, toX);
}

// ---------------- MINI BOARD POINT TO BASE + X ----------------

bool boardPointToPosition(String name, BoardPoint &point) {
  name.trim();
  name.toUpperCase();

  if (name.length() != 2) {
    return false;
  }

  char row = name.charAt(0);
  char col = name.charAt(1);

  if (row == 'A') {
    point.baseAngle = ROW_A_BASE_ANGLE;
  }
  else if (row == 'B') {
    point.baseAngle = ROW_B_BASE_ANGLE;
  }
  else if (row == 'C') {
    point.baseAngle = ROW_C_BASE_ANGLE;
  }
  else {
    return false;
  }

  if (col == '1') {
    point.x = COL_1_X;
  }
  else if (col == '2') {
    point.x = COL_2_X;
  }
  else if (col == '3') {
    point.x = COL_3_X;
  }
  else {
    return false;
  }

  return true;
}

void pickAtBoardPoint(BoardPoint point) {
  Serial.println("===== PICK BOARD POINT =====");

  Serial.print("Base angle = ");
  Serial.println(point.baseAngle);

  Serial.print("X = ");
  Serial.println(point.x);

  moveBaseAngle(point.baseAngle);
  delay(700);

  pickAtX(point.x);
}

void placeAtBoardPoint(BoardPoint point) {
  Serial.println("===== PLACE BOARD POINT =====");

  Serial.print("Base angle = ");
  Serial.println(point.baseAngle);

  Serial.print("X = ");
  Serial.println(point.x);

  moveBaseAngle(point.baseAngle);
  delay(700);

  placeAtX(point.x);
}

void moveBoardPointToPoint(String fromName, String toName) {
  BoardPoint fromPoint;
  BoardPoint toPoint;

  if (!boardPointToPosition(fromName, fromPoint)) {
    Serial.print("Invalid from board point: ");
    Serial.println(fromName);
    Serial.println("Use A1,A2,A3,B1,B2,B3,C1,C2,C3");
    return;
  }

  if (!boardPointToPosition(toName, toPoint)) {
    Serial.print("Invalid to board point: ");
    Serial.println(toName);
    Serial.println("Use A1,A2,A3,B1,B2,B3,C1,C2,C3");
    return;
  }

  Serial.println("===== MOVE2 BOARD SEQUENCE START =====");

  Serial.print("From ");
  Serial.print(fromName);
  Serial.print(" | Base = ");
  Serial.print(fromPoint.baseAngle);
  Serial.print(" | X = ");
  Serial.println(fromPoint.x);

  Serial.print("To ");
  Serial.print(toName);
  Serial.print(" | Base = ");
  Serial.print(toPoint.baseAngle);
  Serial.print(" | X = ");
  Serial.println(toPoint.x);

  pickAtBoardPoint(fromPoint);
  delay(700);

  placeAtBoardPoint(toPoint);
  delay(700);

  Serial.println("===== MOVE2 BOARD SEQUENCE DONE =====");
}


// ---------------- WEB PAGE CONTROL ----------------

String currentPoseText() {
  String pose = "B=" + String(currentBaseAngle);
  pose += ", S=" + String(currentShoulderAngle);
  pose += ", E=" + String(currentElbowAngle);
  pose += ", W=" + String(currentWristPulse);
  pose += ", R=" + String(currentWristRotPulse);
  pose += ", G_PWM=" + String(lastGripperPulse);
  return pose;
}

String webPage() {
  String page = R"rawliteral(
<!DOCTYPE html>
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Chess Robot Arm Calibration</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 0; padding: 16px; background: #111827; color: #f9fafb; }
    .card { background: #1f2937; padding: 16px; border-radius: 14px; margin-bottom: 14px; box-shadow: 0 8px 20px rgba(0,0,0,.25); }
    h1 { font-size: 22px; margin: 0 0 12px; }
    h2 { font-size: 18px; margin: 0 0 10px; color: #93c5fd; }
    label { display: flex; justify-content: space-between; margin-bottom: 8px; font-size: 15px; }
    input[type=range] { width: 100%; height: 34px; }
    button { width: 100%; padding: 13px; border: 0; border-radius: 12px; margin: 6px 0; font-size: 16px; font-weight: bold; }
    .primary { background: #2563eb; color: white; }
    .danger { background: #dc2626; color: white; }
    .ok { background: #16a34a; color: white; }
    .small { color: #d1d5db; font-size: 13px; line-height: 1.5; }
    .pose { background: #030712; padding: 12px; border-radius: 10px; font-family: monospace; overflow-x: auto; }
  </style>
</head>
<body>
  <div class="card">
    <h1>Chess Robot Arm Calibration</h1>
    <div class="small">
      Move sliders slowly. Use this page to calibrate B/S/E values for chess squares.
    </div>
  </div>

  <div class="card">
    <h2>Main Servos</h2>

    <label>Base Angle <span id="baseVal">90</span></label>
    <input type="range" min="0" max="180" value="90" id="base" oninput="setServo('base', this.value)">

    <label>Shoulder Angle <span id="shoulderVal">90</span></label>
    <input type="range" min="0" max="180" value="90" id="shoulder" oninput="setServo('shoulder', this.value)">

    <label>Elbow Angle <span id="elbowVal">90</span></label>
    <input type="range" min="0" max="180" value="90" id="elbow" oninput="setServo('elbow', this.value)">

    <label>Gripper Angle <span id="gripperVal">120</span></label>
    <input type="range" min="0" max="180" value="120" id="gripper" oninput="setServo('gripper', this.value)">
  </div>

  <div class="card">
    <h2>Wrist PWM</h2>

    <label>Wrist PWM <span id="wristVal">320</span></label>
    <input type="range" min="150" max="500" value="320" id="wrist" oninput="setServo('wrist', this.value)">

    <label>Wrist Rotation PWM <span id="rotVal">290</span></label>
    <input type="range" min="150" max="500" value="290" id="rot" oninput="setServo('rot', this.value)">
  </div>

  <div class="card">
    <h2>Actions</h2>
    <button class="primary" onclick="action('home')">Home</button>
    <button class="ok" onclick="action('print')">Print Current Pose</button>
    <button class="danger" onclick="action('detach')">Detach All Servos</button>
  </div>

  <div class="card">
    <h2>Current Pose</h2>
    <div class="pose" id="pose">Loading...</div>
    <button class="ok" onclick="copyPose()">Copy Pose Text</button>
  </div>

<script>
let timers = {};

function setText(id, value) {
  document.getElementById(id + 'Val').innerText = value;
}

function setServo(servo, value) {
  const map = { base:'base', shoulder:'shoulder', elbow:'elbow', gripper:'gripper', wrist:'wrist', rot:'rot' };
  setText(map[servo], value);
  clearTimeout(timers[servo]);
  timers[servo] = setTimeout(() => {
    fetch('/set?servo=' + servo + '&value=' + value)
      .then(r => r.text())
      .then(() => refreshStatus());
  }, 90);
}

function action(name) {
  fetch('/' + name)
    .then(r => r.text())
    .then(t => { refreshStatus(); alert(t); });
}

function refreshStatus() {
  fetch('/status')
    .then(r => r.json())
    .then(s => {
      document.getElementById('base').value = s.base;
      document.getElementById('shoulder').value = s.shoulder;
      document.getElementById('elbow').value = s.elbow;
      document.getElementById('wrist').value = s.wrist;
      document.getElementById('rot').value = s.rot;
      document.getElementById('gripper').value = s.gripperAngle;

      document.getElementById('baseVal').innerText = s.base;
      document.getElementById('shoulderVal').innerText = s.shoulder;
      document.getElementById('elbowVal').innerText = s.elbow;
      document.getElementById('wristVal').innerText = s.wrist;
      document.getElementById('rotVal').innerText = s.rot;
      document.getElementById('gripperVal').innerText = s.gripperAngle;
      document.getElementById('pose').innerText = s.pose;
    });
}

function copyPose() {
  const text = document.getElementById('pose').innerText;
  navigator.clipboard.writeText(text);
}

refreshStatus();
setInterval(refreshStatus, 2500);
</script>
</body>
</html>
)rawliteral";
  return page;
}

int gripperAngleFromPulse() {
  return map(lastGripperPulse, GRIPPER_OPEN_PWM, GRIPPER_CLOSE_PWM, GRIPPER_MIN_ANGLE, GRIPPER_MAX_ANGLE);
}

void handleRoot() {
  server.send(200, "text/html", webPage());
}

void handleStatus() {
  String json = "{";
  json += "\"base\":" + String(currentBaseAngle) + ",";
  json += "\"shoulder\":" + String(currentShoulderAngle) + ",";
  json += "\"elbow\":" + String(currentElbowAngle) + ",";
  json += "\"wrist\":" + String(currentWristPulse) + ",";
  json += "\"rot\":" + String(currentWristRotPulse) + ",";
  json += "\"gripperPulse\":" + String(lastGripperPulse) + ",";
  json += "\"gripperAngle\":" + String(gripperAngleFromPulse()) + ",";
  json += "\"pose\":\"" + currentPoseText() + "\"";
  json += "}";
  server.send(200, "application/json", json);
}

void handleSetServo() {
  if (!server.hasArg("servo") || !server.hasArg("value")) {
    server.send(400, "text/plain", "Missing servo or value");
    return;
  }

  String servo = server.arg("servo");
  int value = server.arg("value").toInt();

  if (servo == "base") {
    writeBaseAngleFast(value);
  }
  else if (servo == "shoulder") {
    moveShoulder(value);
  }
  else if (servo == "elbow") {
    moveElbow(value);
  }
  else if (servo == "wrist") {
    moveWristPWM(value);
  }
  else if (servo == "rot") {
    moveWristRotationPWM(value);
  }
  else if (servo == "gripper") {
    moveGripper(value);
  }
  else {
    server.send(400, "text/plain", "Unknown servo");
    return;
  }

  Serial.print("WEB SET | ");
  Serial.print(servo);
  Serial.print(" = ");
  Serial.println(value);
  server.send(200, "text/plain", "OK " + servo + " = " + String(value));
}

void handleHomeWeb() {
  moveHomePosition();
  server.send(200, "text/plain", "Home done");
}

void handleDetachWeb() {
  detachAllServos();
  server.send(200, "text/plain", "All servos detached");
}

void handlePrintPoseWeb() {
  String pose = currentPoseText();
  Serial.println("===== CURRENT CALIBRATION POSE =====");
  Serial.println(pose);
  Serial.println("====================================");
  server.send(200, "text/plain", pose);
}

void setupWebServer() {
  WiFi.mode(WIFI_AP);
  WiFi.softAP(AP_SSID, AP_PASS);

  IPAddress ip = WiFi.softAPIP();
  Serial.println("===== WIFI AP STARTED =====");
  Serial.print("WiFi Name: ");
  Serial.println(AP_SSID);
  Serial.print("Password: ");
  Serial.println(AP_PASS);
  Serial.print("Open in browser: http://");
  Serial.println(ip);
  Serial.println("===========================");

  server.on("/", handleRoot);
  server.on("/status", handleStatus);
  server.on("/set", handleSetServo);
  server.on("/home", handleHomeWeb);
  server.on("/detach", handleDetachWeb);
  server.on("/print", handlePrintPoseWeb);

  server.begin();
  Serial.println("Web server started.");
}

// ---------------- SETUP ----------------

void setup() {
  Serial.begin(115200);

  setupWebServer();

  Wire.begin(21, 22);
  Wire.setClock(10000);

  pwm.begin();
  pwm.setPWMFreq(50);

  delay(1000);

  // IMPORTANT:
  // Do NOT detach all servos on boot.
  // If ESP32 resets from power drop, detachAllServos makes the arm fall.
  // detachAllServos();
  // delay(500);

  baseCenter();
  moveWristStatic();
  moveWristRotationStatic();

  Serial.println("=== CHESS ROBOT ARM - MINI BOARD 3x3 READY ===");

  Serial.print("BASE_CENTER_ANGLE = ");
  Serial.println(BASE_CENTER_ANGLE);

  Serial.print("USE_X_MIRROR = ");
  Serial.println(USE_X_MIRROR ? "true" : "false");

  Serial.print("USE_MEASURED_FRAME = ");
  Serial.println(USE_MEASURED_FRAME ? "true" : "false");

  Serial.print("X_MEASURED_OFFSET = ");
  Serial.println(X_MEASURED_OFFSET);

  Serial.print("Z_MEASURED_OFFSET = ");
  Serial.println(Z_MEASURED_OFFSET);

  Serial.print("WRIST_STATIC_PWM = ");
  Serial.println(WRIST_STATIC_PWM);

  Serial.print("HOVER_Z = ");
  Serial.println(HOVER_Z);

  Serial.print("PICK_Z = ");
  Serial.println(PICK_Z);

  Serial.print("PLACE_Z = ");
  Serial.println(PLACE_Z);

  Serial.print("MOTION_STEP_DELAY_MS = ");
  Serial.println(MOTION_STEP_DELAY_MS);

  Serial.println("");
  Serial.println("Commands:");
  Serial.println("WEB          -> connect phone to WiFi ChessArm-Calib / 12345678 and open http://192.168.4.1");
  Serial.println("B angle      -> base angle, example B90");
  Serial.println("BL           -> base step left");
  Serial.println("BR           -> base step right");
  Serial.println("BS           -> base hold current angle");
  Serial.println("BD           -> base detach");
  Serial.println("S angle      -> shoulder");
  Serial.println("E angle      -> elbow");
  Serial.println("G angle      -> gripper and HOLD");
  Serial.println("GH           -> hold last gripper position again");
  Serial.println("GD           -> detach gripper PWM manually");
  Serial.println("W            -> wrist static");
  Serial.println("WP pwm       -> wrist manual PWM, example WP320");
  Serial.println("R            -> wrist rotation static");
  Serial.println("RP pwm       -> wrist rotation manual PWM, example RP300");
  Serial.println("H            -> safe home position");
  Serial.println("DALL         -> detach all servos manually");
  Serial.println("XZ x,z       -> move wrist joint");
  Serial.println("PICKX x      -> pick at X");
  Serial.println("PLACEX x     -> place at X");
  Serial.println("MOVEX a,b    -> pick at X=a and place at X=b");
  Serial.println("MOVE A,C     -> old 1D move from point A to point C");
  Serial.println("MOVE2 A1,C3  -> mini-board 3x3 move");
  Serial.println("");
  Serial.println("Mini board map:");
  Serial.println("A1/A2/A3 = Base 60,  X 18.5 / 20 / 22");
  Serial.println("B1/B2/B3 = Base 90,  X 18.5 / 20 / 22");
  Serial.println("C1/C2/C3 = Base 120, X 18.5 / 20 / 22");

  Serial.println("");
  Serial.println("Test examples:");
  Serial.println("H");
  Serial.println("MOVE2 B1,B3");
  Serial.println("MOVE2 A1,A3");
  Serial.println("MOVE2 C1,C3");
  Serial.println("MOVE2 A1,C3");
  Serial.println("MOVE2 C3,A1");
}

// ---------------- LOOP ----------------

void loop() {
  server.handleClient();

  if (!Serial.available()) return;

  String command = Serial.readStringUntil('\n');
  command.trim();
  command.toUpperCase();

  if (command == "B+") {
    moveBaseAngle(currentBaseAngle + 1);
  }

  else if (command == "B-") {
    moveBaseAngle(currentBaseAngle - 1);
  }

  else if (command == "S+") {
    smoothMoveArm(currentShoulderAngle + 1, currentElbowAngle);
  }

  else if (command == "S-") {
    smoothMoveArm(currentShoulderAngle - 1, currentElbowAngle);
  }

  else if (command == "E+") {
    smoothMoveArm(currentShoulderAngle, currentElbowAngle + 1);
  }

  else if (command == "E-") {
    smoothMoveArm(currentShoulderAngle, currentElbowAngle - 1);
  }

  else if (command == "P") {
    Serial.println("===== CURRENT CALIBRATION POSE =====");
    Serial.println(currentPoseText());
    Serial.println("====================================");
  }

  else if (command.startsWith("MOVEX")) {
    command.remove(0, 5);
    command.trim();

    int commaIndex = command.indexOf(',');

    if (commaIndex == -1) {
      Serial.println("Invalid format. Use: MOVEX 18.5,22");
      return;
    }

    float fromX = command.substring(0, commaIndex).toFloat();
    float toX = command.substring(commaIndex + 1).toFloat();

    if (fromX <= 0 || toX <= 0) {
      Serial.println("Invalid values. Use: MOVEX 18.5,22");
      return;
    }

    movePieceX(fromX, toX);
  }

  else if (command.startsWith("MOVE2")) {
    command.remove(0, 5);
    command.trim();

    int commaIndex = command.indexOf(',');

    if (commaIndex == -1) {
      Serial.println("Invalid format. Use: MOVE2 A1,C3");
      return;
    }

    String fromName = command.substring(0, commaIndex);
    String toName = command.substring(commaIndex + 1);

    fromName.trim();
    toName.trim();

    moveBoardPointToPoint(fromName, toName);
  }

  else if (command.startsWith("MOVE")) {
    command.remove(0, 4);
    command.trim();

    int commaIndex = command.indexOf(',');

    if (commaIndex == -1) {
      Serial.println("Invalid format. Use: MOVE A,C");
      return;
    }

    String fromName = command.substring(0, commaIndex);
    String toName = command.substring(commaIndex + 1);

    fromName.trim();
    toName.trim();

    movePointToPoint(fromName, toName);
  }

  else if (command.startsWith("PICKX")) {
    command.remove(0, 5);
    command.trim();

    float x = command.toFloat();

    if (x <= 0) {
      Serial.println("Invalid format. Use: PICKX 18.5");
      return;
    }

    pickAtX(x);
  }

  else if (command.startsWith("PLACEX")) {
    command.remove(0, 6);
    command.trim();

    float x = command.toFloat();

    if (x <= 0) {
      Serial.println("Invalid format. Use: PLACEX 22");
      return;
    }

    placeAtX(x);
  }

  else if (command.startsWith("XZ")) {
    command.remove(0, 2);
    command.trim();

    int commaIndex = command.indexOf(',');

    if (commaIndex == -1) {
      Serial.println("Invalid format. Use: XZ x,z");
      Serial.println("Example: XZ 18.5,11.5");
      return;
    }

    float x = command.substring(0, commaIndex).toFloat();
    float z = command.substring(commaIndex + 1).toFloat();

    moveToXZ(x, z);
  }

  else if (command.startsWith("WP")) {
    int pulse = command.substring(2).toInt();
    moveWristPWM(pulse);
  }

  else if (command.startsWith("RP")) {
    int pulse = command.substring(2).toInt();
    moveWristRotationPWM(pulse);
  }

  else if (command.startsWith("B")) {
    if (command == "BL") {
      baseStepLeft();
    }
    else if (command == "BR") {
      baseStepRight();
    }
    else if (command == "BS") {
      baseHold();
    }
    else if (command == "BD") {
      baseDetach();
    }
    else {
      int angle = command.substring(1).toInt();
      moveBaseAngle(angle);
    }
  }

  else if (command.startsWith("S")) {
    int angle = command.substring(1).toInt();
    moveShoulder(angle);
  }

  else if (command.startsWith("E")) {
    int angle = command.substring(1).toInt();
    moveElbow(angle);
  }

  else if (command == "GD") {
    detachGripper();
  }

  else if (command == "GH") {
    holdGripper();
  }

  else if (command.startsWith("G")) {
    int angle = command.substring(1).toInt();
    moveGripper(angle);
  }

  else if (command == "W") {
    moveWristStatic();
  }

  else if (command == "R") {
    moveWristRotationStatic();
  }

  else if (command == "H") {
    moveHomePosition();
  }

  else if (command == "DALL") {
    detachAllServos();
  }

  else {
    Serial.println("Invalid command.");
    Serial.println("Examples:");
    Serial.println("H");
    Serial.println("B90");
    Serial.println("B60");
    Serial.println("B120");
    Serial.println("MOVE2 B1,B3");
    Serial.println("MOVE2 A1,C3");
    Serial.println("MOVE2 C3,A1");
    Serial.println("MOVEX 18.5,22");
    Serial.println("MOVE A,C");
    Serial.println("PICKX 18.5");
    Serial.println("PLACEX 22");
    Serial.println("XZ 18.5,11.5");
    Serial.println("WP320");
    Serial.println("RP300");
    Serial.println("G20");
    Serial.println("G120");
    Serial.println("GD");
    Serial.println("DALL");
  }
}