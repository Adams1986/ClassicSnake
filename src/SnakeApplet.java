import java.applet.Applet;
import java.awt.*;
import java.util.LinkedList;

/**
 * Created by ADI on 21-10-2015.
 */
public class SnakeApplet extends Applet {

    private ReplaySnake canvas;

    private Snake opponentSnake;
    private Snake userSnake;

    public SnakeApplet() {
        userSnake = null;
        opponentSnake = null;
    }

    @Override
    public void init() {

        userSnake = new Snake(Color.BLUE, new LinkedList<Point>(), new Point(0,0), "dsdsdsdsdsdsdsds");
        opponentSnake = new Snake(Color.RED, new LinkedList<Point>(), new Point(14,14), "awawawawawawawaw");
        canvas = new ReplaySnake(userSnake, opponentSnake);
        canvas.setPreferredSize(new Dimension(640, 480));
        canvas.setVisible(true);
        canvas.setFocusable(true);

        //applet params
        add(canvas);
        setVisible(true);
        setSize(new Dimension(640, 480));

    }

    public void paint(Graphics g){

        setSize(new Dimension(640, 480));
    }
}
