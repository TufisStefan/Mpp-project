import controller.IController;
import domain.Excursion;
import domain.Reservation;
import domain.User;
import domain.validators.ExcursionValidator;
import domain.validators.ReservationValidator;
import domain.validators.UserValidator;
import domain.validators.Validator;
import repository.*;
import services.Services;
import services.TourismAgencyServices;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd_config.properties"));
        } catch (IOException e) {
            System.out.println("Cannot find bd_config "+e);
        }


        UserRepository userRepository = new UserDBRepository(props, new UserValidator());
        ExcursionRepository excursionRepository = new ExcursionDBRepository(props, new ExcursionValidator());
        ReservationRepository reservationRepository = new ReservationDBRepository(props, new ReservationValidator());

        Services services = new TourismAgencyServices(userRepository, excursionRepository, reservationRepository);
        MainFX mainFX = MainFX.getInstance(services);

        mainFX.run();

    }
}
