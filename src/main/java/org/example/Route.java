package org.example;

public class Route {
    private String id;
    private double distance;
    private int popularity;
    private boolean isFavorite;
    private String[] locationPoints;

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
}
