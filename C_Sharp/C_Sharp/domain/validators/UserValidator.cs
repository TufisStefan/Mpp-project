namespace C_Sharp.domain
{
    public class UserValidator: IValidator<User>
    {
        public void validate(User entity)
        {
            string errors = "";
            if(entity.UserName == ""){
                errors += "Username is null!\n";
            }
            if(entity.Password == ""){
                errors += "Password is null!\n";
            }
            if(errors != ""){
                throw new ValidationException(errors);
            }
        }
    }
}