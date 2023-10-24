package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Wojownik extends JPanel {
    public int damage;
    public Image wojownikImage;
    public int pozycjaX;
    public int koszt;

    Wojownik(int damage_in, int koszt_in, String imagePath) {

        damage = damage_in;
        koszt = koszt_in;
        try {
            wojownikImage = ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(75, 150));
        pozycjaX = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (wojownikImage != null) {
            g.drawImage(wojownikImage, 0, 0, null);
        }
    }

    public void rysujWojownika(Graphics g) {
        if (wojownikImage != null) {
            g.drawImage(wojownikImage, pozycjaX, 600, this);
        }
    }

    public void przesunTest(int pixels, Baza bazaPrzeciwnika, JLabel hpBazyWroga, JPanel backgroundPanel, ArrayList<WojownikWrog> wojownicyWrogowie, int nagrodaDojsciaDoBazy, Baza bazaGracza, JLabel pieniadze) {
        this.pozycjaX = getX();
        this.pozycjaX += pixels;
        repaint();
        this.setLocation(pozycjaX, getY());
        for (WojownikWrog wojownikWrog : wojownicyWrogowie) {
            if (this.getX() == wojownikWrog.getX()) {
                if (this.damage > wojownikWrog.damage) {
                    backgroundPanel.remove(wojownikWrog);
                    wojownikWrog.setBounds(2000,2000,0,0);
                } else if (this.damage < wojownikWrog.damage) {
                    backgroundPanel.remove(this);
                    this.setBounds(2000,2000,0,0);
                }
                else {
                    backgroundPanel.remove(this);
                    backgroundPanel.remove(wojownikWrog);
                    wojownikWrog.setBounds(0,20000,0,0);
                    this.setBounds(2000,2000,0,0);
                }
            }
        }

        if (this.getX() == 1000) {
            bazaGracza.dodajPieniadze(nagrodaDojsciaDoBazy);
            pieniadze.setText("Pieniadze: " + bazaGracza.pieniadze);
            bazaPrzeciwnika.odejmijHP(this.damage);
            hpBazyWroga.setText("Punkty życia bazy: " + bazaPrzeciwnika.hp);

            backgroundPanel.remove(this);

            //this = null;    nie da sie     jak usunac obiekt zeby zwalnial pamiec a nie tylko robil sie niewidoczny
            this.removeAll();
            System.gc();

        }
    }


}
