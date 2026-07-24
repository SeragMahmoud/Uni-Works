using Company.DAL.Entites;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection.Emit;
using System.Text;
using System.Threading.Tasks;

namespace Company.DAL.Configration
{
    internal class EmployeeConfg : IEntityTypeConfiguration<Employee>
    {
        public void Configure(EntityTypeBuilder<Employee> builder)
        {
			builder.HasKey(E => E.ID);
			builder.HasOne(E=>E.Departments)
				.WithMany(E=>E.Employees)
				.HasForeignKey(E=>E.DepartmentId);

		}
	}
}
