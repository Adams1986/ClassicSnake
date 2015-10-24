import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Created by simonadams on 22/10/15.
 */
public class App extends JFrame{

    private Snake opponentSnake;
    private Snake userSnake;

//    private ReplaySnake replayer;
    private ModifiedSnakeCanvas replayer;

    public App(){

        userSnake = new Snake(Color.BLUE, new LinkedList<Point>(), new Point(0,0), "dsdwdsssaaasssdddddd");
        opponentSnake = new Snake(Color.RED, new LinkedList<Point>(), new Point(14,14), "awawawawawawawaw");

//        replayer = new ReplaySnake(userSnake, opponentSnake);
        replayer = new ModifiedSnakeCanvas();
        replayer.setPreferredSize(new Dimension(640, 480));
        replayer.setVisible(true);
        replayer.setFocusable(true);

        add(replayer, BorderLayout.CENTER);;

        setSize(new Dimension(640, 480));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //TODO:Points is equal to length of snake
    public static void main(String[] args) {

        new App();

    }
}
