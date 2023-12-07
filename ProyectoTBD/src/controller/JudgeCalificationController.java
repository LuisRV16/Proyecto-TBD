package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.sql.SQLException;
import java.sql.ResultSet;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author manue
 */
public class JudgeCalificationController implements Initializable {

    private String user, password, idEvent, idInstitution, idTeam, category;

    private int judgeId;

    @FXML
    private Label lblMsg;
    @FXML
    private Button btnFin;
    @FXML
    private TextArea txtaRes;

    MySQLConnection connection = new MySQLConnection();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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

    public void setJudgeId(int judgeId) {
        this.judgeId = judgeId;
    }

    public void inic() {
        
        connection.setUser(user);
        connection.setPassword(password);

        Connection con = null;

        try {
            con = connection.MySQLConnection();

            CallableStatement sp = con.prepareCall("{call evaluar(?,?,?,?,?)}");

            sp.setString(1, idEvent);
            sp.setString(2, idInstitution);
            sp.setString(3, idTeam);
            sp.setString(4, category);
            sp.setInt(5, judgeId);
            
            sp.executeUpdate();
                    
            sp = con.prepareCall("{call getCalificacion(?,?,?,?,?)}");

            sp.setString(1, idEvent);
            sp.setString(2, idInstitution);
            sp.setString(3, idTeam);
            sp.setString(4, category);
            sp.setInt(5, judgeId);
            
            
            ResultSet query = sp.executeQuery();
            
            float calf = 0;
            
            while(query.next())
                calf = query.getFloat(1);
            txtaRes.setText("Equipo: " + idTeam + "\n" + "Calificación: " + calf);
            
            query.close();
            sp.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(JudgeCalificationController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(JudgeCalificationController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    @FXML
    private void endEvaluation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/judgeTeamsToEvaluate.fxml"));
            Parent root = loader.load();
            JudgeTeamsToEvaluateController controller = loader.getController();

            controller.setIdEvent(idEvent);
            controller.setUser(user);
            controller.setPassword(password);
            controller.setCategory(category);
            controller.inic();

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(JudgeEvaluationDisenoFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

