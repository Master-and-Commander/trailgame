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
class Identify implements Serializable {
    // The client only sets a direction
    // and the id which acts like a
    // mailbox number
    int direction;
    int id;
    Identify() {
        
    }
    Identify(int direction, int id) {
        this.direction = direction;
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public int getDirection() {
        return direction;
    }
}