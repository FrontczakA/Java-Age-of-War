package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Base extends JPanel {
    public int hp;
    public int money;
    public Image baseImage;
    public Base(int hp_in, String imagePath, int money_in) {
        this.hp = hp_in;
        this.money = money_in;
        try {
            baseImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (baseImage != null) {
            g.drawImage(baseImage, 0, 0, this);
        }
    }

    public void subtractHp(int damage){
        this.hp -= damage;
    }

    public void subtractMoney(int cost_in){
        this.money -= cost_in;
    }

    public void addMoney(int cost_in){
        this.money += cost_in;
    }
    
}