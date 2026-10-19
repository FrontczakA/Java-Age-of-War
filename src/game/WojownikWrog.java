package game;

import javax.swing.*;
import java.util.ArrayList;


public class WojownikWrog extends Wojownik{

    WojownikWrog( int damage_in, int koszt_in, String imagePath, JLabel hpBazyWroga, JPanel backgroundPanel)
    {
        super( damage_in, koszt_in, imagePath);
    };


    public void przesunTest(
            int pixels,
            Baza bazaGracza,
            JLabel hpBazyGracza,
            JPanel backgroundPanel,
            ArrayList<Wojownik> listaWojownikow) {

        if (!aktywny) {
            return;
        }

        pozycjaX = getX() - pixels;
        setLocation(pozycjaX, getY());

        for (Wojownik wojownik : listaWojownikow) {

            if (!wojownik.aktywny) {
                continue;
            }

            if (getBounds().intersects(wojownik.getBounds())) {

                if (damage > wojownik.damage) {
                    wojownik.aktywny = false;
                    backgroundPanel.remove(wojownik);

                } else if (damage < wojownik.damage) {
                    aktywny = false;
                    backgroundPanel.remove(this);

                } else {
                    aktywny = false;
                    wojownik.aktywny = false;

                    backgroundPanel.remove(this);
                    backgroundPanel.remove(wojownik);
                }

                backgroundPanel.repaint();
                break;
            }
        }

        if (!aktywny) {
            return;
        }

        if (getX() <= 100) {
            aktywny = false;

            bazaGracza.odejmijHP(damage);
            hpBazyGracza.setText("Punkty życia bazy: " + bazaGracza.hp);

            backgroundPanel.remove(this);
            backgroundPanel.repaint();
        }
    }
}
