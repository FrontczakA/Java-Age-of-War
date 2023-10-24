package game;

import javax.swing.*;
import java.util.ArrayList;


public class WojownikWrog extends Wojownik{

    WojownikWrog( int damage_in, int koszt_in, String imagePath, JLabel hpBazyWroga, JPanel backgroundPanel)
    {
        super( damage_in, koszt_in, imagePath);
    };


    public void przesunTest(int pixels, Baza bazaGracza, JLabel hpBazyGracza, JPanel backgroundPanel,  ArrayList<Wojownik> listaWojownikow) {
        this.pozycjaX = getX();
        this.pozycjaX -= pixels;
        this.repaint();
        this.setLocation(pozycjaX, getY());

        for (Wojownik wojownik : listaWojownikow) {
            if (this.getX() == wojownik.getX()) {
                if (this.damage > wojownik.damage) {
                    wojownik.setBounds(-100,2000,0,0);
                    backgroundPanel.remove(wojownik);
                } else if (this.damage < wojownik.damage) {
                    backgroundPanel.remove(this);
                    this.setBounds(-100,2000,0,0);

                }
                else {
                    backgroundPanel.remove(this);
                    backgroundPanel.remove(wojownik);
                    wojownik.setBounds(-100,2000,0,0);
                    this.setBounds(-100,2000,0,0);

                }
            }
        }

        if(pozycjaX == 100){
            bazaGracza.odejmijHP(this.damage);
            hpBazyGracza.setText("Punkty życia bazy: " + bazaGracza.hp);

            backgroundPanel.remove(this);
            this.removeAll();
            System.gc();
        }
    }


}
