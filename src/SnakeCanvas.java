import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
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
    private int score = 0;
    private String highscore = getHighScore();

    private Image menuImage = null;
    private boolean isInMenu = true;
    private boolean isAtEndGame = false;
    private boolean hasWon = false;
    private int highScoreInt = -1;

    public void paint(Graphics g){

        //only resetting snake when exists. Update method calls paint method so important that new List not generated
        //all the time -- snake reset alot
        if (runThread == null){

            setPreferredSize(new Dimension(640, 480));
            addKeyListener(this);
            //this thread
            runThread = new Thread(this);
            runThread.start();
        }

        if (isInMenu){
            //draw menu
            drawMenu(g);
        }
        else if (isAtEndGame){

            //show end game
            drawEndGame(g);
            hasWon = false;
            isAtEndGame = true;
        }
        else {

            if (snake == null) {
                snake = new LinkedList<Point>();

                generateDefaultSnake();
                placeFruit();
            }

            if (highscore.equals("")){

                highscore = getHighScore();
            }
            drawFruit(g);
            drawBoard(g);
            drawSnake(g);
            drawScore(g);
        }
    }

    private void drawEndGame(Graphics g) {

        BufferedImage endGameImage = new BufferedImage(
                getPreferredSize().width, getPreferredSize().height, BufferedImage.TYPE_INT_ARGB);

        Graphics endGameGraphics = endGameImage.getGraphics();
        endGameGraphics.setColor(Color.BLACK);

        if (hasWon) {
            endGameGraphics.drawString(
                    "You won. Do you even have a life?", getPreferredSize().width/2, getPreferredSize().height/2);
        }
        else{

            if(highScoreInt < score){
                endGameGraphics.drawString(
                        "Congratulations " + highscore.split(":")[0] + " on setting a new high score!",
                        getPreferredSize().width / 2, getPreferredSize().height / 2);
            }
            endGameGraphics.drawString(
                    "You lost. Try again", getPreferredSize().width/2, getPreferredSize().height/2 + 30);
        }
        endGameGraphics.drawString("Your score: " + score, getPreferredSize().width/2, getPreferredSize().height/2 + 60);
        endGameGraphics.drawString(
                "Press \"SPACE\" to start again", getPreferredSize().width/2, getPreferredSize().height/2 + 90);

        g.drawImage(endGameImage, 0, 0, this);
    }

    private void drawMenu(Graphics g){

        if (menuImage == null){

            try {
                URL imagePath = SnakeCanvas.class.getResource("/teamdepardieu.png");
                menuImage = Toolkit.getDefaultToolkit().getImage(imagePath);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        g.drawImage(menuImage, 0, 0, 640, 480, this);
    }

    //contains the double buffering
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

            //calls update and paint method
            repaint();
            //game continues loop to keep running logic
            if(!isInMenu && !isAtEndGame)
                move();


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

        if (direction == Direction.NO_DIRECTION) return;
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
        if (direction!=Direction.NO_DIRECTION)
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
            checkScore();
            hasWon = false;
            isAtEndGame = true;
            return;

        }
        else if (newPoint.y < 0 || newPoint.y > BOARD_HEIGHT - 1){

            //OOB
            checkScore();
            hasWon = false;
            isAtEndGame = true;
            return;
        }
        else if (snake.contains(newPoint)){

            //gonna have a bad time. cannibalism?!!!!
            checkScore();
            hasWon = false;
            isAtEndGame = true;
            return;
        }
        else if (snake.size() == (BOARD_HEIGHT * BOARD_WIDTH)){

            checkScore();
            hasWon = true;
            isAtEndGame = true;
            //filled out all fields
            return;
        }

        //add head at first position. Push it into first position. Rest follows. Linkedlist ftw
        snake.push(newPoint);
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
        g.drawString("Highscore: " + highscore, 0, FIELD_HEIGHT * BOARD_HEIGHT + 35);
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

    private String getHighScore(){

        FileReader readFile = null;
        BufferedReader reader = null;

        //format: Brandon:100 json?
        try {
            readFile = new FileReader("highscore.dat");
            reader = new BufferedReader(readFile);

            return reader.readLine();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "everyone:0";
        } catch (IOException e) {
            e.printStackTrace();
            return "everyone:0";
        }
        finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkScore(){

        if (highscore.equals("")) return;

        highScoreInt = Integer.parseInt(highscore.split(":")[1]);

        if (score > highScoreInt){

            //better solution!
            String name = JOptionPane.showInputDialog(this,"Congrats, new high score. Enter name: ");
            highscore = name + ":" + score;


            File scoreFile = new File("highscore.dat");

            if (!scoreFile.exists()){
                try {
                    scoreFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileWriter writeFile = null;
            BufferedWriter writer = null;

            try {
                writeFile = new FileWriter(scoreFile);
                writer = new BufferedWriter(writeFile);
                writer.write(highscore);

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if(writer != null)
                        writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

            case KeyEvent.VK_ENTER:
                if(isInMenu){
                    isInMenu = false;
                    repaint();
                }
                break;

            case KeyEvent.VK_ESCAPE:
                isInMenu = true;
                break;

            case KeyEvent.VK_SPACE:
                if (isAtEndGame) {
                    isAtEndGame = false;
                    hasWon = false;
                    generateDefaultSnake();
                    repaint();
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
