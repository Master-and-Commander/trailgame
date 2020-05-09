/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Caleb
 */
class Situation implements Serializable {
    // if someone has crossed something
    // over will be set to true and read
    // from the client
    
    // Collections.synchronizedList makes my information thread safe
    // and easily serializable
    boolean over = false;
    ArrayList<Veh> chump = new ArrayList();
    List easy = Collections.synchronizedList(chump);
   HashSet<Coordinate> theSet = new HashSet<>();
   
   Set<Coordinate> see = Collections.synchronizedSet(theSet);
    
    Situation() {
        easy.add(new Veh(100, 100, 10));
        easy.add(new Veh(200, 200, 10));
    }
    // returns array of qualities from a particular player
    public int[] rArray(int id) {
        
        Veh v = (Veh)easy.get(id);
        return v.getQualities();
        
    }
    // returns array of coordinates from particular player
    public Coordinate[] rPath(int id) {
        Veh v = (Veh)easy.get(id);
        return v.getPath();
    }
    
    
    
    
    public void updatePlayer( Identify helper) {
        chump.get(helper.getId()).setDirection(helper.getDirection());
        chump.get(helper.getId()).move();
        
        Coordinate[] arr = chump.get(helper.getId()).getPath();
        for (Coordinate v : arr) {
            theSet.add(v);
        }
        see = Collections.synchronizedSet(theSet);
        if (see.size() != chump.get(0).getPath().length + chump.get(1).getPath().length) {
            Coordinate[] toCheck = chump.get(1).getPath();
            Coordinate[] f = chump.get(0).getPath();
            // A line has been crossed
            // find out which player crossed the line
            // by cross checking the most recent point for one player
            // against all the points of the other player
            for (int i = 0; i < toCheck.length; i++) {
                if (f[f.length-1].equals(toCheck[i])) {
                    System.out.println("Player 1");
                    over = true;
                }
                
            }
            
            for (int i = 0; i < f.length; i++) {
                if (toCheck[toCheck.length-1].equals(f[i])) {
                    System.out.println("Player 2");
                    over = true;
                }
                
            }
            
        }
        easy = Collections.synchronizedList(chump);
       
        
    }
    
   
}