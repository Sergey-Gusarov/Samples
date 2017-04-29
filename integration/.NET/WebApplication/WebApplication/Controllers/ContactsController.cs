using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebApplication.Models;

namespace WebApplication.Controllers
{
    [Route("api/[controller]")]
    public class ContactsController
    {
        [HttpGet]
        public async Task<IEnumerable<Contact>> Get()
        {
            return null;
        }

        [HttpGet("{id}")]
        public async Task<Contact> Get(Guid id)
        {
            return null;
        }

        [HttpPost]
        public void Post(Contact contact)
        {
        }


        [HttpPut("{id}")]
        public void Put(int id, Contact contact)
        {
        }


        [HttpDelete("{id}")]
        public void Delete(int id)
        {
        }
    }
}
