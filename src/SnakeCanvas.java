import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by ADI on 21-10-2015.
 */
public class SnakeCanvas extends Canvas implements Runnable, KeyListener {

    private static final int FIELD_HEIGHT = 15;
    private static final int FIELD_WIDTH = 15;
    private static final int BOARD_HEIGHT = 25;
    private static final int BOARD_WIDTH = 25;

    private LinkedList <Point> snake;
    private Point fruit;
    private int direction = Direction.NO_DIRECTION;

    //god-object. Runs methods in background
    private Thread runThread;
    private Graphics graphics;
    private int score = 0;

    public void paint(Graphics g){

        setPreferredSize(new Dimension(640, 480));
        snake = new LinkedList<Point>();
        generateDefaultSnake();
        placeFruit();

        //copy of paint graphics
        graphics = g.create();

        addKeyListener(this);

        if (runThread == null){

            //this thread
            runThread = new Thread(this);
            runThread.start();
        }
    }

    private void generateDefaultSnake(){

        score = 0;
        snake.clear();

        snake.add(new Point(0,2));
        snake.add(new Point(0,1));
        snake.add(new Point(0,0));
        direction = Direction.NO_DIRECTION;
    }

    @Override
    public void run() {

        while (true){

            //game continues loop to keep running logic
            move();
            draw(graphics);

            try {
                //access to current thread from run-method
                Thread.currentThread();

                //10 secs per frame
                Thread.sleep(100);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void move(){

        //grab first item of list
        Point head = snake.peekFirst();
        Point newPoint = head;

        //add Point to front of snake and remove from back in linked list

        //Direction of snake object
        //TODO: Clean up double switch statements
        switch (direction){

            case Direction.UP:
                //
                newPoint = new Point(head.x, head.y - 1);
                break;

            case Direction.DOWN:
                newPoint = new Point(head.x, head.y + 1);
                break;

            case Direction.LEFT:
                newPoint = new Point(head.x - 1, head.y);
                break;

            case Direction.RIGHT:
                newPoint = new Point(head.x + 1, head.y);
                break;
        }

        //removing last part of snake before adding below.
        //TODO: Maybe not remove for client/server project
        snake.remove(snake.peekLast());

        if (newPoint.equals(fruit)){

            score += 100;
            //TODO:
            //snake hits fruit. Add to list

            //add to end but which direction to add to?
            /*Point endPoint = snake.peekLast();
            snake.addLast(new Point());*/

            Point addPoint = (Point) newPoint.clone();

            switch (direction){

                case Direction.UP:
                    //
                    newPoint = new Point(head.x, head.y -1);
                    break;

                case Direction.DOWN:
                    newPoint = new Point(head.x, head.y +1);
                    break;

                case Direction.LEFT:
                    newPoint = new Point(head.x -1, head.y);
                    break;

                case Direction.RIGHT:
                    newPoint = new Point(head.x +1, head.y);
                    break;
            }

            //'Popping' in a new head. Again maybe change to the endPoint somehow
            snake.push(addPoint);
            placeFruit();



        }
        else if (newPoint.x < 0 || newPoint.x > BOARD_WIDTH - 1){

            //OOB
            generateDefaultSnake();
            return;

        }
        else if (newPoint.y < 0 || newPoint.y > BOARD_HEIGHT - 1){

            //OOB
            generateDefaultSnake();
            return;
        }
        else if (snake.contains(newPoint)){

            //gonna have a bad time. cannibalism?!!!!
            generateDefaultSnake();
            return;
        }

        //add head at first position. Push it into first position. Rest follows. Linkedlist ftw
        snake.push(newPoint);
    }

    public void draw(Graphics g){

        //plus 30 in end to make sure string score is also cleared. And plus 10 to allow right side board to draw line
        g.clearRect(0, 0, FIELD_WIDTH * BOARD_WIDTH + 10, FIELD_HEIGHT * BOARD_HEIGHT + 30);

        //doubleBuffering?! create a image and then setting canvas to this image?!
        BufferedImage buffer = new BufferedImage(
                FIELD_WIDTH * BOARD_WIDTH + 10, FIELD_HEIGHT * BOARD_HEIGHT + 30, BufferedImage.TYPE_INT_ARGB);

        //draw graphics directly to image. PaintBrush
        Graphics bufferGraphics = buffer.getGraphics();

        //Using paint brush to paint

        drawFruit(bufferGraphics);
        drawBoard(bufferGraphics);
        drawSnake(bufferGraphics);
        drawScore(bufferGraphics);

        //flip. Take what is on our screen and draw image on to it
        g.drawImage(buffer, 0, 0, FIELD_WIDTH * BOARD_WIDTH + 10, FIELD_HEIGHT * BOARD_HEIGHT + 30, this);
    }

    public void drawBoard(Graphics g){

        g.drawRect(0, 0, FIELD_WIDTH * BOARD_WIDTH, FIELD_HEIGHT * BOARD_HEIGHT);


        for (int x = FIELD_WIDTH; x < FIELD_WIDTH * BOARD_WIDTH ; x+= FIELD_WIDTH) {

            g.drawLine(x, 0, x, FIELD_HEIGHT * BOARD_HEIGHT);
        }

        for (int y = FIELD_HEIGHT; y < FIELD_HEIGHT * BOARD_HEIGHT ; y+= FIELD_HEIGHT) {

            g.drawLine(0, y, FIELD_WIDTH * BOARD_WIDTH, y);
        }
    }

    private void drawSnake(Graphics g) {

        g.setColor(Color.BLUE);

        for (Point p : snake){

            g.fillRect(p.x * FIELD_WIDTH, p.y * FIELD_HEIGHT, FIELD_WIDTH, FIELD_HEIGHT);
        }

        g.setColor(Color.BLACK);
    }


    private void drawFruit(Graphics g) {

        g.setColor(Color.CYAN);
        g.fillOval(fruit.x * FIELD_WIDTH, fruit.y * FIELD_HEIGHT, FIELD_WIDTH, FIELD_HEIGHT);
        g.setColor(Color.BLACK);
    }

    private void drawScore(Graphics g){

        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Score: " + score, 0, FIELD_HEIGHT * BOARD_HEIGHT + 20);
        g.setColor(Color.BLACK);
    }

    private void placeFruit(){

        Random random = new Random();
        int randomX = random.nextInt(BOARD_WIDTH);
        int randomY = random.nextInt(BOARD_HEIGHT);

        Point randomPoint = new Point(randomX, randomY);

        while (snake.contains(randomPoint)){

            randomX = random.nextInt(BOARD_WIDTH);
            randomY = random.nextInt(BOARD_HEIGHT);

            randomPoint = new Point(randomX, randomY);
        }
        fruit = randomPoint;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()){

            case KeyEvent.VK_UP:
                if (direction != Direction.DOWN)
                direction = Direction.UP;
                break;

            case KeyEvent.VK_DOWN:
                if (direction != Direction.UP)
                direction = Direction.DOWN;
                break;

            case KeyEvent.VK_LEFT:
                if (direction != Direction.RIGHT)
                direction = Direction.LEFT;
                break;

            case KeyEvent.VK_RIGHT:
                if (direction != Direction.LEFT)
                direction = Direction.RIGHT;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
