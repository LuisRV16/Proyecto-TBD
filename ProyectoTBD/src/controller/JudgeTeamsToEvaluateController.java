package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author manue
 */
public class JudgeTeamsToEvaluateController implements Initializable {

    String user, password, category, idEvent;

    MySQLConnection connection = new MySQLConnection();
    @FXML
    private VBox vboxTeams;
    @FXML
    private Label lblTitle;
    @FXML
    private Button btnGoBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public void inic() {

        Connection con = null;

        connection.setUser(user);
        connection.setPassword(password);

        try {
            con = connection.MySQLConnection();

            CallableStatement sp = con.prepareCall("{call getTeamsToEvaluate(?, ?, ?)}");

            sp.setString(1, idEvent);
            sp.setString(2, user);
            sp.setString(3, category);

            sp.execute();

            ResultSet query = sp.executeQuery();

            while (query.next()) {
                String idEvent = query.getString(1);
                String idInstitution = query.getString(2);
                String team = query.getString(3);
                String category = query.getString(4);
                int idJudge = query.getInt(5);

                Button btnEquipo = new Button(team);
                btnEquipo.setMinWidth(530);
                btnEquipo.setMinHeight(100);
                btnEquipo.setAlignment(Pos.CENTER);

                EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Lógica del evento aquí
                        frameEvaluation(event, idEvent, idInstitution, team, category, idJudge);
                    }

                    private void frameEvaluation(ActionEvent event, String idEvent, String idInstitution, String team, String category, int idJudge) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/judgeEvaluationDisenoFrame.fxml"));
                            Parent root = loader.load();
                            JudgeEvaluationDisenoFrameController controller = loader.getController();

                            controller.setIdEvent(idEvent);
                            controller.setUser(user);
                            controller.setPassword(password);
                            controller.setCategory(category);
                            controller.setIdTeam(team);
                            controller.setIdJudge(idJudge);
                            controller.setIdInstitution(idInstitution);

                            Scene scene = new Scene(root);
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.initStyle(StageStyle.UNDECORATED);
                            stage.show();
                        } catch (IOException ex) {
                            Logger.getLogger(JudgeEvaluationDisenoFrameController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };

                btnEquipo.setOnAction(eventHandler);

                vboxTeams.getChildren().add(btnEquipo);
            }

            query.close();
            sp.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(JudgeTeamsToEvaluateController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(JudgeTeamsToEvaluateController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/judgeParticipatingCategoryFrame.fxml"));
            Parent root = loader.load();
            JudgeParticipatingCategoryFrameController controller = loader.getController();
            controller.setUser(user);
            controller.setPassword(password);
            controller.inic();
            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Menu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(JudgeTeamsToEvaluateController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
