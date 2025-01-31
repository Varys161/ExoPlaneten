package exo;

import java.io.PrintStream;

// Interface fuer eigene Plugin-Robot-Klassen, die per ExoBooster an einen ExoPlanet transferiert werden sollen

public interface Robot {

    public void initRun(Planet planet, String lander, Position landPos, String userData, RobotStatus initStatus, PrintStream out);

    public void crash();

    public void statusChanged(RobotStatus newStatus);

    public String getLanderName();
}