package domain.validators;

import domain.User;

public class UserValidator implements Validator<User>{
    @Override
    public void validate(User entity) throws ValidationException {
        String errors = "";
        if(entity.getUsername().equals("")){
            errors += "Username is null!\n";
        }
        if(entity.getPassword().equals("")){
            errors += "Password is null!\n";
        }
        if(!errors.equals("")){
            throw new ValidationException(errors);
        }
    }
}
