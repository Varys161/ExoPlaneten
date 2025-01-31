package exo;

import java.io.PrintStream;

public class RobotImpl implements Robot {
    private String landerName;
    private int energyLevel;

    @Override
    public void initRun(Planet planet, String lander, Position landPos, String userData, RobotStatus initStatus, PrintStream out) {
        this.landerName = lander;
        this.energyLevel = 100; // Initial energy level

        // Land the robot
        Measure measure = planet.land(this, landPos);
        out.println("Landed at: " + landPos + " with measure: " + measure);

        // Perform initial scan
        Measure scanResult = planet.scan(this);
        out.println("Initial scan result: " + scanResult);

        // Move and scan
        Position newPos = planet.move(this);
        Measure moveScanResult = planet.moveScan(this, newPos);
        out.println("Move and scan result: " + moveScanResult);

        // Manage energy
        manageEnergy(out);
    }

    private void manageEnergy(PrintStream out) {
        if (energyLevel < 20) {
            out.println("Energy low, returning to base for recharge.");
            // Logic to return to base for recharge
        } else {
            out.println("Energy level: " + energyLevel);
        }
    }

    @Override
    public void crash() {
        System.out.println("Robot crashed!");
    }

    @Override
    public void statusChanged(RobotStatus newStatus) {
        System.out.println("Status changed: " + newStatus.getMessage());
    }

    @Override
    public String getLanderName() {
        return landerName;
    }
}