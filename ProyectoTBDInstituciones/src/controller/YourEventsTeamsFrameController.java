package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

public class YourEventsTeamsFrameController implements Initializable {
    private Boolean activeDate;
    private Date date, curdate;
    private String user, password, eventName, teamName, category;
    @FXML
    private ScrollPane scrollPaneEvents;
    @FXML
    private VBox vboxEvents;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnAddTeam;
    private MySQLConnection connection = new MySQLConnection();
    @FXML
    private Label lblInfo;
    @FXML
    private Label lblTitulo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/YourEventsFrame.fxml"));
        Parent root = loader.load();
        YourEventsFrameController controller = loader.getController();
        controller.setUser(user);
        controller.setPassword(password);
        controller.inic();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Tus eventos");
        stage.setScene(scene);
        stage.show();
    }

    public void inic() {    
        lblTitulo.setText(eventName);
        Connection con = null;
        boolean msg = false;
        try {
            connection.setUser(user);
            connection.setPassword(password);
            con = connection.MySQLConnection();
            CallableStatement sp = con.prepareCall("{call isTeamsEmpty(?)}");
            sp.setNull(1, Types.VARCHAR);
            sp.executeUpdate();
            msg = Boolean.parseBoolean(sp.getString(1));
            if (msg) {
                sp = con.prepareCall("{call getTeams(?,?)}");
                sp.setString(1, user);
                sp.setString(2, eventName);
                ResultSet query = sp.executeQuery();
                while (query.next()) {
                    String s = "Nombre: %s\nCategoria: %s";
                    String infoBtnTeamName = query.getString(1);
                    String infoBtnCategory = query.getString(2);
                    s = String.format(s, infoBtnTeamName,infoBtnCategory);
                    s = s.substring(0, s.length());
                    Button btnEvent = new Button(s);
                    EventHandler<ActionEvent> eventHandler;
                    eventHandler = new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            try {
                                teamName = infoBtnTeamName;
                                category = infoBtnCategory;
                                teamInfo(teamName, eventName, category);
                            } catch (SQLException ex) {
                                Logger.getLogger(YourEventsTeamsFrameController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    };
                    btnEvent.setAlignment(Pos.CENTER_LEFT);
                    btnEvent.setMinWidth(380);
                    btnEvent.setMinHeight(80);
                    btnEvent.setPrefWidth(380);
                    btnEvent.setPrefHeight(80);
                    btnEvent.getStylesheets().add("/view/style.css");
                    vboxEvents.getChildren().add(btnEvent);
                    btnEvent.setOnAction(eventHandler);
                }
            }
            sp.close();
            con.close();
            date();
        } catch (SQLException ex) {
            Logger.getLogger(YourEventsTeamsFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(YourEventsTeamsFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void teamInfo(String teamName, String eventName, String category) throws SQLException {
        Connection con = null;
        con = connection.MySQLConnection();
        CallableStatement sp = con.prepareCall("{call getTeamsInfo(?,?,?)}");
        CallableStatement fun = con.prepareCall("{? = call getTeamCoach(?,?,?)}");
        fun.registerOutParameter(1, Types.VARCHAR);
        sp.setString(1, teamName); 
        fun.setString(2, teamName);
        sp.setString(2, eventName); 
        fun.setString(3, eventName);
        sp.setString(3, category); 
        fun.setString(4, category);
        ResultSet query = sp.executeQuery();
        fun.execute();
        String lbl = "";
        String alumnos = "Alumnos:\n";
        String docente = "Docente: \n%s";
        docente = String.format(docente, fun.getString(1));
        while (query.next()) {
            String s = "%s edad:%s\n";
            s = String.format(s,query.getString(1),query.getString(2)); 
//                    queryf.getString(1));
            s = s.substring(0, s.length());
            alumnos += s;
        }
        lbl += alumnos + docente;
        lblInfo.setText(lbl);
    }
    
    public void addTeam(ActionEvent event)throws IOException, SQLException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addTeamFrame.fxml"));
        Parent root = loader.load();
        addTeamFrameController controller = loader.getController();
        controller.setUser(user);
        controller.setPassword(password);
        controller.setEventName(eventName);
        controller.setDate(date);
        controller.inic();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Añadir Equipo");
        stage.setScene(scene);
        stage.show();
    }

    public void date() throws SQLException{
        Connection con = null;
        con = connection.MySQLConnection();
        CallableStatement sp = con.prepareCall("{? = call getCurdate()}");
        sp.registerOutParameter(1, Types.DATE);
        sp.execute();
        curdate = sp.getDate(1);
        if(curdate.after(date)){
            btnAddTeam.setDisable(true);
        }
        sp.close();
        con.close();
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
