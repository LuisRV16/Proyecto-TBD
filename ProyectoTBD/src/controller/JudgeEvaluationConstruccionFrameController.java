
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author manue
 */
public class JudgeEvaluationConstruccionFrameController implements Initializable {

    private String user, password, idEvent, idInstitution, idTeam, category;
    
    private int idJudge;
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblInspGen;
    @FXML
    private Label lblLibIng;
    @FXML
    private CheckBox c1;
    @FXML
    private CheckBox c2;
    @FXML
    private CheckBox c3;
    @FXML
    private CheckBox c4;
    @FXML
    private CheckBox c5;
    @FXML
    private CheckBox c6;
    @FXML
    private CheckBox c7;
    @FXML
    private CheckBox c8;
    @FXML
    private CheckBox c9;
    @FXML
    private CheckBox c10;
    @FXML
    private CheckBox c11;
    @FXML
    private CheckBox c12;
    @FXML
    private CheckBox c13;
    @FXML
    private CheckBox c14;
    @FXML
    private CheckBox c15;
    @FXML
    private CheckBox c16;
    @FXML
    private CheckBox c17;
    @FXML
    private Button btnCalificar;
    
    private MySQLConnection connection = new MySQLConnection();
    
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

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public void setIdInstitution(String idInstitution) {
        this.idInstitution = idInstitution;
    }

    public void setIdTeam(String idTeam) {
        this.idTeam = idTeam;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIdJudge(int idJudge) {
        this.idJudge = idJudge;
    }

    @FXML
    private void frameCalification(ActionEvent event) {
        Connection con = null;
        connection.setUser(user);
        connection.setPassword(password);

        try {
            con = connection.MySQLConnection();
            CallableStatement sp = con.prepareCall("{call createRowConstruccion(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            sp.setBoolean(1, c1.isSelected());
            sp.setBoolean(2, c2.isSelected());
            sp.setBoolean(3, c3.isSelected());
            sp.setBoolean(4, c4.isSelected());
            sp.setBoolean(5, c5.isSelected());
            sp.setBoolean(6, c6.isSelected());
            sp.setBoolean(7, c7.isSelected());
            sp.setBoolean(8, c8.isSelected());
            sp.setBoolean(9, c9.isSelected());
            sp.setBoolean(10, c10.isSelected());
            sp.setBoolean(11, c11.isSelected());
            sp.setBoolean(12, c12.isSelected());
            sp.setBoolean(13, c13.isSelected());
            sp.setBoolean(14, c14.isSelected());
            sp.setBoolean(15, c15.isSelected());
            sp.setBoolean(16, c16.isSelected());
            sp.setBoolean(17, c17.isSelected());
            sp.execute();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/judgeCalification.fxml"));
                Parent root = loader.load();
                JudgeCalificationController controller = loader.getController();

                controller.setIdEvent(idEvent);
                controller.setUser(user);
                controller.setPassword(password);
                controller.setCategory(category);
                controller.setIdTeam(idTeam);
                controller.setJudgeId(idJudge);
                controller.setIdInstitution(idInstitution);
                controller.inic();

                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.initStyle(StageStyle.DECORATED);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(JudgeCalificationController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException ex) {
            Logger.getLogger(JudgeEvaluationConstruccionFrameController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(JudgeEvaluationConstruccionFrameController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
