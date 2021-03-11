namespace C_Sharp.domain
{
    public class Reservation : Entity<long>
    {
        public string Name { get; set; }
        public long PhoneNumber { get; set; }
        public long TicketsNumber { get; set; }
        public long ExcursionId { get; set; }

        public Reservation(string name, long phoneNumber, long ticketsNumber, long excursionId)
        {
            Name = name;
            PhoneNumber = phoneNumber;
            TicketsNumber = ticketsNumber;
            ExcursionId = excursionId;
        }
    }
}