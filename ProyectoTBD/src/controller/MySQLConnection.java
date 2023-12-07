package controller;

import java.sql.*;   
        
public class MySQLConnection {
    private static final String url = "jdbc:mysql://localhost/concursoVEX";

    /*
    LuisR (19/11/23 14:35):
    Modifique el tipo de encapsulamiento de los atributos de la clase
    Lo correcto es que no se tenga acceso a estas de ninguna otra clase.
    Cree el metodo isChange para obtener el valor booleano del atributo
    change, ya que es necesario para verificar si la conexion fue concretada
    y asi continuar con el acceso a los frames.
    */
    private String user, password;
    private boolean change =  false;

    public boolean isChange() {
        return change;
    }
    
    public Connection MySQLConnection(){
        Connection con = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(url, getUser(), getPassword());
            change = true;
        }catch(ClassNotFoundException | SQLException e){
            System.out.println("Error :" + e.getMessage());
        }
        return con;
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
