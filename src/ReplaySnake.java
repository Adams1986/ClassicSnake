import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class contains the logic for drawing one or two snakes to the canvas from a finished game
 */
public class ReplaySnake extends JPanel implements ActionListener{

    private static final int FIELD_HEIGHT = 20;
    private static final int FIELD_WIDTH = 20;
    private static final int BOARD_HEIGHT = 15;
    private static final int BOARD_WIDTH = 15;

    private Snake userSnake;
    private Snake opponentSnake;

    //Timer instead of Thread.sleep. Important in a JPanel
    private Timer tm = new Timer(400, this);
    private int counter = 0;
    ;

    /**
     * Constructor for completed game.
     * Two snake objects, for a completed game with two moving snakes.
     * @param userSnake
     * @param opponentSnake
     */
    public ReplaySnake(Snake userSnake, Snake opponentSnake){

        this.userSnake = userSnake;
        this.opponentSnake = opponentSnake;

    }

    /**
     * Constructor for a single users game, so he can watch his own movement.
     * @param userSnake
     */
    public ReplaySnake(Snake userSnake){

        this.userSnake = userSnake;
    }

    /**
     *The paint method. Contains the method-calls that do the painting to the canvas
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {

        System.out.println("hello");
        super.paintComponent(g);

        drawBoard(g);


            drawSnake(g, userSnake);

            if (opponentSnake != null) {
                drawSnake(g, opponentSnake);
            }
        tm.start();
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


        //draw points in snake to canvas
        for (Point p : snake.getMoves()) {
            g.fillRect(p.x * FIELD_WIDTH, p.y * FIELD_HEIGHT, FIELD_WIDTH, FIELD_HEIGHT);
        }

        //change back to black if we want to draw more.
//        g.setColor(Color.BLACK);
    }

    /**
     * The move method. Uses a snakes controls to asses the direction of the snake
     * @param snake
     * @param ch
     */
    private void move(Snake snake, char ch){

        Point head = snake.getMoves().peekFirst();
        Point newPoint = head;;

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


    /**
     * uses movemethod to make the 'incremental' changes of the snake to make animation materialize
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        //populates snake by taking the string controls and checking whether up, down, left or right (w,s,a,d).
        if (counter < userSnake.getControls().length()) {
            move(userSnake, userSnake.getControls().charAt(counter));
        }
        if (opponentSnake != null) {
            if (counter < opponentSnake.getControls().length()){
                move(opponentSnake, opponentSnake.getControls().charAt(counter));
            }
        }
        if(counter > userSnake.getControls().length() &&
                (opponentSnake!= null && counter > opponentSnake.getControls().length())){

            tm.stop();
        }
        else {
            counter++;
            repaint();
        }
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
