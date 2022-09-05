package dungeon_and_dragon;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Controller {
    // ATTRIBUTS
    /*
    java.sql :
    pour les constructions d'accès
    et de manipulation
    des informations contenues dans la base
     */
    protected Connection connection;

    private static int compteurConnection=1;

    // CONSTRUCTORS

    // SETTERS/GETTERS

    // METHODS

    /**
     * permet d'établir la connection avec la BDD
     */
    public void getConnection(){

        try {
            // Class.forName("com.mysql.jdbc.Driver"); ancienne version

            Class.forName("com.mysql.cj.jdbc.Driver");                          // lien avec la dependence .jar

            String hostName="localhost"; // ou "127.0.0.1"                      // localisation de la BDD
            String schemaName="game_dungeon_and_dragon";                           // nom de la BDD
            String connectionUrl="jdbc:mysql://"+hostName+":3306/"+schemaName;  // adresse complet de la BDD
            String user="tristan";                                                 // username de la bdd
            String mdp="password";                                                  // mot de passe de la bdd

            /*
            Request de connection faite au server
            avec l'adresse de connection,
            le pseudo et le mot de passe
             */
            connection = DriverManager.getConnection(connectionUrl,user,mdp);

            // si connection établie :
            System.out.println("\nConnection établie avec la base de donnée");
            System.out.println( "Connections numéro: "+compteurConnection++ );
        }catch ( SQLException | ClassNotFoundException e){                      // Ajout de la class SQLException
            // pour avoir les erreurs de SQL
            e.printStackTrace();
            System.out.println("Erreur lors de la connection");

        }

    }

    /**
     * Permet de fermer la connection à la base de données
     */
    public void closeConnection(){

        try {

            connection.close();                                                 // request pour fermer la connection
            //si connection fermer correctement
            System.out.println("Connection fermer");

        } catch (SQLException e) {

            // si une erreur, c'est produite durant la fermeture de connection
            e.printStackTrace();
            System.out.println("Erreur lors de la fermeture de connection");
        }
    }

}
