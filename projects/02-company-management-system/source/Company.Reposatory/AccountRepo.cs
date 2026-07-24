using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Company.DAL.context;
using Company.DAL.Entites;

namespace Company.Reposatory
{
   public  class AccountRepo
    {
        private readonly CompanyDBContext _context;


        public AccountRepo()
        {
            _context = new CompanyDBContext();

        }

        public int ADD(Account account)
        {

            _context.Accounts.Add(account);
            return _context.SaveChanges();


        }
        public Account Login(string email, string password)
        {
            var account = _context.Accounts
                                  .FirstOrDefault(a => a.Email == email && a.Password == password);

            if (account == null)
            {
               
                return null;
            }

            return account;
        }
    }
}
