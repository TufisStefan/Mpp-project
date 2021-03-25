import repository.*;

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

        UserRepository userRepository = new UserDBRepository(props);
        ExcursionRepository excursionRepository = new ExcursionDBRepository(props);
        ReservationRepository reservationRepository = new ReservationDBRepository(props);

        //reservationRepository.save(new Reservation("John Smith", 123456789L, 2L, 8L));
        //excursionRepository.save(new Excursion("Eiffel Tower", "Christian Tour", 500, LocalTime.of(20,30,0), 50L ));
        //Excursion excursion = excursionRepository.findOne(9L);
        //System.out.println(excursion);
        //excursionRepository.delete(6L);
        //excursionRepository.delete(7L);
        //System.out.println(excursionRepository.findAll());
        /*userRepository.update(2L,new User("mihai","parola"));
        List<User> users = userRepository.findAll();
        for (User user:users){
            System.out.println(user.getUsername());
        }*/


    }
}
