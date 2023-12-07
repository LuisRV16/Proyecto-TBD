
package controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
public class AdminCreateJudgeFrameController implements Initializable {
    private char[] caracteresEspeciales = {'\\', '|', '/', '\'', '\"', ' '};
    @FXML
    private Label lblTitle;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtInstitution;
    @FXML
    private TextField txtAcadLevel;
    @FXML
    private TextField txtPassword;
    @FXML
    private Label lblMsg;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnGoBack;
    
    private MySQLConnection connection = new MySQLConnection();
    
    private String user, password;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void register(ActionEvent event) throws SQLException{
        Connection con = null;
        String msg;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("");
        if (specialChar(txtName.getText())) {
            lblMsg.setText("El nombre del juez solo acepta letras");
        } 
        else if(passwordSpecialChar(txtPassword.getText())){
            lblMsg.setText("La contraseña contiene caracteres invalidos");
        }
        else if(institutionSpecialChar(txtInstitution.getText())){
            lblMsg.setText("La Institución contiene caracteres invalidos");
        }
        else if(specialChar(txtAcadLevel.getText())){
            lblMsg.setText("El nivel academico contiene caracteres invalidos");
        }
        else if (txtPassword.getText().length() > 32) {
            lblMsg.setText("La contraseña supera el maximo de 32 caracteres");
        }
        else if (txtName.getText().length() > 32) {
            lblMsg.setText("El nombre supera el maximo de 32 caracteres");
        }
        else if (txtInstitution.getText().length() > 32) {
            lblMsg.setText("La Institucion supera el maximo de 32 caracteres");
        }
        else if (txtAcadLevel.getText().length() > 20) {
            lblMsg.setText("El grado academico supera el maximo de 20 caracteres");
        }
        else if (txtPassword.getText().length() <= 8) {
            lblMsg.setText("La contraseña no alcanza el minimo de 9 caracteres");
        }
        else if (txtName.getText().length() < 3) {
            lblMsg.setText("El nombre no alcanza el minimo de 3 caracteres");
        }
        else if (txtInstitution.getText().length() < 3) {
            lblMsg.setText("La Institucion no alcanza el minimo de 3 caracteres");
        }
        else if (txtAcadLevel.getText().length() < 3) {
            lblMsg.setText("El grado academico no alcanza el minimo de 3 caracteres");
        }
        else {
            connection.setUser(user);
            connection.setPassword(password);
            con = connection.MySQLConnection();
            CallableStatement sp = con.prepareCall("{call addJudge(?,?,?,?,?)}");
            sp.setString(1, txtName.getText());
            sp.setString(2, txtInstitution.getText());
            sp.setString(3, txtAcadLevel.getText());
            sp.setString(4, txtPassword.getText());
            sp.registerOutParameter(5, Types.VARCHAR);
            sp.execute();
            msg = sp.getString(5);
            lblMsg.setText(msg);
            sp.close();
            con.close();
        }
        
    }
    
    @FXML
    private void goBack(ActionEvent event) {
        Stage stage = (Stage) btnGoBack.getScene().getWindow();
        stage.close();
    }
    public static boolean specialChar(String cadena) {
        return !cadena.matches("[áéíóúÁÉÍÓÚa-zA-ZñÑ ]+");
    }
    public static boolean passwordSpecialChar(String cadena) {
        return !cadena.matches("[!#$&a-zA-ZñÑ0-9]+");
    }
    public static boolean institutionSpecialChar(String cadena) {
        return !cadena.matches("[áéíóúÁÉÍÓÚa-zA-ZñÑ0-9 ]+");
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
