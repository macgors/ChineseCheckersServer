package ChineseCheckersServer;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Game {
     Board gameboard;
    Player currentPlayer;
    static Player players[];
    int numOfPlayersConnected = 0;

    int numOfPlayers;
    public Game(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
        gameboard = new Board(numOfPlayers);
    }

    void setPlayers(ServerSocket sluchacz) throws IOException {
        Game.players = new Player[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            System.out.println("waiting for " + i + " player");
            Game.players[i] = new Human(sluchacz.accept(),i, numOfPlayers);
            numOfPlayersConnected++;
        }
        for (int i = 0; i < numOfPlayers; i++) {
            Game.players[i].start();
        }
        System.out.println("All players connected.");
    }

public class Human extends Player{
    public Human(Socket socket, int PlayerNumber, int numberOfplayers1) {
        this.socket = socket;
        setColor(PlayerNumber);
        this.PlayerNumber=PlayerNumber;
        this.numberOfPlayers=numberOfplayers1;

        try {

            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("WELCOME");
            output.println("COLOR " + this.color);
            output.println("NUMBER_OF_PLAYERS " + numberOfPlayers);
            output.println("PLAYER_NUMBER " + PlayerNumber);
            for (PrintWriter writer : Server.writers) {
                writer.println("TURN "+ Server.whoseTurn);
            }

            Server.writers.add(output);

        } catch (IOException e) {
            System.out.println("Player died: " + e);
        }
    }

    public void run() {
        try {
            output.println("START");
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            while (true) {
                String command = input.readLine();
                System.out.println(command);
                if (command.startsWith("MOVE")) {
                    String[] parts = command.split(" ");
                    int targetX = Integer.parseInt(parts[1]);
                    int targetY = Integer.parseInt(parts[2]);
                    int fromX = Integer.parseInt(parts[3]);
                    int fromY = Integer.parseInt(parts[4]);
                    Paint moverColor = Color.valueOf(parts[5]);

                    if(gameboard.movePossible(targetX,targetY,fromX,fromY,moverColor)) {
                        gameboard.move(targetX, targetY, fromX, fromY, moverColor);
                        for (PrintWriter writer : Server.writers) {
                            writer.println("MOVE " + targetX + " " + targetY + " " + fromX + " " + fromY);
                        }
                    }
                    else if(gameboard.jumpPossible(targetX,targetY,fromX,fromY)){
                        gameboard.jump(targetX,targetY,fromX,fromY,moverColor);
                        for (PrintWriter writer : Server.writers) {
                            writer.println("MOVE "+ targetX + " " + targetY + " " + fromX+ " " + fromY);
                        }


                    }
                    Color winner = gameboard.winCondition(); //this both checks if there's a winner and informs player if there is one
                    if(!winner.equals(Color.GRAY)){
                        for (PrintWriter writer : Server.writers) {
                            writer.println("WINNER " + winner);
                        }
                    }
                    gameboard.printDebug();

                }
                else if (command.startsWith("QUIT")) {
                    return;
                }
                else if (command.startsWith("TURN")) {
                    Server.whoseTurn= (Server.whoseTurn+1)%numberOfPlayers;
                    for (PrintWriter writer : Server.writers) {
                        writer.println("TURN "+ Server.whoseTurn);
                    }


                }
            }
        } catch (Exception ex) {
            System.out.println("Player error: " + ex);
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }

        }
    }
}


}
