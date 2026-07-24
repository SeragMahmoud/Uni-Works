<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Booking Service</title>
    <link rel="stylesheet" href="css/btn.css">
    <link rel="stylesheet" href="css/index.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }
        .container {
            width: 50%;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
            color: #333;
        }
        .input-container {
            margin: 20px 0;
        }
        input[type="date"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border-radius: 4px;
            border: 1px solid #ccc;
        }
        table {
            width: 100%;
            margin-top: 20px;
            border-collapse: collapse;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 10px;
            text-align: center;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
    <header>
        <div class="navbar">
            <div class="logo"></div>
            <nav>
                <div class="ml-auto">
                    <button class="btn6" type="button" onclick="window.location.href='index.jsp';">
                        <strong>Home</strong>
                    </button>
                </div>
            </nav>
        </div>
    </header>

    <div class="container">
        <h1>Service</h1>

        <h2>Available Schedule</h2>
        <table id="scheduleTable">
            <thead>
                <tr>
                    <th>Start Time</th>
                    <th>End Time</th>
                </tr>
            </thead>
            <tbody id="scheduleBody">
                <!-- Schedule data will be populated here -->
            </tbody>
        </table>

        <div class="input-container">
            <label for="bookingDate">Select Date</label>
            <input type="date" id="bookingDate" name="bookingDate" required>
        </div>

        <button class="btn6" id="submitBookingBtn">
            <strong>Submit Booking</strong>
        </button>

        <table id="resultTable" style="display:none;">
            <thead>
                <tr>
                    <th>Booking ID</th>
                    <th>Customer ID</th>
                    <th>Service ID</th>
                    <th>Date</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <tr id="resultRow">
                    <td id="bookingID"></td>
                    <td id="customerID"></td>
                    <td id="serviceID"></td>
                    <td id="bookingDateDisplay"></td>
                    <td id="status"></td>
                </tr>
            </tbody>
        </table>
    </div>

    <script>
     window.onload = function() {
    fetch('/BookingController?action=fetchSchedule', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    })
    .then(response => response.json())  // Expecting a JSON response
    .then(data => {
        const scheduleBody = document.getElementById('scheduleBody');
        data.forEach(schedule => {
            const row = document.createElement('tr');
            row.innerHTML = `<td>${schedule.startTime}</td><td>${schedule.endTime}</td>`;
            scheduleBody.appendChild(row);
        });
    })
    .catch(error => console.error('Error fetching schedule:', error));
};


       document.getElementById('submitBookingBtn').addEventListener('click', function() {
    const bookingDate = document.getElementById('bookingDateInput').value; // Assuming you have an input element with ID 'bookingDateInput'

if (bookingDate) {
    fetch('/BookingController?action=createBooking', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ bookingDate: bookingDate }),
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.text();
    })
    .then(data => {
        if (data.includes('Booking created successfully')) {
            alert(data);
            // Optionally update the UI with booking details
        } else {
            alert(`Error: ${data}`);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to create booking due to a network or server error.');
    });
} else {
    alert('Please select a date.');
}


    </script>
</body>
</html>
