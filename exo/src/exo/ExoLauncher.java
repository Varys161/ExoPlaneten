package exo;

public class ExoLauncher {

    public static void main(String[] args) {
        // 1) Eine GUI für beide Roboter
        RobotGUI gui = new RobotGUI();
        gui.setVisible(true);
        gui.log("GUI gestartet (Test) für 2 Roboter...");

        RobotImpl robot = new RobotImpl();

        // 2) Roboter A: "RobotLeft", Start (0,5)
        RemoteRobotClient robotLeft = new RemoteRobotClient(
                "localhost",
                8150,
                "RobotLeft",
                0,
                5,
                gui
        );

        // 3) Roboter B: "RobotRight", Start (9,5)
        RemoteRobotClient robotRight = new RemoteRobotClient(
                "localhost",
                8150,
                "RobotRight",
                9,
                5,
                gui
        );

        // 4) Beide Threads starten
        gui.log("Starte zwei Robot-Threads (Left/Right)...");
        new Thread(robotLeft).start();
        new Thread(robotRight).start();
    }
}
