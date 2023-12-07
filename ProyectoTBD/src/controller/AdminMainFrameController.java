package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author manue
 */
public class AdminMainFrameController implements Initializable {

    public String user, password, icon = "/images/icon.jpg";
    @FXML
    private Button btnCreateEvent;
    @FXML
    private Button btnAddCampus;
    @FXML
    private Button btnCreateJudge;
    @FXML
    private ScrollPane scrollPaneEvents;

    private VBox vboxEvents[] = new VBox[6];

    public MySQLConnection connection = new MySQLConnection();
    @FXML
    private ComboBox<String> comboStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (int i = 0; i < vboxEvents.length; i++) {
            vboxEvents[i] = new VBox();
            vboxEvents[i].fillWidthProperty();
            vboxEvents[i].setMinWidth(427);
            vboxEvents[i].setMinHeight(277);
        }

        scrollPaneEvents.setContent(vboxEvents[0]);

        ObservableList<String> status = FXCollections.observableArrayList();

        status.add("Todos los eventos");
        status.add("Vigente");
        status.add("Cancelado");
        status.add("En espera");
        status.add("En proceso");
        status.add("Finalizado");

        comboStatus.setItems(status);
    }

    @FXML
    private void frameCreateEvent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminCreateEventFrame.fxml"));
            Parent root = loader.load();
            AdminCreateEventFrameController controller = loader.getController();
            controller.setUser(user);
            controller.setPassword(password);
            controller.inic();
            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Crear Evento");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminCreateEventFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void frameAddCampus(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminAddCampusFrame.fxml"));
            Parent root = loader.load();
            AdminAddCampusFrameController controller = loader.getController();
            controller.setUser(user);
            controller.setPassword(password);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image(icon));
            stage.setTitle("Añadir Sede");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(AdminAddCampusFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void frameCreateJudge(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminCreateJudgeFrame.fxml"));
            Parent root = loader.load();
            AdminCreateJudgeFrameController controller = loader.getController();
            controller.setUser(user);
            controller.setPassword(password);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image(icon));
            stage.setTitle("Añadir Juez");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(AdminCreateJudgeFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public void inic() {
        Connection con = null;
        boolean msg = false;
        // Este HashMap es para guardar el texto correspondiente a cada categoria
        HashMap<Integer, String> categorias = new HashMap<>();
        // Las key estan en orden del 0 al 3 debido a el posterior uso en un ciclo
        categorias.put(0, "Primaria");
        categorias.put(1, "Secundaria");
        categorias.put(2, "Bachillerato");
        categorias.put(3, "Universidad");
        try {
            connection.setUser(user);
            connection.setPassword(password);

            con = connection.MySQLConnection();

            CallableStatement sp = con.prepareCall("{call isEventsEmpty(?)}");
            sp.setNull(1, Types.VARCHAR);
            sp.executeUpdate();
            msg = Boolean.parseBoolean(sp.getString(1));
            
            sp.close();
            con.close();
            
            if (msg) {
                fillAllEvents(categorias);
                fillFiltersEvents(categorias);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminMainFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminMainFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void filter(ActionEvent event) {
        String value = comboStatus.getValue();

        switch (value) {
            case "Todos los eventos" ->
                scrollPaneEvents.setContent(vboxEvents[0]);
            case "Vigente" ->
                scrollPaneEvents.setContent(vboxEvents[1]);
            case "Cancelado" ->
                scrollPaneEvents.setContent(vboxEvents[2]);
            case "En espera" ->
                scrollPaneEvents.setContent(vboxEvents[3]);
            case "En proceso" ->
                scrollPaneEvents.setContent(vboxEvents[4]);
            case "Finalizado" ->
                scrollPaneEvents.setContent(vboxEvents[5]);
        }
    }

    private void fillAllEvents(HashMap<Integer, String> categorias) {
        Connection con = null;

        try {
            con = connection.MySQLConnection();
            CallableStatement sp = con.prepareCall("{call getEvent}");
            ResultSet query = sp.executeQuery();
            while (query.next()) {
                // Se declara un arreglo booleano con los valores de las categorias en la base de datos
                boolean[] valoresCatg = new boolean[4];
                // Se ingresan en orden desde primaria hasta universidad
                valoresCatg[0] = query.getBoolean(6);
                valoresCatg[1] = query.getBoolean(7);
                valoresCatg[2] = query.getBoolean(8);
                valoresCatg[3] = query.getBoolean(9);

                // Se podria decir que este es el molde para el texto en los botones
                String s = """
                               Evento: %s | Sede: %s 
                               Fecha: %s | Estado: %s
                               Hora Inicial: %s | Hora Final: %s
                               Categorias:""";
                /*
                      Se utiliza String.format para asignar el respectivo texto a cada
                      '%s' del molde.
                 */
                String idEvent = query.getString(1);
                String state = query.getString(10);
                s = String.format(s,idEvent, query.getString(5),query.getDate(4),state,query.getTime(2),query.getTime(3));

                /*
                      Que recorre el arreglo booleano y cada que un valor es true,
                      se obtiene el valor dentro del hashmap con el indice
                      correspondiente en el ciclo
                 */
                for (int i = 0; i < valoresCatg.length; i++) {
                    if (valoresCatg[i] == true) {
                        s += " [" + categorias.get(i) + "]";
                    }
                }
                s = s.substring(0, s.length());
                // Se van creando botones por cada registro en la tabla eventos
                Button btnEvent = new Button(s);
                btnEvent.setAlignment(Pos.CENTER_LEFT);
                btnEvent.setMinWidth(460);
                btnEvent.setMinHeight(100);
                btnEvent.setPrefWidth(460);
                btnEvent.setPrefHeight(100);
                btnEvent.getStylesheets().add("/view/style.css");
                // Se le asigna la accion al botón con una instancia HandlerEvent
                // al cual se le asigna el metodo
                EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Lógica del evento aquí
                        frameEvent(event, idEvent);
                    }

                    private void frameEvent(ActionEvent event, String idEvent) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminEventInfoFrame.fxml"));
                            Parent root = loader.load();
                            AdminEventInfoFrameController controller = loader.getController();

                            controller.setIdEvent(idEvent);
                            controller.setUser(user);
                            controller.setPassword(password);
                            controller.inic();

                            Scene scene = new Scene(root);
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException ex) {
                            Logger.getLogger(AdminEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };

                btnEvent.setOnAction(eventHandler);

                // Se ingresa cada boton al vbox
                vboxEvents[0].getChildren().add(btnEvent);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminMainFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminMainFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void fillFiltersEvents(HashMap<Integer, String> categorias) {
        Connection con = null;

        try {
            con = connection.MySQLConnection();
            CallableStatement sp = con.prepareCall("{call getEvent}");
            ResultSet query = sp.executeQuery();
            while (query.next()) {
                // Se declara un arreglo booleano con los valores de las categorias en la base de datos
                boolean[] valoresCatg = new boolean[4];
                // Se ingresan en orden desde primaria hasta universidad
                valoresCatg[0] = query.getBoolean(6);
                valoresCatg[1] = query.getBoolean(7);
                valoresCatg[2] = query.getBoolean(8);
                valoresCatg[3] = query.getBoolean(9);

                // Se podria decir que este es el molde para el texto en los botones
                String s = """
                               Evento: %s | Sede: %s 
                               Fecha: %s | Estado: %s
                               Hora Inicial: %s | Hora Final: %s
                               Categorias:""";

                /*
                      Se utiliza String.format para asignar el respectivo texto a cada
                      '%s' del molde.
                 */
                String idEvent = query.getString(1);
                String state = query.getString(10);
                s = String.format(s,idEvent, query.getString(5),query.getDate(4),state,query.getTime(2),query.getTime(3));

                /*
                      Que recorre el arreglo booleano y cada que un valor es true,
                      se obtiene el valor dentro del hashmap con el indice
                      correspondiente en el ciclo
                 */
                for (int i = 0; i < valoresCatg.length; i++) {
                    if (valoresCatg[i] == true) {
                        s += " [" + categorias.get(i) + "]";
                    }
                }

                // Se elimina el ultimo caracter, ya que es una coma (,)
                s = s.substring(0, s.length() - 1);

                // Se van creando botones por cada registro en la tabla eventos
                Button btnEvent = new Button(s);
                btnEvent.setAlignment(Pos.CENTER_LEFT);
                btnEvent.setMinWidth(460);
                btnEvent.setMinHeight(100);
                btnEvent.setPrefWidth(460);
                btnEvent.setPrefHeight(100);
                btnEvent.getStylesheets().add("/view/style.css");

                // Se le asigna la accion al botón con una instancia HandlerEvent
                // al cual se le asigna el metodo
                EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Lógica del evento aquí
                        frameEvent(event, idEvent);
                    }

                    private void frameEvent(ActionEvent event, String idEvent) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminEventInfoFrame.fxml"));
                            Parent root = loader.load();
                            AdminEventInfoFrameController controller = loader.getController();

                            controller.setIdEvent(idEvent);
                            controller.setUser(user);
                            controller.setPassword(password);
                            controller.inic();

                            Scene scene = new Scene(root);
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException ex) {
                            Logger.getLogger(AdminEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };

                btnEvent.setOnAction(eventHandler);

                // Se ingresa cada boton al vbox
                switch (state) {
                    case "Vigente" ->
                        vboxEvents[1].getChildren().add(btnEvent);
                    case "Cancelado" ->
                        vboxEvents[2].getChildren().add(btnEvent);
                    case "En espera" ->
                        vboxEvents[3].getChildren().add(btnEvent);
                    case "En proceso" ->
                        vboxEvents[4].getChildren().add(btnEvent);
                    case "Finalizado" ->
                        vboxEvents[5].getChildren().add(btnEvent);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminMainFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminMainFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
