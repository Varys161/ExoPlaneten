package exo;

public class ExoCommandSender {

    /**
     * {"CMD":"orbit","NAME":"robotname"}
     */
    public static String createOrbitCommand(String robotName) {
        return "{\"CMD\":\"orbit\",\"NAME\":\"" + robotName + "\"}";
    }

    /**
     * {"CMD":"init","SIZE":{"WIDTH":int,"HEIGHT":int}}
     */
    public static String createInitCommand(int width, int height) {
        return "{\"CMD\":\"init\",\"SIZE\":{\"WIDTH\":" + width + ",\"HEIGHT\":" + height + "}}";
    }

    /**
     * {"CMD":"land","POSITION":{"X":int,"Y":int,"DIRECTION":"dir"}}
     */
    public static String createLandCommand(int x, int y, String direction) {
        return "{" +
                "\"CMD\":\"land\"," +
                "\"POSITION\":{" +
                "\"X\":" + x + "," +
                "\"Y\":" + y + "," +
                "\"DIRECTION\":\"" + direction + "\"" +
                "}" +
                "}";
    }

    /**
     * {"CMD":"scan"}
     */
    public static String createScanCommand() {
        return "{\"CMD\":\"scan\"}";
    }

    /**
     * {"CMD":"move"}
     */
    public static String createMoveCommand() {
        return "{\"CMD\":\"move\"}";
    }

    /**
     * {"CMD":"mvscan"}
     */
    public static String createMvScanCommand() {
        return "{\"CMD\":\"mvscan\"}";
    }

    /**
     * {"CMD":"rotate","ROTATION":"RIGHT"|"LEFT"}
     */
    public static String createRotateCommand(String rotation) {
        return "{\"CMD\":\"rotate\",\"ROTATION\":\"" + rotation + "\"}";
    }

    /**
     * {"CMD":"getpos"}
     */
    public static String createGetPosCommand() {
        return "{\"CMD\":\"getpos\"}";
    }

    /**
     * {"CMD":"charge","DURATION":int}
     */
    public static String createChargeCommand(int duration) {
        return "{\"CMD\":\"charge\",\"DURATION\":" + duration + "}";
    }
}
