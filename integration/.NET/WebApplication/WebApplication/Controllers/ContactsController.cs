using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebApplication.Models;

namespace WebApplication.Controllers
{
    [Route("api/[controller]")]
    public class ContactsController : Controller
    {
        public static List<Contact> ContactsStore = new List<Contact>() {

            new Contact() { Id = 1, ExternalId = "", Name = "Vladimir", Company = "Breffi", Email = "vladimir@breffi.ru", Phone = "9102349045", Interest = "StoryCLM" },
            new Contact() { Id = 2, ExternalId = "", Name = "Vladilen", Company = "J&J", Email = "v@J&J.ru", Phone = "9102349045", Interest = "StoryCLM" },
            new Contact() { Id = 3, ExternalId = "", Name = "Vlad", Company = "VladSystems", Email = "vladimir@vsystems.ru", Phone = "9102679045", Interest = "StoryCLM" },
            new Contact() { Id = 4, ExternalId = "", Name = "Kolma", Company = "Kolma INC", Email = "kolma@kolma.ru", Phone = "6142349045", Interest = "StoryCLM" },
            new Contact() { Id = 5, ExternalId = "", Name = "Arin", Company = "acorp", Email = "a@acorp.ru", Phone = "9102349045", Interest = "StoryCLM" }
        };

        [HttpGet]
        public async Task<IEnumerable<Contact>> Get()
        {
            return ContactsStore;
        }

        [HttpGet("{id}")]
        public async Task<Contact> Get(int id)
        {
            return ContactsStore.FirstOrDefault(t=> t.Id == id);
        }

        [HttpPost]
        public async Task<Contact> Post([FromBody] Contact contact)
        {
            contact.Id = new Random().Next(100, 10000);
            ContactsStore.Add(contact);
            return contact;
        }

        [HttpPut("{id}")]
        public async Task<Contact> Put([FromBody] Contact contact)
        {
            var contactResult = ContactsStore.FirstOrDefault(t => t.Id == contact.Id);
            contactResult.Company = contact.Company;
            contactResult.Email = contact.Email;
            contactResult.Interest = contact.Interest;
            contactResult.Name= contact.Name;
            contactResult.Phone= contact.Phone;
            return contactResult;
        }

        [HttpDelete("{id}")]
        public void Delete(int id)
        {
            var contactResult = ContactsStore.FirstOrDefault(t => t.Id == id);
            ContactsStore.Remove(contactResult);
        }
    }
}
