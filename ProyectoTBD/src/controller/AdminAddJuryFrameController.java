package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.net.URL;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author manue
 */
public class AdminAddJuryFrameController implements Initializable {

    private String user, password, idEvent, category;
    @FXML
    private Label lblTitle;
    @FXML
    private ComboBox<String> comboJudge1;
    @FXML
    private ComboBox<String> comboJudge2;
    @FXML
    private ComboBox<String> comboJudge3;
    @FXML
    private Button btnAdd;
    @FXML
    private Label lblMsg;

    private MySQLConnection connection = new MySQLConnection();
    @FXML
    private Button btnGoBack;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void inic() {
        Connection con = null;

        try {
            connection.setUser(user);
            connection.setPassword(password);

            con = connection.MySQLConnection();

            CallableStatement sp = con.prepareCall("{call isJudgesEmpty(?)}");

            sp.registerOutParameter(1, Types.BOOLEAN);
            sp.execute();

            boolean msg = sp.getBoolean(1);

            sp.close();

            if (msg) {

                sp = con.prepareCall("{call getJudges(?)}");

                sp.setString(1, category);

                ResultSet query = sp.executeQuery();

                ObservableList<String> names = FXCollections.observableArrayList();

                while (query.next()) {
                    names.add(query.getString(1));
                }

                comboJudge1.setItems(names);
                comboJudge2.setItems(names);
                comboJudge3.setItems(names);

            }

        } catch (SQLException ex) {
            Logger.getLogger(AdminAddJuryFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminAddJuryFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    private void add(ActionEvent event) {

        String juryName = "Jurado " + category;

        Connection con = null;

        try {
            con = connection.MySQLConnection();

            CallableStatement sp = con.prepareCall("{call addJuryJudge(?,?,?,?,?,?,?)}");

            sp.setString(1, juryName);
            sp.setString(2, idEvent);
            sp.setString(3, category);
            sp.setString(4, comboJudge1.getValue());
            sp.setString(5, comboJudge2.getValue());
            sp.setString(6, comboJudge3.getValue());
            sp.setNull(7, Types.VARCHAR);
            sp.execute();
            String s = sp.getString(7);
            
                if (s.equals("El jurado ha sido introducido correctamente.")) {
                    try {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setTitle("Mensaje");
                        alert.setContentText(s);
                        alert.showAndWait();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminSetJuryFrame.fxml"));
                        Parent root = loader.load();
                        AdminSetJuryFrameController controller = loader.getController();
                        controller.setUser(user);
                        controller.setPassword(password);
                        controller.setIdEvent(idEvent);
                        controller.inic();
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(AdminSetJuryFrameController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    lblMsg.setText(s); // hazlo rojito
                }
            sp.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(AdminAddJuryFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminAddJuryFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminSetJuryFrame.fxml"));
            Parent root = loader.load();
            AdminSetJuryFrameController controller = loader.getController();
            controller.setUser(user);
            controller.setPassword(password);
            controller.setIdEvent(idEvent);
            controller.inic();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminSetJuryFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
