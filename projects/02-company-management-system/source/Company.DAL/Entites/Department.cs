using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Company.DAL.Entites
{
    public class Department
    {
        public int ID { get; set; }
        [Required(ErrorMessage = "Name is required")]
        public String Name { get; set; }
        [Required(ErrorMessage = "Desctiption is required")]
        public String Description { get; set; }
        [Required(ErrorMessage = "Code is required")]
        public int Code { get; set; }
        [Required(ErrorMessage = "CreationDate is required")]
        public DateTime CreationDate { get; set; }
		public List<Employee> Employees { get; set; }


	}
}
