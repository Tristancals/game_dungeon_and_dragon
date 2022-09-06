package dungeon_and_dragon;

import dungeon_and_dragon.gears.Gear;
import dungeon_and_dragon.gears.Heal;
import dungeon_and_dragon.heros.Hero;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    // ATTRIBUTS
    /*
    java.sql :
    pour les constructions d'accès
    et de manipulation
    des informations contenues dans la base
     */
    protected Connection connection;

    private static int compteurConnection = 1;

    private final Dotenv dotenv = Dotenv.load();

    /**
     * permet d'établir la connection avec la BDD
     */
    public void getConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");                          // lien avec la dependence .jar
            String hostName = "localhost"; // ou "127.0.0.1"                      // localisation de la BDD
            String schemaName = "game_dungeon_and_dragon";                           // nom de la BDD
            String connectionUrl = "jdbc:mysql://" + hostName + "/" + schemaName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";  // adresse complet de la BDD
            String user = dotenv.get("BDD_USER");                                                 // username de la bdd
            String mdp = dotenv.get("BDD_PASS");                                                  // mot de passe de la bdd
            /*
            Request de connection faite au server
            avec l'adresse de connection,
            le pseudo et le mot de passe
             */
            connection = DriverManager.getConnection(connectionUrl, user, mdp);
            // si connection établie :
            System.out.println("\nConnection établie avec la base de donnée");
            System.out.println("Connections numéro: " + compteurConnection++);
        } catch (SQLException | ClassNotFoundException e) {                      // Ajout de la class SQLException
            // pour avoir les erreurs de SQL
            e.printStackTrace();
            System.out.println("Erreur lors de la connection");
        }
    }

    /**
     * Permet de fermer la connection à la base de données
     */
    public void closeConnection() {
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

    private String sqlInsertGear(Gear gear) {
        return "INSERT INTO gear(gear_name,gear_type,gear_stats) VALUES ('" +
                gear.getName() + "','" + gear.getType() + "'," + gear.getStats() + ");";
    }

    private String sqlInsertHero(Hero player, int id_gear_def, int id_gear_of) {
        return "INSERT INTO hero(hero_name,hero_type," +
                "hero_level,id_gear_def,id_gear_of) VALUES ('" +
                player.getName() + "','" + player.getType() + "'," +
                player.getLevel() + "," + id_gear_def + "," + id_gear_of + ");";
    }

    private String sqlInsertInventory(int id_hero, int id_potion) {
        return "INSERT INTO inventory(id_hero,id_gear_heal) VALUES (" +
                id_hero + "," + id_potion + ");";
    }

    public void selectAllPlayer(){

    }

    public void insertPlayers(List<Hero> players) {
        getConnection();
        try {
            Statement statement = connection.createStatement();
            for (Hero player : players) {
                int id_gearOf = 0, id_gearDef = 0, id_hero = 0, id_potion = 0;

                statement.executeUpdate(sqlInsertGear(player.getOffensive()), Statement.RETURN_GENERATED_KEYS);
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id_gearOf = generatedKeys.getInt(1);
                }

                statement.executeUpdate(sqlInsertGear(player.getDefensive()), Statement.RETURN_GENERATED_KEYS);
                generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id_gearDef = generatedKeys.getInt(1);
                }

                statement.executeUpdate(sqlInsertHero(player, id_gearDef, id_gearOf), Statement.RETURN_GENERATED_KEYS);
                generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id_hero = generatedKeys.getInt(1);
                }

                for (Heal potion : player.getInventory()) {
                    statement.executeUpdate(sqlInsertGear(potion), Statement.RETURN_GENERATED_KEYS);
                    generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        id_potion = generatedKeys.getInt(1);
                    }
                    statement.executeUpdate(sqlInsertInventory(id_hero, id_potion));

                }
            }

        } catch (SQLException e) {
            System.out.println(e);
        }

        closeConnection();
    }

}
