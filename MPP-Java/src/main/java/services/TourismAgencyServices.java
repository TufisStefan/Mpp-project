package services;


import domain.User;
import repository.ExcursionRepository;
import repository.RepositoryException;
import repository.ReservationRepository;
import repository.UserRepository;

public class TourismAgencyServices {
    private UserRepository userRepository;
    private ExcursionRepository excursionRepository;
    private ReservationRepository reservationRepository;

    public TourismAgencyServices(UserRepository userRepository, ExcursionRepository excursionRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.excursionRepository = excursionRepository;
        this.reservationRepository = reservationRepository;
    }

   public User login(String username, String password) throws ServicesException {
        try{
            User user = userRepository.login(username, password);
            return user;
        }
        catch (RepositoryException ex){
            throw new ServicesException(ex.getMessage());
        }
   }



}
