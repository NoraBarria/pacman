///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package pacman;
//
//import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Graphics2D;
//import java.awt.Image;
//import java.awt.FontMetrics;
//import javax.swing.Timer;
//
//public class Dibujador {
//    private Image ghost;
//    private Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down;
//    private Image pacman3up, pacman3down, pacman3left, pacman3right;
//    private Image pacman4up, pacman4down, pacman4left, pacman4right;
//    
//    private short[] screendata;
//    private int pacmananimpos = 0;
//    
//    private final int blocksize = 24;
//    private final int nrofblocks = 15;
//    private final int scrsize = nrofblocks * blocksize;
//    
//    private Color mazecolor;
//    private final Color dotcolor = new Color(192, 192, 0);
//    
//    private int reqdx, reqdy, viewdx, viewdy;
//    private int pacmanx, pacmany, pacmandx, pacmandy;
//    
//    public void Dibujador(){
//        screendata = new short[nrofblocks * nrofblocks];
//        mazecolor = new Color(5, 100, 5);
//        d = new Dimension(400, 400);
//        ghostx = new int[maxghosts];
//        ghostdx = new int[maxghosts];
//        ghosty = new int[maxghosts];
//        ghostdy = new int[maxghosts];
//        ghostspeed = new int[maxghosts];
//        dx = new int[4];
//        dy = new int[4];
//
//        timer = new Timer(40, this);
//        timer.start();
//        
//        this.EstaJugando = true;
//    }
//    
//    public void DibujarPacman(Graphics2D g2d) {
//
//        if (viewdx == -1) {
//            DibujarPacmanIzquierda(g2d);
//        } else if (viewdx == 1) {
//            DibujarPacmanDerecha(g2d);
//        } else if (viewdy == -1) {
//            DibujarPacmanArriba(g2d);
//        } else {
//            DibujarPacmanAbajo(g2d);
//        }
//    }
//
//    public void DibujarPacmanArriba(Graphics2D g2d) {
//
//        switch (pacmananimpos) {
//            case 1:
//                g2d.drawImage(pacman2up, pacmanx + 1, pacmany + 1, this);
//        
//                break;
//            case 2:
//                g2d.drawImage(pacman3up, pacmanx + 1, pacmany + 1, this);
//                break;
//            case 3:
//                g2d.drawImage(pacman4up, pacmanx + 1, pacmany + 1, this);
//                break;
//            default:
//                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
//                break;
//        }
//    }
//
//    public void DibujarPacmanAbajo(Graphics2D g2d) {
//
//        switch (pacmananimpos) {
//            case 1:
//                g2d.drawImage(pacman2down, pacmanx + 1, pacmany + 1, this);
//                break;
//            case 2:
//                g2d.drawImage(pacman3down, pacmanx + 1, pacmany + 1, this);
//                break;
//            case 3:
//                g2d.drawImage(pacman4down, pacmanx + 1, pacmany + 1, this);
//                break;
//            default:
//                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
//                break;
//        }
//    }
//
//    public void DibujarPacmanIzquierda(Graphics2D g2d) {
//
//        switch (pacmananimpos) {
//            case 1:
//                g2d.drawImage(pacman2left, pacmanx + 1, pacmany + 1, this);
//                break;
//            case 2:
//                g2d.drawImage(pacman3left, pacmanx + 1, pacmany + 1, this);
//                break;
//            case 3:
//                g2d.drawImage(pacman4left, pacmanx + 1, pacmany + 1, this);
//                break;
//            default:
//                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
//                break;
//        }
//    }
//
//    public void DibujarPacmanDerecha(Graphics2D g2d) {
//
//        switch (pacmananimpos) {
//            case 1:
//                g2d.drawImage(pacman2right, pacmanx + 1, pacmany + 1, this);
//                break;
//            case 2:
//                g2d.drawImage(pacman3right, pacmanx + 1, pacmany + 1, this);
//                break;
//            case 3:
//                g2d.drawImage(pacman4right, pacmanx + 1, pacmany + 1, this);
//                break;
//            default:
//                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
//                break;
//        }
//    }
//
//    public void DibujarMapa(Graphics2D g2d) {
//
//        short i = 0;
//        int x, y;
//
//        for (y = 0; y < scrsize; y += blocksize) {
//            for (x = 0; x < scrsize; x += blocksize) {
//
//                g2d.setColor(mazecolor);
//                g2d.setStroke(new BasicStroke(2));
//
//                if ((screendata[i] & 1) != 0) {
//                    g2d.drawLine(x, y, x, y + blocksize - 1);
//                }
//
//                if ((screendata[i] & 2) != 0) {
//                    g2d.drawLine(x, y, x + blocksize - 1, y);
//                }
//
//                if ((screendata[i] & 4) != 0) {
//                    g2d.drawLine(x + blocksize - 1, y, x + blocksize - 1,
//                            y + blocksize - 1);
//                }
//
//                if ((screendata[i] & 8) != 0) {
//                    g2d.drawLine(x, y + blocksize - 1, x + blocksize - 1,
//                            y + blocksize - 1);
//                }
//
//                if ((screendata[i] & 16) != 0) {
//                    g2d.setColor(dotcolor);
//                    g2d.fillRect(x + 11, y + 11, 2, 2);
//                }
//
//                i++;
//            }
//        }
//    }
//}
