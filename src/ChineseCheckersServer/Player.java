package ChineseCheckersServer;

import javafx.scene.paint.Color;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;

public class Player extends Thread {
    Color color;
    Player opponent;
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    public void setColor(Color color){
        this.color = color;
    }

    public Player(Socket socket) {
        this.socket = socket;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("WELCOME");
        } catch (IOException e) {
            System.out.println("Player died: " + e);
        }
    }
    public void run(){

    }

}
