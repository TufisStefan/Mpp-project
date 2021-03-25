using System;

namespace C_Sharp.domain
{
    public class Excursion : Entity<long>
    {
        public string Company { get; set; }
        public float Price { get; set; }
        public DateTime Time { get; set; }
        public long Seats { get; set; }
        
        public string Objective { get; set; }

        public Excursion(string company, float price, DateTime time, long seats, string objective)
        {
            Company = company;
            Price = price;
            Time = time;
            Seats = seats;
            Objective = objective;
        }
    }
}