package networking;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientWorker {
    private Socket clientSocket = null;
    PrintWriter writer;
    BufferedReader reader;
    ExecutorService executorService;

    public ClientWorker(String host, int port) {

        try {

            clientSocket = new Socket(host, port);
            OutputStream output = clientSocket.getOutputStream();
            writer = new PrintWriter(output, true);
            InputStream input = clientSocket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> ClientController.start(this));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendData(JSONObject data) {
        writer.println(data);
    }

    public JSONObject receiveData() {
        try {
            return new JSONObject(reader.readLine());
        } catch (IOException ignored) {

        }
        return null;
    }

    public void closeConnection() {
        try {

            clientSocket.shutdownInput();
            executorService.shutdownNow();
            JSONObject closeSignal = new JSONObject();
            closeSignal.put("command", "close");
            sendData(closeSignal);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
