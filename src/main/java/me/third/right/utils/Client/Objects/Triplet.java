package me.third.right.utils.Client.Objects;

public class Triplet<T, U, V> {

    private T first;
    private U second;
    private V third;

    public Triplet(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T getFirst() { return first; }
    public U getSecond() { return second; }
    public V getThird() { return third; }
    public T setFirst(T first) { return this.first = first; }
    public U setSecond(U second) { return this.second = second; }
    public V setThird(V third) { return this.third = third; }
}
