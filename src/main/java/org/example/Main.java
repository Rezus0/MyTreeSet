package org.example;

import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        Route route1 = new Route("a", 100, 3, true, new String[]{"1", "2", "12", "3"});
        Route route2 = new Route("b", 200, 3, false, new String[]{"3", "5", "5", "9"});
        Route route3 = new Route("c", 300, 2, false, new String[]{"5", "3", "6"});
        Route route4 = new Route("d", 400, 6, true, new String[]{"6", "1", "7"});
        Route route5 = new Route("e", 10000, 1, true, new String[]{"1", "5", "12", "3"});
        MyNavigator navigator = new MyNavigator();
        navigator.addRoute(route3);
        navigator.addRoute(route4);
        navigator.addRoute(route5);
        navigator.addRoute(route1);
        navigator.addRoute(route1);
        navigator.addRoute(route2);
        System.out.println(navigator);
        Iterable<Route> someRoutes = navigator.getTop3Routes();
        for (Route someRoute : someRoutes) {
            System.out.println(someRoute);
        }
    }
}