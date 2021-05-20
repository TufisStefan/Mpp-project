package services;


import domain.Excursion;
import domain.Reservation;
import domain.User;
import domain.validators.ValidationException;
import repository.ExcursionRepository;
import repository.RepositoryException;
import repository.ReservationRepository;
import repository.UserRepository;

import java.rmi.RemoteException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TourismAgencyServices implements Services {
    private final UserRepository userRepository;
    private final ExcursionRepository excursionRepository;
    private final ReservationRepository reservationRepository;

    private Map<String, IObserver> loggedClients;

    public TourismAgencyServices(UserRepository userRepository, ExcursionRepository excursionRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.excursionRepository = excursionRepository;
        this.reservationRepository = reservationRepository;
        loggedClients=new ConcurrentHashMap<>();
    }

   public User login(String username, String password, IObserver client) throws ServicesException {
        try{
            User user = userRepository.login(username, password);
            loggedClients.put(user.getUsername(), client);
            return user;
        }
        catch (RepositoryException ex){
            throw new ServicesException(ex.getMessage());
        }
   }

    @Override
    public List<Excursion> findAllExcursions() {
        return excursionRepository.findAll();
    }

    @Override
    public List<Excursion> findFilteredExcursions(String objective, LocalTime startTime, LocalTime endTime) {
        return excursionRepository.filterByObjective(objective, startTime, endTime);
    }

    @Override
    public void addReservation(String name, Long phone, Long tickets, Excursion excursion) throws ServicesException {
        Long id = excursion.getId();
        Excursion repoExcursion = excursionRepository.findOne(id);
        if(repoExcursion == null){
            throw new ServicesException("Excursion not found!");
        }
        if(repoExcursion.getSeats() < tickets){
            throw new ServicesException("Not enough available seats!");
        }
        Reservation reservation = new Reservation(name, phone, tickets, id);
        try {
            reservationRepository.save(reservation);
            repoExcursion.setSeats(repoExcursion.getSeats() - tickets);
            excursionRepository.update(id, repoExcursion);
            excursion = excursionRepository.findOne(id);
            excursion.setId(id);
            notifyClients(excursion);
        }
        catch (ValidationException | RemoteException ex){
            throw new ServicesException(ex.getMessage());
        }
    }

    public void notifyClients(Excursion excursion) throws RemoteException {
        for (IObserver client:loggedClients.values()){
            client.ticketReserved(excursion);
        }
    }


}
