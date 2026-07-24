var loginsec = document.querySelector('.login-section');
var loginlink = document.querySelector('.login-link');
var registerlink = document.querySelector('.register-link');

registerlink.addEventListener('click', function() {
    loginsec.classList.add('active');
});

loginlink.addEventListener('click', function() {
    loginsec.classList.remove('active');
});