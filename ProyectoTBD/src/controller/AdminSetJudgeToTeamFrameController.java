    package controller;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author manue
 */
public class AdminSetJudgeToTeamFrameController implements Initializable {

    private String user, password, idEvent, category, team;

    @FXML
    private Label lblTitle;
    @FXML
    private Button btnSet;
    @FXML
    private ComboBox<String> comboJudges;
    @FXML
    private TextArea txtaInfo;

    private MySQLConnection connection = new MySQLConnection();
    @FXML
    private Button btnGoBack;
    @FXML
    private Label lblMsg;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void setJudgeToTeam(ActionEvent event) {
        Connection con = null;

        try {

            con = connection.MySQLConnection();

            CallableStatement sp = con.prepareCall("{call addEvaluation(?,?,?,?,?)}");

            sp.setString(1, idEvent);
            sp.setString(2, team);
            sp.setString(3, category);
            sp.setString(4, comboJudges.getValue());

            sp.registerOutParameter(5, Types.VARCHAR);
             sp.execute();
            String s = sp.getString(5);

            if (s.equals("Al equipo " + team + " de la categoria " + category + " se le ha asignado el juez " + comboJudges.getValue() + ".")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
            } else {
                //lblMsg.setText(s); // hacerlo rojito
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Mensaje");
                alert.setContentText(s);
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminSetJudgeToTeamFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdminEventInfoFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminSetJudgeToTeamFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void inic() {
        Connection con = null;

        try {

            connection.setUser(user);
            connection.setPassword(password);

            con = connection.MySQLConnection();

            CallableStatement sp = con.prepareCall("call getJudgeJury(?,?)");

            sp.setString(1, idEvent);
            sp.setString(2, category);

            ResultSet query = sp.executeQuery();

            ObservableList<String> judge = FXCollections.observableArrayList();

            while (query.next()) {
                judge.add(query.getString(1));
            }

            comboJudges.setItems(judge);
            comboJudges.getSelectionModel().selectFirst();

            String info = "Equipo: " + team + "\n" + "Categoría: " + category;

            txtaInfo.setText(info);

        } catch (SQLException ex) {
            Logger.getLogger(AdminSetJudgeToTeamFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminSetJudgeToTeamFrameController.class.getName()).log(Level.SEVERE, null, ex);
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

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTeam(String team) {
        this.team = team;
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
            Logger.getLogger(AdminSetJudgeToTeamFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
