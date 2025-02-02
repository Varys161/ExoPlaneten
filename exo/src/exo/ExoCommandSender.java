package exo;

public class ExoCommandSender {

    public static String createOrbitCommand(String robotName) {
        return "{\"CMD\":\"orbit\",\"NAME\":\"" + robotName + "\"}";
    }

    public static String createInitCommand(int width, int height) {
        return "{\"CMD\":\"init\",\"SIZE\":{\"WIDTH\":" + width + ",\"HEIGHT\":" + height + "}}";
    }

    public static String createLandCommand(int x, int y, String direction) {
        return "{\"CMD\":\"land\",\"POSITION\":{\"X\":" + x + ",\"Y\":" + y + ",\"DIRECTION\":\"" + direction + "\"}}";
    }

    public static String createScanCommand() {
        return "{\"CMD\":\"scan\"}";
    }

    public static String createMoveCommand() {
        return "{\"CMD\":\"move\"}";
    }

    public static String createMvScanCommand() {
        return "{\"CMD\":\"mvscan\"}";
    }

    public static String createRotateCommand(String rotation) {
        return "{\"CMD\":\"rotate\",\"ROTATION\":\"" + rotation + "\"}";
    }

    public static String createGetPosCommand() {
        return "{\"CMD\":\"getpos\"}";
    }

    public static String createChargeCommand(int duration) {
        return "{\"CMD\":\"charge\",\"DURATION\":" + duration + "}";
    }

    // Optionaler Test
    public static void main(String[] args) {
        System.out.println(createOrbitCommand("Robot1"));
        System.out.println(createInitCommand(10, 6));
        System.out.println(createLandCommand(2, 3, "NORTH"));
        System.out.println(createScanCommand());
        System.out.println(createMoveCommand());
        System.out.println(createMvScanCommand());
        System.out.println(createRotateCommand("RIGHT"));
        System.out.println(createGetPosCommand());
        System.out.println(createChargeCommand(120));
    }
}
