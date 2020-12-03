package pacman;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Controlador extends JPanel implements ActionListener {

    private Dimension d;
    private final Font smallfont = new Font("Helvetica", Font.BOLD, 14);

    private Image ii;
    private final Color dotcolor = new Color(192, 192, 0);
    private Color colorParedes;

    private boolean EstaJugando = false;
    private boolean dying = false;

    private final int tamañoBloque = 24;
    private final int cantidadBloque = 15;
    private final int tamañoPantalla = cantidadBloque * tamañoBloque;
    private final int delayAnimacion = 2;
    private final int pacmananimcount = 4;
    private final int maximoFantasmas = 12;
    private final int pacmanspeed = 6;

    private int pacanimcount = delayAnimacion;
    private int pacanimdir = 1;
    private int posicionPacman = 0;
    private int cantidadFantasmas = 6;
    private int vidas, puntaje;
    private int[] dimensionX, dimensionY;
    private int[] fantasmaX, fantasmaY, fantasmaDX, fantasmaDY, velocidadFantasma;

    private Image ghost;
    private Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down;
    private Image pacman3up, pacman3down, pacman3left, pacman3right;
    private Image pacman4up, pacman4down, pacman4left, pacman4right;

    private int pacmanx, pacmany, pacmandx, pacmandy;
    private int reqdx, reqdy, viewdx, viewdy;

    private final short leveldata[] = {
        19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
        21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        21, 0, 0, 0, 17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20,
        17, 18, 18, 18, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 16, 24, 20,
        25, 16, 16, 16, 24, 24, 28, 0, 25, 24, 24, 16, 20, 0, 21,
        1, 17, 16, 20, 0, 0, 0, 0, 0, 0, 0, 17, 20, 0, 21,
        1, 17, 16, 16, 18, 18, 22, 0, 19, 18, 18, 16, 20, 0, 21,
        1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21,
        1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21,
        1, 17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 0, 21,
        1, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 21,
        1, 25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20,
        9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 25, 24, 24, 24, 28
    };

    private final int validspeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxspeed = 6;

    private int velocidadActual = 3;
    private short[] screendata;
    private Timer timer;

    public Controlador() {

        CargarImagenes();
        Inicializar();

        addKeyListener(new AdaptadorTeclado());

        setFocusable(true);
    }

    private void Inicializar() {
        screendata = new short[cantidadBloque * cantidadBloque];
        colorParedes = Color.BLUE;
        d = new Dimension(400, 400);
        fantasmaX = new int[maximoFantasmas];
        fantasmaDX = new int[maximoFantasmas];
        fantasmaY = new int[maximoFantasmas];
        fantasmaDY = new int[maximoFantasmas];
        velocidadFantasma = new int[maximoFantasmas];
        dimensionX = new int[4];
        dimensionY = new int[4];

        timer = new Timer(40, this);
        timer.start();
        
        this.EstaJugando = true;
        
        //Configuracion del juego
        vidas = 10;
        puntaje = 0;
        cantidadFantasmas = 2;
        velocidadActual = 3;
        screendata = leveldata;
        
        ContinuarNivel();
    }

    private void Animar() {

        this.pacanimcount--;

        if (this.pacanimcount <= 0) {
            this.pacanimcount = this.delayAnimacion;
            this.posicionPacman = this.posicionPacman + this.pacanimdir;

            if (this.posicionPacman == (this.pacmananimcount - 1) || this.posicionPacman == 0) {
                this.pacanimdir = -this.pacanimdir;
            }
        }
    }

    private void Jugar(Graphics2D g2d) {

        if (dying) {

            Perder();

        } else {

            MoverPacman();
            DibujarPacman(g2d);
            MoverFantasma(g2d);
            VerificarMapa(g2d);
        }
    }

    private void DibujarPuntaje(Graphics2D g) {

        int i;
        String s;

        g.setFont(smallfont);
        g.setColor(new Color(96, 128, 255));
        s = "Score: " + puntaje;
        g.drawString(s, tamañoPantalla / 2 + 96, tamañoPantalla + 16);

        for (i = 0; i < vidas; i++) {
            g.drawImage(pacman3left, i * 28 + 8, tamañoPantalla + 1, this);
        }
    }

    private void VerificarMapa(Graphics2D g) {

        short i = 0;
        boolean finished = true;

        while (i < cantidadBloque * cantidadBloque && finished) {

            if ((screendata[i] & 48) != 0) {
                finished = false;
            }

            i++;
        }

        if (finished || puntaje >=2) {

        g.drawString("GANASTE!",150,185);
        EstaJugando = false;
        
        }
    }

    private void Perder() {//murio

        vidas--;

        if (vidas == 0) {
            this.EstaJugando = false;
        }

        ContinuarNivel();
    }

    private void MoverFantasma(Graphics2D g2d) {//mover fantasmas

        short i;
        int pos;
        int count;

        for (i = 0; i < cantidadFantasmas; i++) {
            if (fantasmaX[i] % tamañoBloque == 0 && fantasmaY[i] % tamañoBloque == 0) {
                pos = fantasmaX[i] / tamañoBloque + cantidadBloque * (int) (fantasmaY[i] / tamañoBloque);

                count = 0;

                if ((screendata[pos] & 1) == 0 && fantasmaDX[i] != 1) {
                    dimensionX[count] = -1;
                    dimensionY[count] = 0;
                    count++;
                }

                if ((screendata[pos] & 2) == 0 && fantasmaDY[i] != 1) {
                    dimensionX[count] = 0;
                    dimensionY[count] = -1;
                    count++;
                }

                if ((screendata[pos] & 4) == 0 && fantasmaDX[i] != -1) {
                    dimensionX[count] = 1;
                    dimensionY[count] = 0;
                    count++;
                }

                if ((screendata[pos] & 8) == 0 && fantasmaDY[i] != -1) {
                    dimensionX[count] = 0;
                    dimensionY[count] = 1;
                    count++;
                }

                if (count == 0) {

                    if ((screendata[pos] & 15) == 15) {
                        fantasmaDX[i] = 0;
                        fantasmaDY[i] = 0;
                    } else {
                        fantasmaDX[i] = -fantasmaDX[i];
                        fantasmaDY[i] = -fantasmaDY[i];
                    }

                } else {

                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    fantasmaDX[i] = dimensionX[count];
                    fantasmaDY[i] = dimensionY[count];
                }

            }

            fantasmaX[i] = fantasmaX[i] + (fantasmaDX[i] * velocidadFantasma[i]);
            fantasmaY[i] = fantasmaY[i] + (fantasmaDY[i] * velocidadFantasma[i]);
            DibujarFantasma(g2d, fantasmaX[i] + 1, fantasmaY[i] + 1);

            if (pacmanx > (fantasmaX[i] - 12) && pacmanx < (fantasmaX[i] + 12)
                    && pacmany > (fantasmaY[i] - 12) && pacmany < (fantasmaY[i] + 12)
                    && this.EstaJugando) {

                dying = true;
            }
        }
    }

    private void DibujarFantasma(Graphics2D g2d, int x, int y) {

        g2d.drawImage(ghost, x, y, this);
    }

    private void MoverPacman() {

        int pos;
        short ch;

        if (reqdx == -pacmandx && reqdy == -pacmandy) {
            pacmandx = reqdx;
            pacmandy = reqdy;
            viewdx = pacmandx;
            viewdy = pacmandy;
        }

        if (pacmanx % tamañoBloque == 0 && pacmany % tamañoBloque == 0) {
            pos = pacmanx / tamañoBloque + cantidadBloque * (int) (pacmany / tamañoBloque);
            ch = screendata[pos];

            if ((ch & 16) != 0) {
                screendata[pos] = (short) (ch & 15);
                puntaje++;
            }

            if (reqdx != 0 || reqdy != 0) {
                if (!((reqdx == -1 && reqdy == 0 && (ch & 1) != 0)
                        || (reqdx == 1 && reqdy == 0 && (ch & 4) != 0)
                        || (reqdx == 0 && reqdy == -1 && (ch & 2) != 0)
                        || (reqdx == 0 && reqdy == 1 && (ch & 8) != 0))) {
                    pacmandx = reqdx;
                    pacmandy = reqdy;
                    viewdx = pacmandx;
                    viewdy = pacmandy;
                }
            }

            // Check for standstill
            if ((pacmandx == -1 && pacmandy == 0 && (ch & 1) != 0)
                    || (pacmandx == 1 && pacmandy == 0 && (ch & 4) != 0)
                    || (pacmandx == 0 && pacmandy == -1 && (ch & 2) != 0)
                    || (pacmandx == 0 && pacmandy == 1 && (ch & 8) != 0)) {
                pacmandx = 0;
                pacmandy = 0;
            }
        }
        pacmanx = pacmanx + pacmanspeed * pacmandx;
        pacmany = pacmany + pacmanspeed * pacmandy;
    }

    private void DibujarPacman(Graphics2D g2d) {

        if (viewdx == -1) {
            DibujarPacmanIzquierda(g2d);
        } else if (viewdx == 1) {
            DibujarPacmanDerecha(g2d);
        } else if (viewdy == -1) {
            DibujarPacmanArriba(g2d);
        } else {
            DibujarPacmanAbajo(g2d);
        }
    }

    private void DibujarPacmanArriba(Graphics2D g2d) {

        switch (posicionPacman) {
            case 1:
                g2d.drawImage(pacman2up, pacmanx + 1, pacmany + 1, this);
        
                break;
            case 2:
                g2d.drawImage(pacman3up, pacmanx + 1, pacmany + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4up, pacmanx + 1, pacmany + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
                break;
        }
    }

    private void DibujarPacmanAbajo(Graphics2D g2d) {

        switch (posicionPacman) {
            case 1:
                g2d.drawImage(pacman2down, pacmanx + 1, pacmany + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3down, pacmanx + 1, pacmany + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4down, pacmanx + 1, pacmany + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
                break;
        }
    }

    private void DibujarPacmanIzquierda(Graphics2D g2d) {

        switch (posicionPacman) {
            case 1:
                g2d.drawImage(pacman2left, pacmanx + 1, pacmany + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3left, pacmanx + 1, pacmany + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4left, pacmanx + 1, pacmany + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
                break;
        }
    }

    private void DibujarPacmanDerecha(Graphics2D g2d) {

        switch (posicionPacman) {
            case 1:
                g2d.drawImage(pacman2right, pacmanx + 1, pacmany + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3right, pacmanx + 1, pacmany + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4right, pacmanx + 1, pacmany + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
                break;
        }
    }

    private void DibujarMapa(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < tamañoPantalla; y += tamañoBloque) {
            for (x = 0; x < tamañoPantalla; x += tamañoBloque) {

                g2d.setColor(colorParedes);
                g2d.setStroke(new BasicStroke(2));

                if ((screendata[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + tamañoBloque - 1);
                }

                if ((screendata[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + tamañoBloque - 1, y);
                }

                if ((screendata[i] & 4) != 0) {
                    g2d.drawLine(x + tamañoBloque - 1, y, x + tamañoBloque - 1,
                            y + tamañoBloque - 1);
                }

                if ((screendata[i] & 8) != 0) {
                    g2d.drawLine(x, y + tamañoBloque - 1, x + tamañoBloque - 1,
                            y + tamañoBloque - 1);
                }

                if ((screendata[i] & 16) != 0) {
                    g2d.setColor(dotcolor);
                    g2d.fillRect(x + 11, y + 11, 2, 2);
                }

                i++;
            }
        }
    }

    private void ContinuarNivel() {

        int i;
        int dx = 1;
        int random;

        for (i = 0; i < cantidadFantasmas; i++) {

            fantasmaY[i] = 4 * tamañoBloque;
            fantasmaX[i] = 4 * tamañoBloque;
            fantasmaDY[i] = 0;
            fantasmaDX[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (velocidadActual + 1));

            if (random > velocidadActual) {
                random = velocidadActual;
            }

            velocidadFantasma[i] = validspeeds[random];
        }

        pacmanx = 7 * tamañoBloque;
        pacmany = 11 * tamañoBloque;
        pacmandx = 0;
        pacmandy = 0;
        reqdx = 0;
        reqdy = 0;
        viewdx = -1;
        viewdy = 0;
        dying = false;
    }

    private void CargarImagenes() {

        ghost = new ImageIcon(getClass().getResource("../images/ghost.gif")).getImage();
        pacman1 = new ImageIcon(getClass().getResource("../images/pacman.gif")).getImage();
        pacman2up = new ImageIcon(getClass().getResource("../images/up1.gif")).getImage();
        pacman3up = new ImageIcon(getClass().getResource("../images/up2.gif")).getImage();
        pacman4up = new ImageIcon(getClass().getResource("../images/up3.gif")).getImage();
        pacman2down = new ImageIcon(getClass().getResource("../images/down1.gif")).getImage();
        pacman3down = new ImageIcon(getClass().getResource("../images/down2.gif")).getImage();
        pacman4down = new ImageIcon(getClass().getResource("../images/down3.gif")).getImage();
        pacman2left = new ImageIcon(getClass().getResource("../images/left1.gif")).getImage();
        pacman3left = new ImageIcon(getClass().getResource("../images/left2.gif")).getImage();
        pacman4left = new ImageIcon(getClass().getResource("../images/left3.gif")).getImage();
        pacman2right = new ImageIcon(getClass().getResource("../images/right1.gif")).getImage();
        pacman3right = new ImageIcon(getClass().getResource("../images/right2.gif")).getImage();
        pacman4right = new ImageIcon(getClass().getResource("../images/right3.gif")).getImage();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dibujar(g);
    }

    private void Dibujar(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        DibujarMapa(g2d);
        DibujarPuntaje(g2d);
        Animar();
        Jugar(g2d);

        g2d.drawImage(ii, 5, 5, this);
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    class AdaptadorTeclado extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (EstaJugando) {
                if (key == KeyEvent.VK_LEFT) {
                    reqdx = -1;
                    reqdy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    reqdx = 1;
                    reqdy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    reqdx = 0;
                    reqdy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    reqdx = 0;
                    reqdy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    EstaJugando = false;
                } else if (key == KeyEvent.VK_PAUSE) {
                    if (timer.isRunning()) {
                        timer.stop();
                    } else {
                        timer.start();
                    }
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == Event.LEFT || key == Event.RIGHT
                    || key == Event.UP || key == Event.DOWN) {
                reqdx = 0;
                reqdy = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        repaint();
    }
}