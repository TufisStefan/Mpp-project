package controller;

import domain.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.Services;
import services.ServicesException;

import java.io.IOException;

public class LoginWindowController extends IController{
    public static User loggedUser = null;

    @FXML
    TextField usernameTextField;

    @FXML
    PasswordField passwordTextField;

    @FXML
    Button loginButton;

    @FXML
    public void initialize() {

    }

    public void handleLogin(MouseEvent mouseEvent) throws IOException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        try{
            loggedUser = services.login(username, password);
            //new Alert(Alert.AlertType.CONFIRMATION, "Login Successful!").show();
            Stage loginStage = (Stage) loginButton.getScene().getWindow();
            loginStage.close();

            FXMLLoader loader = new FXMLLoader();
            Stage mainStage = new Stage();
            loader.setLocation(this.getClass().getResource("/views/MainWindow.fxml"));
            AnchorPane root = loader.load();
            IController controller = loader.getController();
            controller.setService(services);

            mainStage.setScene(new Scene(root));
            mainStage.setTitle("Main");
            mainStage.show();
        }
        catch (ServicesException ex){
            new Alert(Alert.AlertType.ERROR, "Invalid username or password!").show();
        }

    }
}
