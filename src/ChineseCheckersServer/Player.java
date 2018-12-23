package ChineseCheckersServer;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class Player extends Thread {
    Color color;
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    int PlayerNumber;

    public void setColor(int a) {
        if (a == 0) this.color = Color.GREEN;
        if (a == 1) this.color = Color.RED;
        if (a == 2) this.color = Color.YELLOW;
        if (a == 3) this.color = Color.BLUE;
        if (a == 4) this.color = Color.PINK;
        if (a == 5) this.color = Color.DARKMAGENTA;
    }

    public Player(Socket socket, int PlayerNumber, int numberOfplayers) {
        this.socket = socket;
        setColor(PlayerNumber);
        this.PlayerNumber=PlayerNumber;

        try {

            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("WELCOME");
            output.println("COLOR " + this.color);
            output.println("NUMBER_OF_PLAYERS " + numberOfplayers);
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
                    Paint moverColor =Color.valueOf(parts[5]);

                    if(Game.board.movePossible(targetX,targetY,fromX,fromY,moverColor)) {
                        Game.board.move(targetX, targetY, fromX, fromY, moverColor);
                        for (PrintWriter writer : Server.writers) {
                            writer.println("MOVE " + targetX + " " + targetY + " " + fromX + " " + fromY);
                        }
                    }
                    else if(Game.board.jumpPossible(targetX,targetY,fromX,fromY,moverColor)){
                        Game.board.jump(targetX,targetY,fromX,fromY,moverColor);
                        for (PrintWriter writer : Server.writers) {
                            writer.println("MOVE "+ targetX + " " + targetY + " " + fromX+ " " + fromY);
                        }

                    }

                }
                else if (command.startsWith("QUIT")) {
                    return;
                }
                else if (command.startsWith("TURN")) {
                    Server.whoseTurn= (Server.whoseTurn+1)%4;
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


