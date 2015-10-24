import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Created by simonadams on 23/10/15.
 */
public class BackupReplaySnakeCanvas extends Canvas implements Runnable{

    private static final int FIELD_HEIGHT = 20;
    private static final int FIELD_WIDTH = 20;
    private static final int BOARD_HEIGHT = 15;
    private static final int BOARD_WIDTH = 15;

    private String moves;
    private LinkedList<Point> snake = new LinkedList<>();
    private StringBuilder sb = new StringBuilder();

    private Thread runThread;

    @Override
    public void paint(Graphics g) {

        snake.add(new Point(0, 0));

        System.out.println("in paint method");
        if (runThread == null){

            setPreferredSize(new Dimension(640, 480));
            //this thread
            runThread = new Thread(this);
            runThread.start();
        }

        drawBoard(g);
        drawSnake(g);

    }

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

        //flip cancas to onscreen. Imageobserver specified as this
        g.drawImage(offscreen, 0, 0, this);
    }

    private void move2(char ch){


        Point head = snake.peekFirst();
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
        System.out.println(newPoint.toString());
        snake.push(newPoint);

    }

    private void move(char ch){

                /*if (newPoint.x < 0 || newPoint.x > BOARD_WIDTH - 1){

                    //OOB
                    checkScore();
                    hasWon = false;
                    isAtEndGame = true;
                    return;

                }
                else if (newPoint.y < 0 || newPoint.y > BOARD_HEIGHT - 1){

                    //OOB
                    hasWon = false;
                    isAtEndGame = true;
                    return;
                }
                else if (snake.contains(newPoint)){

                    //gonna have a bad time. cannibalism?!!!!
                    hasWon = false;
                    isAtEndGame = true;
                    return;
                }
                else if (snake.size() == (BOARD_HEIGHT * BOARD_WIDTH)){

                    hasWon = true;
                    isAtEndGame = true;
                    //filled out all fields
                    return;
                }*/

        //add head at first position. Push it into first position. Rest follows. Linkedlist ftw

    }
    private void drawSnake(Graphics g) {

        g.setColor(Color.BLUE);


        for (Point p : snake) {

            g.fillRect(p.x * FIELD_WIDTH, p.y * FIELD_HEIGHT, FIELD_WIDTH, FIELD_HEIGHT);
        }

        g.setColor(Color.BLACK);
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


    //Run metode kaldes kun én gang ved opstart, men paint kaldes først. Paint kaldes 10(+1) gang. Repaint kalder metoden.
    @Override
    public void run() {

        System.out.println("inside run method");
        moves = "dsdsdsdsds";

        for (int i = 0; i < moves.length(); i++) {


            //calls update and paint method
            repaint();
            //game continues loop to keep running logic
            move2(moves.charAt(i));


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
}