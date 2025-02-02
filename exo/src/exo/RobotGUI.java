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

    /**
     * Standardkonstruktor – initiiert die GUI mit einer
     * Minimalgröße von 1×1 (oder 0×0), falls man wirklich
     * erst nach dem init-Befehl vom Server die Größe erfährt.
     */
    public RobotGUI() {
        // Starten wir mal mit 1×1, damit nichts kaputt geht,
        // solange wir noch nicht die echte Größe kennen.
        this.gridWidth = 1;
        this.gridHeight = 1;

        robotPositions = new HashMap<>();
        groundTypes = new HashMap<>();

        setTitle("Exoplanet Exploration");
        setSize(800, 700); // Fenstergröße, nicht Grid-Size
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
        panel.setPreferredSize(new Dimension(800, 600));
        panel.setBackground(Color.WHITE);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 100));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel, scrollPane);
        splitPane.setDividerLocation(600);
        add(splitPane);
    }

    private void drawGrid(Graphics g) {
        int cellWidth = (gridWidth > 0) ? panel.getWidth() / gridWidth : panel.getWidth();
        int cellHeight = (gridHeight > 0) ? panel.getHeight() / gridHeight : panel.getHeight();

        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= gridWidth; i++) {
            g.drawLine(i * cellWidth, 0, i * cellWidth, panel.getHeight());
        }
        for (int i = 0; i <= gridHeight; i++) {
            g.drawLine(0, i * cellHeight, panel.getWidth(), i * cellHeight);
        }
    }

    private void drawGround(Graphics g) {
        if (gridWidth <= 0 || gridHeight <= 0) return;

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
            case "SAND": return Color.YELLOW;
            case "GEROELL": return Color.LIGHT_GRAY;
            case "FELS": return Color.DARK_GRAY;
            case "WASSER": return Color.BLUE;
            case "PFLANZEN": return Color.GREEN;
            case "MORAST": return Color.BLACK;
            case "LAVA": return Color.ORANGE;
            case "NICHTS":
            default: return Color.WHITE;
        }
    }

    private void drawRobots(Graphics g) {
        if (gridWidth <= 0 || gridHeight <= 0) return;

        int cellWidth = panel.getWidth() / gridWidth;
        int cellHeight = panel.getHeight() / gridHeight;

        for (Map.Entry<String, Position> entry : robotPositions.entrySet()) {
            Position pos = entry.getValue();
            int cellX = pos.getX() * cellWidth;
            int cellY = pos.getY() * cellHeight;

            // Kleiner Kreis
            int diameter = Math.min(cellWidth, cellHeight) / 2;
            int centerX = cellX + (cellWidth - diameter) / 2;
            int centerY = cellY + (cellHeight - diameter) / 2;

            g.setColor(Color.RED);
            g.fillOval(centerX, centerY, diameter, diameter);

            // Name
            g.setColor(Color.BLACK);
            g.drawString(entry.getKey(), centerX + 2, centerY - 2);
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

    /**
     * Methode, um das Grid dynamisch anzupassen.
     * Wird vom ResponseParser aufgerufen,
     * sobald der Server "init" sendet.
     */
    public void setGridSize(int w, int h) {
        this.gridWidth = w;
        this.gridHeight = h;
        log("[INFO] Grid-Size aktualisiert auf: " + w + " x " + h);
        panel.repaint();
    }

    public void log(String message) {
        textArea.append(message + "\n");
        System.out.println(message); // zusätzlich in Konsole
    }

    public static void main(String[] args) {
        RobotGUI gui = new RobotGUI();
        gui.setVisible(true);
        gui.log("GUI gestartet (Test)");
    }
}
