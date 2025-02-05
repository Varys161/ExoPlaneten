package exo;

public class ExoResponseParser {

    public static void parseServerResponse(String json,
                                           RobotGUI gui,
                                           String robotName,
                                           RemoteRobotClient client) {
        String cmdVal = extractValueByKey(json, "CMD");
        if (cmdVal == null) {
            gui.log("[ERROR] Ungültige Server-Antwort (kein CMD): " + json);
            return;
        }

        gui.log("[CMD " + cmdVal + "]");

        switch (cmdVal) {

            case "init":
                // {"CMD":"init","SIZE":{"WIDTH":..., "HEIGHT":...}}
                parseSize(json, gui);
                break;

            case "landed":
                // {"CMD":"landed","MEASURE":{"GROUND":"...","TEMP":...}}
                   parseLanded(json, gui, client);
                break;

            case "scaned":
                // {"CMD":"scaned","MEASURE":{"GROUND":"...","TEMP":...}}
                parseScaned(json, gui, client);
                break;

            case "moved":
                // {"CMD":"moved","POSITION":{"X":...,"Y":...,"DIRECTION":"..."}}
                parseMoved(json, gui, robotName, client);
                break;

            case "mvscaned":
                // {"CMD":"mvscaned","MEASURE":{"GROUND":"...","TEMP":...},
                //  "POSITION":{"X":...,"Y":...,"DIRECTION":"..."}}
                parseMvScaned(json, gui, robotName, client);
                break;

            case "rotated":
                // {"CMD":"rotated","DIRECTION":"..."}
                String dirVal = extractValueByKey(json, "DIRECTION");
                gui.log("→ Neue Richtung: " + dirVal);
                if (dirVal != null && client.getCurrentPosition() != null) {
                    client.getCurrentPosition().setDir(Direction.valueOf(dirVal));
                    gui.updateRobotPosition(robotName, client.getCurrentPosition());
                }
                break;

            case "crashed":
                // {"CMD":"crashed"}
                gui.log("[CRASH] Roboter ist gecrasht!");
                break;

            case "exit":
                // {"CMD":"exit"}
                gui.log("[EXIT] Server beendet Verbindung.");
                break;

            case "error":
                // {"CMD":"error","ERROR":"..."}
                String errVal = extractValueByKey(json, "ERROR");
                gui.log("[ERROR] " + errVal);
                break;

            case "pos":
                // {"CMD":"pos","POSITION":{...}}
               parseMoved(json, gui, robotName, client);
                break;

            case "charged":
            case "status":
                // {"CMD":"charged","STATUS":{"TEMP":..., "ENERGY":..., "MESSAGE":"..."}}
                parseStatus(json, gui);
                break;

            default:
                gui.log("[WARN] Unbekanntes CMD: " + cmdVal);
        }
    }

    private static void parseLanded(String json, RobotGUI gui, RemoteRobotClient client) {
        String measureObj = extractObject(json, "MEASURE");
        if (measureObj != null) {
            String ground = extractValueByKey(measureObj, "GROUND");
            String temp   = extractValueByKey(measureObj, "TEMP");
            gui.log("→ MEASURE: GROUND=" + ground + ", TEMP=" + temp);

           Position pos = client.getCurrentPosition();
            if (pos != null) {
                gui.log("→ Färbe Landeplatz: " + pos.getX() + "," + pos.getY() +
                        " (" + ground + ")");
                gui.updateGroundType(pos.getX(), pos.getY(), ground);
            }
        }
    }

    private static void parseScaned(String json, RobotGUI gui, RemoteRobotClient client) {
        String measureObj = extractObject(json, "MEASURE");
        if (measureObj != null) {
            String ground = extractValueByKey(measureObj, "GROUND");
            String temp   = extractValueByKey(measureObj, "TEMP");
            gui.log("→ MEASURE: GROUND=" + ground + ", TEMP=" + temp);

            Position currentPos = client.getCurrentPosition();
            if (currentPos != null) {
                Position frontPos = getTileInFront(currentPos);
                gui.log("→ Färbe Scan-Feld vor dem Roboter: " +
                        frontPos.getX() + "," + frontPos.getY() + " (" + ground + ")");
                gui.updateGroundType(frontPos.getX(), frontPos.getY(), ground);
            }
        }
    }
private static void parseMoved(String json, RobotGUI gui,
                                   String robotName,
                                   RemoteRobotClient client) {
        String posObj = extractObject(json, "POSITION");
        if (posObj != null) {
            String xVal = extractValueByKey(posObj, "X");
            String yVal = extractValueByKey(posObj, "Y");
            String dir  = extractValueByKey(posObj, "DIRECTION");

            gui.log("→ POSITION: X=" + xVal + ", Y=" + yVal + ", DIR=" + dir);

            try {
                int x = Integer.parseInt(xVal);
                int y = Integer.parseInt(yVal);
                Direction direction = Direction.valueOf(dir);

                Position newPos = new Position(x, y, direction);
                 client.setCurrentPosition(newPos);
                gui.updateRobotPosition(robotName, newPos);

            } catch (Exception e) {
                gui.log("[WARN] parseMoved: " + e.getMessage());
            }
        }
    }

    private static void parseMvScaned(String json, RobotGUI gui,
                                      String robotName,
                                      RemoteRobotClient client) {
        String posObj = extractObject(json, "POSITION");
        Position newPos = null;

        if (posObj != null) {
            String xVal = extractValueByKey(posObj, "X");
            String yVal = extractValueByKey(posObj, "Y");
            String dir  = extractValueByKey(posObj, "DIRECTION");
            gui.log("→ POSITION: X=" + xVal + ", Y=" + yVal + ", DIR=" + dir);

            try {
                int x = Integer.parseInt(xVal);
                int y = Integer.parseInt(yVal);
                Direction direction = Direction.valueOf(dir);

                newPos = new Position(x, y, direction);
                client.setCurrentPosition(newPos);
                gui.updateRobotPosition(robotName, newPos);

            } catch (Exception e) {
                gui.log("[WARN] parseMvScaned (pos): " + e.getMessage());
            }
        }

       String measureObj = extractObject(json, "MEASURE");
        if (measureObj != null) {
            String ground = extractValueByKey(measureObj, "GROUND");
            String temp   = extractValueByKey(measureObj, "TEMP");
            gui.log("→ MEASURE: GROUND=" + ground + ", TEMP=" + temp);

            if (newPos != null) {
                gui.log("→ Färbe mvscan-Feld: " + newPos.getX() + "," + newPos.getY() +
                        " (" + ground + ")");
                gui.updateGroundType(newPos.getX(), newPos.getY(), ground);
            }
        }
    }

   private static Position getTileInFront(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        switch (pos.getDir()) {
            case NORTH: y -= 1; break;
            case SOUTH: y += 1; break;
            case WEST:  x -= 1; break;
            case EAST:  x += 1; break;
        }
        return new Position(x, y, pos.getDir());
    }

    private static void parseStatus(String json, RobotGUI gui) {
        // z. B. {"CMD":"status","STATUS":{"TEMP":..., "ENERGY":..., "MESSAGE":"..."}}
        String statusObj = extractObject(json, "STATUS");
        if (statusObj != null) {
            String temp   = extractValueByKey(statusObj, "TEMP");
            String energy = extractValueByKey(statusObj, "ENERGY");
            String msg    = extractValueByKey(statusObj, "MESSAGE");
            gui.log(String.format("→ STATUS: TEMP=%s, ENERGY=%s, MSG=%s", temp, energy, msg));
        }
    }

    private static void parseSize(String json, RobotGUI gui) {
        // {"CMD":"init","SIZE":{"WIDTH":..., "HEIGHT":...}}
        String sizeObj = extractObject(json, "SIZE");
        if (sizeObj != null) {
            String wVal = extractValueByKey(sizeObj, "WIDTH");
            String hVal = extractValueByKey(sizeObj, "HEIGHT");
            gui.log("→ SIZE: WIDTH=" + wVal + ", HEIGHT=" + hVal);
            try {
                int w = Integer.parseInt(wVal);
                int h = Integer.parseInt(hVal);
                // Grid in der GUI anpassen
                gui.setGridSize(w, h);
            } catch (NumberFormatException e) {
                gui.log("[WARN] parseSize: " + e.getMessage());
            }
        }
    }

   private static String extractValueByKey(String json, String key) {
        String search = "\"" + key + "\":";
        int idx = json.indexOf(search);
        if (idx == -1) return null;
        int start = idx + search.length();
        while (start < json.length() &&
                (json.charAt(start) == ' ' ||
                        json.charAt(start) == '\"' ||
                        json.charAt(start) == ':')) {
            start++;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '\"') break;  // Ende
            if (c == ',' || c == '}' || c == ']') break;
            sb.append(c);
        }
        return sb.toString().trim();
    }

    private static String extractObject(String json, String key) {
        String search = "\"" + key + "\":";
        int idx = json.indexOf(search);
        if (idx == -1) return null;

        int start = idx + search.length();
        while (start < json.length() && json.charAt(start) != '{') {
            start++;
        }
        if (start >= json.length()) return null;

        int braceCount = 0;
        for (int i = start; i < json.length(); i++) {
            if (json.charAt(i) == '{') {
                braceCount++;
            } else if (json.charAt(i) == '}') {
                braceCount--;
                if (braceCount == 0) {
                    return json.substring(start, i + 1);
                }
            }
        }
        return null;
    }
}