package controller;

import services.Services;

public class IController {
    static protected Services services = null;

    public void setService(Services services) {
        IController.services = services;
    }

}
