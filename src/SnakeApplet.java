import java.applet.Applet;
import java.awt.*;

/**
 * Created by ADI on 21-10-2015.
 */
public class SnakeApplet extends Applet {

    private SnakeCanvas canvas;

    @Override
    public void init() {

        canvas = new SnakeCanvas();
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
