using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CSharp.domain.validators
{
    public class UserValidator : IValidator<User>
    {
        public void validate(User entity)
        {
            string errors = "";
            if (entity.UserName == "")
            {
                errors += "Username is null!\n";
            }
            if (entity.Password == "")
            {
                errors += "Password is null!\n";
            }
            if (errors != "")
            {
                throw new ValidationException(errors);
            }
        }
    }
}