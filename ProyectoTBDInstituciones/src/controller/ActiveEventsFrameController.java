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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ActiveEventsFrameController implements Initializable {

    private String user, password, eventName, icon = "/images/icon.jpg";
    private String[] eventCategory = {"Primaria","Secundaria","Bachillerato","Universidad"};
    @FXML
    private ScrollPane scrollPaneEvents;
    @FXML
    private VBox vboxEvents;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnRegister;
    private MySQLConnection connection = new MySQLConnection();
    @FXML
    private Label lblInfo;
    @FXML
    private ComboBox comboCategory;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> options = FXCollections.observableArrayList("Todos los eventos", eventCategory[0], eventCategory[1], eventCategory[2],eventCategory[3]);
        comboCategory.setItems(options);
        comboCategory.getSelectionModel().selectFirst();
    }

    public void registerLabel() {
        if (connection.getUser() == "consulta") {
            btnRegister.setText("R E G I S T R A T E");
        }
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        if (!connection.getUser().equals("consulta")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menuFrame.fxml"));
            Parent root = loader.load();
            MenuFrameController controller = loader.getController();
            controller.setUser(user);
            controller.setPassword(password);
            controller.inic();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Menu");
            stage.setScene(scene);
            stage.show();
        } else {
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.close();
        }

    }
    
    public void inic() {
        btnRegister.setDisable(true);
        vboxEvents.getChildren().clear();
        Connection con = null;
        boolean msg = false;
        String categoria = comboCategory.getValue().toString();
        try {
            connection.setUser(user);
            connection.setPassword(password);
            con = connection.MySQLConnection();
            CallableStatement sp = con.prepareCall("{call isEventsEmpty(?)}");
            sp.setNull(1, Types.VARCHAR);
            sp.executeUpdate();
            msg = Boolean.parseBoolean(sp.getString(1));
            if (msg) {
                sp = con.prepareCall("{call getEventNamesByCategory(?)}");
                sp.setString(1, categoria);
                ResultSet query = sp.executeQuery();
                while (query.next()) {
                    String s = query.getString(1);
                    s = s.substring(0, s.length());
                    Button btnEvent = new Button(s);
                    EventHandler<ActionEvent> eventHandler;
                    eventHandler = new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            try {
                                eventInfo(btnEvent.getText());
                            } catch (SQLException ex) {
                                Logger.getLogger(ActiveEventsFrameController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    };
                    btnEvent.setAlignment(Pos.CENTER_LEFT);
                    btnEvent.setMinWidth(380);
                    btnEvent.setMinHeight(50);
                    btnEvent.setPrefWidth(380);
                    btnEvent.setPrefHeight(50);
                    btnEvent.getStylesheets().add("/view/style.css");
                    vboxEvents.getChildren().add(btnEvent);
                    btnEvent.setOnAction(eventHandler);
                }
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ActiveEventsFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ActiveEventsFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        registerLabel();
    }

    public void eventInfo(String nombre) throws SQLException {
        btnRegister.setDisable(false);
        eventName = nombre;
        Connection con = null;
        con = connection.MySQLConnection();
        CallableStatement sp = con.prepareCall("{call getEventByID(?)}");
        sp.setString(1, nombre);
        ResultSet query = sp.executeQuery();
        while (query.next()) {
            boolean[] valoresCatg = new boolean[4];
            valoresCatg[0] = query.getBoolean(6);
            valoresCatg[1] = query.getBoolean(7);
            valoresCatg[2] = query.getBoolean(8);
            valoresCatg[3] = query.getBoolean(9);
            String s = "Sede:\n%s\nHora de Inicio:\n%s\nHora de Finalizacion:\n%s\nFecha:\n%s\nCategorias:\n";
            String category = query.getString(10);
            s = String.format(s,
                    query.getString(5), query.getTime(2), 
                    query.getTime(3), query.getDate(4), category);
            for (int i = 0; i < valoresCatg.length; i++) {
                if (valoresCatg[i] == true) {
                    s += " • " + eventCategory[i] + "\n";
                }
            }
            s = s.substring(0, s.length() - 1);
            lblInfo.setText(s);
        }
    }
    
    public String getUser() {
        return user;
    }
    
    public void register(ActionEvent event) throws IOException, SQLException {
        if (connection.getUser() == "consulta") {
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.close();
        }
        else{
            Connection con = null;
            con = connection.MySQLConnection();
            CallableStatement sp = con.prepareCall("{call joinEvent(?,?,?)}");
            sp.setString(1, user);
            sp.setString(2, eventName);
            sp.registerOutParameter(3, Types.VARCHAR);
            sp.execute();
            String msg = sp.getString(3);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("");
            alert.setContentText(msg);
            alert.showAndWait();
        }
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
