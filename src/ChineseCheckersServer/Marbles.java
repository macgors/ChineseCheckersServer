package ChineseCheckersServer;

import javafx.scene.paint.Color;

public class Marbles extends javafx.scene.shape.Circle {

    public void setDefaultGrayColor(Color color) {
        this.setFill(Color.GRAY);
    }

    public void setDefaultPlayerColor(Color color) {
        this.setFill(Color.DARKGRAY);
    }

    public void setColor(Color color) {
        this.setFill(color);
    }

}

