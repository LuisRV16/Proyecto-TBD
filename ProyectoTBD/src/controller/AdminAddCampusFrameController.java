package controller;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author manue
 */
public class AdminAddCampusFrameController implements Initializable {

    @FXML
    private Label lblTitle;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAddress;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnGoBack;
    @FXML
    private Label lblMsg;

    private String user, password;

    private MySQLConnection connection = new MySQLConnection();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void addCampus(ActionEvent event) {
        if(txtName.getText().length() > 50){
            lblMsg.setText("El nombre excede el maximo de 50 caracteres");
        }
        else if(txtAddress.getText().length() > 100){
            lblMsg.setText("La direccion excede el maximo de 100 caracteres");
        }
        if(txtName.getText().length() < 3){
            lblMsg.setText("El nombre no alcanza el minimo de 3 caracteres");
        }
        else if(txtAddress.getText().length() < 3){
            lblMsg.setText("la direccion no alcanza el minimo de 3 caracteres");
        }
        else if (nameSpecialChar(txtName.getText())) {
            lblMsg.setText("El nombre contiene caracteres no validos");
        } else if (addressSpecialChar(txtAddress.getText())) {
            lblMsg.setText("La dirección contiene caracteres no validos");
        } else {
            Connection con = null;
            connection.setUser(user);
            connection.setPassword(password);
            try {
                String msg;
                con = connection.MySQLConnection();
                CallableStatement sp = con.prepareCall("{call addCampus(?,?,?)}");

                sp.setString(1, txtName.getText());
                sp.setString(2, txtAddress.getText());
                sp.setNull(3, Types.VARCHAR);

                sp.execute();

                msg = sp.getString(3);

                lblMsg.setText(msg);

                sp.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AdminAddCampusFrameController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(AdminAddCampusFrameController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        Stage stage = (Stage) btnGoBack.getScene().getWindow();
        stage.close();
    }

    public static boolean nameSpecialChar(String cadena) {
        return !cadena.matches("[áéíóúÁÉÍÓÚa-zA-ZñÑ0-9 ]+");
    }

    public static boolean addressSpecialChar(String cadena) {
        return !cadena.matches("[,.áéíóúÁÉÍÓÚa-zA-ZñÑ0-9 ]+");
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

}
