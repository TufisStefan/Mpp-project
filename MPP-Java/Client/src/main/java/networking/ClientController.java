package networking;

import domain.Excursion;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import controller.TourismAgencyController;

public class ClientController {
    private static final Map<String, Function<JSONObject, Void>> commandList = new HashMap<>(Map.ofEntries(
            Map.entry("login", ClientController::loginButtonPressed),
            Map.entry("updateTable", ClientController::updateTable),
            Map.entry("error", ClientController::errorHandle)
    ));

    private static TourismAgencyController controller = null;

    public static void start(ClientWorker client) {
        JSONObject answer;
        while (true) {
            answer = client.receiveData();
            if (answer == null)
                break;
            commandList.get(answer.getString("command")).apply(answer.getJSONObject("data"));
        }
    }

    private static Void loginButtonPressed(JSONObject obj) {
        if (obj.getString("status").equals("successful")) {
            Platform.runLater(() -> {
                controller = TourismAgencyController.getController();
            });
        }
        else {
            Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Invalid username or password!").show());
        }
        return null;
    }

    private static Void updateTable(JSONObject obj) {
        JSONArray arr = obj.getJSONArray("excursions");
        List<Excursion> excursionList = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            excursionList.add(new Excursion(arr.getJSONObject(i)));
        }
        Platform.runLater(() -> controller.updateTable(excursionList));
        return null;
    }

    private static Void errorHandle(JSONObject obj) {
        Platform.runLater(() -> {
            new Alert(Alert.AlertType.ERROR, obj.getString("message")).show();
        });
        return null;
    }
}
