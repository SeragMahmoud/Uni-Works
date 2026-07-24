<%-- 
    Document   : bookingadmin
    Created on : Dec 13, 2024, 1:57:41 PM
    Author     : kaled
--%>
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
            <div class="logo">
             
            </div>
            <nav>
                <div class="ml-auto">
                    <button class="btn6" type="button" onclick="window.location.href='adminpage.jsp';">
                        <strong>home</strong>
                        <div id="container-stars">
                            <div id="stars"></div>
                        </div>
        
                        <div id="glow">
                            <div class="circle"></div>
                            <div class="circle"></div>
                        </div>
                    </button>
        
                </div>
              

            </nav>
        </div>
    </header>

    <div class="container">
        <h1>Service</h1>
        
<!--         Date Input 
        <div class="input-container">
            <label for="bookingDate">Select Date</label>
            <input type="date" id="bookingDate" name="bookingDate" required>
        </div>

         Submit Button 

        <button class="btn6" id="submitBookingBtn">
            
            <strong>Submit Booking</strong>
        
                  
        </button>
     -->

        <!-- Table to display booking result -->
<!--        <table id="resultTable" style="display:none;">
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
        </table>-->
    </div>