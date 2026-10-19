package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Unit extends JPanel {
    public int damage;
    public Image unitImage;
    public int positionX;
    public int cost;
    public boolean active = true;

    Unit(int damage_in, int cost_in, String imagePath) {

        damage = damage_in;
        cost = cost_in;
        try {
            unitImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(75, 150));
        positionX = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (unitImage != null) {
            g.drawImage(unitImage, 0, 0, null);
        }
    }

    public void move(int pixels, Base enemyBase, JLabel enemyBaseHp, JPanel backgroundPanel, ArrayList<EnemyUnit> enemyUnits, int baseReachReward, Base playerBase, JLabel money) {
        if (!active) {
            return;
        }
        positionX = getX() + pixels;
        setLocation(positionX, getY());
        for (EnemyUnit enemy : enemyUnits) {
            if (!enemy.active) {
                continue;
            }
            // Collision even if units do not have exact same X value
            if (getBounds().intersects(enemy.getBounds())) {
                if (damage > enemy.damage) {
                    enemy.active = false;
                    backgroundPanel.remove(enemy);
                } else if (damage < enemy.damage) {
                    active = false;
                    backgroundPanel.remove(this);
                } else {
                    active = false;
                    enemy.active = false;
                    backgroundPanel.remove(this);
                    backgroundPanel.remove(enemy);
                }
                backgroundPanel.repaint();
                break;
            }
        }

        if (!active) {
            return;
        }

        if (getX() >= 1000) {
            active = false;
            playerBase.addMoney(baseReachReward);
            money.setText("Money: " + playerBase.money);
            enemyBase.subtractHp(damage);
            enemyBaseHp.setText("Base HP: " + enemyBase.hp);
            backgroundPanel.remove(this);
            backgroundPanel.repaint();
        }
    }
}
