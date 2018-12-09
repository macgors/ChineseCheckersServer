package ChineseCheckersServer;

import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;



public class Server {
    private static final int PORT = 9001;
    boolean inPlay=false;
    int NumberOfPlayers;

    public static void main(String[] args) throws Exception {
        System.out.println("The game server is running.");
        ServerSocket listener = new ServerSocket(PORT);

        try {
            while (true) {
                Player player=new Player(listener.accept());
            }
        } finally {
            listener.close();
        }
    }



}
