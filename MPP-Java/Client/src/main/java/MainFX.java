
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.Services;
import controller.IController;

public class MainFX extends Application {

    private static Services services;
    private static MainFX mainFX;

    public static MainFX getInstance(Services services) {
        if (mainFX == null) {
            mainFX = new MainFX();
            MainFX.services = services;
        }
        return mainFX;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/views/LoginWindow.fxml"));
        AnchorPane root = loader.load();
        IController controller = loader.getController();
        controller.setService(services);


        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    public void run() {
        main(null);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
