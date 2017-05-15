using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using WebApplication.DB;
using WebApplication.Models;

namespace WebApplication.Controllers
{
    [Route("api/[controller]")]
    public class ContactsController : Controller
    {
        const string kMediaTypeHeader = "application/json";
        const string url = "https://api.storyclm.com/v1/webhooks/590b1a02bc977d176ce11ffd/824ae822317e4d859d5dfd3d73b7fec3";
        private readonly DBContext _context;

        public ContactsController(DBContext context)
        {
            _context = context;
        }

        #region StoryCLM Notifications

        private async Task<T> POSTAsync<T>(object o)
        {
            string result = null;
            using (var handler = new HttpClientHandler()
            {
                AllowAutoRedirect = false
            })
            using (var client = new HttpClient(handler))
            {
                client.DefaultRequestHeaders.Accept.Clear();
                client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue(kMediaTypeHeader));
                HttpResponseMessage response = await client.PostAsync(url,
                    new StringContent(await Task.Factory.StartNew(() => JsonConvert.SerializeObject(o)), Encoding.UTF8, kMediaTypeHeader));
                result = await response.Content.ReadAsStringAsync();
            }
            return await Task.Factory.StartNew(() => JsonConvert.DeserializeObject<T>(result));
        }

        async Task InsertContact(Contact contact)
        {
            await _context.Contacts.AddAsync(contact);
            await _context.SaveChangesAsync();
            await POSTAsync<Contact>(contact);
        }

        async Task InsertContacts(List<Contact> contacts)
        {
            await _context.Contacts.AddRangeAsync(contacts);
            await _context.SaveChangesAsync();
            await POSTAsync<List<Contact>>(contacts);
        }

        async Task UpdateContact(Contact contact)
        {
            var contactResult = await _context.Contacts.FindAsync(contact.Id);
            contactResult.Company = contact.Company;
            contactResult.Email = contact.Email;
            contactResult.Interest = contact.Interest;
            contactResult.Name = contact.Name;
            contactResult.Phone = contact.Phone;
            _context.Entry(contactResult).State = Microsoft.EntityFrameworkCore.EntityState.Modified;
            await _context.SaveChangesAsync();
        }

        async Task UpdateContacts(List<Contact> contacts)
        {
            var ids = contacts.Select(m => m.Id);
            var contactResults = _context.Contacts.Where(t => ids.Contains(t.Id));
            foreach (var t in contactResults)
            {
                var contact = contacts.FirstOrDefault(m => m.Id == t.Id);
                t.Company = contact.Company;
                t.Email = contact.Email;
                t.Interest = contact.Interest;
                t.Name = contact.Name;
                t.Phone = contact.Phone;
                _context.Entry(t).State = Microsoft.EntityFrameworkCore.EntityState.Modified;
            }
            await _context.SaveChangesAsync();
        }

        async Task DeleteContact(Contact contact)
        {
            var contactResult = await _context.Contacts.FindAsync(contact.Id);
            _context.Remove(contactResult);
            await _context.SaveChangesAsync();
        }

        async Task DeleteContacts(List<Contact> contacts)
        {
            var ids = contacts.Select(m => m.Id);
            var contactResults = _context.Contacts.Where(t => ids.Contains(t.Id));
            _context.RemoveRange(contactResults);
            await _context.SaveChangesAsync();
        }

        [HttpPost("notifications")]
        public async Task Notifications()
        {
            if (Request.Body.CanSeek) Request.Body.Position = 0;
            var input = await new StreamReader(Request.Body).ReadToEndAsync();
            StoryMessage storyMessage = JsonConvert.DeserializeObject<StoryMessage>(input);
            if (storyMessage.TableId != 44) return;
            if (!(storyMessage.EventId == 1 || storyMessage.EventId == 2 || storyMessage.EventId == 3)) return;
            if (storyMessage.Data is JArray)
            {
                JArray messages = storyMessage.Data as JArray;
                if (messages == null) throw new InvalidOperationException("Content error");
                List<Contact> contacts = messages.ToObject<List<Contact>>();
                switch (storyMessage.EventId)
                {
                    case 1:
                        {
                            await InsertContacts(contacts);
                            return;
                        }
                    case 2:
                        {
                            await UpdateContacts(contacts);
                            return;
                        }
                    case 3:
                        {
                            await DeleteContacts(contacts);
                            return;
                        }
                    default: return;
                }
            }
            else if (storyMessage.Data is JObject)
            {
                JObject message = storyMessage.Data as JObject;
                if (message == null) throw new InvalidOperationException("Content error");
                Contact contact = message.ToObject<Contact>();
                switch (storyMessage.EventId)
                {
                    case 1:
                        {
                            await InsertContact(contact);
                            return;
                        }
                    case 2:
                        {
                            await UpdateContact(contact);
                            return;
                        }
                    case 3:
                        {
                            await DeleteContact(contact);
                            return;
                        }
                    default: return;
                }
            }
        }

        #endregion

        #region Angular App API

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
            Contact storyResult = await POSTAsync<Contact>(contact);
            var contactResult = await _context.Contacts.FindAsync(contact.Id);
            contactResult._id = storyResult._id;
            _context.Entry(contactResult).State = Microsoft.EntityFrameworkCore.EntityState.Modified;
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
            Contact storyResult = await POSTAsync<Contact>(contactResult);
            _context.Entry(contactResult).State = Microsoft.EntityFrameworkCore.EntityState.Modified;
            await _context.SaveChangesAsync();
            return contactResult;
        }

        [HttpDelete("{id}")]
        public async Task Delete(int id)
        {
            var contactResult = await _context.Contacts.FindAsync(id);
            Contact storyResult = await POSTAsync<Contact>(new Dictionary<string, string>() { ["_id"] = contactResult._id });
            _context.Remove(contactResult);
            await _context.SaveChangesAsync();
        }

        #endregion

        protected override void Dispose(bool disposing)
        {
            if (disposing && _context != null) _context.Dispose();
            base.Dispose(disposing);
        }
    }
}
