package networking;

import services.Services;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerWorker {
    private ServerSocket serverSocket = null;
    private static Services networkService;
    public static Map<Socket, ServerController> sockets = Collections.synchronizedMap(new HashMap<>());

    public static Services getNetworkService() {
        return networkService;
    }

    public ServerWorker(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void startServer(Services networkService) {
        ServerWorker.networkService = networkService;
        System.out.println("Server online!\n");
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(20);
            while (true) {
                Socket clientSocket = serverSocket.accept();

                System.out.println("New client connected!");

                executorService.execute(() -> {
                    try {
                        new ServerController(clientSocket).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeClient(Socket clientSocket) {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
