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
    int numberOfPlayers;

    public void setColor(int a) {
        if (a == 0) this.color = Color.GREEN;
        if (a == 1) this.color = Color.RED;
        if (a == 2) this.color = Color.YELLOW;
        if (a == 3) this.color = Color.BLUE;
        if (a == 4) this.color = Color.PINK;
        if (a == 5) this.color = Color.DARKMAGENTA;
    }

}



