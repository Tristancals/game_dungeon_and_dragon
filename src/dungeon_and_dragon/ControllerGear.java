package dungeon_and_dragon;


public class ControllerGear {
    private String sqlSelectGearHero() {
        return "SELECT gear_name,gear_type,gear_stats " +
                "FROM gear " +
                "WHERE gear.id=?;";
    }

    private String sqlUpdateGearHero() {
        return "UPDATE gear " +
                "SET " +
                "gear_name=?, " +
                "gear_type=?, " +
                "gear_stats=? " +
                "WHERE gear.id=?;";
    }

    private String sqlInsertGear() {
        return "INSERT INTO gear(gear_name," +
                "gear_type," +
                "gear_stats) " +
                "VALUES (?,?,?);";
    }
}
