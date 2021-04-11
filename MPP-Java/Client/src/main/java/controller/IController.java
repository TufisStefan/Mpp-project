package controller;

import services.Services;

public class IController {
    static public Services services = null;

    public void setService(Services services) {
        IController.services = services;
    }

}
