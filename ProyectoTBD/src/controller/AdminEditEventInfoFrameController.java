package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ResourceBundle;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author manue
 */
public class AdminEditEventInfoFrameController implements Initializable {

    private String user, password, idEvent;

    private MySQLConnection connection = new MySQLConnection();

    @FXML
    private Label lblTitle;
    @FXML
    private TextField txtName;
    @FXML
    private DatePicker dpDate;
    @FXML
    private ComboBox<String> comboCampus;
    @FXML
    private ComboBox<String> comboHInic;
    @FXML
    private ComboBox<String> comboMInic;
    @FXML
    private ComboBox<String> comboFinalH;
    @FXML
    private ComboBox<String> comboFinalM;
    @FXML
    private CheckBox checkPrimaria;
    @FXML
    private CheckBox checkSecundaria;
    @FXML
    private CheckBox checkBachillerato;
    @FXML
    private CheckBox checkUniversidad;
    @FXML
    private Label lblStatusEvent;
    @FXML
    private ComboBox<String> comboStatus;
    @FXML
    private Button btnUpdate;
    @FXML
    private Label lblMsg;
    @FXML
    private Button btnGoBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void update(ActionEvent event) {
        String iHour = comboHInic.getValue() + ":" + comboMInic.getValue() + ":00";
        String fHour = comboFinalH.getValue() + ":" + comboFinalM.getValue() + ":00";

        Connection con = null;

        try {

            con = connection.MySQLConnection();

            CallableStatement sp = con.prepareCall("{call updateEvent(?,?,?,?,?,?,?,?,?,?,?)}");

            sp.setString(1, idEvent);
            sp.setTime(2, Time.valueOf(iHour));
            sp.setTime(3, Time.valueOf(fHour));
            sp.setDate(4, Date.valueOf(dpDate.getValue()));
            sp.setString(5, comboCampus.getValue());
            sp.setBoolean(6, checkPrimaria.isSelected());
            sp.setBoolean(7, checkSecundaria.isSelected());
            sp.setBoolean(8, checkBachillerato.isSelected());
            sp.setBoolean(9, checkUniversidad.isSelected());
            sp.setString(10, comboStatus.getValue());

            sp.registerOutParameter(11, Types.VARCHAR);

            sp.executeUpdate();

            String s = sp.getString(11);

            if (s.equals("El evento ha sido actualizado.")) {
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Mensaje");
                    alert.setContentText(s);
                    alert.showAndWait();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminEventInfoFrame.fxml"));
                    Parent root = loader.load();
                    AdminEventInfoFrameController controller = loader.getController();
                    controller.setUser(user);
                    controller.setPassword(password);
                    controller.setIdEvent(idEvent);
                    controller.inic();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(AdminEditEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                lblMsg.setText(s); //hazlo rojito
            }

        } catch (SQLException ex) {
            Logger.getLogger(AdminEditEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminEditEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminEventInfoFrame.fxml"));
            Parent root = loader.load();
            AdminEventInfoFrameController controller = loader.getController();
            controller.setUser(user);
            controller.setPassword(password);
            controller.setIdEvent(idEvent);
            controller.inic();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminEditEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            connection.setUser(user);
            connection.setPassword(password);
            con = connection.MySQLConnection();

            CallableStatement sp = con.prepareCall("{call getCampus()}");

            ResultSet query = sp.executeQuery();

            txtName.setText(idEvent);

            ObservableList<String> campus = FXCollections.observableArrayList();

            while (query.next()) {
                campus.add(query.getString(1));
            }

            comboCampus.setItems(campus);

            ObservableList<String> hours = FXCollections.observableArrayList();
            for (int i = 0; i <= 23; i++) {
                hours.add(String.format("%02d", i));
            }
            ObservableList<String> minutes = FXCollections.observableArrayList();
            for (int i = 0; i <= 59; i++) {
                minutes.add(String.format("%02d", i));
            }

            comboHInic.setItems(hours);
            comboMInic.setItems(minutes);
            comboFinalH.setItems(hours);
            comboFinalM.setItems(minutes);

            ObservableList<String> status = FXCollections.observableArrayList();

            status.add("Vigente");
            status.add("En espera");
            status.add("Finalizado");
            status.add("Cancelado");

            comboStatus.setItems(status);

            sp = con.prepareCall("{call getEventByID(?)}");

            sp.setString(1, idEvent);

            query = sp.executeQuery();

            while (query.next()) {
                comboCampus.getSelectionModel().select(query.getString(5));

                String initialH = query.getTime(2).toString().substring(0, 2);
                String initialM = query.getTime(2).toString().substring(3, 5);

                String finalH = query.getTime(3).toString().substring(0, 2);
                String finalM = query.getTime(3).toString().substring(3, 5);

                comboHInic.getSelectionModel().select(initialH);
                comboMInic.getSelectionModel().select(initialM);
                comboFinalH.getSelectionModel().select(finalH);
                comboFinalM.getSelectionModel().select(finalM);

                dpDate.setValue(query.getDate(4).toLocalDate());

                checkBoxPrimaria();
                checkBoxSecundaria();
                checkBoxBachillerato();
                checkBoxUniversidad();

                comboStatus.getSelectionModel().select(query.getString(10));

            }

            query.close();
            sp.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(AdminEditEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminEditEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    private void checkBoxPrimaria() {
        Connection con = null;
        try {
            con = connection.MySQLConnection();

            CallableStatement sp = con.prepareCall("{call getEventByID(?)}");

            sp.setString(1, idEvent);

            ResultSet query = sp.executeQuery();

            query = sp.executeQuery();

            while (query.next()) {
                boolean band = query.getBoolean(6);
                checkPrimaria.setSelected(band);
                checkPrimaria.setDisable(band);
            }

            query.close();
            sp.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(AdminEditEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminEditEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void checkBoxSecundaria() {
        Connection con = null;
        try {
            con = connection.MySQLConnection();

            CallableStatement sp = con.prepareCall("{call getEventByID(?)}");

            sp.setString(1, idEvent);

            ResultSet query = sp.executeQuery();

            query = sp.executeQuery();

            while (query.next()) {
                boolean band = query.getBoolean(7);
                checkSecundaria.setSelected(band);
                checkSecundaria.setDisable(band);
            }

            query.close();
            sp.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(AdminEditEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminEditEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void checkBoxBachillerato() {
        Connection con = null;
        try {
            con = connection.MySQLConnection();

            CallableStatement sp = con.prepareCall("{call getEventByID(?)}");

            sp.setString(1, idEvent);

            ResultSet query = sp.executeQuery();

            query = sp.executeQuery();

            while (query.next()) {
                boolean band = query.getBoolean(8);
                checkBachillerato.setSelected(band);
                checkBachillerato.setDisable(band);
            }

            query.close();
            sp.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(AdminEditEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminEditEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void checkBoxUniversidad() {
        Connection con = null;
        try {
            con = connection.MySQLConnection();

            CallableStatement sp = con.prepareCall("{call getEventByID(?)}");

            sp.setString(1, idEvent);

            ResultSet query = sp.executeQuery();

            query = sp.executeQuery();

            while (query.next()) {
                boolean band = query.getBoolean(9);
                checkUniversidad.setSelected(band);
                checkUniversidad.setDisable(band);
            }

            query.close();
            sp.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(AdminEditEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminEditEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
