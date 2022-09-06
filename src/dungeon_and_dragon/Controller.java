package dungeon_and_dragon;
import io.github.cdimascio.dotenv.Dotenv;

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

    private Dotenv dotenv=Dotenv.load();
    // CONSTRUCTORS

    // SETTERS/GETTERS

    // METHODS

    /**
     * permet d'établir la connection avec la BDD
     */
    public Connection getConnection(){

        try {
            System.out.println("test connection");
//             Class.forName("com.mysql.jdbc.Driver"); //ancienne version

            Class.forName("com.mysql.cj.jdbc.Driver");                          // lien avec la dependence .jar

            String hostName="localhost"; // ou "127.0.0.1"                      // localisation de la BDD
            String schemaName="game_dungeon_and_dragon";                           // nom de la BDD
//            String connectionUrl="jdbc:mysql://"+hostName+":3306/"+schemaName;  // adresse complet de la BDD
            String connectionUrl="jdbc:mysql://localhost/"+schemaName+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";  // adresse complet de la BDD
            String user= dotenv.get("BDD_USER");                                                 // username de la bdd
            String mdp=dotenv.get("BDD_PASS");                                                  // mot de passe de la bdd
//            System.out.println(connectionUrl+user+mdp);

            jdbc:mysql://localhost/db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC

            /*
            Request de connection faite au server
            avec l'adresse de connection,
            le pseudo et le mot de passe
             */
            connection = DriverManager.getConnection(connectionUrl,user,mdp);

            // si connection établie :
            System.out.println("\nConnection établie avec la base de donnée");
            System.out.println( "Connections numéro: "+compteurConnection++ );

            return connection;


        }catch ( SQLException | ClassNotFoundException e){                      // Ajout de la class SQLException
            // pour avoir les erreurs de SQL
            e.printStackTrace();
            System.out.println("Erreur lors de la connection");

            return connection;

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

            // si une erreur, c'est produit durant la fermeture de connection
            e.printStackTrace();
            System.out.println("Erreur lors de la fermeture de connection");
        }
    }

}
