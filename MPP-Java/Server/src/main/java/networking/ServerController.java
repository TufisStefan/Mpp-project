package networking;

import domain.Excursion;
import org.json.JSONArray;
import org.json.JSONObject;
import services.ServicesException;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ServerController {
    private final Map<String, Function<JSONObject, Void>> commandList;

    {
        Function<JSONObject, Void> login  = this::loginButtonPressed;
        Function<JSONObject, Void> logout  = this::logoutButtonPressed;
        Function<JSONObject, Void> filter  = this::filterExcursions;
        Function<JSONObject, Void> reserve  = this::reserveButtonPressed;
        Function<JSONObject, Void> findall  = this::findAllExcursions;

        commandList = new HashMap<String, Function<JSONObject, Void>>(Map.ofEntries(
                Map.entry("loginButtonPressed", login),
                Map.entry("logoutButtonPressed", logout),
                Map.entry("filterExcursions", filter),
                Map.entry("findAllExcursions", findall),
                Map.entry("reserveButtonPressed", reserve)
        ));
    }

    private Void findAllExcursions(JSONObject jsonObject) {
        JSONArray excursions = new JSONArray();
        for (Excursion ex : ServerWorker.getNetworkService().findAllExcursions()) {
            excursions.put(ex.toJSON());
        }
        sendData(new JSONObject(Map.ofEntries(
                Map.entry("command", "updateTable"),
                Map.entry("data", Map.ofEntries(
                        Map.entry("excursions", excursions)
                ))
        )));
        return null;
    }

    public static synchronized void sendDataToAll() {
        JSONArray excursions = new JSONArray();
        for (Excursion ex : ServerWorker.getNetworkService().findAllExcursions()) {
            excursions.put(ex.toJSON());
        }
        for (Map.Entry<Socket, ServerController> entry : ServerWorker.sockets.entrySet()) {
            entry.getValue().sendData(new JSONObject(Map.ofEntries(
                    Map.entry("command", "updateTable"),
                    Map.entry("data", Map.ofEntries(
                            Map.entry("excursions", excursions)
                    ))
            )));
        }
    }

    private Void reserveButtonPressed(JSONObject obj) {
        try {
            ServerWorker.getNetworkService().addReservation(obj.getString("name"), obj.getLong("phone"), obj.getLong("tickets"), new Excursion(obj.getJSONObject("excursion")));
            sendDataToAll();
        }
        catch (ServicesException ex){

        }
        return null;
    }

    private final Socket clientSocket;
    private PrintWriter writer;

    public ServerController(Socket clientSocket) {
        this.clientSocket = clientSocket;
        OutputStream output = null;
        try {
            output = clientSocket.getOutputStream();
            this.writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendData(JSONObject data) {
        writer.println(data);
    }

    private JSONObject receiveData(BufferedReader reader) throws IOException {
        return new JSONObject(reader.readLine());
    }

    public void start() throws IOException {
        InputStream input = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        JSONObject object;
        while (true) {
            object = receiveData(reader);
            String command = object.getString("command");
            System.out.println(object);
            if (command.equals("close")) {
                break;
            }
            if (!commandList.containsKey(command)) {
                JSONObject error = new JSONObject();
                error.put("command", "error");
                error.put("data", "Command does not exists!");
                sendData(error);
                continue;
            }
            try {
                commandList.get(command).apply(object.getJSONObject("data"));
            }
            catch (Exception e) {
                sendData(new JSONObject(
                        Map.ofEntries(
                                Map.entry("command", "error"),
                                Map.entry("data", new JSONObject(
                                        Map.ofEntries(
                                                Map.entry("message", e.getMessage())
                                        )
                                ))
                        )
                ));
            }
        }
        clientSocket.close();
    }

    private Void loginButtonPressed(JSONObject obj) {
        try {
            ServerWorker.getNetworkService().login(obj.getString("username"), obj.getString("password"));
            sendData(new JSONObject(Map.ofEntries(
                    Map.entry("command", "login"),
                    Map.entry("data", Map.ofEntries(
                            Map.entry("status", "successful")
                    ))
            )));
            ServerWorker.sockets.put(this.clientSocket, this);
        }
        catch (ServicesException ex){
            sendData(new JSONObject(
                    Map.ofEntries(
                            Map.entry("command", "error"),
                            Map.entry("data", new JSONObject(
                                    Map.ofEntries(
                                            Map.entry("message", ex.getMessage())
                                    )
                            ))
                    )
            ));
        }
        return null;
    }

    private Void logoutButtonPressed(JSONObject obj) {
        if (!ServerWorker.sockets.containsKey(clientSocket)) {
            return null;
        }
        ServerWorker.sockets.remove(clientSocket);
        return null;
    }

    private Void filterExcursions(JSONObject obj) {
        JSONArray excursions = new JSONArray();
        for (Excursion ex : ServerWorker.getNetworkService().findFilteredExcursions(obj.getString("text"),
                LocalTime.parse(obj.getString("startHour")),
                LocalTime.parse(obj.getString("endHour")))) {
            excursions.put(ex.toJSON());
        }
        sendData(new JSONObject(Map.ofEntries(
                Map.entry("command", "updateTable"),
                Map.entry("data", Map.ofEntries(
                        Map.entry("excursions", excursions)
                ))
        )));
        return null;
    }

}
