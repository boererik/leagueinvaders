import java.awt.event.*;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board extends JPanel implements Runnable {
    private boolean ingame = true;
    private boolean Victory = false, Lost = false;
    private boolean started = true;
    final int BOARD_WIDTH = 1480;
    final int BOARD_HEIGHT = 800;
    private BufferedImage img;
    private BufferedImage backGround;
    private BufferedImage beginGame;
    private Thread animator;
    private Sounds backGroundMusic, bomb1,bomb2,bomb3, enemyDead, victoryMusic, loseMusic ;
    private final SpaceShip spaceShip;
    private final int numEnemy = 18;
    private final Enemy[] enemies = new Enemy[numEnemy];
    private final List<Bomb> bullet = new ArrayList<Bomb>();
    private Bomb a;
    private Thread soundAnimator;

    public Board()
    {
        addKeyListener(new Adapter());
        setFocusable(true);

            bomb1 = new Sounds("src/ziggs1.wav");
            bomb2 = new Sounds("src/ziggs2.wav");
            bomb3 = new Sounds("src/ziggs3.wav");
            enemyDead = new Sounds("src/explosion.wav");
            victoryMusic = new Sounds("src/win.wav");
            loseMusic = new Sounds("src/lose.wav");

        //BOARD AND BACKGROUND - - - - select a background and a backgroundmusic randomly
        Random r2 = new Random();
        int randomnumber2 = r2.nextInt(3);
        if(randomnumber2 == 0) {
            backGroundMusic = new Sounds("src/startmusic.wav");
            try {
                backGround = ImageIO.read(this.getClass().getResource("Pics/background2.png"));
            } catch (IOException e) {
                System.out.println("Image could not be read");
                System.exit(1);
            }
        } else if(randomnumber2 == 1) {
            backGroundMusic = new Sounds("src/startmusic2.wav");
            try {
                backGround = ImageIO.read(this.getClass().getResource("Pics/background3.png"));
            } catch (IOException e) {
                System.out.println("Image could not be read");
                System.exit(1);
            }
        } else {
            backGroundMusic = new Sounds("src/startmusic3.wav");
            try {
                backGround = ImageIO.read(this.getClass().getResource("Pics/background1.png"));
            } catch (IOException e) {
                System.out.println("Image could not be read");
                System.exit(1);
            }
        }

        //start screen picture
        try {
             beginGame = ImageIO.read(this.getClass().getResource("Pics/start.png"));
        } catch (IOException e) {
            System.out.println("Image could not be read");
            System.exit(1);
        }

        //SPACESHIP(ziggs)
        spaceShip = new SpaceShip(BOARD_WIDTH/2, BOARD_HEIGHT - 150, 10);

        //enemies
        int enemyX = 15;
        int enemyY = 15;

        //row-column settings
        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Enemy(enemyX, enemyY,8);
            enemyX += 65;
            if (i == numEnemy/3 - 1 || i == 2*numEnemy/3 - 1) {
                enemyX = 15;
                enemyY += 120;
            }
        }

        //picture for enemy
        try {
            img = ImageIO.read(this.getClass().getResource("Pics/teemo.png"));
        } catch (IOException e) {
            System.out.println("Image could not be read");
            System.exit(1);
        }
        for (int i = 0; i < enemies.length; i++) {
            enemies[i].setImg(img);
        }

        //BOMB
        a = new Bomb(spaceShip.getX() + 35, spaceShip.getY(), 15);
        try {
            img = ImageIO.read(this.getClass().getResource("Pics/bomb.png"));
        } catch (IOException e) {
            System.out.println("Image could not be read");
            System.exit(1);
        }
        a.setImg(img);


        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
            backGroundMusic.start();
        }

        setDoubleBuffered(true);
    }

    //kirajzolások
    public void paint(Graphics g)
    {
        super.paint(g);

        if (!ingame && started) {
            g.drawImage(beginGame,0,0,BOARD_WIDTH,BOARD_HEIGHT ,null);
        }

        //background
        if (ingame) {
            g.drawImage(backGround,0,0,BOARD_WIDTH,BOARD_HEIGHT ,null);
        }

        //spaceShip
        if (ingame) {
            g.drawImage(spaceShip.getImg(),spaceShip.getX() ,spaceShip.getY() ,100 ,100 ,null);
        }

        //bomb
        if (ingame) {
            for (Bomb i : bullet) {
                if (i.isActive())
                g.drawImage(i.getImg(), i.getX(), i.getY(), 30, 30, null);
            }
         }

        //Enemy
        if (ingame) {
            for (int i = 0; i < enemies.length; i++) {
                if (enemies[i].isVisible()) {
                    //enemy mérete
                    g.drawImage(enemies[i].getImg(), enemies[i].getX(), enemies[i].getY(), 45, 45, null);
                }
            }
        }

        //move spaceship
        if (ingame) {
            spaceShip.moveSpaceShip();
            moveEnemies();
            moveLaser();
            EnemyHitByLaser();
            enemiesHitSpaceShip();
            checkEnd();
        }

        //win/lose kezelés
        if (!ingame) {
            BufferedImage endGame;
            if (Victory) {
                try {
                    endGame = ImageIO.read(this.getClass().getResource("Pics/win.png"));
                    g.drawImage(endGame, 0, 0, BOARD_WIDTH, BOARD_HEIGHT, null);
                } catch (IOException e) {
                    System.out.println("Image could not be read");
                    System.exit(1);
                }
                try {
                    if (backGroundMusic.clip.isActive()) {
                        backGroundMusic.clip.stop();
                        soundAnimator = new Thread(victoryMusic);
                        soundAnimator.start();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }else
                if (Lost) {
                    try {
                        endGame = ImageIO.read(this.getClass().getResource("Pics/defeat.png"));
                        g.drawImage(endGame, 0, 0, BOARD_WIDTH, BOARD_HEIGHT, null);
                    } catch (IOException e) {
                        System.out.println("Image could not be read");
                        System.exit(1);
                    }
                    try {
                        if (backGroundMusic.clip.isActive()) {
                            backGroundMusic.clip.stop();
                            soundAnimator = new Thread(loseMusic);
                            soundAnimator.start();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
        }

        g.dispose();
    }

    public void checkEnd() {
        ingame = false;
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].isVisible() && !started) {
                ingame = true;
                Victory = true;
                break;
            }
        }
        if (spaceShip.isDestroyed()) {
            ingame = false;
            Lost = true;
            Victory = false;
        }
    }

    public void EnemyHitByLaser() {
        for (int i = 0; i < enemies.length; i++) {
            for (Bomb j : bullet) {
                //HITBOX
                if (Math.abs((enemies[i].getY()) - j.getY()) <= 35 && Math.abs((enemies[i].getX()) - j.getX()) <= 45 && j.isActive() && enemies[i].isVisible()) {
                    enemies[i].setVisible(false);
                    soundAnimator = new Thread(enemyDead);
                    soundAnimator.start();
                    j.setInActive();
                }
            }
        }
    }

    public void enemiesHitSpaceShip() {
        for (int i = 0; i < enemies.length; i++) {
            if (Math.abs((enemies[i].getY()) - spaceShip.getY()) <= 45 && enemies[i].isVisible()) {
                spaceShip.setDestroyed();
            }
        }
    }

    public void shoot() {
        a = new Bomb(spaceShip.getX() + 35, spaceShip.getY(), 15);
        a.setImg(img);
        bullet.add(a);
        a.setMoveForward(true);
        moveLaser();
    }

    public void moveLaser() {
        for (Bomb i : bullet) {
            if (i.getY() > -30) {
                i.moveLaser();
            }
        }
    }

    public void moveEnemies() {

        for (int i = 0; i < enemies.length; i++) {

            if (enemies[i].getMoveL()) {
                enemies[i].setX(enemies[i].getX() - enemies[i].getSpeed());
            }

            if (enemies[i].getMoveR()) {
                enemies[i].setX(enemies[i].getX() + enemies[i].getSpeed());
            }
        }


        for (int i = 0; i < enemies.length; i++) {

            if (enemies[i].getX() >= BOARD_WIDTH - 110) {
                for (int j = 0; j < enemies.length; j++) {
                    enemies[j].setMoveL(true);
                    enemies[j].setMoveR(false);
                    enemies[j].setY(enemies[j].getY() + enemies[j].getSpeed());
                }
            }

            if (enemies[i].getX() <= 0) {
                for (int j = 0; j < enemies.length; j++) {
                    enemies[j].setMoveL(false);
                    enemies[j].setMoveR(true);
                    enemies[j].setY(enemies[j].getY() + enemies[j].getSpeed());
                }
            }
        }
    }

    private class Adapter extends KeyAdapter{

        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            spaceShip.setMoveR(false);
            spaceShip.setMoveL(false);
        }

        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if(key == 39){
                spaceShip.setMoveR(true);
            }

            //mute music
            if (key == 77) {

                if (backGroundMusic.clip.isActive()) {
                    backGroundMusic.clip.stop();
                } else {
                    soundAnimator = new Thread(backGroundMusic);
                    soundAnimator.start();
                }
            }

            if(key == 37){
                spaceShip.setMoveL(true);
            }

            if (key == 32) {
                shoot();
                Random r = new Random();
                int randomnumber = r.nextInt(3);
                if(randomnumber == 0)
                    soundAnimator = new Thread(bomb1);
                if (randomnumber == 1)
                    soundAnimator = new Thread(bomb2);
                if (randomnumber == 2)
                    soundAnimator = new Thread(bomb3);
                soundAnimator.start();
            }

            if (key == 49) {
                if (backGroundMusic.clip.isActive()) {
                    backGroundMusic.clip.close();}
                    backGroundMusic = new Sounds("src/startmusic.wav");
                    soundAnimator = new Thread(backGroundMusic);
                    soundAnimator.start();

            }

            if (key == 50) {
                if (backGroundMusic.clip.isActive()) {
                    backGroundMusic.clip.close();}
                backGroundMusic = new Sounds("src/startmusic2.wav");
                soundAnimator = new Thread(backGroundMusic);
                soundAnimator.start();

            }

            if (key == 51) {
                if (backGroundMusic.clip.isActive()) {
                    backGroundMusic.clip.close();}
                backGroundMusic = new Sounds("src/startmusic3.wav");
                soundAnimator = new Thread(backGroundMusic);
                soundAnimator.start();

            }

            if (key == 106){
                ingame = false;
            }

            if (key == 10) {
                ingame = true;
                started = false;
            }
        }

    }

    public void run() {
        int animationDelay = 15;
        long time = System.currentTimeMillis();
        while (true) {
            repaint();
            try {
                time += animationDelay;
                Thread.sleep(Math.max(0,time - System.currentTimeMillis()));
            }catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }


}
