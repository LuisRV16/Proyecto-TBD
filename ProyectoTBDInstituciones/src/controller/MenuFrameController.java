package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MenuFrameController implements Initializable{
    public String user, password;
    private Stage panel;
    private Scene escena;
    private Boolean cambiar=null;
    private MySQLConnection connection = new MySQLConnection();
    @FXML
    private Label lblName;
    @FXML
    private Button btnActiveEvents;
    @FXML
    private Button btnYourEvents;

    
    public void activeEvents(ActionEvent event) throws IOException{
        connection.setUser(user);
        connection.setPassword(password); 
        connection.MySQLConnection();
        if(connection.isChange() == true){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/activeEventsFrame.fxml"));
            Parent root = loader.load();
            ActiveEventsFrameController controller = loader.getController();
            controller.setUser(user);
            controller.setPassword(password);
            controller.inic();
            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("Eventos activos");
            stage.setScene(scene);
            stage.show();
        }
    }
    
    public void yourEvents(ActionEvent event) throws IOException{
        connection.setUser(user);
        connection.setPassword(password);
        connection.MySQLConnection();
        if(connection.isChange() == true){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/yourEventsFrame.fxml"));
            Parent root = loader.load();
            YourEventsFrameController controller = loader.getController();
            controller.setUser(user);
            controller.setPassword(password);
            controller.inic();
            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("Tus eventos");
            stage.setScene(scene);
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void inic(){
        connection.setUser(user);
        connection.setPassword(password);
        connection.MySQLConnection();
        lblName.setText(user);
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
