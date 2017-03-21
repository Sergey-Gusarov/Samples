using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using WebApp.Models;

namespace WebApp.Controllers
{
    [Route("api/[controller]")]
    public class ProfilesController : Controller
    {
        [HttpGet]
        public IEnumerable<Profile> Get()
        {
            return Enumerable.Empty<Profile>();
        }

       
    }
}
