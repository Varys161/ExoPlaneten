package exo;

import java.io.Serializable;

// Messdaten-Klasse

public class Measure implements Serializable {

    private static final long serialVersionUID = 2L;
    protected Ground ground;
    protected float temperature;
    public static final float TEMP_UNKNOWN = -999.9f;

    public Measure(Ground ground, float temperature) {
        setMeasure(ground, temperature);
    }

    public Measure(Ground ground) {
        this(ground, TEMP_UNKNOWN);
    }

    public Measure() {
        this(Ground.NICHTS, TEMP_UNKNOWN);
    }

    public Ground getGround() {
        return ground;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setMeasure(Ground ground, float temperature){
        this.ground = ground;
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "MEASURE|" + ground.name() + "|" + temperature;
    }

    public static Measure parse(String s) {
        String[] token = s.trim().split("\\|");
        if (token.length == 3 && token[0].equals("MEASURE")) {
            try {
                Ground g = Ground.valueOf(token[1]);
                float temp = Float.parseFloat(token[2]);
                return new Measure(g, temp);
            } catch (Exception e) {
                // Handle exception
            }
        }
        return null;
    }
}