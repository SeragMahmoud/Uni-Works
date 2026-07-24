"""
Validates the SQUARE_LUT in config.py without any hardware.
Run: python test_config.py
"""
import sys
from config import SQUARE_LUT, BASE_MIN, BASE_MAX, HOME_ANGLES

SERVO_MIN = 0
SERVO_MAX = 180
FILES = "abcdefgh"
RANKS = range(1, 9)
ALL_SQUARES = [f"{f}{r}" for f in FILES for r in RANKS]

PASS = "\033[92mPASS\033[0m"
FAIL = "\033[91mFAIL\033[0m"

errors = []

def check(condition, message):
    if not condition:
        errors.append(message)
        print(f"  {FAIL}  {message}")
    else:
        print(f"  {PASS}  {message}")


# ── 1. All 64 squares present ────────────────────────────────────────────────
print("\n[1] Square coverage")
missing = [sq for sq in ALL_SQUARES if sq not in SQUARE_LUT]
check(len(missing) == 0,
      f"All 64 squares present" if not missing else f"Missing: {missing}")
check(len(SQUARE_LUT) == 64,
      f"LUT has exactly 64 entries (found {len(SQUARE_LUT)})")


# ── 2. Each entry has 'hover' and 'ground' with 3 ints ──────────────────────
print("\n[2] Entry structure")
bad_structure = []
for sq in ALL_SQUARES:
    entry = SQUARE_LUT.get(sq, {})
    for key in ("hover", "ground"):
        val = entry.get(key)
        if not (isinstance(val, (tuple, list)) and len(val) == 3):
            bad_structure.append(f"{sq}.{key}")
check(len(bad_structure) == 0,
      f"All entries have hover/ground (B,S,E)" if not bad_structure
      else f"Malformed: {bad_structure}")


# ── 3. All angles within servo range 0-180 ──────────────────────────────────
print("\n[3] Angle range (0–180°)")
out_of_range = []
for sq in ALL_SQUARES:
    entry = SQUARE_LUT.get(sq, {})
    for key in ("hover", "ground"):
        angles = entry.get(key, ())
        for i, a in enumerate(angles):
            if not (SERVO_MIN <= a <= SERVO_MAX):
                out_of_range.append(f"{sq}.{key}[{i}]={a}")
check(len(out_of_range) == 0,
      f"All angles in [0, 180]" if not out_of_range
      else f"Out-of-range: {out_of_range}")


# ── 4. Hover is always higher than ground (shoulder lower, elbow higher) ─────
print("\n[4] Hover vs Ground direction")
wrong_hover = []
for sq in ALL_SQUARES:
    entry = SQUARE_LUT.get(sq, {})
    hov = entry.get("hover", ())
    gnd = entry.get("ground", ())
    if len(hov) == 3 and len(gnd) == 3:
        _, s_hov, e_hov = hov
        _, s_gnd, e_gnd = gnd
        if not (s_hov < s_gnd):
            wrong_hover.append(f"{sq}: hover_S({s_hov}) >= ground_S({s_gnd})")
        if not (e_hov > e_gnd):
            wrong_hover.append(f"{sq}: hover_E({e_hov}) <= ground_E({e_gnd})")
check(len(wrong_hover) == 0,
      "Hover shoulder < Ground shoulder and Hover elbow > Ground elbow"
      if not wrong_hover else f"Direction errors: {wrong_hover}")


# ── 5. Base angle monotonically increases a→h (per rank) ────────────────────
print("\n[5] Base angle progression (a->h increases)")
bad_base = []
for rank in RANKS:
    bases = [SQUARE_LUT[f"{f}{rank}"]["ground"][0] for f in FILES]
    for i in range(len(bases) - 1):
        if bases[i] >= bases[i + 1]:
            bad_base.append(f"rank{rank}: {FILES[i]}={bases[i]} >= {FILES[i+1]}={bases[i+1]}")
check(len(bad_base) == 0,
      "Base angles increase from file a to h"
      if not bad_base else f"Non-monotonic base: {bad_base}")


# ── 6. Same file → same base angle across all ranks ─────────────────────────
print("\n[6] Base angle consistency (same file = same base)")
bad_file = []
for f in FILES:
    bases = [SQUARE_LUT[f"{f}{r}"]["ground"][0] for r in RANKS]
    if len(set(bases)) != 1:
        bad_file.append(f"file {f}: {bases}")
check(len(bad_file) == 0,
      "Base angle is constant per file" if not bad_file
      else f"Inconsistent: {bad_file}")


# ── 7. Home angles in range ──────────────────────────────────────────────────
print("\n[7] Home angles")
for joint, angle in HOME_ANGLES.items():
    check(SERVO_MIN <= angle <= SERVO_MAX,
          f"HOME {joint}={angle} in range")


# ── 8. Print sample squares for visual inspection ────────────────────────────
print("\n[8] Sample values (a1, d4, h8)")
for sq in ("a1", "d4", "h8"):
    entry = SQUARE_LUT[sq]
    print(f"  {sq}: hover={entry['hover']}  ground={entry['ground']}")


# ── Result ───────────────────────────────────────────────────────────────────
print()
if errors:
    print(f"RESULT: {len(errors)} check(s) FAILED")
    sys.exit(1)
else:
    total = 64 * 2 * 3  # squares × poses × axes
    print(f"RESULT: All checks passed. {total} angle values validated across 64 squares.")
    sys.exit(0)
