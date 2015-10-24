import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class contains the logic for drawing one or two snakes to the canvas from a finished game
 */
public class BackupReplaySnake extends JPanel implements ActionListener {

    private static final int FIELD_HEIGHT = 20;
    private static final int FIELD_WIDTH = 20;
    private static final int BOARD_HEIGHT = 15;
    private static final int BOARD_WIDTH = 15;

    private Snake userSnake;
    private Snake opponentSnake;

    //Timer instead of Thread.sleep. Important in a JPanel
    private Timer tm = new Timer(500, this);
    private int counter = 0;
    ;

    /**
     * Constructor for completed game.
     * Two snake objects, for a completed game with two moving snakes.
     * @param userSnake
     * @param opponentSnake
     */
    public BackupReplaySnake(Snake userSnake, Snake opponentSnake){

        this.userSnake = userSnake;
        this.opponentSnake = opponentSnake;

    }

    /**
     * Constructor for a single users game, so he can watch his own movement.
     * @param userSnake
     */
    public BackupReplaySnake(Snake userSnake){

        this.userSnake = userSnake;
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);

        System.out.println("in paint method");

        drawBoard(g);

        tm.start();
        if (counter == 1){

            drawInstantReplayMessage(g);
        }

        drawSnake(g, userSnake);

        if (opponentSnake != null){
            drawSnake(g, opponentSnake);
        }

        counter = 1;
    }
    /**
     *The paint method. Contains the method-calls that do the painting to the canvas
     * @param g
     */
        /*@Override
        public void paint(Graphics g) {

            System.out.println("in paint method");

            drawBoard(g);

            if (counter == 1){

                drawInstantReplayMessage(g);
            }

            drawSnake(g, userSnake);

            if (opponentSnake != null){
                drawSnake(g, opponentSnake);
            }

            counter = 1;
        }
    */
    /**
     * For flipping graphics. Better for performance?
     * @param g
     */
        /*@Override
        public void update(Graphics g) {

            super.update(g);

            System.out.println("in update method");
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

            //flip canvas to onscreen. Image observer specified as this
            g.drawImage(offscreen, 0, 0, this);

            offscreenGraphics.dispose();
        }*/

    /**
     * Draws a string to the canvas letting user know this is an instant replay
     * @param g
     */
    private void drawInstantReplayMessage(Graphics g) {

        g.setColor(Color.BLACK);

        //TODO: is it a slow-mo?
        g.drawString("Instant replay!", BOARD_WIDTH, FIELD_HEIGHT * BOARD_HEIGHT + 30);
    }

    /**
     * Draws the snake objects linked list to the canvas. Uses the move method to populate the list.
     * @param g takes a graphics object as parameter
     * @param snake takes a snake object as a parameter (see snake class for more info)
     */
    private void drawSnake(Graphics g, Snake snake) {


        g.setColor(snake.getColor());

        g.fillRect(snake.getMoves().peekFirst().x * FIELD_WIDTH,
                snake.getMoves().peekFirst().y * FIELD_HEIGHT, FIELD_WIDTH, FIELD_HEIGHT);

        //Pauses thread so the snakes dont instantly start moving.
        //        try{
        //            Thread.sleep(1200);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }

        //populates snake by taking the string controls and checking whether up, down, left or right (w,s,a,d).
        for (int i = 0; i < snake.getControls().length(); i++) {
            move(snake.getControls().charAt(i), snake);

            try {

                    /*basically creates the 'instant' replay. First time paint() runs, snake moves faster than other
                     *the next times
                     */
                if (counter == 1) {
                    //TODO: Use timer to sleep
                }
                //                else

            } catch (Exception e) {
                e.printStackTrace();
            }

            //draw points in snake to canvas
            for (Point p : snake.getMoves()) {

                g.fillRect(p.x * FIELD_WIDTH, p.y * FIELD_HEIGHT, FIELD_WIDTH, FIELD_HEIGHT);
            }
        }
        //lets user see fully drawn canvas for some time.
        //        try {
        //            //TODO: timer instead
        //            Thread.sleep(1000);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        //resetting the snake so next time drawSnake method runs. It shows the same animation.
        Point p = snake.getMoves().peekLast();
        snake.getMoves().clear();
        snake.getMoves().add(p);

        //change back to black if we want to draw more.
        //        g.setColor(Color.BLACK);
    }

    //adds point to the snake.

    /**
     * The move method. Uses a snakes controls to asses the direction of the snake
     * @param ch
     * @param snake
     */
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


    @Override
    public void actionPerformed(ActionEvent e) {

        repaint();
    }


    /**
     * Fills canvas with the board.
     * Firstly with the complete board as a rectangle. Next, the lines horizontally and vertically
     * @param g takes a graphics object
     */
    private void drawBoard(Graphics g){

        g.drawRect(0, 0, FIELD_WIDTH * BOARD_WIDTH, FIELD_HEIGHT * BOARD_HEIGHT);


        //horizontal lines
        for (int x = FIELD_WIDTH; x < FIELD_WIDTH * BOARD_WIDTH ; x+= FIELD_WIDTH) {

            g.drawLine(x, 0, x, FIELD_HEIGHT * BOARD_HEIGHT);
        }

        //vertical lines
        for (int y = FIELD_HEIGHT; y < FIELD_HEIGHT * BOARD_HEIGHT ; y+= FIELD_HEIGHT) {

            g.drawLine(0, y, FIELD_WIDTH * BOARD_WIDTH, y);
        }
    }
}
