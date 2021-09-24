package edu.vsu.csf.course2.vetrov;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

class Coordinates {
    private final int x;
    private final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

class Frame extends JFrame {
    public Frame() {
        setTitle("Drawing Panel");
        setSize(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private static final int BACKGROUND_WIDTH = 1200;
    private static final int BACKGROUND_HEIGHT = 600;

    static class DrawingPanel extends JPanel implements ActionListener {
        public DrawingPanel() {
            setSize(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
            setVisible(true);
            timer.start();
        }

        private static final int WIDTH = 200;
        private static final int HEIGHT = 200;
        private int currX = 0;
        private int currY = 100;
        private boolean isWall = false;
        private boolean isFloor = false;
        private int red = 255;
        private int green = 0;
        private int blue = 0;
        private static final Coordinates[] COORDINATES = new Coordinates[10];
        private static final int TIMER_DELAY = 10;
        private final Timer timer = new Timer(TIMER_DELAY, this);
        private int ticks = 0;

        @Override
        public void paintComponent(Graphics g) {
            if (COORDINATES[0] == null) {
                Arrays.fill(COORDINATES, new Coordinates(currX, currY));
            } else {
                System.arraycopy(COORDINATES, 1, COORDINATES, 0, COORDINATES.length - 1);
                COORDINATES[COORDINATES.length - 1] = new Coordinates(currX, currY);
            }

            if (red != 0 && blue == 0) {
                red--;
                green++;
            } else if (green != 0 && red == 0) {
                green--;
                blue++;
            } else if (blue != 0 && green == 0) {
                blue--;
                red++;
            }

            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

            for (int i = 0; i < COORDINATES.length; i++) {
                g2d.setColor(new Color(red, green, blue, 10 * (i + 1)));
                g2d.fillOval(COORDINATES[i].getX(), COORDINATES[i].getY(), WIDTH, HEIGHT);
            }

            g2d.setColor(Color.BLACK);
            g2d.drawRect(COORDINATES[0].getX(), COORDINATES[0].getY(), WIDTH, HEIGHT);
            g2d.drawRect(COORDINATES[COORDINATES.length - 1].getX(),
                    COORDINATES[COORDINATES.length - 1].getY(), WIDTH, HEIGHT);

            g2d.drawLine(COORDINATES[0].getX(),
                    COORDINATES[0].getY(),
                    COORDINATES[COORDINATES.length - 1].getX(),
                    COORDINATES[COORDINATES.length - 1].getY());
            g2d.drawLine(COORDINATES[0].getX() + WIDTH,
                    COORDINATES[0].getY(),
                    COORDINATES[COORDINATES.length - 1].getX() + WIDTH,
                    COORDINATES[COORDINATES.length - 1].getY());
            g2d.drawLine(COORDINATES[0].getX(),
                    COORDINATES[0].getY() + HEIGHT,
                    COORDINATES[COORDINATES.length - 1].getX(),
                    COORDINATES[COORDINATES.length - 1].getY() + HEIGHT);
            g2d.drawLine(COORDINATES[0].getX() + WIDTH,
                    COORDINATES[0].getY() + HEIGHT,
                    COORDINATES[COORDINATES.length - 1].getX() + WIDTH,
                    COORDINATES[COORDINATES.length - 1].getY() + HEIGHT);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == timer) {
                repaint();

                if (isWall) {
                    currX -= 10;
                    if (currX <= 0) {
                        isWall = false;
                        currX = 0;
                    }
                } else {
                    currX += 10;
                    if (currX >= BACKGROUND_WIDTH - WIDTH - 1) {
                        isWall = true;
                        currX = BACKGROUND_WIDTH - WIDTH - 1;
                    }
                }

                if (ticks <= 0) {
                    isFloor = false;
                }
                if (isFloor) {
                    --ticks;
                    currY -= (double) ticks / 2;
                } else {
                    ++ticks;
                    currY += (double) ticks / 2;
                    if (currY >= BACKGROUND_HEIGHT - HEIGHT - 1) {
                        isFloor = true;
                        currY = BACKGROUND_HEIGHT - HEIGHT - 1;
                    }
                }
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        JFrame frame = new Frame();
        JPanel panel = new Frame.DrawingPanel();
        frame.getContentPane().add(panel);
    }
}
