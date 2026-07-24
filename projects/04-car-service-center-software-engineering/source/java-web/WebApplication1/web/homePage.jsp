<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ar">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Car Services</title>
    <style>
        /* Embedded CSS */
        @import url('https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,300;0,400;0,500;1,500&display=swap');
        
        /* Reset some default styles */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        /* Body styling with background image */
        body {
            font-family: Arial, sans-serif;
            background-image: url('img/banzema.jpg');
            background-size: cover;
            background-position: center;
            background-color: #f0f0f0; /* Fallback color */
            background-attachment: fixed;
            color: #333;
        }

        /* Navbar styling */
        .navbar {
            display: flex;
            justify-content: left;
            align-items: center;
            padding: 20px;
            background-color: #2c3e50;
            color: #fff;
        }
        .navbar .logo {
            flex: 1;
            text-align: left;
        }
        .navbar .logo h1 {
            font-size: 24px;
            margin: 0;
        }
        .navbar nav a {
            color: #fff;
            text-decoration: none;
            margin: 0 15px;
            font-size: 18px;
        }
        .navbar nav a:hover {
            color: #3498db;
        }

        /* Main section styling */
        main {
            padding: 40px 20px;
        }
        .products {
            text-align: center;
        }
        .products h2 {
            font-size: 36px;
            margin-bottom: 30px;
        }
        .product-list {
            display: flex;
            justify-content: space-around;
            flex-wrap: wrap;
        }
        .product {
            background-color: #fff;
            width: 30%;
            padding: 20px;
            margin: 10px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease;
            text-align: center;
        }
        .product img {
            width: 50%;
            height: 200px;
            object-fit: cover;
            border-radius: 8px;
            margin-bottom: 10px;
        }
        .product h3 {
            font-size: 24px;
            color: #333;
            margin-bottom: 10px;
        }
        .product button {
            padding: 10px 20px;
            background-color: #3498db;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        .product button:hover {
            background-color: #2980b9;
        }
        .product:hover {
            transform: translateY(-10px);
        }

        /* Footer styling */
        .footer {
            background: #121518;
            color: #ffffff;
            padding: 40px 0;
            text-align: center;
            position: relative;
            bottom: 0;
            width: 100%;
        }
        .footer .footer-section {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-bottom: 30px;
            flex-wrap: wrap;
        }
        .footer .footer-column {
            margin: 0 30px;
            max-width: 250px;
        }
        .footer .footer-header {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 15px;
        }
        .footer .footer-links li {
            margin: 8px 0;
        }
        .footer .footer-links a {
            color: #999999;
            text-decoration: none;
            font-size: 14px;
            transition: color 0.3s ease;
        }
        .footer .footer-links a:hover {
            color: #aa9166;
        }
        .footer .copyright {
            padding: 15px 0;
            background-color: #1a1d22;
            color: #999999;
            font-size: 14px;
        }
        .footer .copyright a {
            color: #aa9166;
            font-weight: 500;
            letter-spacing: 1px;
        }
        .footer .copyright a:hover {
            color: #ffffff;
        }
        .btn6 {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 13rem;
    height: 3rem;
    background-size: 300% 300%;
    backdrop-filter: blur(1rem);
    border-radius: 5rem;
    transition: 0.5s;
    animation: gradient_301 5s ease infinite;
    border: double 4px transparent;
    background-image: linear-gradient(#212121, #212121), linear-gradient(137.48deg, #ffdb3b 10%,#805c1f 45%, #4c3c20 67%, #e2ab4c 87%);
    background-origin: border-box;
    background-clip: content-box, border-box;
}
#container-stars {
    position: absolute;
    z-index: -1;
    width: 100%;
    height: 100%;
    overflow: hidden;
    transition: 0.5s;
    backdrop-filter: blur(1rem);
    border-radius: 5rem;
}
strong {
    z-index: 2;
    font-family: sans-serif;
    font-size: 12px;
    letter-spacing: 5px;
    color: #FFFFFF;
    text-shadow: 0 0 4px white;
}
.circle {
    width: 100%;
    height: 30px;
    filter: blur(2rem);
    animation: pulse_3011 4s infinite;
    z-index: -1;
}

    .circle:nth-of-type(1) {
        background: #b38435;
    }

    .circle:nth-of-type(2) {
        background: #aa9168;
    }

.btn6:hover #container-stars {
    z-index: 1;
    background-color: #212121;
}

.btn6:hover {
    transform: scale(1.1)
}

.btn6:active {
    border: double 4px #805c1f;
    background-origin: border-box;
    background-clip: content-box, border-box;
    animation: none;
}

    .btn6:active .circle {
        background: #b38435;
    }

#stars {
    position: relative;
    background: transparent;
    width: 200rem;
    height: 200rem;
}

    #stars::after {
        content: "";
        position: absolute;
        top: -10rem;
        left: -100rem;
        width: 100%;
        height: 100%;
        animation: animStarRotate 90s linear infinite;
    }

    #stars::after {
        background-image: radial-gradient(#ffffff 1px, transparent 1%);
        background-size: 50px 50px;
    }

    #stars::before {
        content: "";
        position: absolute;
        top: 0;
        left: -50%;
        width: 170%;
        height: 500%;
        animation: animStar 60s linear infinite;
    }

    #stars::before {
        background-image: radial-gradient(#ffffff 1px, transparent 1%);
        background-size: 50px 50px;
        opacity: 0.5;
    }
    
@keyframes animStar {
    from {
        transform: translateY(0);
    }

    to {
        transform: translateY(-135rem);
    }
}

@keyframes animStarRotate {
    from {
        transform: rotate(360deg);
    }

    to {
        transform: rotate(0);
    }
}

@keyframes gradient_301 {
    0% {
        background-position: 0% 50%;
    }

    50% {
        background-position: 100% 50%;
    }

    100% {
        background-position: 0% 50%;
    }
}

@keyframes pulse_3011 {
    0% {
        transform: scale(0.75);
        box-shadow: 0 0 0 0 rgba(0, 0, 0, 0.7);
    }

    70% {
        transform: scale(1);
        box-shadow: 0 0 0 10px rgba(0, 0, 0, 0);
    }

    100% {
        transform: scale(0.75);
        box-shadow: 0 0 0 0 rgba(0, 0, 0, 0);
    }
}
    </style>
</head>
<body>
    <!-- Header -->
    <header>
        <div class="navbar">
            <div class="logo">
                <h1>Car Service</h1>
            </div>
            <nav>
                <button class="btn6" type="button" onclick="window.location.href='signout.html';">
                    <strong>Sign Out</strong>
                </button>
            </nav>
        </div>
    </header>

    <!-- Main Section -->
    <main>
        <section class="products">
            <h2>Services</h2>
            <div class="product-list">
                <!-- Product 1 -->
                <div class="product">
                    <img src="img/car-service1.jpg" alt="Booking Appointment">
                    <h3>Booking Appointment</h3>
                    <a href="booking.jsp">
                        <button>Make Booking</button>
                    </a>
                </div>
                <!-- Product 2 -->
                <div class="product">
                    <img src="img/car-service2.jpg" alt="Product Selection">
                    <h3>Product</h3>
                    <a href="product.jsp">
                        <button>Select Product</button>
                    </a>
                </div>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <div class="footer">
        <div class="copyright">
            <p>&copy; 2024 <a href="#">Car Service Banzema</a>. All Rights Reserved.</p>
        </div>
    </div>
</body>
</html>
