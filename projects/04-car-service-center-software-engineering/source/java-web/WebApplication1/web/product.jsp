<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Page</title>
    <link rel="stylesheet" href="CSS/index.css">
    <link rel="stylesheet" href="CSS/btn.css">
</head>

<body>
    <!-- Header -->
    <header>
        <div class="navbar">
            <div class="logo">
                <h1>Products</h1>
            </div>
            <nav>
                <div class="ml-auto">
                    <button class="btn6" type="button" onclick="window.location.href='index.jsp'">
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
                <!-- Basket Icon -->
                <div id="basket-logo" class="basket-logo" onclick="toggleCart()">
                    <img src="img/basket-icon.png" alt="Basket Icon" />
                </div>
            </nav>
        </div>
    </header>

    <main class="product-container">
        <!-- Product 1 -->
        <div class="product-card" data-id="1" data-name="Product 1" data-price="99.99">
            <img src="img/product1.jpeg" alt="Product Image" class="product-image">
            <h2 class="product-title">Product 1</h2>
            <p class="product-description">Description of Product 1</p>
            <p class="product-price">$99.99</p>
            <button class="buy-button" onclick="addToCart(this)">Add to Cart</button>
        </div>

        <!-- Product 2 -->
        <div class="product-card" data-id="2" data-name="Product 2" data-price="99.99">
            <img src="img/product2.jpeg" alt="Product Image" class="product-image">
            <h2 class="product-title">Product 2</h2>
            <p class="product-description">Description of Product 2</p>
            <p class="product-price">$99.99</p>
            <button class="buy-button" onclick="addToCart(this)">Add to Cart</button>
        </div>

        <!-- Product 3 -->
        <div class="product-card" data-id="3" data-name="Product 3" data-price="99.99">
            <img src="img/product3.jpg" alt="Product Image" class="product-image">
            <h2 class="product-title">Product 3</h2>
            <p class="product-description">Description of Product 3</p>
            <p class="product-price">$99.99</p>
            <button class="buy-button" onclick="addToCart(this)">Add to Cart</button>
        </div>

        <!-- Product 4 -->
        <div class="product-card" data-id="4" data-name="product4" data-price="99.99">
            <img src="img/product4.jpg" alt="Product Image" class="product-image">
            <h2 class="product-title">flashaya</h2>
            <p class="product-description">Description of Product 4</p>
            <p class="product-price">$99.99</p>
            <button class="buy-button" onclick="addToCart(this)">Add to Cart</button>
        </div>

        <!-- Product 5 -->
        <div class="product-card" data-id="5" data-name="Product 5" data-price="99.99">
            <img src="img/product5.jpeg" alt="Product Image" class="product-image">
            <h2 class="product-title">Product 5</h2>
            <p class="product-description">Description of Product 5</p>
            <p class="product-price">$99.99</p>
            <button class="buy-button" onclick="addToCart(this)">Add to Cart</button>
        </div>

        <!-- Product 6 -->
        <div class="product-card" data-id="6" data-name="Product 6" data-price="99.99">
            <img src="img/product6.jpeg" alt="Product Image" class="product-image">
            <h2 class="product-title">Product 6</h2>
            <p class="product-description">Description of Product 6</p>
            <p class="product-price">$99.99</p>
            <button class="buy-button" onclick="addToCart(this)">Add to Cart</button>
        </div>

        <!-- Cart Container (Initially Hidden) -->
        <div id="cart-container" style="display:none;">
            <h3>Your Cart</h3>
            <ul id="cart-items"></ul>
            <p>Total: $<span id="total-price">0.00</span></p>
            <button id="checkout-button" onclick="checkout()">Proceed to Checkout</button>
        </div>

        <!-- Modal for Checkout Payment -->
        <div id="payment-modal" class="payment-modal" style="display:none;">
            <div class="payment-modal-content">
                <span class="close-btn" onclick="closePaymentModal()">&times;</span>
                <h3>Choose Payment Method</h3>
                <button onclick="payWithCash()">Cash</button>
                <button onclick="payWithCard()">Card</button>
            </div>
        </div>

    </main>

    <!-- Footer -->
<div class="footer">
    <!-- Footer Sections -->
   

    <!-- Copyright Section -->
    <div class="copyright">
        <p>&copy; 2024 <a href="#">Car Service Banzema</a>. All Rights Reserved. 
    </div>
</div>
<!-- Footer End -->

<script>
    // Cart functionality
var cart = [];
var totalPrice = 0;

// Add Product to Cart
function addToCart(button) {
    var productCard = button.closest('.product-card');
    var product = {
        id: productCard.getAttribute('data-id'),
        name: productCard.getAttribute('data-name'),
        price: parseFloat(productCard.getAttribute('data-price')),
        quantity: 1
    };

    // Check if product already exists in the cart
    var existingProduct = cart.find(function(item) {
        return item.id === product.id;
    });
    if (existingProduct) {
        existingProduct.quantity += 1;
    } else {
        cart.push(product);
    }

    updateCartDisplay();
}

// Update Cart Display
function updateCartDisplay() {
    totalPrice = 0;

    var cartItems = cart.map(function(product) {
        totalPrice += product.price * product.quantity;
        return '<li>' + product.name + ' - $' + product.price.toFixed(2) + ' x ' + product.quantity + '</li>';
    }).join('');

    var cartItemsContainer = document.getElementById('cart-items');
    if (cartItemsContainer) {
        cartItemsContainer.innerHTML = cartItems;
    }
    document.getElementById('total-price').textContent = '$' + totalPrice.toFixed(2);

    updateBasketVisibility();
}

// Show/Hide Cart
function toggleCart() {
    var cartContainer = document.getElementById('cart-container');
    cartContainer.style.display = (cartContainer.style.display === 'none' || cartContainer.style.display === '') ? 'block' : 'none';
}

// Show Cart in a New Window
function openCartWindow() {
    if (!window.cartWindow || window.cartWindow.closed) {
        window.cartWindow = window.open('', 'Cart Window', 'width=500,height=400');
    }
    var cartWindow = window.cartWindow;

 var cartHtml = '<h3>Your Cart</h3><ul id="cart-items">';
          cart.map(function(product) {
              cartHtml += '<li>' + product.name + ' - $' + product.price.toFixed(2) + ' x ' + product.quantity + '</li>';
            }).join('');
             cartHtml += '</ul><h4>Total: $' + totalPrice.toFixed(2) + '</h4><button onclick="checkout()">Proceed to Checkout</button>';

    cartWindow.document.body.innerHTML = cartHtml;
}

// Checkout and Payment Modal (Redirect to payment options)
function checkout() {
    // Open the cart window
    openCartWindow();
    var cartWindow = window.cartWindow;

    // Generate the HTML for the cart
    var paymentHtml = '<h3>Your Cart</h3><ul id="cart-items">';
    cart.map(function(product) {
        paymentHtml += '<li>' + product.name + ' - $' + product.price.toFixed(2) + ' x ' + product.quantity + '</li>';
    }).join('');
    paymentHtml += '</ul><h4>Total: $' + totalPrice.toFixed(2) + '</h4>';
    
    // Validate the totalPrice before adding the payment form
    if (isNaN(totalPrice) || totalPrice <= 0) {
        console.error("Invalid totalPrice:", totalPrice);
        return;
    }

    // Add hidden form with the total amount as a string
    paymentHtml += `
        <form action="Payment" method="POST" id="cash-payment-form">
            <input type="hidden" name="amount" value="${totalPrice.toFixed(2).toString()}">
            <button type="submit" id="payCash">Pay with Cash</button>
        </form>
    `;
    
    // Add the "Pay with Card" button
    paymentHtml += `
        <button id="payCard">Pay with Card</button>
    `;

    // Inject the HTML into the cart window
    cartWindow.document.body.innerHTML = paymentHtml;

    // Debugging: Log the HTML content to verify it's being set correctly
    console.log(cartWindow.document.body.innerHTML);

    // Add an event listener for the "Pay with Card" button
    setTimeout(function() {
        var payCardButton = cartWindow.document.getElementById("payCard");
        if (payCardButton) {
            payCardButton.addEventListener("click", payWithCard);
        } else {
            console.error('Pay with Card button not found.');
        }
    }, 100);
}



// Card Payment Form Handler (unchanged)
function payWithCard() {
    // Open a new window for entering card details
    var paymentWindow = window.open('', 'Payment Window', 'width=400,height=300');
    var cardDetailsForm = `
        <h3>Enter Card Details</h3>
        <form action="Payment" id="card-form">
            <label for="bookingID">Booking ID:</label>
            <input type="text" id="bookingID" name="bookingID" required><br><br>
            <label for="cardNumber">Card Number (16 digits):</label>
            <input type="text" id="cardNumber" name="cardNumber" maxlength="16" required><br><br>
            <label for="cvv">CVV (3 digits):</label>
            <input type="text" id="cvv" name="cvv" maxlength="3" required><br><br>
            <label for="expiry">Expiry Date (MM/YY):</label>
            <input type="text" id="expiry" name="expiry" maxlength="7" required><br><br>
            <button type="submit">Pay</button>
        </form>
    `;
    paymentWindow.document.body.innerHTML = cardDetailsForm;
}

function payWithCash() {
    var paymentData = {
        paymentType: "cash",
        totalAmount: totalPrice.toFixed(2)
    };

    fetch('/PaymentController/cash', {
        method: 'POST',
        body: JSON.stringify(paymentData),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        alert(data.message); // Display the message received from the backend
        clearCart();
        closePaymentModal();
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

// Card Payment Function

function payWithCard() {
    // Open a new window for entering card details
    var paymentWindow = window.open('', 'Payment Window', 'width=400,height=300');
    var cardDetailsForm = `
        <h3>Enter Card Details</h3>
        <form action="Payment",id="card-form">
            <label for="bookingID">Booking ID:</label>
            <input type="text" id="bookingID" name="bookingID" required><br><br>
            <label for="cardNumber">Card Number (16 digits):</label>
            <input type="text" id="cardNumber" name="cardNumber" maxlength="16" required><br><br>
            <label for="cvv">CVV (3 digits):</label>
            <input type="text" id="cvv" name="cvv" maxlength="3" required><br><br>
            <label for="expiry">Expiry Date (MM/YY):</label>
            <input type="text" id="expiry" name="expiry" maxlength="7" required><br><br>
            <button type="submit">Pay</button>
        </form>
    `;
    paymentWindow.document.body.innerHTML = cardDetailsForm;

    // Handle the form submission inside the opened window
    paymentWindow.document.getElementById("card-form").addEventListener("submit", function(e) {
        e.preventDefault(); // Prevent form from submitting normally

        // Get the user input from the form
        var bookingID = paymentWindow.document.getElementById("bookingID").value;
        var cardNumber = paymentWindow.document.getElementById("cardNumber").value;
        var cvv = paymentWindow.document.getElementById(" cvv").value;
        var expiry = paymentWindow.document.getElementById("expiry").value;

        // Validate inputs
        if (bookingID && cardNumber.length === 16 && cvv.length === 3 && expiry) {
            var paymentData = {
                paymentType: "card",
                bookingID: bookingID,
                cardNumber: cardNumber,
                cvv: cvv,
                expiry: expiry
            };

            // Send payment data to the backend (Controller)
            fetch('/PaymentController', {
                method: 'POST',
                body: JSON.stringify(paymentData),  // Send payment data as JSON
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => response.json())
            .then(data => {
                alert(data.message);  // Display the message received from the backend
                clearCart();  // Clear cart after successful payment
                paymentWindow.close();  // Close payment window
            })
            .catch(error => {
                console.error('Error:', error);
            });
        } else {
            alert("Please fill in all card details correctly.");
        }
    });
}



// Function to send payment data to the server (for cash payments)
function sendPaymentToController(paymentType) {
    var paymentData = {
        paymentType: paymentType,
        totalAmount: totalPrice.toFixed(2)  // Include the total price in payment data
    };

    fetch('/PaymentController', {
        method: 'POST',
        body: JSON.stringify(paymentData),  // Send payment data as JSON
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        alert(data.message);  // Display the message received from the backend
        clearCart();  // Clear cart after successful payment
    })
    .catch(error => {
        console.error('Error:', error);
    });
}




function clearCart() {
    cart = [];
    totalPrice = 0;
    updateCartDisplay();

    // Optionally, close the cart and reset UI
    var cartContainer = document.getElementById('cart-container');
    if (cartContainer) {
        cartContainer.style.display = 'none';
    }
}


function closeCart() {
    var cartContainer = document.getElementById('cart-container');
    if (cartContainer) {
        cartContainer.style.display = 'none';
    }
}

function updateBasketVisibility() {
    var basketLogo = document.getElementById('basket-logo');
    if (cart.length > 0) {
        basketLogo.style.opacity = '1';
    } else {
        basketLogo.style.opacity = '0.3';
    }
}

    
    
    
</script>


</body>
</html>