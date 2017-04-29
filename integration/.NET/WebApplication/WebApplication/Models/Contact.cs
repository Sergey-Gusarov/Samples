using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebApplication.Models
{
    public class Contact
    {
        public int Id { get; set; }

        public string ExternalId { get; set; }

        public string Name { get; set; }

        public string Company { get; set; }

        public string Email { get; set; }

        public string Phone { get; set; }

        public string Interest { get; set; }
    }
}
