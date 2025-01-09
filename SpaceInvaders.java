
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;


public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private Player player;
    private List<Bullet> bullets;
    private List<Invader> invaders;
    private boolean left, right, shoot;
    private int score, highscore, level;
    private boolean gameOver, levelCleared;
    private int invaderSpeed;

    private Image playerImg, bulletImg, invaderImg, explosionImg;
    private SoundPlayer soundPlayer;

    public SpaceInvaders() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        // Load sprites
        playerImg = new ImageIcon("player.png").getImage();
        bulletImg = new ImageIcon("bullet.png").getImage();
        invaderImg = new ImageIcon("alien.png").getImage();
        explosionImg = new ImageIcon("explosion.png").getImage();
         // Initialize the sound player and start looping the background sound
        soundPlayer = new SoundPlayer("background.wav");
        soundPlayer.playLoop();

        initGame();
    }

    private void initGame() {
        player = new Player(375, 550);
        bullets = new ArrayList<>();
        invaders = new ArrayList<>();

        score = 0;
        highscore = Database.getHighScore();
        level = 1;
        invaderSpeed = 2; // Initial speed
        gameOver = false;
        levelCleared = false;

        createInvaders();

        timer = new Timer(16, this); // 60 FPS
        timer.start();
    }

    private void createInvaders() {
        invaders.clear();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                invaders.add(new Invader(50 + col * 60, 50 + row * 50, invaderSpeed));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            Database.saveScore(score);
            soundPlayer.stop();
            return;
        }

        if (levelCleared) {
            // Display "Level Up" for a short duration
            repaint();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            levelCleared = false;
            level++;
            invaderSpeed += 1; // Increase speed for the next level
            createInvaders();
        }

        if (left) {
            player.moveLeft();
        }
        if (right) {
            player.moveRight();
        }
        if (shoot) {
            bullets.add(new Bullet(player.getX() + player.getWidth() / 2 - 2, player.getY()));
            shoot = false;
        }

        for (Bullet bullet : bullets) {
            bullet.moveUp();
        }

        for (Invader invader : invaders) {
            invader.moveSideways();
            if (invader.getBounds().intersects(player.getBounds())) {
                gameOver = true;
            }
        }

        for (Bullet bullet : bullets) {
            for (Invader invader : invaders) {
                if (bullet.getBounds().intersects(invader.getBounds())) {
                    invader.destroy();
                    bullet.destroy();
                    score += 10;
                }
            }
        }

        bullets.removeIf(Bullet::isDestroyed);
        invaders.removeIf(Invader::isDestroyed);

        if (score > highscore) {
            highscore = score;
        }

        if (invaders.isEmpty() && !gameOver) {
            levelCleared = true;
        }

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", 300, 250);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.WHITE);
            g.drawString("Press R to Try Again", 320, 300);
            return;
        }

        player.draw(g);
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        for (Invader invader : invaders) {
            invader.draw(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 10, 30);
        g.drawString("Highscore: " + highscore, 650, 30);
        g.drawString("Level: " + level, 350, 30);

        if (levelCleared) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("LEVEL UP!", 300, 250);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            left = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            right = true;
        }
        if (key == KeyEvent.VK_SPACE) {
            shoot = true;
        }
        if (key == KeyEvent.VK_R && gameOver) {
            // Restart the game
            initGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            left = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            right = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders");
        SpaceInvaders game = new SpaceInvaders();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class Player {

    private int x, y;
    private final int width = 50, height = 30;
    private final int speed = 5;
    private Image playerImg;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        playerImg = new ImageIcon("player.png").getImage();
    }

    public void moveLeft() {
        if (x > 0) {
            x -= speed;
        }
    }

    public void moveRight() {
        if (x + width < 800) {
            x += speed;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(playerImg, x, y, width, height, null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}

class Bullet {

    private int x, y;
    private final int width = 5, height = 10;
    private final int speed = 7;
    private boolean destroyed = false;
    private Image bulletImg;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        bulletImg = new ImageIcon("bullet.png").getImage();
    }

    public void moveUp() {
        y -= speed;
        if (y < 0) {
            destroyed = true;
        }
    }

    public void draw(Graphics g) {
        if (!destroyed) {
            g.drawImage(bulletImg, x, y, width, height, null);
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void destroy() {
        destroyed = true;
    }
}

class Invader {

    private int x, y, width = 40, height = 40, speed;
    private boolean destroyed = false;
    private int direction = 1; // 1 means moving right, -1 means moving left
    private Image invaderImg;

    public Invader(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        invaderImg = new ImageIcon("alien.png").getImage();
    }

    public void moveSideways() {
        x += direction * speed;
        if (x + width >= 800 || x <= 0) {
            direction *= -1; // Change direction at boundaries
            y += height; // Move down
        }
    }

    public void draw(Graphics g) {
        if (!destroyed) {
            g.drawImage(invaderImg, x, y, width, height, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void destroy() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
