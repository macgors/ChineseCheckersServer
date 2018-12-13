package ChineseCheckersServer;

public class Game {
    public Board board;
    Player currentPlayer;
    int numOfPlayers;
    public Game(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
        board = new Board(numOfPlayers);
    }

}
