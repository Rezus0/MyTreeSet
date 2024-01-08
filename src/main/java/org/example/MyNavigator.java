package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MyNavigator implements Navigator {

    private MyTreeSet routes;

    public MyNavigator() {
        this.routes = new MyTreeSet();
    }

    @Override
    public void addRoute(Route route) {
        routes.add(route);
    }

    @Override
    public void removeRoute(String routeId) {
        routes.remove(routeId);
    }

    @Override
    public boolean contains(Route route) {
        return routes.contains(route.getId());
    }

    @Override
    public int size() {
        return routes.size();
    }

    @Override
    public Route getRoute(String routeId) {
        return routes.get(routeId);
    }

    @Override
    public void chooseRoute(String routeId) {
        routes.get(routeId).setFavorite(true);
    }

    @Override
    public Iterable<Route> searchRoutes(String startPoint, String endPoint) {
        List<Route> result = new ArrayList<>();
        for (Route route : routes) {
            String[] points = route.getLocationPoints();
            if (points[0].equals(startPoint) && points[points.length - 1].equals(endPoint)) {
                result.add(route);
            }
        }
        result.sort(Comparator.comparing(Route::isFavorite)
                .thenComparingInt(this::distanceBetweenStartAndEnd)
                .reversed()
                .thenComparingInt(Route::getPopularity)
                .reversed());
        return result;
    }

    private int distanceBetweenStartAndEnd(Route route) {
        return route.getLocationPoints().length - 2;
    }

    @Override
    public Iterable<Route> getFavoriteRoutes(String destinationPoint) {
        List<Route> result = new ArrayList<>();
        for (Route route : routes) {
            if (route.isFavorite() && containsPoint(route, destinationPoint)) {
                result.add(route);
            }
        }

        result.sort(Comparator.comparingInt(this::distanceBetweenStartAndEnd)
                .reversed()
                .thenComparingInt(Route::getPopularity)
                .reversed());

        return result;
    }

    private boolean containsPoint(Route route, String point) {
        for (String locationPoint : route.getLocationPoints()) {
            if (locationPoint.equals(point)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterable<Route> getTop3Routes() {
        List<Route> result = new ArrayList<>();
        for (Route value : routes) {
            result.add(value);
        }
        Comparator<Route> comparator = Comparator.comparingInt(Route::getPopularity).reversed();
        comparator = comparator.thenComparingDouble(Route::getDistance);
        comparator = comparator.thenComparingInt(route -> route.getLocationPoints().length);
        result.sort(comparator);
        return result.subList(0, Math.min(3, result.size()));
    }

    @Override
    public String toString() {
        return routes.toString();
    }
}
