package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class YourEventsFrameController implements Initializable {
    
    private String user, password, eventName;
    private Date date;
    private String[] eventCategory = {"Primaria","Secundaria","Bachillerato","Universidad"};
    @FXML
    private ScrollPane scrollPaneEvents;
    @FXML
    private VBox vboxEvents;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnRegister;
    @FXML
    private Button btnTeams;
    private MySQLConnection connection = new MySQLConnection();
    @FXML
    private Label lblInfo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
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
    }

    public void inic() {
        btnTeams.setDisable(true);
        Connection con = null;
        boolean msg = false;
        try {
            connection.setUser(user);
            connection.setPassword(password);
            con = connection.MySQLConnection();
            CallableStatement sp = con.prepareCall("{call isEventsEmpty(?)}");
            sp.setNull(1, Types.VARCHAR);
            sp.executeUpdate();
            msg = Boolean.parseBoolean(sp.getString(1));
            if (msg) {
                sp = con.prepareCall("{call instituteGetEventNames(?)}");
                sp.setString(1, user);
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
                                Logger.getLogger(YourEventsFrameController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(YourEventsFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(YourEventsFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void eventInfo(String nombre) throws SQLException {
        btnTeams.setDisable(false);
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
            date = query.getDate(4);
        }
    }
    
    public void eventTeams (ActionEvent event)throws IOException{
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
