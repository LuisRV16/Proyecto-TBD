package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author manue
 */
public class AdminSetJuryFrameController implements Initializable {

    private String user, password;

    private String idEvent;

    @FXML
    private Button btnPrimaria;
    @FXML
    private Button btnSecundaria;
    @FXML
    private Button btnBachillerato;
    @FXML
    private Button btnUniversidad;
    @FXML
    private Label lblTitle;

    private MySQLConnection connection = new MySQLConnection();
    @FXML
    private Button btnGoBack;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public void inic() {
        Connection con = null;

        connection.setUser(user);
        connection.setPassword(password);

        try {
            con = connection.MySQLConnection();

            CallableStatement sp = con.prepareCall("{call getEventByID(?)}");

            sp.setString(1, idEvent);

            ResultSet query = sp.executeQuery();

            while (query.next()) {
                if (query.getBoolean(6)) btnPrimaria.setDisable(false);
                if (query.getBoolean(7)) btnSecundaria.setDisable(false);
                if (query.getBoolean(8)) btnBachillerato.setDisable(false);
                if (query.getBoolean(9)) btnUniversidad.setDisable(false);
            }

        } catch (SQLException ex) {
            Logger.getLogger(AdminSetJuryFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminSetJuryFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    private void frameAddJury(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminAddJuryFrame.fxml"));
            Parent root = loader.load();
            AdminAddJuryFrameController controller = loader.getController();
            controller.setUser(user);
            controller.setPassword(password);
            controller.setIdEvent(idEvent);
            controller.setCategory(((Button) event.getSource()).getText().toLowerCase());
            controller.inic();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminAddJuryFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        Stage stage = (Stage) btnGoBack.getScene().getWindow();
        stage.close();

    }

}
