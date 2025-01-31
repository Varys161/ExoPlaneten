package exo;

// Planet beschreibt alles, was man auf einem ExoPlanet mit seinem PlugIn-Robot tun kann...

public interface Planet {

    public Measure land(Robot robot, Position landPos);

    public Position getPosition(Robot robot);

    public Position move(Robot robot);

    public Direction rotate(Robot robot, Rotation rotation);

    public Measure scan(Robot robot);

    public Measure moveScan(Robot robot, Position newPos);

    public Size getSize();

    public void remove(Robot robot);

    public RobotStatus charge(Robot robot, int duration);
}