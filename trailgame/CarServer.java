/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 *
 * @author Caleb
 */
public class CarServer
{
    private static ServerSocket special = null;
    private static int counter = -1;
    private static Situation ensitu;
    private static Identify helper;
    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        ensitu = new Situation();

        new Thread(() -> {

            try {
                special = new ServerSocket(7000);
                System.out.println("Server started at "+ new Date());
               while (true) {
                   Socket socket = special.accept();
                   counter++;
                   // just for two players
                   if (counter < 2)
                   new Thread(new Player(socket,counter)).start();
               }
            }

            catch (Exception ex) {}

        }).start();
    }

    public static void messWithSituation(int decision, Identify helper, ObjectOutputStream tClient, int counter) {
        lock.lock();
            /*
            0 is send
            1 is writeTo
            */
            try {
                if (decision == 0) {
                  tClient.reset();
                  tClient.writeUnshared(ensitu);
                }
                else if (decision == 1) {
                    ensitu.updatePlayer(helper);
                }


            }
            catch (Exception ex) {}

            finally {
                lock.unlock();
            }


    }

    // about the same as book's HandleClient

    private static class Player implements Runnable {
        private Socket socket;
        int id;
        Player() {

        }

        Player(Socket socket, int counter) {
            this.socket = socket;
            id = counter;
        }

        public void run() {
            try {
                ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());
                toClient.flush();
                ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
                outputToClient.writeInt(id);

                while (true) {
                    // accesses the situation, but only one thread at a time
                    messWithSituation(1,(Identify)fromClient.readObject(), toClient, id);
                    messWithSituation(0, new Identify(2,0),toClient,id);
                }

            }
            catch (Exception ex) {

            }


        }
    }


}
