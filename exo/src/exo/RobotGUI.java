package exo;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RobotGUI extends JFrame {
    private JPanel panel;
    private JTextArea textArea;
    private Map<String, Position> robotPositions;
    private Map<Point, String> groundTypes;
    private int gridWidth;
    private int gridHeight;

    public RobotGUI(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        robotPositions = new HashMap<>();
        groundTypes = new HashMap<>();

        setTitle("Exoplanet Exploration");
        setSize(800, 700); // Größeres Fenster
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g);
                drawGround(g);
                drawRobots(g);
            }
        };
        panel.setPreferredSize(new Dimension(800, 600)); // Größeres Spielfeld
        panel.setBackground(Color.WHITE);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 100)); // Kleinere Konsole

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel, scrollPane);
        splitPane.setDividerLocation(600);
        add(splitPane);
    }

    private void drawGrid(Graphics g) {
        int cellWidth = panel.getWidth() / gridWidth;
        int cellHeight = panel.getHeight() / gridHeight;

        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= gridWidth; i++) {
            g.drawLine(i * cellWidth, 0, i * cellWidth, panel.getHeight());
        }
        for (int i = 0; i <= gridHeight; i++) {
            g.drawLine(0, i * cellHeight, panel.getWidth(), i * cellHeight);
        }
    }

    private void drawGround(Graphics g) {
        int cellWidth = panel.getWidth() / gridWidth;
        int cellHeight = panel.getHeight() / gridHeight;

        for (Map.Entry<Point, String> entry : groundTypes.entrySet()) {
            Point point = entry.getKey();
            String groundType = entry.getValue();
            Color color = getColorForGroundType(groundType);
            g.setColor(color);
            g.fillRect(point.x * cellWidth, point.y * cellHeight, cellWidth, cellHeight);
        }
    }

    private Color getColorForGroundType(String groundType) {
        switch (groundType) {
            case "SAND":
                return Color.YELLOW;
            case "GEROELL":
                return Color.LIGHT_GRAY;
            case "FELS":
                return Color.DARK_GRAY;
            case "WASSER":
                return Color.BLUE;
            case "PFLANZEN":
                return Color.GREEN;
            case "MORAST":
                return Color.BLACK;
            case "LAVA":
                return Color.ORANGE;
            case "NICHTS":
            default:
                return Color.WHITE;
        }
    }

    private void drawRobots(Graphics g) {
        int cellWidth = panel.getWidth() / gridWidth;
        int cellHeight = panel.getHeight() / gridHeight;

        g.setColor(Color.RED);
        for (Map.Entry<String, Position> entry : robotPositions.entrySet()) {
            Position pos = entry.getValue();
            int x = pos.getX() * cellWidth;
            int y = pos.getY() * cellHeight;
            g.fillOval(x, y, cellWidth, cellHeight);
            g.drawString(entry.getKey(), x + cellWidth / 2, y + cellHeight / 2);
        }
    }

    public void updateRobotPosition(String robotName, Position position) {
        robotPositions.put(robotName, position);
        panel.repaint();
    }

    public void updateGroundType(int x, int y, String groundType) {
        groundTypes.put(new Point(x, y), groundType);
        panel.repaint();
    }

    public String getGroundType(int x, int y) {
        return groundTypes.getOrDefault(new Point(x, y), "NICHTS");
    }

    public void log(String message) {
        textArea.append(message + "\n");
    }

    public static void main(String[] args) {
        RobotGUI gui = new RobotGUI(10, 6);
        gui.setVisible(true);

        // Erstelle und starte mehrere Roboter an unterschiedlichen Positionen
        RemoteRobotClient robot1 = new RemoteRobotClient("localhost", 8150, "Robot1", 2, 2, gui);
        RemoteRobotClient robot2 = new RemoteRobotClient("localhost", 8150, "Robot2", 4, 4, gui);
        RemoteRobotClient robot3 = new RemoteRobotClient("localhost", 8150, "Robot3", 6, 6, gui);

        new Thread(robot1::start).start();
        new Thread(robot2::start).start();
        new Thread(robot3::start).start();
    }
}