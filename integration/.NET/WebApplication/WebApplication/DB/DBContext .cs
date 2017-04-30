using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebApplication.Models;

namespace WebApplication.DB
{
    public class DBContext : DbContext
    {
        public DbSet<Contact> Contacts { get; set; }

        public DBContext(DbContextOptions<DBContext> options): base(options)
        {
            this.Database.EnsureCreated();
            //this.Database.Migrate();
        }
    }
}
