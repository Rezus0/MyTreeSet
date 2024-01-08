package org.example;

public class MyEntry implements Comparable<MyEntry> {
    private Route key;
    private Object value;

    public MyEntry(Route key) {
        this.key = key;
        this.value = new Object();
    }

    @Override
    public int compareTo(MyEntry o) {
        return key.compareTo(o.getKey());
    }

    public Route getKey() {
        return key;
    }

    public void setKey(Route key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
