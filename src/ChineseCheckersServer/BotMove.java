package ChineseCheckersServer;

public class BotMove {
    int targetX;
    int targetY;
    int fromX;
    int fromY;
    public BotMove(int targetX,int targetY,int fromX, int fromY){
        this.targetX=targetX;
        this.targetY=targetY;
        this.fromX=fromX;
        this.fromY=fromY;
    }
}
