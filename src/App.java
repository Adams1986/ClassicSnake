import javax.swing.*;
import java.awt.*;

/**
 * Created by simonadams on 22/10/15.
 */
public class App extends JFrame{

    ModifiedSnakeCanvas canvas;

    public App(){

        JPanel panel = new JPanel();
        canvas = new ModifiedSnakeCanvas();
        canvas.setPreferredSize(new Dimension(640, 480));
        canvas.setVisible(true);
        canvas.setFocusable(true);

        panel.add(canvas);
        add(panel);
        setSize(new Dimension(640, 480));

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //TODO:Points is equal to length of snake
    public static void main(String[] args) {

        new App();

    }
}
