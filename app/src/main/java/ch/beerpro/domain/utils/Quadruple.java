package ch.beerpro.domain.utils;

import java.io.Serializable;

public class Quadruple<T, U, V, W> extends Object implements Serializable{
    private T first;
    private U second;
    private V third;
    private W fourth;

    public Quadruple(T first, U second, V third, W fourth){
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public T getFirst(){
        return this.first;
    }

    public U getSecond(){
        return this.second;
    }

    public V getThird(){
        return this.third;
    }

    public W getFourth(){
        return this.fourth;
    }
}
