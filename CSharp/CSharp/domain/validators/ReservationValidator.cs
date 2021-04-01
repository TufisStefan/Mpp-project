using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CSharp.domain.validators
{
    public class ReservationValidator : IValidator<Reservation>
    {
        public void validate(Reservation entity)
        {
            string errors = "";
            if (entity.ExcursionId <= 0)
            {
                errors += "ID must be greater than 0!\n";
            }
            if (entity.Name == "")
            {
                errors += "Name is null!\n";
            }
            if (entity.TicketsNumber <= 0)
            {
                errors += "Number of tickets must be greater than 0!\n";
            }
            if (entity.PhoneNumber.ToString().Length != 9)
            {
                errors += "Phone number must contain 9 digits!\n";
            }
            if (errors != "")
            {
                throw new ValidationException(errors);
            }
        }
    }
}