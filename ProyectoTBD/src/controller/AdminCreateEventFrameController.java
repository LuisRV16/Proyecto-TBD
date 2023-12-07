package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminCreateEventFrameController implements Initializable {

    private String user, password;
    @FXML
    private Label lblTitle;
    @FXML
    private TextField txtName;
    @FXML
    private DatePicker dpDate; // Calendario para seleccionar la fecha del evento
    @FXML
    private ComboBox<String> comboSede;
    @FXML
    private ComboBox<String> comboHInic; // ComboBox para ingresar la hora inicial
    @FXML
    private ComboBox<String> comboMInic; // ComboBox para ingresar el minuto inicial
    @FXML
    private ComboBox<String> comboFinH; // ComboBox para ingresar la hora final
    @FXML
    private ComboBox<String> comboFinM; // ComboBox para ingresar el minuto final
    @FXML
    private CheckBox checkP; // CheckBox para seleccionar la categoria primaria
    @FXML
    private CheckBox checkS; // CheckBox para seleccionar la categoria secundaria
    @FXML
    private CheckBox checkB; // CheckBox para seleccionar la categoria primaria
    @FXML
    private CheckBox checkU; // CheckBox para seleccionar la categoria primaria
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnGoBack;
    @FXML
    private Label lblMsg;
    @FXML
    private Label lblHoraI;
    @FXML
    private Label lblHoraF;
    private MySQLConnection connection = new MySQLConnection();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void inic() {
        dpDate.setValue(LocalDate.now().plusDays(8));
        dpDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now().plusDays(8)));
            }
        });
        Connection con = null;
        connection.setUser(user);
        connection.setPassword(password);
        boolean msg = false;
        try {
            con = connection.MySQLConnection();
            CallableStatement sp = con.prepareCall("{call isCampusEmpty(?)}");
            sp.setNull(1, Types.VARCHAR);
            sp.executeUpdate();
            msg = Boolean.parseBoolean(sp.getString(1));

            if (msg) {
                sp = con.prepareCall("{call getCampus}");
                ResultSet query = sp.executeQuery();
                ObservableList<String> sedes = FXCollections.observableArrayList();
                while (query.next()) {
                    sedes.add(query.getString(1));
                }
                comboSede.setItems(sedes);
            }
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
        ObservableList<String> horas = FXCollections.observableArrayList();
        for (int i = 0; i <= 23; i++) {
            horas.add(String.format("%02d", i));
        }
        ObservableList<String> minutos = FXCollections.observableArrayList();
        for (int i = 0; i <= 59; i++) {
            minutos.add(String.format("%02d", i));
        }
        comboHInic.setItems(horas);
        comboMInic.setItems(minutos);
        comboFinH.setItems(horas);
        comboFinM.setItems(minutos);
        comboHInic.setDisable(false);
        comboMInic.setDisable(true);
    }

    @FXML
    private void add(ActionEvent event) {
        Connection con = null;
        String name = txtName.getText();
        String msg = "";
        if (specialChar(name)) {
            lblMsg.setText("El nombre contiene caracteres invalidos");
        } else if (txtName.getText().length() > 80) {
            lblMsg.setText("El nombre excede el maximo de 80 caracteres");
        } else if (txtName.getText().length() < 3) {
            lblMsg.setText("El nombre no alcanza el minimo de 3 caracteres");
        } else {
            try {

                con = connection.MySQLConnection();
                CallableStatement sp = con.prepareCall("{call addEvent(?,?,?,?,?,?,?,?,?,?,?)}"); // 11 parametros en el procedimiento

                // Nombre del evento
                // Nombre de la sede
                String campus = comboSede.getValue();

                // Hora inicial dividida en hora y minuto
                int inicH = Integer.parseInt(comboHInic.getValue());
                int inicM = Integer.parseInt(comboMInic.getValue());
                //Hora final dividida en hora y minuto
                int finH = Integer.parseInt(comboFinH.getValue());
                int finM = Integer.parseInt(comboFinM.getValue());

                // Parametros booleanos para las categorias del evento
                boolean primaria = checkP.isSelected();
                boolean secundaria = checkS.isSelected();
                boolean bachillerato = checkB.isSelected();
                boolean universidad = checkU.isSelected();

                // Estado predefinido del evento
                String status = "Vigente";

                // Se crea un objeto que almacene la hora inicial
                LocalTime time = LocalTime.of(inicH, inicM, 0);
                // Posteriormente se realiza la conversion a milisegundos de la hora inicial

                /*
                c es una constante de tiempo para los milisegundos esto debido a
                que al ingresar solamente la variable mili en el constructor de
                la clase Time, la hora que se selecciono se desfasa a 18 horas
                mas de las que deberia. Utilizando la constante en el constructor
                esas 18 horas son restadas y la hora seleccionada se ingresa
                correctamente.
                 */
                long c = 3600000 * 18;

                long mili = time.toNanoOfDay() / 1000000;

                /*Se crea el objeto de la clase Time y se ingresa por constructor
              el valor de la hora inicial.*/
                Time initialH = new Time(mili - c);

                // Se crea un objeto que almacene la hora final
                time = LocalTime.of(finH, finM, 0);
                // Posteriormente se realiza la conversion a milisegundos de la hora final
                mili = mili = time.toNanoOfDay() / 1000000;

                /* 
               Se crea el objeto de la clase Time y se ingresa por constructor
               el valor de la hora final.
                 */
                Time finalH = new Time(mili - c);

                sp.setString(1, name);
                sp.setTime(2, initialH);
                sp.setTime(3, finalH);
                sp.setDate(4, Date.valueOf(dpDate.getValue()));
                sp.setString(5, campus);
                sp.setBoolean(6, primaria);
                sp.setBoolean(7, secundaria);
                sp.setBoolean(8, bachillerato);
                sp.setBoolean(9, universidad);
                sp.setString(10, status);
                sp.setNull(11, Types.VARCHAR);

                sp.executeUpdate();

                msg = sp.getString(11);
                lblMsg.setText(msg);
                sp.close();
                con.close();

            } catch (SQLException | NumberFormatException ex) {
                if (ex.getClass().getSimpleName().equals("NumberFormatException")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    alert.setContentText("Seleccione una hora correcta");
                    alert.showAndWait();
                } else {
                    Logger.getLogger(AdminAddCampusFrameController.class.getName()).log(Level.SEVERE, null, ex);
                }
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
    private void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminMainFrame.fxml"));
        Parent root = loader.load();
        AdminMainFrameController controller = loader.getController();
        controller.setUser(user);
        controller.setPassword(password);
        controller.inic();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public static boolean specialChar(String cadena) {
        return !cadena.matches("[-áéíóúÁÉÍÓÚa-zA-ZñÑ0-9/\" ]+");
    }

    @FXML
    private void unblockMinic(ActionEvent event) {
        comboMInic.setDisable(false);
    }

    @FXML
    private void unblockFinalM(ActionEvent event) {
        comboFinM.setDisable(false);
    }

    @FXML
    private void unblockFinalH(ActionEvent event) {
        comboFinH.setDisable(false);
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
