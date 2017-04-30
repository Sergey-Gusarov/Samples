using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebApplication.DB;
using WebApplication.Models;

namespace WebApplication.Controllers
{
    [Route("api/[controller]")]
    public class ContactsController : Controller
    {
        private readonly DBContext _context;

        public ContactsController(DBContext context)
        {
            _context = context;
        }

        [HttpGet]
        public async Task<IEnumerable<Contact>> Get()
        {
            return _context.Contacts;
        }

        [HttpGet("{id}")]
        public async Task<Contact> Get(int id)
        {
            return await _context.Contacts.FindAsync(id);
        }

        [HttpPost]
        public async Task<Contact> Post([FromBody] Contact contact)
        {
            await _context.Contacts.AddAsync(contact);
            await _context.SaveChangesAsync();
            return contact;
        }

        [HttpPut("{id}")]
        public async Task<Contact> Put([FromBody] Contact contact)
        {
            var contactResult = await _context.Contacts.FindAsync(contact.Id);
            contactResult.Company = contact.Company;
            contactResult.Email = contact.Email;
            contactResult.Interest = contact.Interest;
            contactResult.Name= contact.Name;
            contactResult.Phone= contact.Phone;
            _context.Entry(contactResult).State = Microsoft.EntityFrameworkCore.EntityState.Modified;
            await _context.SaveChangesAsync();
            return contactResult;
        }

        [HttpDelete("{id}")]
        public async Task Delete(int id)
        {
            var contactResult = await _context.Contacts.FindAsync(id);
            _context.Remove(contactResult);
            await _context.SaveChangesAsync();
        }
    }
}
