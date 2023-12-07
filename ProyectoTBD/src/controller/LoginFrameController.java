package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LoginFrameController implements Initializable {

    private String user, password;
    private Stage panel;
    private Scene escena;
    private Boolean cambiar = null;
    private MySQLConnection connection = new MySQLConnection();
    @FXML
    private PasswordField txtContrasenna;
    @FXML
    private TextField txtUsuario;
    @FXML
    private Button btnIniciarSesion;
    @FXML
    private ToggleButton btnPassword;
    @FXML
    private Pane paneTop;
    @FXML
    private AnchorPane pane;
    @FXML
    private Label lblLoginTitle;
    @FXML
    private Label lblPasswordShow;

    public void cambiarPanel(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        String user = txtUsuario.getText();
        String password = txtContrasenna.getText();
        connection.setUser(user);
        connection.setPassword(password);
        connection.MySQLConnection();
        if (connection.isChange() == true) {
            if (user.equals("admin")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminMainFrame.fxml"));
                Parent root = loader.load();
                AdminMainFrameController controller = loader.getController();
                controller.setUser(user);
                controller.setPassword(password);
                controller.inic();
                Scene scene = new Scene(root);
                Stage stage;
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Menu");
                stage.setScene(scene);
                stage.show();
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/judgeParticipatingCategoryFrame.fxml"));
                Parent root = loader.load();
                JudgeParticipatingCategoryFrameController controller = loader.getController();
                controller.setUser(user);
                controller.setPassword(password);
                if (controller.inic()) {
                    Scene scene = new Scene(root);
                    Stage stage;
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setTitle("Menu");
                    stage.setScene(scene);
                    stage.show();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Usuario o contraseña inválido.");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());
            alert.getDialogPane().setMinHeight(100);
            alert.getDialogPane().setMinWidth(100);
            alert.getDialogPane().setPrefHeight(100);
            alert.getDialogPane().setPrefWidth(300);
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    @FXML
    void updateShowPassword() {
        lblPasswordShow.textProperty().bind(Bindings.concat(txtContrasenna.getText()));
    }

    @FXML
    void showPassword(ActionEvent event) {
        if (btnPassword.isSelected()) {
            lblPasswordShow.setVisible(true);
            updateShowPassword();
            btnPassword.setText("!");
        } else {
            lblPasswordShow.setVisible(false);
            btnPassword.setText("?");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblPasswordShow.setVisible(false);
    }
}
