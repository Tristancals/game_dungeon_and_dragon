//package dungeon_and_dragon;
//
//import dungeon_and_dragon.gears.*;
//import dungeon_and_dragon.heros.Hero;
//import dungeon_and_dragon.heros.Warrior;
//import dungeon_and_dragon.heros.Wizard;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ControllerHero {
//
//
//    private String sqlUpdateHero() {
//        return "UPDATE  hero " +
//                "SET " +
//                "hero_name=?, " +
//                "hero_type=?, " +
//                "hero_level=?, " +
//                "id_gear_def=?, " +
//                "id_gear_of=?, " +
//                "hero_life=?, " +
//                "hero_position=? " +
//                "WHERE id=?;";
//    }
//    private String sqlInsertHero() {
//        return "INSERT INTO hero(hero_name," +
//                "hero_type," +
//                "hero_level," +
//                "id_gear_def," +
//                "id_gear_of," +
//                "hero_life," +
//                "hero_position) " +
//                "VALUES (?,?,?,?,?,?,?);";
//    }
//    private String sqlSelectAllPlayers() {
//        return "SELECT * FROM hero;";
//    }
//
//    private String sqlDeletePlayer() {
//        return "DELETE FROM hero " +
//                "WHERE hero.id=? ;";
//    }
//
//
//
//    public void selectAllPlayers(Game game) {
//
//        getConnection();
//
//        List<Hero> players = new ArrayList<>();
//        try {
//            Statement st = connection.createStatement();
//
//            ResultSet resultSet = st.executeQuery(sqlSelectAllPlayers());
//
//            while (resultSet.next()) {
//                Statement statement = connection.createStatement();
//
//                String type = resultSet.getString("hero_type");
//                System.out.println(type);
//                Defensive gearDef = null;
//                Offensive gearOf = null;
//                int id_gear_def = resultSet.getInt("id_gear_def");
//                int id_gear_of = resultSet.getInt("id_gear_of");
//                String hero_name = resultSet.getString("hero_name");
//                int hero_level = resultSet.getInt("hero_level");
//                int id_hero = resultSet.getInt("id");
//                ResultSet rGear = statement.executeQuery(sqlSelectGearHero(id_gear_def));
//                while (rGear.next()) {
//                    if (type.equals("Magicien")) {
//                        gearDef = new EnergyShield(rGear.getString("gear_name"),
//                                rGear.getInt("gear_stats"));
//                    } else {
//                        gearDef = new Shield(rGear.getString("gear_name"),
//                                rGear.getInt("gear_stats"));
//                    }
//
//                }
//                rGear = statement.executeQuery(sqlSelectGearHero(id_gear_of));
//                while (rGear.next()) {
//                    if (type.equals("Magicien")) {
//                        gearOf = new Spell(rGear.getString("gear_name"),
//                                rGear.getInt("gear_stats"));
//                    } else {
//                        gearOf = new Weapon(rGear.getString("gear_name"),
//                                rGear.getInt("gear_stats"));
//                    }
//                }
//                ArrayList<Heal> inventory = new ArrayList<>();
//                ResultSet rInventory = statement.executeQuery(sqlSelectInventoryHero(id_hero));
//                while (rInventory.next()) {
//                    inventory.add(new Heal(rInventory.getString("gear_name"), rInventory.getInt("gear_stats")));
//                }
//                if (type.equals("Magicien")) {
//                    players.add(new Wizard(id_hero, hero_name,
//                            hero_level,
//                            gearOf,
//                            gearDef,
//                            inventory));
//                } else {
//                    players.add(new Warrior(id_hero, hero_name,
//                            hero_level,
//                            gearOf,
//                            gearDef,
//                            inventory));
//                }
//            }
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//        game.setPlayersSave(players);
//        System.out.println(players);
//        closeConnection();
//    }
//}
