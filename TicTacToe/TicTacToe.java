/**
* TicTacToe with GUI
* @Autor Anton Avraamov
* @Version 0.1.2 dated November 01, 2016
*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*; 
import javax.swing.*;

class TicTacToe {
    public static void main(String[] args){
        new MyWindow();
    }
}

class MyWindow extends JFrame {
    Map jpMap; // Main field
    Font btnFont = new Font("Times New Roman", Font.PLAIN, 16); // Font for buttons
    public MyWindow() { // MyWindow constructor
        setSize(505, 587);
        setResizable(false);
        setLocation(800, 200);
        setTitle("Tic-Tac-Toe");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jpMap = new Map();
        jpMap.startNewGame(3);

        add(jpMap, BorderLayout.CENTER);
        // Game main menu
        JPanel jpBottom = new JPanel(new CardLayout());
        jpBottom.setPreferredSize(new Dimension(1, 60));
        add(jpBottom, BorderLayout.SOUTH);
        // Start and exit buttons
        JPanel jpStartExit = new JPanel(new GridLayout());
        JButton jbStart = new JButton("Start new Game");
        jbStart.setFont(btnFont);
        jbStart.addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) jpBottom.getLayout()).show(jpBottom, "jpSelectPlayers");
            }
        });
            jpStartExit.add(jbStart);
        JButton jbExit = new JButton("Exit Game");
        jbExit.setFont(btnFont);
        jbExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        jpStartExit.add(jbExit);
        jpBottom.add(jpStartExit, "jpStartExit");

        // Select players
        JPanel jpSelectPlayers = new JPanel(new GridLayout());
        JButton jbHumanVsHuman = new JButton("Human vs human");
        jbHumanVsHuman.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) jpBottom.getLayout()).show(jpBottom, "jpSelectMap");
            }
        });
        JButton jbHumanVsAI = new JButton("Hunan vs AI");
        jbHumanVsAI.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                ((CardLayout) jpBottom.getLayout()).show(jpBottom, "jpSelectMap");
            }
        });
        jpSelectPlayers.add(jbHumanVsHuman);
        jpSelectPlayers.add(jbHumanVsAI);
        jpBottom.add(jpSelectPlayers, "jpSelectPlayers");

        // Field size and series
        JPanel jpSelectMap = new JPanel(new GridLayout());
        JButton jbSM3x3l3 = new JButton("Field: 3x3 Series: 3");
        JButton jbSM5x5l4 = new JButton("Field: 5x5 Series: 4");
        JButton jbSM10x10l5 = new JButton("Field: 10x10 Series: 5");
        jpSelectMap.add(jbSM3x3l3);
        jbSM3x3l3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) jpBottom.getLayout()).show(jpBottom, "jpStartExit");
                createMap(3, 3);
            }
        });
        jpSelectMap.add(jbSM5x5l4);
        jbSM5x5l4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) jpBottom.getLayout()).show(jpBottom, "jpStartExit");
                createMap(5, 4);
            }
        });
        jpSelectMap.add(jbSM10x10l5);
        jbSM10x10l5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) jpBottom.getLayout()).show(jpBottom, "jpStartExit");
                createMap(10, 5);
            }
        });
        jpBottom.add(jpSelectMap, "jpSelectMap");
        ((CardLayout) jpBottom.getLayout()).show(jpBottom, "jpStartExit");
        setVisible(true);
    }
    // Start a new game with the fild size and with the number of dots 
    public void createMap(int size, int dots){
        jpMap.startNewGame(size);
        jpMap.setDotsToWin(dots);
    }
}

class Map extends JPanel{
    private int linesCount;
    private int dotsToWin = 3;
    private final int PANEL_SIZE = 500;
    private int CELL_SIZE;
    private boolean gameOver;
    private String gameOverMsg;
    Random rand = new Random();
    private int[][] field;
    private final int PLAYER1_DOT = 1;
    private final int PLAYER2_DOT = 2;
    // Map constructor
    public Map(){
        startNewGame(3);
        setBackground(Color.white);

        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                super.mouseReleased(e);
                int cmx, cmy;
                cmx = e.getX()/CELL_SIZE;
                cmy = e.getY()/CELL_SIZE;
                if(!gameOver){
                    if (setDot(cmx, cmy, PLAYER1_DOT)) {
                        checkFieldFull();
                        checkWin(PLAYER1_DOT);
                        repaint();
                        aiTurn();
                    }
                }
            }
        });
    }
    public void setDotsToWin(int dots){
        this.dotsToWin = dots;
    }
    // AI turn
    public void aiTurn() {
        if (gameOver) return;
        int x, y;
        do {
            x = rand.nextInt(linesCount);
            y = rand.nextInt(linesCount);
        } while (!setDot(x, y, PLAYER2_DOT));
        checkFieldFull();
        checkWin(PLAYER2_DOT);
        repaint();
    }
    // Start a new game
    public void startNewGame(int linesCount){
        this.linesCount = linesCount;
        CELL_SIZE = PANEL_SIZE/linesCount;
        gameOver = false;
        field = new int[linesCount][linesCount];
        repaint();
    }
    // is the field full?
    public void checkFieldFull(){
        boolean b = true;
        for (int i = 0; i < linesCount; i++) {
            for (int j = 0; j < linesCount; j++) {
                if(field[i][j] == 0) b = false; 
            }
        }
        if (b) {
            gameOver = true;
            gameOverMsg = "DRAW...";
        }
    }
    // Checking for a victory
    public boolean checkWin(int ox){
        for (int i = 0; i < linesCount; i++) {
            for (int j = 0; j < linesCount; j++) {
                if (checkLine(i, j, 1, 0, dotsToWin, ox) || checkLine(i, j, 0, 1, dotsToWin, ox) || 
                    checkLine(i, j, 1, 1, dotsToWin, ox)|| checkLine(i, j, 1, -1, dotsToWin, ox)){
                    gameOver = true;
                    if(PLAYER1_DOT == ox) gameOverMsg = "PLAYER 1 WIN!";
                    if(PLAYER2_DOT == ox) gameOverMsg = "PLAYER 2 WIN!";
                    return true;
                }
            }
        }
        return false;
    }
    // Checking lines
    public boolean checkLine(int cx, int cy, int vx, int vy, int l, int ox){
        if (cx + l * vx > linesCount || cy + l * vy > linesCount || cy + l * vy < -1) return false;
            for (int i = 0; i < l; i++) {
                if(field[cx + i * vx][cy + i * vy] != ox) return false;
            }
        return true;
    }

    public boolean setDot(int x, int y, int dot){
        if (field[x][y] == 0) {
            field[x][y] = dot;
            return true;
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.black);
        for (int i = 0; i <= linesCount; i++) {
            g.drawLine(0, i * CELL_SIZE, PANEL_SIZE, i * CELL_SIZE); // horizontal lines
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, PANEL_SIZE); // vertical lines
        }
            Graphics2D g2 = (Graphics2D) g; // use Graphics2D
            g2.setStroke(new BasicStroke(5));
            for (int y = 0; y < linesCount; y++) {
                for (int x = 0; x < linesCount; x++) {
                    if (field[x][y] == PLAYER1_DOT) {
                        g.setColor(Color.blue);
                        g2.draw(new Line2D.Float(x*CELL_SIZE+CELL_SIZE/4, y*CELL_SIZE+CELL_SIZE/4, (x+1)*CELL_SIZE-CELL_SIZE/4, (y+1)*CELL_SIZE-CELL_SIZE/4));
                        g2.draw(new Line2D.Float(x*CELL_SIZE+CELL_SIZE/4, (y+1)*CELL_SIZE-CELL_SIZE/4, (x+1)*CELL_SIZE-CELL_SIZE/4, y*CELL_SIZE+CELL_SIZE/4));
                    }
                    if (field[x][y] == PLAYER2_DOT) {
                        g.setColor(Color.red);
                        g2.draw(new Ellipse2D.Float(x*CELL_SIZE+CELL_SIZE/4, y*CELL_SIZE+CELL_SIZE/4, CELL_SIZE/2, CELL_SIZE/2));
                    }
                }
            }
        if (gameOver){
            g.setFont(new Font("Arial", Font.BOLD, 64));
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 220, 500, 70);
            g.setColor(Color.black);
            g.drawString(gameOverMsg, 12, 277);
            g.setColor(Color.green);
            g.drawString(gameOverMsg, 8, 273);
        }
    }
}