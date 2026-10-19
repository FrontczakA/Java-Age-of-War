package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Wojownik extends JPanel {
    public int damage;
    public Image wojownikImage;
    public int pozycjaX;
    public int koszt;
    public boolean aktywny = true;

    Wojownik(int damage_in, int koszt_in, String imagePath) {

        damage = damage_in;
        koszt = koszt_in;
        try {
            wojownikImage = ImageIO.read(new File(imagePath));
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

    public void przesunTest(
            int pixels,
            Baza bazaPrzeciwnika,
            JLabel hpBazyWroga,
            JPanel backgroundPanel,
            ArrayList<WojownikWrog> wojownicyWrogowie,
            int nagrodaDojsciaDoBazy,
            Baza bazaGracza,
            JLabel pieniadze) {

        if (!aktywny) {
            return;
        }

        pozycjaX = getX() + pixels;
        setLocation(pozycjaX, getY());

        for (WojownikWrog wrog : wojownicyWrogowie) {

            if (!wrog.aktywny) {
                continue;
            }

            // Kolizja, również gdy jednostki nie mają identycznego X
            if (getBounds().intersects(wrog.getBounds())) {

                if (damage > wrog.damage) {
                    wrog.aktywny = false;
                    backgroundPanel.remove(wrog);

                } else if (damage < wrog.damage) {
                    aktywny = false;
                    backgroundPanel.remove(this);

                } else {
                    aktywny = false;
                    wrog.aktywny = false;

                    backgroundPanel.remove(this);
                    backgroundPanel.remove(wrog);
                }

                backgroundPanel.repaint();
                break;
            }
        }

        if (!aktywny) {
            return;
        }

        if (getX() >= 1000) {
            aktywny = false;

            bazaGracza.dodajPieniadze(nagrodaDojsciaDoBazy);
            pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);

            bazaPrzeciwnika.odejmijHP(damage);
            hpBazyWroga.setText("Punkty życia bazy: " + bazaPrzeciwnika.hp);

            backgroundPanel.remove(this);
            backgroundPanel.repaint();
        }
    }
}
