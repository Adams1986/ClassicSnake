import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Created by simonadams on 23/10/15.
 */
public class ReplaySnake extends Canvas {

    private static final int FIELD_HEIGHT = 20;
    private static final int FIELD_WIDTH = 20;
    private static final int BOARD_HEIGHT = 15;
    private static final int BOARD_WIDTH = 15;

    /*
    private String userControls;
    private String opponentControls;
    private LinkedList<Point> userSnake = new LinkedList<>();
    private LinkedList<Point> opponentSnake = new LinkedList<>();
    private Point startPointH;
    private Point startPointO;
    */
    private Snake userSnake;
    private Snake opponentSnake;

    public ReplaySnake(Snake userSnake, Snake opponentSnake){

        this.userSnake = userSnake;
        this.opponentSnake = opponentSnake;

    }

    public ReplaySnake(Snake userSnake){

        this.userSnake = userSnake;
    }

    /*public ReplaySnake(){

        startPointH = new Point(0, 0);
        startPointO = new Point(14, 14);
        userSnake.add(startPointH);
        opponentSnake.add(startPointO);

        userControls = "dsdsdsdsds";
        opponentControls = "awawawawaw";
    }*/

    @Override
    public void paint(Graphics g) {

        System.out.println("inside run method");

        setPreferredSize(new Dimension(640, 480));


        System.out.println("in paint method");

        drawBoard(g);

        /*drawSnake(g, userSnake, userControls, Color.BLUE);
        drawSnake(g,opponentSnake, opponentControls, Color.RED);*/
        drawSnake(g, userSnake);

        if(opponentSnake != null)
        drawSnake(g, opponentSnake);
    }

    //For flipping graphics. Better performance?
    @Override
    public void update(Graphics g) {

        Graphics offscreenGraphics;
        BufferedImage offscreen = null;
        Dimension d = getSize();

        //type of image allows rgb color system and alpha transparency. So transparent images also possible
        offscreen = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
        offscreenGraphics = offscreen.getGraphics();

        //color naturally painted in background. White as default
        offscreenGraphics.setColor(getBackground());
        offscreenGraphics.fillRect(0, 0, d.width, d.height);

        //Board. Default black
        offscreenGraphics.setColor(getForeground());

        paint(offscreenGraphics);

        //flip canvas to onscreen. Imageobserver specified as this
        g.drawImage(offscreen, 0, 0, this);
    }


    private void drawSnake(Graphics g, Snake snake) {

        g.setColor(snake.getColor());

        g.fillRect(snake.getMoves().peekFirst().x * FIELD_WIDTH, snake.getMoves().peekFirst().y * FIELD_HEIGHT, FIELD_WIDTH, FIELD_HEIGHT);

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < snake.getControls().length(); i++) {
            move(snake.getControls().charAt(i), snake);

            try {

                //10 secs per frame
                Thread.sleep(175);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (Point p : snake.getMoves()) {

                g.fillRect(p.x * FIELD_WIDTH, p.y * FIELD_HEIGHT, FIELD_WIDTH, FIELD_HEIGHT);
            }
        }

        //change back to black if we want to draw more.
        g.setColor(Color.BLACK);
    }

    private void drawSnake(Graphics g, LinkedList<Point> snake, String moves, Color color) {

        g.setColor(color);

        g.fillRect(snake.peekFirst().x * FIELD_WIDTH, snake.peekFirst().y * FIELD_HEIGHT, FIELD_WIDTH, FIELD_HEIGHT);

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < moves.length(); i++) {
            //move(moves.charAt(i), snake);

            try {

                //10 secs per frame
                Thread.sleep(175);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (Point p : snake) {

                g.fillRect(p.x * FIELD_WIDTH, p.y * FIELD_HEIGHT, FIELD_WIDTH, FIELD_HEIGHT);
            }
        }

        //change back to black if we want to draw more.
        g.setColor(Color.BLACK);
    }

    //adds point to the snake.
    private void move(char ch, Snake snake){

        Point head = snake.getMoves().peekFirst();
        Point newPoint = head;

        switch (ch) {

            case 'w':
                //
                newPoint = new Point(head.x, head.y - 1);
                break;

            case 's':
                newPoint = new Point(head.x, head.y + 1);
                break;

            case 'a':
                newPoint = new Point(head.x - 1, head.y);
                break;

            case 'd':
                newPoint = new Point(head.x + 1, head.y);
                break;
        }
        snake.getMoves().push(newPoint);

    }


    //Fill canvas with the board
    private void drawBoard(Graphics g){

        g.drawRect(0, 0, FIELD_WIDTH * BOARD_WIDTH, FIELD_HEIGHT * BOARD_HEIGHT);


        for (int x = FIELD_WIDTH; x < FIELD_WIDTH * BOARD_WIDTH ; x+= FIELD_WIDTH) {

            g.drawLine(x, 0, x, FIELD_HEIGHT * BOARD_HEIGHT);
        }

        for (int y = FIELD_HEIGHT; y < FIELD_HEIGHT * BOARD_HEIGHT ; y+= FIELD_HEIGHT) {

            g.drawLine(0, y, FIELD_WIDTH * BOARD_WIDTH, y);
        }
    }
}
