using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Company.DAL.Entites
{
    public class Account
    {
        public int id { get; set; }


        [Required]
        [EmailAddress(ErrorMessage = "Invalid email format")]
        [RegularExpression(@"^[a-zA-Z0-9._%+-]+@gmail\.com$", ErrorMessage = "Only @gmail.com emails are allowed")]
        public String Email { get; set; }

        [Required]
        public String Password { get; set; }
    }
}
