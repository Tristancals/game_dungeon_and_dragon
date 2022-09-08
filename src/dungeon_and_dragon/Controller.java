package dungeon_and_dragon;

import dungeon_and_dragon.gears.*;
import dungeon_and_dragon.heros.Hero;
import dungeon_and_dragon.heros.Warrior;
import dungeon_and_dragon.heros.Wizard;
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
    private void getConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");                          // lien avec la dependence .jar
            String hostName = "localhost"; // ou "127.0.0.1"                      // localisation de la BDD
            String schemaName = "game_dungeon_and_dragon";                           // nom de la BDD
            String connectionUrl = "jdbc:mysql://" + hostName + "/" + schemaName + "?allowPublicKeyRetrieval=True&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";  // adresse complet de la BDD
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
    private void closeConnection() {
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

    private String sqlInsertHero(Hero player, int id_gear_def, int id_gear_of,boolean save) {
        return "INSERT INTO hero(hero_name,hero_type," +
                "hero_level,id_gear_def,id_gear_of,hero_life,hero_position) VALUES ('" +
                player.getName() + "','" + player.getType() + "'," +
                player.getLevel() + "," + id_gear_def + "," + id_gear_of + "," +
                (save?player.getLife():null)+","+(save?player.getPosition():null)+");";//TODO idGAME...id_game ","+(save?player.getPosition():null)+
    }

    private String sqlInsertInventory(int id_hero, int id_potion) {
        return "INSERT INTO inventory(id_hero,id_gear_heal) VALUES (" +
                id_hero + "," + id_potion + ");";
    }

    private String sqlSelectAllPlayers() {
        return "SELECT * FROM hero;";
    }

    private String sqlSelectGearHero(int id_gear_hero) {
        return "SELECT gear_name,gear_type,gear_stats FROM gear " +
                "WHERE gear.id=" + id_gear_hero + ";";
    }

    private String sqlSelectInventoryHero(int id_hero) {
        return "SELECT  gear.gear_stats,gear.gear_name  FROM inventory " +
                "INNER JOIN gear " +
                "ON inventory.id_gear_heal = gear.id " +
                "WHERE inventory.id_hero=" + id_hero + ";";
    }
//             "INNER JOIN inventory ON hero.id = inventory.id_hero " +
//             "INNER JOIN gear " +
//             "ON inventory.id_gear_heal = gear.id" +
//             "GROUP BY hero.id

    public void selectAllPlayers() {
        getConnection();

        List<Hero> players = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery(sqlSelectAllPlayers());

            while (resultSet.next()) {
                Statement statement = connection.createStatement();

                String type = resultSet.getString("hero_type");
                System.out.println(type);
                Defensive gearDef = null;
                Offensive gearOf = null;
                int id_gear_def = resultSet.getInt("id_gear_def");
                int id_gear_of = resultSet.getInt("id_gear_of");
                String hero_name = resultSet.getString("hero_name");
                int hero_level = resultSet.getInt("hero_level");
                int id_hero = resultSet.getInt("id");
                ResultSet rGear = statement.executeQuery(sqlSelectGearHero(id_gear_def));
                while (rGear.next()) {
                    if (type.equals("Magicien")) {
                        gearDef = new EnergyShield(rGear.getString("gear_name"),
                                rGear.getInt("gear_stats"));
                    } else {
                        gearDef = new Shield(rGear.getString("gear_name"),
                                rGear.getInt("gear_stats"));
                    }

                }
                rGear = statement.executeQuery(sqlSelectGearHero(id_gear_of));
                while (rGear.next()) {
                    if (type.equals("Magicien")) {
                        gearOf = new Spell(rGear.getString("gear_name"),
                                rGear.getInt("gear_stats"));
                    } else {
                        gearOf = new Weapon(rGear.getString("gear_name"),
                                rGear.getInt("gear_stats"));
                    }
                }
                ArrayList<Heal> inventory = new ArrayList<>();
                ResultSet rInventory = statement.executeQuery(sqlSelectInventoryHero(id_hero));
                while (rInventory.next()) {
                    inventory.add(new Heal(rInventory.getString("gear_name"), rInventory.getInt("gear_stats")));
                }
                if (type.equals("Magicien")) {
                    players.add(new Wizard(id_hero,hero_name,
                            hero_level,
                            gearOf,
                            gearDef,
                            inventory));
                } else {
                    players.add(new Warrior(id_hero,hero_name,
                            hero_level,
                            gearOf,
                            gearDef,
                            inventory));
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        System.out.println(players);
closeConnection();
    }

    public void insertPlayers(List<Hero> players,boolean save) {
        getConnection();
        try {
            Statement statement = connection.createStatement();
            for (Hero player : players) {
                if (player.isALife()) {
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

                    statement.executeUpdate(sqlInsertHero(player, id_gearDef, id_gearOf,save), Statement.RETURN_GENERATED_KEYS);
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
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
closeConnection();
    }

}
