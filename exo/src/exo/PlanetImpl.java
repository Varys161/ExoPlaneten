package exo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class PlanetImpl implements Planet {
    private Socket socket;
    private PrintStream out;
    private BufferedReader in;

    public PlanetImpl(Socket socket, PrintStream out, BufferedReader in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
    }

    @Override
    public Measure land(Robot robot, Position landPos) {
        out.println("land:" + landPos.getX() + ":" + landPos.getY() + ":" + landPos.getDir().name());
        try {
            String response = in.readLine();
            if (response.startsWith("landed:")) {
                return Measure.parse(response.substring(7));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Position getPosition(Robot robot) {
        out.println("getpos");
        try {
            String response = in.readLine();
            if (response.startsWith("pos:")) {
                return Position.parse(response.substring(4));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Position move(Robot robot) {
        out.println("move");
        try {
            String response = in.readLine();
            if (response.startsWith("moved:")) {
                return Position.parse(response.substring(6));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Direction rotate(Robot robot, Rotation rotation) {
        out.println("rotate:" + rotation.name());
        try {
            String response = in.readLine();
            if (response.startsWith("rotated:")) {
                return Direction.valueOf(response.substring(8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Measure scan(Robot robot) {
        out.println("scan");
        try {
            String response = in.readLine();
            if (response.startsWith("scanned:")) {
                return Measure.parse(response.substring(8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Measure moveScan(Robot robot, Position newPos) {
        out.println("mvscan");
        try {
            String response = in.readLine();
            if (response.startsWith("mvscanned:")) {
                return Measure.parse(response.substring(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Size getSize() {
        out.println("size");
        try {
            String response = in.readLine();
            if (response.startsWith("SIZE:")) {
                return Size.parse(response.substring(5));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove(Robot robot) {
        out.println("remove");
    }

    @Override
    public RobotStatus charge(Robot robot, int duration) {
        out.println("charge:" + duration);
        try {
            String response = in.readLine();
            if (response.startsWith("charged:")) {
                return RobotStatus.parse(response.substring(8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}