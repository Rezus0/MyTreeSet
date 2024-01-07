package org.example;

public class MyNavigator implements Navigator {

    private MyTreeSet<Route> routes;

    @Override
    public void addRoute(Route route) {

    }

    @Override
    public void removeRoute(String routeId) {

    }

    @Override
    public boolean contains(Route route) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Route getRoute(String routeId) {
        return null;
    }

    @Override
    public void chooseRoute(String routeId) {

    }

    @Override
    public Iterable<Route> searchRoutes(String startPoint, String endPoint) {
        return null;
    }

    @Override
    public Iterable<Route> getFavoriteRoutes(String destinationPoint) {
        return null;
    }

    @Override
    public Iterable<Route> getTop3Routes() {
        return null;
    }
}
