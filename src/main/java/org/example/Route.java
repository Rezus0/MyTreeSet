package org.example;

import java.util.Arrays;
import java.util.Objects;

public class Route implements Comparable<Route> {
    private String id;
    private double distance;
    private int popularity;
    private boolean isFavorite;
    private String[] locationPoints;

    public Route(String id, double distance, int popularity, boolean isFavorite, String[] locationPoints) {
        this.id = id;
        this.distance = distance;
        this.popularity = popularity;
        this.isFavorite = isFavorite;
        this.locationPoints = locationPoints;
    }

    public String getId() {
        return id;
    }

    public double getDistance() {
        return distance;
    }

    public int getPopularity() {
        return popularity;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public String[] getLocationPoints() {
        return locationPoints;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setLocationPoints(String[] locationPoints) {
        this.locationPoints = locationPoints;
    }

    @Override
    public int compareTo(Route o) {
        if (this.equals(o))
            return 0;
        return this.id.compareTo(o.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return id.equals(route.getId())
                || (locationPoints.length == route.getLocationPoints().length &&
                locationPoints[0].equals(route.getLocationPoints()[0]) &&
                locationPoints[locationPoints.length - 1].equals(route.getLocationPoints()[route.getLocationPoints().length - 1])
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "[" +
                id +
                ", " +
                distance +
                ", " +
                popularity +
                ", " +
                isFavorite +
                ", " +
                Arrays.toString(locationPoints) +
                "]";
    }
}
