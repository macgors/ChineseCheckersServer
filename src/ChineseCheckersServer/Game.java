package ChineseCheckersServer;

import java.io.PrintWriter;

public class Game {
    public static Board board;
    Player currentPlayer;
    static Player players[];

    int numOfPlayers;
    public Game(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
        board = new Board(numOfPlayers);
    }

}
