using System;
using Company.DAL.context;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Company.DAL.Entites;

namespace Company.Reposatory
{
    public class DepartmentRepo
    {
        private readonly CompanyDBContext _context;

        public DepartmentRepo()
        {
            _context = new CompanyDBContext();

        }
        public IEnumerable<Department> GetAll()
        {

            return _context.Departments.ToList();      //retrn all departments 

        }
        public Department GetbyId(int ID)
        {
            return _context.Departments.Find(ID);
        }
        public int ADD(Department department)
        {

            _context.Departments.Add(department);
            return _context.SaveChanges();


        }
        public int Update(Department department)
        {
            _context.Update(department);
            return _context.SaveChanges();


        }
        public int Delete(Department department)
        {

            _context.Remove(department);
            return _context.SaveChanges();

        }


    }
}
