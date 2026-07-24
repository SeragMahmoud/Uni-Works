<%-- 
    Document   : adminpage
    Created on : Dec 13, 2024, 1:54:53 PM
    Author     : kaled
--%>

<!DOCTYPE html>
<html lang="ar">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>car services </title>
    <link rel="stylesheet" href="css/index.css">
    <link rel="stylesheet" href="css/btn.css">
</head>
<body>
    <!-- Header -->
    <header>
        <div class="navbar">
            <div class="logo">
                <h1> Admin control page </h1>
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

       

    <!-- Main Section -->
    <main>
        <section class="products">
            
            <div class="product-list">
                <!-- Product 1 -->
                <div class="product">
                    <img src="img/car-service1.jpg" alt="">
                    <h3>make booking appointment</h3>
                    <p></p>
                    <a href="bookingadmin.jsp">
                    <button>Make booking</button>
                    </a>
                </div>

              

              
            </div>
        </section>
    </main>

    <!-- Footer -->
    <footer>
        <p>&copy; 2024 </p>
    </footer>
</body>
</html>
