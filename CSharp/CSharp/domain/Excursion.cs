using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CSharp.domain
{
    public class Excursion : Entity<long>
    {
        public string Company { get; set; }
        public float Price { get; set; }
        public TimeSpan Time { get; set; }
        public long Seats { get; set; }

        public string Objective { get; set; }

        public Excursion(string company, float price, TimeSpan time, long seats, string objective)
        {
            Company = company;
            Price = price;
            Time = time;
            Seats = seats;
            Objective = objective;
        }
    }
}