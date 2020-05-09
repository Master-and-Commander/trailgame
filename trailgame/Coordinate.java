/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;

/**
 *
 * @author Caleb
 */
public class Coordinate implements Serializable, Comparable<Coordinate> {
    int x;
    int y;
    // this was used instead of the unserializable POINT2D
    Coordinate (int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int compareTo(Coordinate that) {
        if(that.x == x && that.y == y)
            return 0;
        
        return 1;
    }
    
    
    // must be overridden for the HashSet to know that a Coordinate has been
    // repeated
    
    @Override
    public boolean equals(Object o) {
        Coordinate second = (Coordinate)o;
        if (second.x == this.x && second.y == this.y) {
            return true;
        }
    return false;
    }
    @Override
    public int hashCode() {
        int result = 17;
        Integer theX = (Integer)x;
        Integer theY = (Integer)y;
        result = 31 * result + theX.hashCode();
        result = 31 * result + theY.hashCode();
        return result;
    }
}
