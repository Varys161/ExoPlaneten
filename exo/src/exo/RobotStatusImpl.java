package exo;

public class RobotStatusImpl implements RobotStatus {
    private float workTemp;
    private int energy;
    private String message;

    public RobotStatusImpl(float workTemp, int energy, String message) {
        this.workTemp = workTemp;
        this.energy = energy;
        this.message = message;
    }

    @Override
    public float getWorkTemp() {
        return workTemp;
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    @Override
    public String getMessage() {
        return message;
    }
}