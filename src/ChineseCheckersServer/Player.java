package ChineseCheckersServer;

import javafx.scene.paint.Color;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;

public class Player extends Thread {
    Color color;
    Socket socket;
    BufferedReader input;
    PrintWriter output;

    public void setColor(int a) {
        if (a == 0) this.color = Color.GREEN;
        if (a == 1) this.color = Color.RED;
        if (a == 2) this.color = Color.YELLOW;
        if (a == 3) this.color = Color.BLUE;
        if (a == 4) this.color = Color.PINK;
        if (a == 5) this.color = Color.DARKMAGENTA;
    }

    public Player(Socket socket, int PlayerNumber) {
        this.socket = socket;
        setColor(PlayerNumber);

        try {

            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("WELCOME " + this.color);
        } catch (IOException e) {
            System.out.println("Player died: " + e);
        }
    }

    public void run() {
        try {
            output.println("MESSAGE All players connected.");
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String command = input.readLine();
                if (command.startsWith("MOVE")) {
                    int fromX = command.charAt(6);
                    int fromY = command.charAt(8);
                    int targetX = command.charAt(10);
                    int targetY = command.charAt(11);

                } else if (command.startsWith("QUIT")) {
                    return;
                }
                else if (command.startsWith("TURN")) {

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


