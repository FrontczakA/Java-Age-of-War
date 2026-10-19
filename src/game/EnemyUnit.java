package game;

import javax.swing.*;
import java.util.ArrayList;


public class EnemyUnit extends Unit {

    EnemyUnit(int damage_in, int cost_in, String imagePath, JLabel enemyBaseHp, JPanel backgroundPanel) {
        super(damage_in, cost_in, imagePath);
    }

    public void move(int pixels, Base playerBase, JLabel playerBaseHp, JPanel backgroundPanel, ArrayList<Unit> unitList) {
        if (!active) {
            return;
        }
        positionX = getX() - pixels;
        setLocation(positionX, getY());
        for (Unit unit : unitList) {
            if (!unit.active) {
                continue;
            }
            if (getBounds().intersects(unit.getBounds())) {
                if (damage > unit.damage) {
                    unit.active = false;
                    backgroundPanel.remove(unit);
                } else if (damage < unit.damage) {
                    active = false;
                    backgroundPanel.remove(this);
                } else {
                    active = false;
                    unit.active = false;
                    backgroundPanel.remove(this);
                    backgroundPanel.remove(unit);
                }
                backgroundPanel.repaint();
                break;
            }
        }
        if (!active) {
            return;
        }
        if (getX() <= 100) {
            active = false;
            playerBase.subtractHp(damage);
            playerBaseHp.setText("Base HP: " + playerBase.hp);
            backgroundPanel.remove(this);
            backgroundPanel.repaint();
        }
    }
}
