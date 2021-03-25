package domain.validators;

import domain.Excursion;

public class ExcursionValidator implements Validator<Excursion> {
    @Override
    public void validate(Excursion entity) throws ValidationException {
        String errors = "";
        if(entity.getPrice() <= 0){
            errors += "Price must be greater than 0!\n";
        }
        if(entity.getSeats() <= 0){
            errors += "Number of seats must be greater than 0!\n";
        }
        if(entity.getCompany().equals("")) {
            errors += "Company name is null!\n";
        }
        if(!errors.equals("")){
            throw new ValidationException(errors);
        }
    }
}
