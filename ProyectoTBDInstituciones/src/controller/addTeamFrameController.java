package controller;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class addTeamFrameController {

    private Date date;
    private String[] eventCategory = {"Primaria", "Secundaria", "Bachillerato", "Universidad"};
    private String user, password, eventName;
    private Boolean primaria, secundaria, bachillerato, universidad;
    @FXML
    private Button btnGoBack;
    @FXML
    private Button btnSubmit;
    @FXML
    private TextField txtTeamName;
    @FXML
    private TextField txtCoachName;
    @FXML
    private TextField txtCoachMail;
    @FXML
    private TextField txtStudent1;
    @FXML
    private TextField txtStudent2;
    @FXML
    private TextField txtStudent3;
    @FXML
    private TextField txtAge1;
    @FXML
    private TextField txtAge2;
    @FXML
    private TextField txtAge3;
    @FXML
    private ComboBox comboCategory;
    @FXML
    private Label lblError;
    private MySQLConnection connection = new MySQLConnection();

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/yourEventsTeamsFrame.fxml"));
        Parent root = loader.load();
        YourEventsTeamsFrameController controller = loader.getController();
        controller.setUser(user);
        controller.setPassword(password);
        controller.setEventName(eventName);
        controller.setDate(date);
        controller.inic();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Equipos");
        stage.setScene(scene);
        stage.show();
    }

    public void inic() throws SQLException {
        connection.setUser(user);
        connection.setPassword(password);
        connection.MySQLConnection();
        categoryCombo();
    }

    public void createTeam() throws SQLException {
        Connection con = null;
        String msg = "";
        con = connection.MySQLConnection();
        CallableStatement sp = con.prepareCall("{call addTeam(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
        sp.setString(1, eventName);
        sp.setString(2, user);
        sp.setString(3, txtCoachName.getText());
        sp.setString(4, txtCoachMail.getText());
        sp.setString(5, txtTeamName.getText());
        sp.setString(6, comboCategory.getValue().toString());
        sp.setString(7, txtStudent1.getText());
        sp.setString(8, txtAge1.getText());
        sp.setString(9, txtStudent2.getText());
        sp.setString(10, txtAge2.getText());
        sp.setString(11, txtStudent3.getText());
        sp.setString(12, txtAge3.getText());
        sp.setNull(13, Types.VARCHAR);
        //Longitud estudiantes
        if (txtCoachMail.getText().length() <= 3) {
            lblError.setText("El correo debe tener mas de 3 caracteres");
        } else if (txtCoachMail.getText().length() > 100) {
            lblError.setText("El correo debe tener menos de 100 caracteres");
        } else if (spaceCheck(txtCoachMail.getText())) {
            lblError.setText("El correo no debe tener espacios en blanco");
        } else if (!mailValidation(txtCoachMail.getText())) {
            lblError.setText("En el correo falta o sobra una @");
        }   else if (mailSpecialChar(txtCoachMail.getText())) {
            lblError.setText("El correo tiene caracteres invalidos");
        }  else if (mailValidationDotPosition(txtCoachMail.getText())) {
            lblError.setText("En el correo no puedes poner . al inicio o final");
        } else if (!mailValidationDot(txtCoachMail.getText())) {
            lblError.setText("En el correo falta el . despues de la @");
        } else if (txtStudent1.getText().length() > 50) {
            lblError.setText("El nombre del primer estudiante excede el maximo de 50 caracteres");
        } else if (txtStudent2.getText().length() > 50) {
            lblError.setText("El nombre del segundo estudiante excede el maximo de 50 caracteres");
        } else if (txtStudent3.getText().length() > 50) {
            lblError.setText("El nombre del tercer estudiante excede el maximo de 50 caracteres");
        } else if (txtStudent1.getText().length() < 3) {
            lblError.setText("El nombre del primer estudiante no alcanza el minimo de 3 caracteres");
        } else if (txtStudent2.getText().length() < 3) {
            lblError.setText("El nombre del segundo estudiante no alcanza el minimo de 3 caracteres");
        } else if (txtStudent3.getText().length() < 3) {
            lblError.setText("El nombre del tercer estudiante no alcanza el minimo de 3 caracteres");
        } //Longitud asesor
        else if (txtCoachName.getText().length() > 50) {
            lblError.setText("El nombre del asesor excede el maximo de 50 caracteres");
        } else if (txtCoachName.getText().length() < 3) {
            lblError.setText("El nombre del asesor no alcanza el minimo de 3 caracteres");
        } //Caracteres
        else if (specialChar(txtCoachName.getText())) {
            lblError.setText("El nombre del coach tiene caracteres invalidos");
        } else if (specialChar(txtStudent1.getText())) {
            lblError.setText("El nombre del primer estudiante tiene caracteres invalidos");
        } else if (specialChar(txtStudent2.getText())) {
            lblError.setText("El nombre del segundo estudiante tiene caracteres invalidos");
        } else if (specialChar(txtStudent3.getText())) {
            lblError.setText("El nombre del tercer estudiante tiene caracteres invalidos");
        } else if (ageCharVerify(txtAge1.getText())) {
            lblError.setText("La edad del primer estudiante es invalida");
        } else if (ageCharVerify(txtAge2.getText())) {
            lblError.setText("La segundo del primer estudiante es invalida");
        } else if (ageCharVerify(txtAge3.getText())) {
            lblError.setText("La tercer del primer estudiante es invalida");
        }
        else {
            sp.execute();
            msg = sp.getString(13);
            lblError.setText(msg);
        }
    }
    /*
    Reglamento del correo
        Letras (mayúsculas y minúsculas): A-Z, a-z
        Números: 0-9
        Caracteres especiales permitidos en el nombre de usuario: ! # $ % & ' * + / = ? ^ _ { | } ~`
        Punto (.): Permitido en el nombre de usuario y en el dominio, pero no al principio ni al final del nombre de usuario.
     */
    public static boolean specialChar(String cadena) {
        return !cadena.matches("[áéíóúÁÉÍÓÚa-zA-ZñÑ ]+");
    }

    public static boolean spaceCheck(String cadena) {
        return cadena.matches("[ ]+");
    }

    public static boolean mailSpecialChar(String cadena) {
        return !cadena.matches("[@!#$%&\'*+=/?^|{}._a-zA-Z0-9]+");
    }
    
    public static boolean mailValidationDot(String cadena) {
        //String regex = "^[a-zA-Z0-9_!#$%&'*+/=?{|}^.-]+@[a-zA-Z0-9.-]+$";
        return cadena.matches("^[a-zA-Z0-9_!#$%&'*+/=?{|}^.-]+@[a-zA-Z0-9]+.[a-z]+$");
        //return cadena.matches("(.*)@(.*).(.*)");
        //return cadena.matches("^(.+)@(.+)$");
        //return cadena.matches("^[a-zA-Z0-9_!#$%&'*+/=?{|}^.-]+@[a-zA-Z0-9.-]+$");
    }

    public static boolean mailValidation(String cadena) {
        int c = 0;
        for (char character : cadena.toCharArray()) {
            if (character == '@') {
                c++;
            }
        }
        return c == 1;
    }

    public static boolean mailValidationDotPosition(String cadena) {
        return (cadena.startsWith(".") || cadena.endsWith("."));
    }

    public static boolean ageCharVerify(String cadena) {
        return !cadena.matches("[0-9]+");
    }

    public String getUser() {
        return user;
    }

    public void categoryCombo() throws SQLException {
        Connection con = null;
        con = connection.MySQLConnection();
        CallableStatement sp = con.prepareCall("{call getCategory(?,?,?,?,?)}");
        sp.setString(1, eventName);
        sp.registerOutParameter(2, Types.BOOLEAN);
        sp.registerOutParameter(3, Types.BOOLEAN);
        sp.registerOutParameter(4, Types.BOOLEAN);
        sp.registerOutParameter(5, Types.BOOLEAN);
        sp.execute();
        if (sp.getBoolean(2) == true) {
            comboCategory.getItems().addAll("Primaria");
        }
        if (sp.getBoolean(3) == true) {
            comboCategory.getItems().addAll("Secundaria");
        }
        if (sp.getBoolean(4) == true) {
            comboCategory.getItems().addAll("Bachillerato");
        }
        if (sp.getBoolean(5) == true) {
            comboCategory.getItems().addAll("Universidad");
        }
        comboCategory.getSelectionModel().selectFirst();
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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
