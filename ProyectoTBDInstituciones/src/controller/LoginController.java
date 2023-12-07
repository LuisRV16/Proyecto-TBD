package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController implements Initializable {

    private static final String url = "jdbc:mysql://localhost/concursoVEX";
    private String user = "consulta", password, icon = "/images/icon.jpg";
    private char[] caracteresEspeciales = {'\\', '|', '/', '\'', '\"', ' '};
    private Stage panel;
    private Scene escena;
    private Boolean cambiar = null, create = null;
    private MySQLConnection connection = new MySQLConnection();
    @FXML
    private Button btnIniciarSesion;
    @FXML
    private Button btnEvents;
    @FXML
    private Button btnRegistrar;
    @FXML
    private ToggleButton btnPassword;
    @FXML
    private ToggleButton btnPasswordRegister;
    @FXML
    private Pane paneTop;
    @FXML
    private PasswordField txtContrasenna;
    @FXML
    private PasswordField txtPasswordRegister;
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtUserRegister;
    @FXML
    private TextField txtAdressRegister;
    @FXML
    private Label lblLoginTitle;
    @FXML
    private ImageView wallpaper;
    @FXML
    private Label lblError;
    @FXML
    private Label lblPasswordShow;
    @FXML
    private Label lblPasswordShowRegister;
    @FXML
    private ComboBox comboEvent;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        lblPasswordShow.setVisible(false);
        lblPasswordShowRegister.setVisible(false);
        TranslateTransition anim = new TranslateTransition();
        anim.setNode(wallpaper);
        anim.setDuration(Duration.millis(50000));
        anim.setCycleCount(TranslateTransition.INDEFINITE);
        anim.setByX(535);
        anim.play();
        try {
            activeEventsCombo();

        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void iniciarSesion(ActionEvent event) throws IOException {
        connection.setUser(txtUsuario.getText());
        connection.setPassword(txtContrasenna.getText());
        connection.MySQLConnection();
        if (connection.isChange() == true) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menuFrame.fxml"));
            Parent root = loader.load();
            MenuFrameController controller = loader.getController();
            controller.setUser(txtUsuario.getText());
            controller.setPassword(txtContrasenna.getText());
            controller.inic();
            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            lblError.setText("Error al iniciar sesión");
            user = "consulta";
        }
    }

    public void activeEvents(ActionEvent event) throws IOException {
        connection.setUser("consulta");
        connection.MySQLConnection();
        if (connection.isChange() == true) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/activeEventsFrame.fxml"));
            Parent root = loader.load();
            ActiveEventsFrameController controller = loader.getController();
            controller.setUser("consulta");
            controller.inic();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image(icon));
            stage.setTitle("Eventos Activos");
            stage.setScene(scene);
            stage.showAndWait();
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

    @FXML
    void updateShowPasswordRegister() {
        lblPasswordShowRegister.textProperty().bind(Bindings.concat(txtPasswordRegister.getText()));
    }

    @FXML
    void showPasswordRegister(ActionEvent event) {
        if (btnPasswordRegister.isSelected()) {
            lblPasswordShowRegister.setVisible(true);
            updateShowPasswordRegister();
            btnPasswordRegister.setText("!");
        } else {
            lblPasswordShowRegister.setVisible(false);
            btnPasswordRegister.setText("?");
        }
    }

    public void activeEventsCombo() throws SQLException {
        Connection con = null;
        connection.setUser(user);
        con = connection.MySQLConnection();
        CallableStatement sp = con.prepareCall("{call isEventsEmpty(?)}");
        sp.setNull(1, Types.VARCHAR);
        boolean msg = false;
        sp.executeUpdate();
        msg = Boolean.parseBoolean(sp.getString(1));
        if (msg) {
            sp = con.prepareCall("{call getActiveEvent}");
            ResultSet query = sp.executeQuery();
            ObservableList<String> events = FXCollections.observableArrayList();
            while (query.next()) {
                events.add(query.getString(1));
            }
            comboEvent.setItems(events);
            con.close();
        }
        connection.setUser("");
    }

    public void register() throws SQLException {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        
        alert.getDialogPane().setMinHeight(100);
        alert.getDialogPane().setMinWidth(100);
        alert.getDialogPane().setPrefHeight(100);
        alert.getDialogPane().setPrefWidth(300);
        alert.setHeaderText(null);

        String msg;
        alert.setTitle("");
        if (specialChar(txtUserRegister.getText())) {
            alert.setContentText("El nombre del usuario contiene caracteres invalidos");
        } else if (passwordSpecialChar(txtPasswordRegister.getText())) {
            alert.setContentText("La contraseña contiene caracteres invalidos");
        } else if (addressSpecialChar(txtAdressRegister.getText())) {
            alert.setContentText("La dirección contiene caracteres invalidos");
        }else if (txtPasswordRegister.getText().length() < 8) {
            alert.setContentText("La contraseña no alcanza el minimo de 8 caracteres");
        }else if (txtUserRegister.getText().length() < 2) {
            alert.setContentText("El usuario no alcanza el minimo de 2 caracteres");
        }else if (txtAdressRegister.getText().length() < 5) {
            alert.setContentText("La dirección no alcanza el minimo de 5 caracteres");
        }else if (txtPasswordRegister.getText().length() > 32) {
            alert.setContentText("La contraseña excede el maximo de 32 caracteres");
        }else if (txtUserRegister.getText().length() > 32) {
            alert.setContentText("El usuario excede el maximo de 32 caracteres");
        }else if (txtAdressRegister.getText().length() > 100) {
            alert.setContentText("La dirección excede el maximo de 100 caracteres");
        }else {
            Connection con = null;
            connection.setUser(user);
            con = connection.MySQLConnection();
            CallableStatement sp = con.prepareCall("{call userInstitution(?,?,?,?,?)}");
            sp.setString(1, txtUserRegister.getText());
            sp.setString(2, comboEvent.getValue().toString());
            sp.setString(3, txtAdressRegister.getText());
            sp.setString(4, txtPasswordRegister.getText());
            sp.setNull(5, Types.VARCHAR);
            sp.execute();
            msg = sp.getString(5);
            alert.setContentText(msg);
            con.close();
        }
        alert.showAndWait();
    }

    public static boolean specialChar(String cadena) {
        return !cadena.matches("[áéíóúa-zA-ZñÑ ]+");
    }

    public static boolean passwordSpecialChar(String cadena) {
        return !cadena.matches("[!#$%&a-zA-ZñÑ0-9]+");
    }
    
    public static boolean addressSpecialChar(String cadena) {
        return !cadena.matches("[.a-zA-ZñÑ0-9 ]+");
    }
}
