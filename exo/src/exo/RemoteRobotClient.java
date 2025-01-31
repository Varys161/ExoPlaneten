package exo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class RemoteRobotClient implements Runnable {
    private String hostname;
    private int port;
    private String robotName;
    private int startX;
    private int startY;
    private RobotGUI gui;

    public RemoteRobotClient(String hostname, int port, String robotName, int startX, int startY, RobotGUI gui) {
        this.hostname = hostname;
        this.port = port;
        this.robotName = robotName;
        this.startX = startX;
        this.startY = startY;
        this.gui = gui;
    }

    @Override
    public void run() {
        start();
    }

    public void start() {
        try (Socket socket = new Socket(hostname, port);
             PrintStream out = new PrintStream(socket.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            gui.log("Connected to server: " + socket.isConnected());

            // Sende init-Befehl mit verschachteltem JSON-Objekt für die Größe
            String initCommand = createInitCommand(10, 6);
            gui.log("Sending init command: " + initCommand);
            out.println(initCommand);

            String initResponse = in.readLine();
            gui.log("Init response: " + initResponse);

            // Sende orbit-Befehl
            String orbitCommand = createOrbitCommand(robotName);
            gui.log("Sending orbit command: " + orbitCommand);
            out.println(orbitCommand);

            String orbitResponse = in.readLine();
            gui.log("Orbit response: " + orbitResponse);

            // Sende land-Befehl an einer sicheren Position
            String landCommand = createLandCommand(robotName, startX, startY, Direction.NORTH);
            gui.log("Sending land command: " + landCommand);
            out.println(landCommand);

            String landResponse = in.readLine();
            gui.log("Land response: " + landResponse);
            if (isSafeToLand(landResponse)) {
                gui.updateRobotPosition(robotName, new Position(startX, startY, Direction.NORTH));
                updateGroundType(landResponse);
            } else {
                gui.log("Unsafe landing position for " + robotName);
                return;
            }

            // Sende scan-Befehl
            String scanCommand = createScanCommand(robotName);
            gui.log("Sending scan command: " + scanCommand);
            out.println(scanCommand);

            String scanResponse = in.readLine();
            gui.log("Scan response: " + scanResponse);
            updateGroundType(scanResponse);

            // Sende move-Befehl
            String moveCommand = createMoveCommand(robotName);
            gui.log("Sending move command: " + moveCommand);
            out.println(moveCommand);

            String moveResponse = in.readLine();
            gui.log("Move response: " + moveResponse);
            if (moveResponse != null && moveResponse.contains("\"CMD\":\"moved\"")) {
                Position newPosition = parsePositionFromMoveResponse(moveResponse);
                gui.updateRobotPosition(robotName, newPosition);
            } else {
                gui.log("Move command failed for " + robotName);
            }

        } catch (IOException e) {
            gui.log("Connection error: " + e.getMessage());
        }
    }

    private String createInitCommand(int width, int height) {
        return String.format("{\"CMD\":\"init\",\"SIZE\":{\"WIDTH\":%d,\"HEIGHT\":%d}}", width, height);
    }

    private String createOrbitCommand(String robotName) {
        return String.format("{\"CMD\":\"orbit\",\"name\":\"%s\"}", robotName);
    }

    private String createLandCommand(String robotName, int x, int y, Direction direction) {
        return String.format("{\"CMD\":\"land\",\"name\":\"%s\",\"x\":%d,\"y\":%d,\"direction\":\"%s\"}", robotName, x, y, direction);
    }

    private String createScanCommand(String robotName) {
        return String.format("{\"CMD\":\"scan\",\"name\":\"%s\"}", robotName);
    }

    private String createMoveCommand(String robotName) {
        return String.format("{\"CMD\":\"move\",\"name\":\"%s\"}", robotName);
    }

    private boolean isSafeToLand(String response) {
        return response != null && response.contains("\"status\":\"safe\"");
    }

    private void updateGroundType(String response) {
        if (response != null && response.contains("\"CMD\":\"scan\"")) {
            int x = extractXFromResponse(response);
            int y = extractYFromResponse(response);
            String groundType = extractGroundTypeFromResponse(response);
            gui.updateGroundType(x, y, groundType);
        }
    }

    private int extractXFromResponse(String response) {
        // Implementiere die Logik, um den x-Wert aus der Antwort zu extrahieren
        return 0; // Beispielwert
    }

    private int extractYFromResponse(String response) {
        // Implementiere die Logik, um den y-Wert aus der Antwort zu extrahieren
        return 0; // Beispielwert
    }

    private String extractGroundTypeFromResponse(String response) {
        // Implementiere die Logik, um den Bodentyp aus der Antwort zu extrahieren
        return "NICHTS"; // Beispielwert
    }

    private Position parsePositionFromMoveResponse(String response) {
        // Implementiere die Logik, um die neue Position aus der Antwort zu extrahieren
        return new Position(0, 0, Direction.NORTH); // Beispielwert
    }
}