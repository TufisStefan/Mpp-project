package services;

import domain.Excursion;
import domain.Reservation;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver extends Remote {
    void ticketReserved(Excursion excursion) throws RemoteException;
}
