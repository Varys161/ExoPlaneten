package exo;

public class ExoLauncher {

    public static void main(String[] args) {
        // 1) GUI erstellen
        RobotGUI gui = new RobotGUI();  // Startet mit Grid 1×1
        gui.setVisible(true);
        gui.log("GUI gestartet (Test)");

        // 2) Client anlegen
        // Achte darauf, dass ein Server auf localhost:8150 läuft
        // und dass "Robot1" etc. akzeptiert werden.
        RemoteRobotClient client = new RemoteRobotClient(
                "localhost",      // Host
                8150,             // Port
                "Robot1",         // Robotername
                4,                // Start-X
                2,                // Start-Y
                gui               // Referenz auf die GUI
        );

        // 3) Client in eigenem Thread starten
        gui.log("Starte jetzt den Client-Thread...");
        new Thread(client).start();
    }
}