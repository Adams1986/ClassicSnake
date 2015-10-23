import java.awt.*;
import java.util.LinkedList;

/**
 * Created by simonadams on 23/10/15.
 */
public class Snake {

    Color color;
    LinkedList<Point>moves;
    String controls;
    Point startPoint;


    public Snake(Color color, LinkedList<Point> moves, Point startPoint, String controls) {
        this.color = color;
        this.moves = moves;
        this.startPoint = startPoint;
        this.controls = controls;

        moves.add(startPoint);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public LinkedList<Point> getMoves() {
        return moves;
    }

    public void setMoves(LinkedList<Point> moves) {
        this.moves = moves;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public String getControls() {
        return controls;
    }

    public void setControls(String controls) {
        this.controls = controls;
    }
}
