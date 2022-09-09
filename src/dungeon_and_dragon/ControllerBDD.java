package dungeon_and_dragon;

import dungeon_and_dragon.gears.*;
import dungeon_and_dragon.heros.Hero;
import dungeon_and_dragon.heros.Warrior;
import dungeon_and_dragon.heros.Wizard;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton pattern
 * https://refactoring.guru/fr/design-patterns/singleton/java/example
 */
public class ControllerBDD {

    protected Connection connection;
    private static ControllerBDD instance;
    private static int compteurConnection = 1;
    private final Dotenv dotenv = Dotenv.load();

    /**
     * private sur le constructeur oblige de passer par getInstance()
     * qui s'assure qu'il n'en existe qu'une seule.
     */
    private ControllerBDD() {

    }

    /**
     * permet de créer une instance de ControllerBDD
     *
     * @return new ControllerBDD() / si déjà instancier @return null
     */
    public static ControllerBDD getInstance() {
        if (instance == null) {
            instance = new ControllerBDD();
        }
        return instance;
    }

    /**
     * permet d'établir la connection avec la BDD
     */
    private void getConnection() {
        try {
            String hostName = "localhost"; // ou "127.0.0.1"
            String schemaName = "game_dungeon_and_dragon";
            String connectionUrl = "jdbc:mysql://" + hostName + "/" + schemaName +
                    "?allowPublicKeyRetrieval=True&useSSL=false&useUnicode=true" +
                    "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false" +
                    "&serverTimezone=UTC";
            String user = dotenv.get("BDD_USER");
            String mdp = dotenv.get("BDD_PASS");
            connection = DriverManager.getConnection(connectionUrl, user, mdp);

            System.out.println("\nConnection établie avec la base de donnée" +
                    "\nConnections numéro: " + compteurConnection++);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connection\n\n" + e);
        }
    }

    /**
     * Permet de fermer la connection à la base de données
     */
    private void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection fermer");
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Erreur lors de la fermeture de connection");
        }
    }

    private String sqlInsertGear(Gear gear) {
        return "INSERT INTO gear(gear_name,gear_type,gear_stats) VALUES ('" +
                gear.getName() + "','" + gear.getType() + "'," + gear.getStats() + ");";
    }

    private String sqlUpdateHero() {
        return "UPDATE  hero " +
                "SET " +
                "hero_name=?, " +
                "hero_type=?, " +
                "hero_level=?, " +
                "id_gear_def=?, " +
                "id_gear_of=?, " +
                "hero_life=?, " +
                "hero_position=? " +
                "WHERE id=?;";
    }


    private String sqlInsertHero() {
        return "INSERT INTO hero(hero_name,hero_type," +
                "hero_level,id_gear_def,id_gear_of,hero_life,hero_position) VALUES (" +
                "?,?,?,?,?,?,?);";
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

    private String sqlDeletePlayer(Hero player) {
        return "DELETE from hero where hero.id=" + player.getId() + " ;";
    }


    public void selectAllPlayers(Game game) {
        getConnection();
        List<Hero> players = new ArrayList<>();
        try {
            Statement st = connection.createStatement();

            ResultSet resultSet = st.executeQuery(sqlSelectAllPlayers());

            while (resultSet.next()) {
                Statement statement = connection.createStatement();

                String type = resultSet.getString("hero_type");
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
                    players.add(new Wizard(id_hero, hero_name,
                            hero_level,
                            gearOf,
                            gearDef,
                            inventory));
                } else {
                    players.add(new Warrior(id_hero, hero_name,
                            hero_level,
                            gearOf,
                            gearDef,
                            inventory));
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        game.setPlayersSave(players);
        closeConnection();
    }

    public void insertPlayers(List<Hero> players, boolean save) {
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
                    if (player.getId() != 0) {
                        PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateHero(),
                                Statement.RETURN_GENERATED_KEYS);

                        preparedStatement.setString(1, player.getName());
                        preparedStatement.setString(2, player.getType());
                        preparedStatement.setInt(3, player.getLevel());
                        preparedStatement.setInt(4, id_gearDef);
                        preparedStatement.setInt(5, id_gearOf);
                        if (save) {
                            preparedStatement.setInt(6, player.getLife());
                            preparedStatement.setInt(7, player.getPosition());
                        } else {
                            preparedStatement.setNull(6, Types.INTEGER);
                            preparedStatement.setNull(7, Types.INTEGER);
                        }
                        preparedStatement.setInt(8, player.getId());
                        preparedStatement.executeUpdate();
                    } else {
                        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsertHero());
                        preparedStatement.setString(1, player.getName());
                        preparedStatement.setString(2, player.getType());
                        preparedStatement.setInt(3, player.getLevel());
                        if (save) {
                            preparedStatement.setInt(6, player.getLife());
                            preparedStatement.setInt(7, player.getPosition());
                        } else {
                            preparedStatement.setNull(6, Types.INTEGER);
                            preparedStatement.setNull(7, Types.INTEGER);
                        }
                        preparedStatement.setInt(4, id_gearDef);
                        preparedStatement.setInt(5, id_gearOf);
                        preparedStatement.executeUpdate();
                    }
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
                } else {
                    Statement st = connection.createStatement();
                    st.executeUpdate(sqlDeletePlayer(player));
                }
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        closeConnection();
    }

}
