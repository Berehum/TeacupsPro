package io.github.berehum.teacups.utils;

import org.bukkit.Location;

public class MathUtils {

    /**
     * @param location     location of center
     * @param r            radius of circle
     * @param currentPoint number of the iterator
     * @param totalPoints  total amount of points to create
     * @param offset       rotate the points on the circle
     * @return Location of the selected point on the circle.
     */
    public static Location drawPoint(Location location, double r, int currentPoint, int totalPoints, double offset) {

        double theta = ((Math.PI * 2) / totalPoints);
        double angle = (theta * (currentPoint));
        Location newLocation = location.clone();
        newLocation.add(r * Math.cos(angle + offset), 0, r * Math.sin(angle + offset));
        return newLocation;
    }

}
