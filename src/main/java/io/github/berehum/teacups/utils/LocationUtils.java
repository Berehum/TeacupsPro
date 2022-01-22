package io.github.berehum.teacups.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class LocationUtils {

    private LocationUtils() {
    }

    /**
     * @param location     location of center
     * @param r            radius of circle
     * @param currentPoint number of the iterator
     * @param totalPoints  total amount of points to create
     * @param offset       rotate the points on the circle
     * @return Location of the selected point on the circle.
     */
    public static Location drawPoint(Location location, double r, int currentPoint, int totalPoints, double offset) {

        double theta = (2 * Math.PI) / totalPoints;
        double angle = theta * currentPoint;
        double fullAngle = angle + offset;

        Location pointLocation = location.clone();
        pointLocation.add(r * Math.cos(fullAngle), 0, r * Math.sin(fullAngle));

        return faceLocation(pointLocation, location);
    }

    /**
     * @param location       location to adjust
     * @param locationToFace location to face towards
     * @return location which is adjusted so that it faces the locationToFace
     * pitchOffset and yawOffset will both be 0.0f
     */
    public static Location faceLocation(Location location, Location locationToFace) {
        return faceLocation(location, locationToFace, 0.0f, 0.0f);
    }

    /**
     * @param location       location to adjust
     * @param locationToFace location to face towards
     * @param pitchOffset    offset of the pitch
     * @param yawOffset      offset of the yaw
     * @return location which is adjusted so that it faces the locationToFace
     */
    public static Location faceLocation(Location location, Location locationToFace, float pitchOffset, float yawOffset) {
        Location loc = location.clone();

        Vector from = loc.toVector();
        Vector to = locationToFace.toVector();

        loc.setDirection(to.subtract(from));

        return changeDirection(loc, pitchOffset, yawOffset);
    }

    public static Location changeDirection(Location location, float pitchOffset, float yawOffset) {
        return setDirection(location, location.getPitch() + pitchOffset, location.getYaw() + yawOffset);
    }

    public static Location setDirection(Location location, float pitch, float yaw) {
        Location loc = location.clone();
        loc.setPitch(numberRange(-90, 90, pitch));
        loc.setYaw(numberRange(-180, 180, yaw));
        return loc;
    }

    public static float numberRange(int min, int max, float input) {
        if (max <= min) throw new UnsupportedOperationException("Max number must be bigger than min number.");
        if (input > max) input = min + (input % max);
        if (input < min) input = max + (input % min);
        return input;
    }

}
