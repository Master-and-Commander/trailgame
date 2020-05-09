/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.*;
import java.net.*;
import java.util.HashSet;
// Javafx
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

/**
 *
 * @author Caleb
 */
public class CarClient extends Application {
    // here variables should be private
    private DataInputStream fromServer;
    private DataOutputStream toServer;
    private ObjectOutputStream update;
    private ObjectInputStream resend;
    private Situation ensitu;
    private boolean gameOver = false;
    private static Circle curl;
    private static Veh v = new Veh();
    private static boolean[] direction = new boolean[4];
    private static Button[] navigation = new Button[4];
    private static int timeInterval = 150;
    private static int increaseInterval = 10;
    private static HBox controls = new HBox();
    private static BorderPane Bp = new BorderPane();
    private static Pane p = new Pane();
    private static int id;
    ////////////////////GUI///////////////////
    public void start(Stage primaryStage) {
        initialize(primaryStage);
        primaryStage.show();

    }

    //////////////////Initialization////////
    public void initialize(Stage primaryStage){

        try {
        // open socket for communication between client and server
        Socket clientSocket = new Socket("localhost", 7000);
        System.out.println("clientSet opened");
        update = new ObjectOutputStream(clientSocket.getOutputStream());
        resend = new ObjectInputStream(clientSocket.getInputStream());
        toServer = new DataOutputStream(clientSocket.getOutputStream());
        fromServer = new DataInputStream(clientSocket.getInputStream());

        }

        catch (Exception ex) {
            ex.printStackTrace();
        }

        Bp.setCenter(p);
        Bp.setBottom(controls);
        curl = new Circle(0, 100, 10);

        navigation[0] = new Button("UP");
        navigation[1] = new Button("DOWN");
        navigation[2] = new Button("RIGHT");
        navigation[3] = new Button("LEFT");

        for (int i = 0; i < 4; i++) {
            controls.getChildren().add(navigation[i]);
        }


        primaryStage.setScene(new Scene(Bp, 500,500));
        changeDirection(2); ///// car will start going right

        setRules();
        primaryStage.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    id = fromServer.readInt();
                    System.out.println(id);
                    while (!gameOver) {
                        Platform.runLater(new Runnable() {
                            public void run() {
                                try {
                                    // give server updated coordinates
                                    update.writeObject(new Identify(sendDirection(),id));
                                    update.flush();
                                    ensitu = (Situation)resend.readUnshared();
                                    if (ensitu.over)
                                        gameOver = true;
                                    redisplay();

                                }
                                catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                            }
                        });
                        Thread.sleep(timeInterval);
                    }
                }
                catch (Exception ex) {

                }
            }
        }).start();


    }


    public void redisplay() {
        int[] theList;
        Coordinate[] zaList;
        p.getChildren().clear();
        for (int i = 0; i < 2; i++) {
            theList = ensitu.rArray(i);
            zaList = ensitu.rPath(i);
            // only displays player if movement has been made
            if (zaList.length > 1) {
            Circle curl = new Circle(theList[0], theList[1], theList[2]);
            // colors the player's own circle red
            if (i == id) {
                curl.setFill(Color.RED);
            }
            p.getChildren().add(curl);
            putLinesTogether(zaList);
            }
        }
    }

    public int sendDirection() {
       for (int i = 0; i < 4; i++) {
            if (direction[i] == true)
                return i;
       }
                return 2;
    }

    /////////////beginning///////////////

    //////////////////ruleset//////////////
    public void setRules() {
        navigation[0].setOnAction(new upHandler());
        navigation[1].setOnAction(new downHandler());
        navigation[2].setOnAction(new rightHandler());
        navigation[3].setOnAction(new leftHandler());
    }
    public void changeDirection(int number) {
        for (int i = 0; i < 4; i++)
            direction[i] = false;
        direction[number] = true;
    }



    public void putLinesTogether(Coordinate[] path) {
        Line[] lines = new Line[path.length - 1];
        for (int i = 0; i < path.length - 1; i++) {
            lines[i] = new Line(path[i].x, path[i].y, path[i+1].x, path[i+1].y);
            p.getChildren().add(lines[i]);
        }

        // if the player gets out of the box, GAMEOVER
        if(path[path.length-1].x > 500 || path[path.length-1].y >500
                || path[path.length-1].x < 0 || path[path.length-1].y < 0) {
            gameOver=true;
        }
        HashSet<Coordinate> set = new HashSet();
        for (Coordinate c : path) {
            set.add(c);
        }
        // if set differs in length from the set player has crossed own line
        if (set.size()!=path.length) {
            gameOver=true;
            System.out.println("GAME OVER");
        }

    }


    ///////////////Handlers
    /// controls for moving the circle
    class upHandler implements EventHandler<ActionEvent> {
         @Override
         public void handle(ActionEvent e) {
             if(!direction[1])
                 changeDirection(0);
         }
     }
     class downHandler implements EventHandler<ActionEvent> {
         @Override
         public void handle(ActionEvent e) {
             if(!direction[0])
             changeDirection(1);
         }
     }
     class leftHandler implements EventHandler<ActionEvent> {
         @Override
         public void handle(ActionEvent e) {
             if (!direction[2])
             changeDirection(3);
         }
     }
     class rightHandler implements EventHandler<ActionEvent> {
         @Override
         public void handle(ActionEvent e) {
             if(!direction[3])
                 changeDirection(2);
         }
     }

     public static void main(String[] args) {
        Application.launch(args);
    }
}
