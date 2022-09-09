package dungeon_and_dragon;

public class ControllerInventory {

    private String sqlInsertInventory() {
        return "INSERT INTO inventory(id_hero,id_gear_heal) " +
                "VALUES (?,?);";
    }
    private String sqlDeleteInventory() {
        return "DELETE FROM inventory " +
                "WHERE id_hero=? " +
                "AND id_gear_heal=?;";
    }
    private String sqlSelectInventoryHero() {
        return "SELECT gear.gear_stats,gear.gear_name " +
                "FROM inventory " +
                "INNER JOIN gear " +
                "ON inventory.id_gear_heal = gear.id " +
                "WHERE inventory.id_hero=?;";
    }
}
