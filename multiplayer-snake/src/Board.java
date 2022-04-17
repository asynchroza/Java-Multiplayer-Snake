import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];
    private final int x2[] = new int[ALL_DOTS];
    private final int y2[] = new int[ALL_DOTS];

    private int sone_dots;
    private int stwo_dots;
    private int apple_x;
    private int apple_y;

    private boolean sone_leftDirection = false;
    private boolean sone_rightDirection = true;
    private boolean sone_upDirection = false;
    private boolean sone_downDirection = false;
    private boolean stwo_leftDirection = false;
    private boolean stwo_rightDirection = true;
    private boolean stwo_upDirection = false;
    private boolean stwo_downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("resources/head.png");
        head = iih.getImage();
    }

    private void initGame() {

        sone_dots = 3;
        stwo_dots = 3;

        for (int z = 0; z < sone_dots; z++) { // draws first snake
            x[z] = 50 - (z * 10);
            y[z] = 100;
        }

        for (int y = 0; y< stwo_dots; y++){ // draws second snake
            x2[y] = 150 - (y * 10);
            y2[y] = 100;
        }
        
        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < sone_dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                    // g.drawImage(head, x2[z], y2[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                    // g.drawImage(ball, x2[z], y2[z], this);
                }
            }

            for (int p = 0; p < stwo_dots; p++){
                if (p==0){
                    g.drawImage(head, x2[p], y2[p], this);
                } else {
                    g.drawImage(ball, x2[p], y2[p], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }        
    }

    private void gameOver(Graphics g) {
        
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) { // first snake eats apple
            sone_dots++;
            locateApple();
        }

        if ((x2[0] == apple_x) && (y2[0] == apple_y)) {
            stwo_dots++;
            locateApple();
        }
    }

    private void move() {

        for (int z = sone_dots; z > 0; z--) {
            // x[z] = x[(z - 1)];
            // y[z] = y[(z - 1)];
        }

        for (int p = stwo_dots; p > 0; p--){
            x2[p] = x2[(p-1)];
            y2[p] = y2[(p-1)];
        }

        if (sone_leftDirection) {
            // x[0] -= DOT_SIZE;
        }

        if(stwo_leftDirection){
            x2[0] -= DOT_SIZE;
        }

        if (sone_rightDirection) {
            // x[0] += DOT_SIZE;
        }

        if(stwo_rightDirection){
            x2[0] += DOT_SIZE;
        }

        if (sone_upDirection) {
            // y[0] -= DOT_SIZE;
        }

        if(stwo_upDirection){
            y2[0] -= DOT_SIZE;
        }

        if (sone_downDirection) {
            // y[0] += DOT_SIZE;
        }

        if(stwo_downDirection){
            y2[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = sone_dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false; // add flag that snake_one has eaten itself
                break;
            }

            if((y[0] == y2[z]) && (x[0] == x2[z])){ // first snake bites second snake
                inGame = false;
                break;
            }
            
        }

        if((y[0] == y2[0]) && (x[0] == x2[0])){ // head to head collision
            System.out.println("Head to head collision");
            inGame = false;
        }

        for (int p = stwo_dots; p > 0; p--) {

            if ((p > 4) && (x2[0] == x[p]) && (y2[0] == y2[p])){
                inGame = false; // add flag that snake_two has eaten itself
                break;
            }

            if((y2[0] == y[p]) && (x2[0] == x[p])){ // second snake bites first snake
                inGame = false;
                break;
            }

        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y2[0] >= B_HEIGHT){
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (y2[0] < 0){
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x2[0] >= B_WIDTH){
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if (x2[0] < 0){
            inGame = false;
        }
        
        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!sone_rightDirection)) { // left movement snake one
                sone_leftDirection = true;
                sone_upDirection = false;
                sone_downDirection = false;
            }

            if((key == KeyEvent.VK_A) && (!stwo_rightDirection)) { // left movement snake two
                stwo_leftDirection = true;
                stwo_upDirection = false;
                stwo_downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!sone_leftDirection)) { // right movement snake one
                sone_rightDirection = true;
                sone_upDirection = false;
                sone_downDirection = false;
            }

            if((key == KeyEvent.VK_D) && (!stwo_leftDirection)){ // right movement snake two
                stwo_rightDirection = true;
                stwo_upDirection = false;
                stwo_downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!sone_downDirection)) { // up movement snake one
                sone_upDirection = true;
                sone_rightDirection = false;
                sone_leftDirection = false;
            }

            if ((key == KeyEvent.VK_W) && (!stwo_downDirection)) { // up movement snake two
                stwo_upDirection = true;
                stwo_rightDirection = false;
                stwo_leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!sone_upDirection)) { // down movement snake one
                sone_downDirection = true;
                sone_rightDirection = false;
                sone_leftDirection = false;
            }

            if ((key == KeyEvent.VK_S) && (!stwo_upDirection)) {
                stwo_downDirection = true;
                stwo_rightDirection = false;
                stwo_leftDirection = false;
            }
        }
    }
}