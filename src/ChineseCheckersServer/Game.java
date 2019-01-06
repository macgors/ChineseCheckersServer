package ChineseCheckersServer;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Math;
import java.util.ArrayList;

public class Game {
     Board gameboard;
    Player currentPlayer;
    static Player players[];
    int numOfPlayersConnected = 0;
    public int whoseTurn=0;
    int numOfBots;
    int numOfPlayers;
    public Game(int numOfPlayers, int numOfBots) {
        this.numOfPlayers = numOfPlayers;
        this.numOfBots = numOfBots;
        gameboard = new Board(numOfPlayers);
    }

    void setPlayers(ServerSocket sluchacz) throws IOException {
        Game.players = new Player[numOfPlayers];
        for (int i = 0; i < numOfPlayers-numOfBots; i++) {
            System.out.println("waiting for " + i + " player");
            Game.players[i] = new Human(sluchacz.accept(),i, numOfPlayers);
            numOfPlayersConnected++;
        }
        for(int i=numOfPlayers-numOfBots; i<numOfPlayers;i++){
            Game.players[i] = new Bot(i, numOfPlayers);
        }
        for (int i = 0; i < numOfPlayers; i++) {
            Game.players[i].start();
        }
        System.out.println("All players connected.");
    }

public class Human extends Player{
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    public Human(Socket socket, int PlayerNumber, int numberOfplayers1) {
        this.socket = socket;
        setColor(PlayerNumber);
        this.PlayerNumber=PlayerNumber;
        this.numberOfPlayers=numberOfplayers1;

        try {

            output = new PrintWriter(socket.getOutputStream(), true);
            Server.writers.add(output);
            output.println("WELCOME");
            output.println("COLOR " + this.color);
            output.println("NUMBER_OF_PLAYERS " + numberOfPlayers);
            output.println("PLAYER_NUMBER " + PlayerNumber);
            output.println("TURN "+ whoseTurn);




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
                    whoseTurn= (whoseTurn+1)%numberOfPlayers;
                    for (PrintWriter writer : Server.writers) {
                        writer.println("TURN "+ whoseTurn);
                    }
                    if(players[whoseTurn] instanceof Bot) {
                        players[whoseTurn].makeMoves();
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

    public class Bot extends Player{
        private Board pawnsBoards[]=new Board[10];
          public Bot(int playerNumber, int NumberOfPlayers){
             this.PlayerNumber=playerNumber;
             setColor(PlayerNumber);
             this.numberOfPlayers=NumberOfPlayers;
             for(int i=0;i<10;i++){
                 pawnsBoards[i]=gameboard;
             }
          }
          public void makeMoves() {

              for (int x = 0; x < 13; x++) {
                  for (int y = 0; y < 17; y++) {
                      if (gameboard.board[x][y].getFill().equals(this.color)) {
                          for (int i = 0; i < 13; i++) {
                              for (int j = 0; j < 17; j++) {
                                  if (gameboard.movePossible(i, j, x, y, this.color)) {
                                      gameboard.move(i, j, x, y,this.color);
                                      for (PrintWriter writer : Server.writers) {
                                          writer.println("MOVE "+ i + " " + j + " " + x+ " " + y);
                                      }

                                      return;
                                  }
                                    else if (gameboard.jumpPossible(i, j, x, y)){
                                      for (PrintWriter writer : Server.writers) {
                                          writer.println("MOVE "+ i + " " + j + " " + x+ " " + y);
                                      }

                                      gameboard.jump(i, j, x, y,this.color);
                                      return;
                                  }
                              }
                          }
                      }
                  }
              }
          }



          public int evaluate(Board BotBoard){
              int evaluation=0;
              int whereToWinX=-1;
              int whereToWinY=-1;
              if(this.color.equals(Color.RED)){
                  whereToWinY=0;
                  whereToWinX=6;
              }
              else if(this.color.equals(Color.GREEN)){
                  whereToWinY=16;
                  whereToWinX=6;
              }
              else if(this.color.equals(Color.YELLOW)){
                  whereToWinY=12;
                  whereToWinX=12;
              }
              else if(this.color.equals(Color.PURPLE)){
                  whereToWinY=4;
                  whereToWinX=12;
              }
              else if(this.color.equals(Color.PINK)){
                  whereToWinY=12;
                  whereToWinX=0;
              }
              else if(this.color.equals(Color.BLUE)){
                  whereToWinY=4;
                  whereToWinX=0;
              }
              for (int i=0;i<13;i++){
                  for(int j=0;j<17;j++){
                      if(BotBoard.board[i][j].getFill().equals(this.color)){
                        evaluation+=Math.abs(whereToWinX-i);
                        evaluation+=Math.abs(whereToWinY-j);
                      }
                  }
              }

            return evaluation;
          }

    }


}
