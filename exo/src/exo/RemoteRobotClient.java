package exo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Ein RemoteRobotClient, der nach dem Landen mehrere Befehle
 * (scan, move, rotate) ausführt, um den Planeten ein wenig
 * zu "erkunden".
 */
public class RemoteRobotClient implements Runnable {

    private String hostname;
    private int port;
    private String robotName;
    private int startX;
    private int startY;
    private RobotGUI gui;
    private Position currentPosition;

    public RemoteRobotClient(String hostname, int port, String robotName,
                             int startX, int startY, RobotGUI gui) {
        this.hostname = hostname;
        this.port = port;
        this.robotName = robotName;
        this.startX = startX;
        this.startY = startY;
        this.gui = gui;
    }

    @Override
    public void run() {
        startClient();
    }

    /**
     * Hauptablauf:
     * 1) socket-Verbindung
     * 2) orbit
     * 3) land (sofort Roboter in GUI anzeigen)
     * 4) mehrfache Befehle: scan, move, rotate ...
     */
    public void startClient() {
        try (Socket socket = new Socket(hostname, port);
             PrintStream out = new PrintStream(socket.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            gui.log("[INFO] Verbunden mit Server: " + hostname + ":" + port);

            // 1) orbit
            String orbitCmd = ExoCommandSender.createOrbitCommand(robotName);
            logSend("orbit", orbitCmd);
            out.println(orbitCmd);
            processServerLine(in.readLine());

            // 2) land: Startposition + Richtung NORTH
            currentPosition = new Position(startX, startY, Direction.NORTH);
            gui.updateRobotPosition(robotName, currentPosition);

            String landCmd = ExoCommandSender.createLandCommand(
                    startX, startY, currentPosition.getDir().name());
            logSend("land", landCmd);
            out.println(landCmd);
            processServerLine(in.readLine());

            // 3) Erkundungsschleife
            for (int i = 1; i <= 5; i++) {
                // a) scan
                String scanCmd = ExoCommandSender.createScanCommand();
                logSend("scan #" + i, scanCmd);
                out.println(scanCmd);
                processServerLine(in.readLine());

                // b) move
                String moveCmd = ExoCommandSender.createMoveCommand();
                logSend("move #" + i, moveCmd);
                out.println(moveCmd);
                processServerLine(in.readLine());

                // c) rotate
                String rotateCmd = ExoCommandSender.createRotateCommand("RIGHT");
                logSend("rotate #" + i, rotateCmd);
                out.println(rotateCmd);
                processServerLine(in.readLine());

                // Du könntest noch mehr Befehle einbauen, z. B. charge, mvscan usw.
            }

            gui.log("[INFO] Erkundung beendet (Orbit, Land, 5× (scan, move, rotate)).");

        } catch (IOException e) {
            gui.log("[ERROR] Verbindung abgebrochen: " + e.getMessage());
        }
    }

    /**
     * Verarbeitet eine Zeile vom Server.
     */
    private void processServerLine(String line) {
        if (line != null) {
            gui.log("[RECV] " + line);
            ExoResponseParser.parseServerResponse(line, gui, robotName, this);
        } else {
            gui.log("[WARN] Server hat null/empty geschickt.");
        }
    }

    /**
     * Log-Helfer
     */
    private void logSend(String cmdName, String fullJson) {
        gui.log("[SEND " + cmdName.toUpperCase() + "] " + fullJson);
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Position newPos) {
        this.currentPosition = newPos;
    }
}
