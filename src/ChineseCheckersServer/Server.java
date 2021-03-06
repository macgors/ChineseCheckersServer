package ChineseCheckersServer;

import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;


public class Server {
    private static final int PORT = 9001;
    boolean inPlay=false;
    int numOfPlayers;
    int numOfBots;
    int numOfPlayersConnected;


    private ServerSocket listener;
    static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    Game game;



    void settings() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Number of players: ");
        numOfPlayers = scanner.nextInt();
        System.out.println(numOfPlayers);
        System.out.println("Number of bots included: ");
        numOfBots = scanner.nextInt();
        System.out.println(numOfBots);

    }


    private void start() throws IOException {
        listener = new ServerSocket(PORT);
        settings();
        try {
            while (true) {
                Game game = new Game(numOfPlayers,numOfBots);
                if (numOfPlayersConnected == 0) {
                    numOfPlayersConnected++;
                    game.setPlayers(listener);
                }
            }
        }
        finally {
            listener.close();
        }
    }



    public static void main(String[] args) throws Exception {
        System.out.println("The game server is running.");
        Server server = new Server();
        server.start();

    }


}





