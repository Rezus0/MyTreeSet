package org.example;

import java.util.Iterator;

public class MyTreeMap implements Iterable<Route> {

    private RedBlackTree tree;

    public MyTreeMap() {
        this.tree = new RedBlackTree();
    }

    public Route get(String key) {
        return tree.searchData(key).getKey();
    }

    public void put(Route route) {
        tree.insertEntry(new MyEntry(route));
    }

    public void remove(String key) {
        tree.deleteEntry(key);
    }

    public boolean contains(String key) {
        return tree.searchData(key) != null;
    }

    public int size() {
        return tree.getSize();
    }

    @Override
    public Iterator<Route> iterator() {
        return tree.iterator();
    }
}
