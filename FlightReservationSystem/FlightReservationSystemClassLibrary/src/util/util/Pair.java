/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.util;
import java.io.Serializable;
/**
 *
 * @author jeromegoh
 */

public class Pair<T> implements Serializable{
    private T first; 
    private T second;
    
    public Pair(T first, T second) {
        this.first = first;
        this.second = second; 
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public T getSecond() {
        return second;
    }

    public void setSecond(T second) {
        this.second = second;
    }
    
    
}
