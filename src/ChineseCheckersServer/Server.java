package ChineseCheckersServer;

import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;


public class Server {
    private static final int PORT = 9001;
    boolean inPlay=false;
    int NumberOfPlayers;
    private static HashSet<Player> players = new HashSet<Player>();

    /**
     * The set of all the print writers for all the clients.  This
     * set is kept so we can easily broadcast messages.
     */
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();



    public static void main(String[] args) throws Exception {
        System.out.println("The game server is running.");
        ServerSocket listener = new ServerSocket(PORT);

        try {
            while (true) {
                new Player(listener.accept()).start(); // when new connection is established we spawn a new player and start the thread
            }
        } finally {
            listener.close();
        }
    }



}
