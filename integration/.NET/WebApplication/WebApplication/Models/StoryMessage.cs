using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebApplication.Models
{
    public class StoryMessage
    {
        public int TableId { get; set; }
        public string TableName { get; set; }
        public object Data { get; set; }
        public int EventId { get; set; }
        public string EventName { get; set; }
    }
}
