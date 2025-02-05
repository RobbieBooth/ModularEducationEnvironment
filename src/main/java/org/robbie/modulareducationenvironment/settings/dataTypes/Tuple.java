package org.robbie.modulareducationenvironment.settings.dataTypes;

public class Tuple<K, V> {
    private K first;
    private V second;

    // Constructors
    public Tuple() {}

    public Tuple(K first, V second) {
        this.first = first;
        this.second = second;
    }

    // Getters and Setters
    public K getFirst() {
        return first;
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public V getSecond() {
        return second;
    }

    public void setSecond(V second) {
        this.second = second;
    }
}