package domain.validators;

import domain.Reservation;

public class ReservationValidator implements Validator<Reservation> {
    @Override
    public void validate(Reservation entity) throws ValidationException {
        String errors = "";
        if(entity.getExcursionID() <= 0){
            errors += "ID must be greater than 0!\n";
        }
        if(entity.getName().equals("")){
            errors += "Name is null!\n";
        }
        if(entity.getTicketsNumber()<=0) {
            errors += "Number of tickets must be greater than 0!\n";
        }
        if(entity.getPhoneNumber().toString().length() != 9){
            errors += "Phone number must contain 9 digits!\n";
        }
        if(!errors.equals("")){
            throw new ValidationException(errors);
        }
    }
}
