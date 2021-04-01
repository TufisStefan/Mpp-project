using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CSharp.domain.validators
{
    public class ExcursionValidator : IValidator<Excursion>
    {
        public void validate(Excursion entity)
        {
            string errors = "";
            if (entity.Price <= 0)
            {
                errors += "Price must be greater than 0!\n";
            }
            if (entity.Seats <= 0)
            {
                errors += "Number of seats must be greater than 0!\n";
            }
            if (entity.Company == "")
            {
                errors += "Company name is null!\n";
            }
            if (errors != "")
            {
                throw new ValidationException(errors);
            }
        }
    }
}