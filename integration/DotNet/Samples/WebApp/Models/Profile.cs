using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebApp.Models
{
    public class Profile
    {
        public string Name { get; set; }

        public int Age { get; set; }

        public double Rating { get; set; }

        public bool Gender { get; set; }

        public DateTime Created { get; set; }
    }
}
