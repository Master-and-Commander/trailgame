/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.geometry.Point2D;

/**
 *
 * @author Caleb
 */
class Veh implements Serializable {
    // you can use bitmaps
    // SVGs

    String name;
    // the synchronized lists make everything thread safe and reliable
    public List<Integer> qual = Collections.synchronizedList(new ArrayList<Integer>());

    List<Boolean> dist = Collections.synchronizedList(new ArrayList<Boolean>());
    List<Coordinate> path = Collections.synchronizedList(new ArrayList<Coordinate>());
    static Boolean[] direction = new Boolean[4];
    int increaseInterval = 5;
    Integer[] qualities = new Integer[3];

    Veh() {
        qualities[0] = 100;
        qualities[1] = 100;
        qualities[2] = 10;

        qual = Collections.synchronizedList((Arrays.asList(qualities)));
    }



    Veh (int x, int y, int radius) {
        qualities[0] = x;
        qualities[1] = y;
        qualities[2] = radius;
        qual = Collections.synchronizedList((Arrays.asList(qualities)));
    }


    public int[] getQualities() {
        int[] ar = new int[3];
        ar[0] = (Integer)qual.get(0);
        ar[1] = (Integer)qual.get(1);
        ar[2] = (Integer)qual.get(2);
        return ar;
    }

    public Coordinate[] getPath() {
        Coordinate[] pat = new Coordinate[path.size()];
        for (int i = 0; i < path.size(); i++) {
            pat[i] = (Coordinate)path.get(i);
        }
        return pat;
    }

    public boolean[] getDist() {
        boolean[] places = new boolean[4];
        for (int i = 0; i < 4; i++) {
            places[i] = (boolean)dist.get(i);
        }
        return places;
    }

    public void setDirection(int d) {
        for (int i = 0; i < 4; i++)
            direction[i] = false;
        if (d < 4 && d > -1)
        direction[d] = true;
        else
            System.out.println("Array out of bounds");
        dist = Collections.synchronizedList((Arrays.asList(direction)));
    }
    // getters and setters
    public String getName() {
        return name;
    }
     public int getX() {
        return qualities[0];
    }
    public int getY() {
        return qualities[1];
    }
    public int getRadius() {
        return qualities[2];
    }

    private void setX(int x) {
        qualities[0] = x;
    }
    private void setY(int y) {
        qualities[1] = y;
    }
    private void setRadius(int radius) {
        qualities[2] = radius;
    }

    // moves the circle

    public void move() {
        Coordinate coord = new Coordinate(getX(), getY());
        path.add(coord);

        if (direction[0])
            setY(getY() - increaseInterval);

        if (direction[1])
            setY(getY() + increaseInterval);

        if (direction[2])  {
            setX(getX() + increaseInterval);
        }
        if (direction[3])
            setX(getX() - increaseInterval);

        qual = Collections.synchronizedList((Arrays.asList(qualities)));
    }

}
