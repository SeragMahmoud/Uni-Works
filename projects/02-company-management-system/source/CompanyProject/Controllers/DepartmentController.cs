using Company.DAL.Entites;
using Company.Reposatory;
using Microsoft.AspNetCore.Mvc;

namespace CompanyProject.Controllers
{
    public class DepartmentController : Controller
    {
        private readonly DepartmentRepo _repo;

        public DepartmentController()
        {

            _repo = new DepartmentRepo();
        }

        public IActionResult Index()
        {
            var departments = _repo.GetAll();
            return View(departments);
        }
        [HttpGet]
        public IActionResult Create()
        {
            return View();
        }
        [HttpPost]
        public IActionResult Create(Department department)
        {
      
            var result = _repo.ADD(department);
            if (result > 0)
            {
                return RedirectToAction("Index"); 
            }

          
            ModelState.AddModelError("", "An error occurred while saving the department.");
            return View(department); 
        }

        [HttpGet]
        public IActionResult Details(int id)
        {
            var department = _repo.GetbyId(id);
            if (department is null)
            {
                return NotFound();
            }
            return View(department);

        }

        [HttpGet]

        public IActionResult Update(int id)
        {
            var department = _repo.GetbyId(id);
            if (department is null)
            {
                return NotFound();
            }
            return View(department);

        }
        [HttpPost]
        public IActionResult Update(Department department)
        {
            var result = _repo.Update(department);
            if (result > 0)
            {
                return RedirectToAction("Index");
            }
            return View(result);
        }
        [HttpGet]
        public IActionResult Delete(int id)
        {
            var department = _repo.GetbyId(id);
            if (department is null)
            {
                return NotFound();
            }
            return View(department);

        }
        [HttpPost]
        public IActionResult Delete(Department department)
        {
            var result = _repo.Delete(department);
            if (result > 0)
            {
                return RedirectToAction("Index");
            }
            return View(result);
        }

    }
}
