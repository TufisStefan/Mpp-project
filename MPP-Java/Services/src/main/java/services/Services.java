package services;

import domain.Excursion;
import domain.User;

import java.time.LocalTime;
import java.util.List;

public interface Services {
    User login(String username, String password, IObserver client) throws ServicesException;
    List<Excursion> findAllExcursions();
    List<Excursion> findFilteredExcursions(String objective, LocalTime startTime, LocalTime endTime);
    void addReservation(String name, Long phone, Long tickets, Excursion excursion) throws ServicesException;

}
