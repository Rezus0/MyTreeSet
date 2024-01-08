package org.example;


import java.util.Arrays;
import java.util.Iterator;

public class MyTreeSet implements Iterable<Route> {

    private MyTreeMap map;

    public MyTreeSet() {
        this.map = new MyTreeMap();
    }

    public Route get(String id) {
        return map.get(id);
    }

    public void add(Route route) {
        map.put(route);
    }

    public boolean contains(String id) {
        return map.contains(id);
    }

    public void remove(String id) {
        map.remove(id);
    }

    public int size() {
        return map.size();
    }

    @Override
    public Iterator<Route> iterator() {
        return map.iterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Route route : this) {
            builder.append("[");
            builder.append(route.getId());
            builder.append(", ");
            builder.append(route.getDistance());
            builder.append(", ");
            builder.append(route.getPopularity());
            builder.append(", ");
            builder.append(route.isFavorite());
            builder.append(", ");
            builder.append(Arrays.toString(route.getLocationPoints()));
            builder.append("]");
            builder.append("\n");
        }
        return builder.toString();
    }
}
