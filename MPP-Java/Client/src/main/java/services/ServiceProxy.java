package services;

import domain.Excursion;
import domain.User;
import networking.ClientWorker;
import org.json.JSONObject;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceProxy  implements Services{

    private final ClientWorker client;

    public ServiceProxy(ClientWorker client) {
        this.client = client;
    }

    @Override
    public User login(String username, String password) throws ServicesException {
            client.sendData(new JSONObject(Map.ofEntries(
                    Map.entry("command", "loginButtonPressed"),
                    Map.entry("data", Map.ofEntries(
                            Map.entry("username", username),
                            Map.entry("password", password)
                    ))
            )));
            return null;
    }

    @Override
    public List<Excursion> findAllExcursions() {
        client.sendData(new JSONObject(Map.ofEntries(
                Map.entry("command", "findAllExcursions"),
                Map.entry("data", Map.ofEntries(
                        Map.entry("findall", "nofilter")
                ))
        )));
        return new ArrayList<>();
    }

    @Override
    public List<Excursion> findFilteredExcursions(String objective, LocalTime startTime, LocalTime endTime) {
        client.sendData(new JSONObject(Map.ofEntries(
                Map.entry("command", "filterExcursions"),
                Map.entry("data", Map.ofEntries(
                        Map.entry("text", objective),
                        Map.entry("startHour", startTime),
                        Map.entry("endHour", endTime)
                ))
        )));
        return new ArrayList<>();
    }

    @Override
    public void addReservation(String name, Long phone, Long tickets, Excursion excursion) throws ServicesException {
        client.sendData(new JSONObject(Map.ofEntries(
                Map.entry("command", "reserveButtonPressed"),
                Map.entry("data", Map.ofEntries(
                        Map.entry("excursion", excursion),
                        Map.entry("name", name),
                        Map.entry("phone", phone),
                        Map.entry("tickets", tickets)
                ))
        )));
    }
}
