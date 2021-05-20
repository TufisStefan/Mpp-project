package controller;

import domain.Excursion;
import domain.Reservation;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import services.IObserver;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MainController extends UnicastRemoteObject implements IObserver, Serializable {
    private MainController() throws RemoteException{
        super();
    };
    private static MainController controller;
    private ObservableList<Excursion> listModel;
    public static MainController getController() throws RemoteException {
        if(controller == null){
            controller = new MainController();
        }
        return controller;
    }

    public void setLists(ObservableList<Excursion> modelExcursion) {
        this.listModel = modelExcursion;
    }

    @Override
    public void ticketReserved(Excursion excursion) throws RemoteException {
        Platform.runLater(() -> {
            for (int i = 0; i < listModel.size(); i++){
                if(listModel.get(i).getId().equals(excursion.getId())){
                    listModel.set(i, excursion);
                    break;
                }
            }
        });

    }
}
