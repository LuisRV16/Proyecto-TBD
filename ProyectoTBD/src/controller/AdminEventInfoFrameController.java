package controller;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author manue
 */
public class AdminEventInfoFrameController implements Initializable {
    public String user, password, icon = "/images/icon.jpg";
    @FXML
    private Pane pane;
    @FXML
    private Button btnEditInfo;
    @FXML
    private Button setJury;
    @FXML
    private TextArea txtaInfo;
    @FXML
    private ScrollPane scrollP;
    @FXML
    private Label lblTitle;
    private VBox[] filtro = new VBox[5];

    @FXML
    private ComboBox<String> comboCategory;

    @FXML
    private Button btnGoBack;

    private MySQLConnection connection = new MySQLConnection();

    private String idEvent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void frameEditInfo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminEditEventInfoFrame.fxml"));
            Parent root = loader.load();
            AdminEditEventInfoFrameController controller = loader.getController();
            controller.setUser(user);
            controller.setPassword(password);
            controller.setIdEvent(idEvent);
            controller.inic();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void frameSetJury(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminSetJuryFrame.fxml"));
            Parent root = loader.load();
            AdminSetJuryFrameController controller = loader.getController();
            controller.setUser(user);
            controller.setPassword(password);
            controller.setIdEvent(idEvent);
            controller.inic();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.getIcons().add(new Image(icon));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
            
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminSetJuryFrame.fxml"));
//            Parent root = loader.load();
//            AdminSetJuryFrameController controller = loader.getController();
//            controller.setUser(user);
//            controller.setPassword(password);
//            controller.setIdEvent(idEvent);
//            controller.inic();
//            Scene scene = new Scene(root);
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.setScene(scene);
//            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(AdminMainFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void filter(ActionEvent event) {
        String value = String.valueOf(comboCategory.getValue());

        switch (value) {
            case "primaria" -> {
                scrollP.setContent(filtro[0]);
            }
            case "secundaria" -> {
                scrollP.setContent(filtro[1]);
            }
            case "bachillerato" -> {
                scrollP.setContent(filtro[2]);
            }
            case "universidad" -> {
                scrollP.setContent(filtro[3]);
            }
            case "Todas las categorías" -> {
                scrollP.setContent(filtro[4]);
            }
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
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
        } catch (IOException ex) {
            Logger.getLogger(AdminMainFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
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
        lblTitle.setText(idEvent);
        Connection con = null;
        for (int i = 0; i < filtro.length; i++) {
            filtro[i] = new VBox();
            
            filtro[i].fillWidthProperty();
            filtro[i].setMinWidth(380);
            filtro[i].setMinHeight(385);
        }

        connection.setUser(user);
        connection.setPassword(password);

        try {
            con = connection.MySQLConnection();

            CallableStatement sp = con.prepareCall("{call getEventByID(?)}");
            sp.setString(1, idEvent);

            ResultSet query = sp.executeQuery();

            String s = """
                       Evento: %s
                       Fecha: %s
                       Sede: %s
                       Hora Inicial: %s
                       Hora Final: %s
                       Estado: %s
                       Categoría:""";

            LinkedHashSet<String> categoriess = new LinkedHashSet<>();

            while (query.next()) {
                s = String.format(s, query.getString(1),query.getDate(4),
                        query.getString(5), 
                        query.getTime(2), query.getTime(3), 
                        query.getString(10));
                if (query.getBoolean(6)) {
                    categoriess.add("\n\t • Primaria");
                }
                if (query.getBoolean(7)) {
                    categoriess.add("\n\t • Secundaria");
                }
                if (query.getBoolean(8)) {
                    categoriess.add("\n\t • Bachillerato");
                }
                if (query.getBoolean(9)) {
                    categoriess.add("\n\t • Universidad");
                }
            }

            Object[] categoriesArray = categoriess.toArray();

            for (int i = 0; i < categoriesArray.length; i++) {
                s += categoriesArray[i];
                if (i != categoriesArray.length) {
                }
            }

            query.close();
            sp.close();
            con.close();

            fillFilter();
            fillMainVBox();

            scrollP.setContent(filtro[4]);

            con = connection.MySQLConnection();

            sp = con.prepareCall("{call getCategories(?)}");
            sp.setString(1, idEvent);

            query = sp.executeQuery();

            ObservableList<String> categories = FXCollections.observableArrayList();

            while (query.next()) {
                categories.add(query.getString(1));
            }

            categories.add("Todas las categorías");
            comboCategory.setItems(categories);

            txtaInfo.setText(s);

            query.close();
            sp.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(AdminEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void fillFilter() {
        Connection con = null;

        try {
            con = connection.MySQLConnection();
            CallableStatement sp = con.prepareCall("{call getEventByName(?)}");
            sp.setString(1, idEvent);

            ResultSet query = sp.executeQuery();

            String[] buttonText = new String[3];

            int cont = 0;
            while (query.next()) {
                switch (cont) {
                    case 0 -> {
                        buttonText[0] = """
                                            Equipo: %s
                                            Nombre: %s | Edad: %d | Institución: %s""";
                        buttonText[1] = "Nombre: %s | Edad: %d | Asesor: %s";
                        buttonText[2] = "Nombre: %s | Edad: %d | Categoría: %s";
                        buttonText[cont] = String.format(buttonText[cont], query.getString(25),
                                query.getString(27), query.getInt(28), query.getString(24));
                    }
                    case 1 ->
                        buttonText[cont] = String.format(buttonText[cont], query.getString(27), query.getInt(28), query.getString(21));
                    case 2 -> {
                        String category = query.getString(26);
                        String team = query.getString(25);
                        buttonText[cont] = String.format(buttonText[cont], query.getString(27),
                                query.getInt(28), category);
                        cont = -1;
                        String text = buttonText[0] + "\n" + buttonText[1] + "\n" + buttonText[2];
                        Button btnEquipo = new Button(text);

                        EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                // Lógica del evento aquí
                                frameSetJudgeToTeam(event, idEvent, category, team);
                            }

                            private void frameSetJudgeToTeam(ActionEvent event, String idEvent, String category, String team) {
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminSetJudgeToTeamFrame.fxml"));
                                    Parent root = loader.load();
                                    AdminSetJudgeToTeamFrameController controller = loader.getController();

                                    controller.setIdEvent(idEvent);
                                    controller.setUser(user);
                                    controller.setPassword(password);
                                    controller.setCategory(category);
                                    controller.setTeam(team);
                                    controller.inic();

                                    Scene scene = new Scene(root);
                                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                    stage.setScene(scene);
                                    stage.show();
                                } catch (IOException ex) {
                                    Logger.getLogger(AdminSetJudgeToTeamFrameController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        };
                        
                        btnEquipo.setOnAction(eventHandler);

                        btnEquipo.setAlignment(Pos.CENTER_LEFT);
                        btnEquipo.setMinWidth(360);
                        btnEquipo.setMinHeight(100);
                        btnEquipo.setPrefWidth(500);
                        btnEquipo.setPrefHeight(100);
                        switch (category) {

                            case "primaria" -> {
                                filtro[0].getChildren().add(btnEquipo);
                            }
                            case "secundaria" -> {
                                filtro[1].getChildren().add(btnEquipo);
                            }
                            case "bachillerato" -> {
                                filtro[2].getChildren().add(btnEquipo);
                            }
                            case "universidad" -> {
                                filtro[3].getChildren().add(btnEquipo);
                            }
                        }
                    }
                }
                cont++;
            }
            query.close();
            sp.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(AdminEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void fillMainVBox() {
        Connection con = null;

        try {
            con = connection.MySQLConnection();
            CallableStatement sp = con.prepareCall("{call getEventByName(?)}");
            sp.setString(1, idEvent);

            ResultSet query = sp.executeQuery();

            String[] buttonText = new String[3];

            int cont = 0;
            while (query.next()) {
                switch (cont) {
                    case 0 -> {
                        buttonText[0] = """
                                            Equipo: %s
                                            Nombre: %s | Edad: %d | Institución: %s""";
                        buttonText[1] = "Nombre: %s | Edad: %d | Asesor: %s";
                        buttonText[2] = "Nombre: %s | Edad: %d | Categoría: %s";
                        buttonText[cont] = String.format(buttonText[cont], query.getString(25),
                                query.getString(27), query.getInt(28), query.getString(24));
                    }
                    case 1 ->
                        buttonText[cont] = String.format(buttonText[cont], query.getString(27), query.getInt(28), query.getString(21));
                    case 2 -> {
                        String team = query.getString(25);
                        String category = query.getString(26);
                        buttonText[cont] = String.format(buttonText[cont], query.getString(27),
                                query.getInt(28), category);
                        cont = -1;
                        String text = buttonText[0] + "\n" + buttonText[1] + "\n" + buttonText[2];
                        Button btnEquipo = new Button(text);
                        
                        EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                // Lógica del evento aquí
                                frameSetJudgeToTeam(event, idEvent, category, team);
                            }

                            private void frameSetJudgeToTeam(ActionEvent event, String idEvent, String category, String team) {
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminSetJudgeToTeamFrame.fxml"));
                                    Parent root = loader.load();
                                    AdminSetJudgeToTeamFrameController controller = loader.getController();

                                    controller.setIdEvent(idEvent);
                                    controller.setUser(user);
                                    controller.setPassword(password);
                                    controller.setCategory(category);
                                    controller.setTeam(team);
                                    controller.inic();

                                    Scene scene = new Scene(root);
                                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                    stage.setScene(scene);
                                    stage.show();
                                } catch (IOException ex) {
                                    Logger.getLogger(AdminSetJudgeToTeamFrameController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        };
                        
                        btnEquipo.setOnAction(eventHandler);
                        
                        btnEquipo.setAlignment(Pos.CENTER_LEFT);
                        btnEquipo.setMinWidth(360);
                        btnEquipo.setMinHeight(100);
                        btnEquipo.setPrefWidth(500);
                        btnEquipo.setPrefHeight(100);
                        filtro[4].getChildren().add(btnEquipo);
                    }
                }
                cont++;
            }
            query.close();
            sp.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(AdminEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
