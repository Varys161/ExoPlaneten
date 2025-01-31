package exo;

public class JSONStuff {
    public static void main(String[] args) {
        String jsonString = createOrbitCommand("Robot1");
        System.out.println("Erstellter JSON-String: " + jsonString);

        parseJSONString(jsonString);
    }

    private String createInitCommand(int width, int height) {
        return String.format("{\"CMD\":\"init\",\"SIZE\":{\"WIDTH\":%d,\"HEIGHT\":%d}}", width, height);
    }

    public static String createOrbitCommand(String name) {
        return "{"
                + "\"CMD\":\"orbit\","
                + "\"NAME\":\"" + name + "\""
                + "}";
    }

    public static String createLandCommand(String name, int x, int y, String direction) {
        return "{"
                + "\"CMD\":\"land\","
                + "\"NAME\":\"" + name + "\","
                + "\"POSITION\":{"
                + "\"X\":" + x + ","
                + "\"Y\":" + y + ","
                + "\"DIRECTION\":\"" + direction + "\""
                + "}"
                + "}";
    }

    public static String createScanCommand(String name) {
        return "{"
                + "\"CMD\":\"scan\","
                + "\"NAME\":\"" + name + "\""
                + "}";
    }

    public static String createMoveCommand(String name) {
        return "{"
                + "\"CMD\":\"move\","
                + "\"NAME\":\"" + name + "\""
                + "}";
    }

    public static String createRotateCommand(String name, String rotation) {
        return "{"
                + "\"CMD\":\"rotate\","
                + "\"NAME\":\"" + name + "\","
                + "\"ROTATION\":\"" + rotation + "\""
                + "}";
    }

    public static void parseJSONString(String jsonString) {
        String command = jsonString.split("\"CMD\":\"")[1].split("\"")[0];
        String name = jsonString.split("\"NAME\":\"")[1].split("\"")[0];
        String direction = jsonString.contains("\"DIRECTION\":\"") ? jsonString.split("\"DIRECTION\":\"")[1].split("\"")[0] : null;
        int x = jsonString.contains("\"X\":") ? Integer.parseInt(jsonString.split("\"X\":")[1].split(",")[0]) : 0;
        int y = jsonString.contains("\"Y\":") ? Integer.parseInt(jsonString.split("\"Y\":")[1].split(",")[0]) : 0;

        System.out.println("Command: " + command);
        System.out.println("Name: " + name);
        if (direction != null) {
            System.out.println("Direction: " + direction);
        }
        if (jsonString.contains("\"X\":") && jsonString.contains("\"Y\":")) {
            System.out.println("Position: x=" + x + ", y=" + y);
        }
    }
}