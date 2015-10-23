import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Created by simonadams on 22/10/15.
 */
public class App extends JFrame{

    private final Snake opponentSnake;
    private final Snake userSnake;

    private ModifiedSnakeCanvas canvas;

    private ReplaySnake replayer;
//    private BackupReplaySnake replayer;

    public App(){

        userSnake = new Snake(Color.BLUE, new LinkedList<Point>(), new Point(0,0), "dsdsdsdsdsdsdsds");
        opponentSnake = new Snake(Color.RED, new LinkedList<Point>(), new Point(14,14), "awawawawawawawaw");
        replayer = new ReplaySnake(userSnake, opponentSnake);
//        replayer = new BackupReplaySnake();
        replayer.setPreferredSize(new Dimension(640, 480));
        replayer.setVisible(true);
        replayer.setFocusable(true);

        JPanel panel = new JPanel();

        canvas = new ModifiedSnakeCanvas();
        canvas.setPreferredSize(new Dimension(640, 480));
        canvas.setVisible(true);
        canvas.setFocusable(true);

        panel.add(new JButton("Restart"), BorderLayout.NORTH);
        panel.add(replayer, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);

        setSize(new Dimension(640, 480));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //TODO:Points is equal to length of snake
    public static void main(String[] args) {

        new App();

    }
}
