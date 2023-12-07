package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author manue
 */
public class JudgeParticipatingCategoryFrameController implements Initializable {

    private String user, password, idEvent;

    @FXML
    private Button btnPrimaria;
    @FXML
    private Button btnSecundaria;
    @FXML
    private Button btnBachillerato;
    @FXML
    private Button btnUniversidad;
    @FXML
    private Label lblTitle;

    private MySQLConnection connection = new MySQLConnection();
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
    private void frameTeamsToEvaluate(ActionEvent event) {
        try {
            String category = ((Button) event.getSource()).getText().toLowerCase();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/judgeTeamsToEvaluate.fxml"));
            Parent root = loader.load();
            JudgeTeamsToEvaluateController controller = loader.getController();
            controller.setUser(user);
            controller.setPassword(password);
            controller.setCategory(category);
            controller.setIdEvent(idEvent);
            controller.inic();
            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Menu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(JudgeParticipatingCategoryFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean inic() {

        boolean bool = false;
        
        Connection con = null;

        connection.setUser(user);
        connection.setPassword(password);

        try {
            con = connection.MySQLConnection();

            CallableStatement sp = con.prepareCall("{call getParticipatingCategories(?,?)}");

            sp.setString(1, user);
            sp.registerOutParameter(2, Types.VARCHAR);

            boolean band = sp.execute();

            if (band) {
                ResultSet query = sp.executeQuery();
                while (query.next()) {
                    switch (query.getString(1)) {
                        case "primaria" ->
                            btnPrimaria.setDisable(false);
                        case "secundaria" ->
                            btnSecundaria.setDisable(false);
                        case "bachillerato" ->
                            btnBachillerato.setDisable(false);
                        case "universidad" ->
                            btnUniversidad.setDisable(false);
                    }
                }

                sp = con.prepareCall("{? = call getEventByCurrentDate()}");

                sp.registerOutParameter(1, Types.VARCHAR);

                sp.execute();

                idEvent = sp.getString(1);
                
                bool = true;

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(sp.getString(2));
                alert.getDialogPane().setMinHeight(100);
                alert.getDialogPane().setMinWidth(100);
                alert.getDialogPane().setPrefHeight(100);
                alert.getDialogPane().setPrefWidth(300);
                alert.setHeaderText(null);
                alert.showAndWait();
            }

        } catch (SQLException ex) {
            Logger.getLogger(JudgeParticipatingCategoryFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(JudgeParticipatingCategoryFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return bool;
    }

}
