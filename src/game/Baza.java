package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Baza extends JPanel {
    public int hp;
    public int pieniadze;
    public Image bazaImage;
    public Baza(int hp_in, String imagePath, int pieniadze_in) {

        this.hp = hp_in;
        this.pieniadze = pieniadze_in;

        try {
            bazaImage = ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bazaImage != null) {
            g.drawImage(bazaImage, 0, 0, this);
        }
    }

    public void odejmijHP(int damage){
        this.hp -= damage;
    }

    public void odejmijPieniandze(int koszt_in){

        this.pieniadze -= koszt_in;

    }

    public void dodajPieniadze(int koszt_in){

        this.pieniadze += koszt_in;

    }


}