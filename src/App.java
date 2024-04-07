import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class App extends JPanel implements MouseListener {

    public static final int WIDTH = 24;
    public static final int HEIGHT = 24;
    public static final int SCALE = 24;
    public static final int BOMB_RARITY = 6;

    boolean firstClick = true;
    int flagCount = 0;
    int bombCount = 0;
    int shownTileCount = 0;

    JFrame frame;

    boolean[][] bomb = new boolean[WIDTH][HEIGHT];
    int[][] adj = new int[WIDTH][HEIGHT];
    boolean[][] flag = new boolean[WIDTH][HEIGHT];
    boolean[][] shown = new boolean[WIDTH][HEIGHT];

    public App() {
        frame = new JFrame("Minesweeper");
        frame.add(this);
        frame.addMouseListener(this);
        frame.setResizable(false);
        addMouseListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension( (SCALE*WIDTH),  ((HEIGHT+1)*SCALE)));
        frame.pack();

        frame.setVisible(true);

        for(int x = 0; x<WIDTH; x++) {
            for(int y = 0; y<HEIGHT; y++) {
                bomb[x][y] = ((int) (Math.random()*BOMB_RARITY) == 0); // RNG -> chance of being bomb 
                if(bomb[x][y]) {
                    bombCount++;
                }
            }
        }
        System.out.println(bombCount);

        repaint();
    }

    public void showSurrounding(int x, int y) {
        if(x>0 && y>0) {
            shown[x-1][y-1] = true;
        }
        if(y>0) {
            shown[x][y-1] = true;
        }
        if(x<WIDTH-1 && y>0) {
            shown[x+1][y-1] = true;
        }
        if(x>0) {
            shown[x-1][y] = true;
        }
        if(x<WIDTH-1) {
            shown[x+1][y] = true;
        }
        if(y<HEIGHT-1 && x>0) {
            shown[x-1][y+1] = true;
        }
        if(y<HEIGHT-1) {
            shown[x][y+1] = true;
        }
        if(y<HEIGHT-1 && x<WIDTH-1) {
            shown[x+1][y+1] = true;
        }
    }

    public void boom(int x, int y) {

        shown[x][y] = true;
        shownTileCount++;
        
        if(x>0 && y>0) {
            if(!shown[x-1][y-1]) {
                if(adj[x-1][y-1] == 0) {
                    boom(x-1, y-1);
                } else {
                    shown[x-1][y-1] = true;
                    shownTileCount++;
                }
            }
        }
        if(y>0) {
            if(!shown[x][y-1]) {
                if(adj[x][y-1] == 0) {
                    boom(x, y-1);
                } else {
                    shown[x][y-1] = true;
                    shownTileCount++;
                }
            }
        }
        if(x<WIDTH-1 && y>0) {
            if(!shown[x+1][y-1]) {
                if(adj[x+1][y-1] == 0) {
                    boom(x+1, y-1);
                } else {
                    shown[x+1][y-1] = true;
                    shownTileCount++;
                }
            }
        }
        if(x>0) {
            if(!shown[x-1][y]) {
                if(adj[x-1][y] == 0) {
                    boom(x-1, y);
                } else {
                    shown[x-1][y] = true;
                    shownTileCount++;
                }
            }
        }
        if(x<WIDTH-1) {
            if(!shown[x+1][y]) {
                if(adj[x+1][y]==0) {
                    boom(x+1, y);
                } else {
                    shown[x+1][y] = true;
                    shownTileCount++;
                }
            }
        }
        if(y<HEIGHT-1 && x>0) {
            if(!shown[x-1][y+1]) {
                if(adj[x-1][y+1]==0) {
                    boom(x-1, y+1);
                } else {
                    shown[x-1][y+1] = true;
                    shownTileCount++;
                }
            }
        }
        if(y<HEIGHT-1) {
            if(!shown[x][y+1]) {
                if(adj[x][y+1]==0) {
                    boom(x, y+1);
                } else {
                    shown[x][y+1] = true;
                    shownTileCount++;
                }
            }
        }
        if(y<HEIGHT-1 && x<WIDTH-1) {
            if(!shown[x+1][y+1]) {
                if(adj[x+1][y+1]==0) {
                    boom(x+1, y+1);
                } else {
                    shown[x+1][y+1] = true;
                    shownTileCount++;
                }
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        new App();
    }

    public void paint(Graphics g) {

        g.setFont(new Font("TimesRoman", Font.PLAIN,  SCALE));

        for(int x = 0; x<WIDTH; x++) {
            for(int y = 0; y<HEIGHT; y++) {
                g.setColor(Color.BLACK);
                g.fillRect( (x*SCALE),  (y*SCALE),  SCALE,  SCALE);
                if(bomb[x][y]) {
                    g.setColor(Color.RED);
                    g.fillRect( (x*SCALE)+1,  (y*SCALE)+1,  SCALE-2,  SCALE-2);
                }
                if(adj[x][y]>0) {
                    g.setColor(Color.WHITE);
                    g.fillRect( (x*SCALE)+1,  (y*SCALE)+1,  SCALE-2,  SCALE-2);

                    if(adj[x][y]<=2) {
                        g.setColor(Color.GREEN);
                    } else if(adj[x][y]<=4) {
                        g.setColor(Color.YELLOW);
                    } else {
                        g.setColor(Color.RED);
                    }
                    g.drawString(""+adj[x][y], ((int) ((x*SCALE)+(0.25*SCALE))), (int) ((y*SCALE)+((0.87*SCALE)))); // janky
                }
                if(adj[x][y]==0) {
                    g.setColor(Color.GRAY);
                    g.fillRect( (x*SCALE)+1,  (y*SCALE)+1,  SCALE-2,  SCALE-2);
                }
                if(!shown[x][y]) {
                    g.setColor(Color.WHITE);
                    g.fillRect( (x*SCALE)+1,  (y*SCALE)+1,  SCALE-2,  SCALE-2);
                }
                if(flag[x][y]) {
                    g.setColor(Color.BLACK);
                    g.fillOval( (x*SCALE)+1,  (y*SCALE)+1,  SCALE-2,  SCALE-2);
                }
            }
        }

        g.setColor(Color.BLACK);
        g.fillRect(0,  (HEIGHT*SCALE),  (WIDTH*SCALE),  SCALE);
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN,  (SCALE/2)));
        if(!firstClick) {
            g.drawString("Bombs: " + bombCount + "     Flags: " + flagCount,  (int) (SCALE/2), (int) (HEIGHT*SCALE)+( (SCALE)/2)+2);
        }
    }

    public void firstClick(int x, int y) {
        if(x>0 && y>0) {
            if(bomb[x-1][y-1]) {
                bombCount--;
                bomb[x-1][y-1] = false;
            }
        }
        if(y>0) {
            if(bomb[x][y-1]) {
                bombCount--;
                bomb[x][y-1] = false;
            }
        }
        if(x<WIDTH-1 && y>0) {
            if(bomb[x+1][y-1]) {
                bombCount--;
                bomb[x+1][y-1] = false;
            }
        }
        if(x>0) {
            if(bomb[x-1][y]) {
                bombCount--;
                bomb[x-1][y] = false;
            }
        }
        if(x<WIDTH-1) {
            if(bomb[x+1][y]) {
                bombCount--;
                bomb[x+1][y] = false;
            }
        }
        if(y<HEIGHT-1 && x>0) {
            if(bomb[x-1][y+1]) {
                bombCount--;
                bomb[x-1][y+1] = false;
            }
        }
        if(y<HEIGHT-1) {
            if(bomb[x][y+1]) {
                bombCount--;
                bomb[x][y+1] = false;
            }
        }
        if(y<HEIGHT-1 && x<WIDTH-1) {
            if(bomb[x+1][y+1]) {
                bombCount--;
                bomb[x+1][y+1] = false;
            }
        }
    }

    public void doAdjacent() {
        for(int x = 0; x<WIDTH; x++) {
            for(int y = 0; y<HEIGHT; y++) {

                if(bomb[x][y]) {
                    adj[x][y] = -1;
                    continue;
                }
                if(x>0 && y>0) {
                    if(bomb[x-1][y-1]) {
                        adj[x][y]++;
                    }
                }
                if(y>0) {
                    if(bomb[x][y-1]) {
                        adj[x][y]++;
                    }
                }
                if(x<WIDTH-1 && y>0) {
                    if(bomb[x+1][y-1]) {
                        adj[x][y]++;
                    }
                }
                if(x>0) {
                    if(bomb[x-1][y]) {
                        adj[x][y]++;
                    }
                }
                if(x<WIDTH-1) {
                    if(bomb[x+1][y]) {
                        adj[x][y]++;
                    }
                }
                if(y<HEIGHT-1 && x>0) {
                    if(bomb[x-1][y+1]) {
                        adj[x][y]++;
                    }
                }
                if(y<HEIGHT-1) {
                    if(bomb[x][y+1]) {
                        adj[x][y]++;
                    }
                }
                if(y<HEIGHT-1 && x<WIDTH-1) {
                    if(bomb[x+1][y+1]) {
                        adj[x][y]++;
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        int x = e.getX() / ( SCALE);
        int y = e.getY() / ( SCALE);
        
        if(e.getButton()==MouseEvent.BUTTON1) {

            if(firstClick) {

                bomb[x][y] = false;
                firstClick(x, y);
                doAdjacent();

                firstClick = false;
            }

            if(bomb[x][y]) {
                if(!flag[x][y]) {
                    System.exit(0);
                }
            } else {
                shown[x][y] = true;
                shownTileCount++;

                if(adj[x][y]==0) {
                    boom(x, y);
                }
            }

            if(shownTileCount==((WIDTH*HEIGHT)-bombCount)) {
                System.out.print("YOU WIN");
            }
        }
        if(e.getButton()==MouseEvent.BUTTON3) {
            if(!flag[x][y]) {
                if(shown[x][y]==false) {
                    flag[x][y] = true;
                    flagCount++;
                }
            } else {
                flag[x][y] = false;
                flagCount--;
            }
        }

        repaint();
    }

    // Unused
    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
}
