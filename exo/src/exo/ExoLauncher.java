package exo;

public class ExoLauncher {

    public static void main(String[] args) {

        RobotGUI gui = new RobotGUI();
        gui.setVisible(true);
        gui.log("GUI gestartet (Test) für 2 Roboter...");

        RobotImpl robot = new RobotImpl();

        RemoteRobotClient robotLeft = new RemoteRobotClient(
                "localhost",
                8150,
                "RobotLeft",
                0,
                5,
                gui
        );

        RemoteRobotClient robotRight = new RemoteRobotClient(
                "localhost",
                8150,
                "RobotRight",
                9,
                5,
                gui
        );

        gui.log("Starte zwei Robot-Threads (Left/Right)...");
        new Thread(robotLeft).start();
        new Thread(robotRight).start();
    }
}
