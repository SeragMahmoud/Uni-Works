using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Company.DAL.Entites
{
    public class Employee
	{
		public int ID { get; set; }
        [Required(ErrorMessage = "Name is required")]
        public String Name { get; set; }
        [Required(ErrorMessage = "Age is required")]
        public int Age { get; set; }
        [Required(ErrorMessage = "Salary is required")]

        public double Salary { get; set; }
        [Required(ErrorMessage = "DepartmentId is required")]

        public int DepartmentId { get; set; }
		public Department? Departments { get; set; }
	}
}
