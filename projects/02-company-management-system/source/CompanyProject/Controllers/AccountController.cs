using Company.DAL.Entites;
using Company.Reposatory;
using Microsoft.AspNetCore.Mvc;

namespace CompanyProject.Controllers
{
    public class AccountController : Controller
    {
        private readonly AccountRepo _repo;

        public AccountController()
        {

            _repo = new AccountRepo();
        }
        [HttpGet]

        public IActionResult Create()
        {
            return View("Registration");
        }

        [HttpPost]
        public IActionResult Create(Account account)
        {
            if (ModelState.IsValid)
            {
                var result = _repo.ADD(account);
                if (result > 0)
                {
                    TempData["Success"] = "Account created successfully!";
                    return RedirectToAction("Login");
                }
            }

            ViewBag.regError = "Invalid email or password.";
            return View("Registration", account);
        }



        [HttpGet]
        public IActionResult Login()
        {
            return View();
        }

        [HttpPost]
        public IActionResult Login(Account account)
        {
            var user = _repo.Login(account.Email, account.Password);
            if (user != null)
            {
                TempData["Success"] = "Login successful!";
                return RedirectToAction("Index", "Home"); 
            }

            ViewBag.LoginError = "Invalid email or password.";
            return View(account);
        }


    }
}
