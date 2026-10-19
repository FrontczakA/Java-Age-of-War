package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;


class Game extends JFrame implements ActionListener {
    public static final int DEFAULT_WIDTH = 1200;
    public static final int DEFAULT_HEIGHT = 770;
    public Image backgroundImage;
    public JButton rozpocznij;
    public JButton zakoncz;
    public JButton walkaT1;
    public JButton walkaT2;
    public JButton walkaT3;
    public JButton ewolucja;
    public Clip soundtrack;
    public Baza bazaGracza;
    public Baza bazaPrzeciwnika;
    public JLabel pieniadze;
    public JLabel hpBazyGracza;
    public JLabel hpBazyWroga;
    public JLabel kosztT1;
    public JLabel kosztT2;
    public JLabel kosztT3;
    public JLabel kosztE;
    public JPanel backgroundPanel;
    public JLabel koniecGry = new JLabel();
    ArrayList<Wojownik> listaWojownikow = new ArrayList<>();
    ArrayList<WojownikWrog> listaWrogow = new ArrayList<>();
    public int era = 1;
    public int eraPrzeciwnika = 1;
    private long ostatniaRekrutacjaGracza = 0;
    private long ostatniaRekrutacjaWroga = 0;
    private static final long COOLDOWN_REKRUTACJI = 750;

    public Game() {
        setTitle("Age of War [FrontczakJZOgr1]");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        // Przyciski
        rozpocznij = new JButton("Rozpocznij grę");
        zakoncz = new JButton("Zakończ grę");
        walkaT1 = new JButton("Zrekrutuj słabego wojownika");
        walkaT2 = new JButton("Zrekrutuj zwyczajnego wojownika");
        walkaT3 = new JButton("Zrekrutuj silnego wojownika");
        ewolucja = new JButton("Przejdź do następnej ery");
        rozpocznij.setBounds(460, 150, 250, 100);
        zakoncz.setBounds(460, 300, 250, 100);
        walkaT3.setBounds(615, 50, 250, 50);
        walkaT2.setBounds(315, 50, 250, 50);
        walkaT1.setBounds(15, 50, 250, 50);
        ewolucja.setBounds(915, 50, 250, 50);
        walkaT1.setVisible(false);
        walkaT2.setVisible(false);
        walkaT3.setVisible(false);
        ewolucja.setVisible(false);
        rozpocznij.addActionListener(this);
        zakoncz.addActionListener(this);
        walkaT1.addActionListener(this);
        walkaT2.addActionListener(this);
        walkaT3.addActionListener(this);
        ewolucja.addActionListener(this);

        // Wczytanie obrazka tła
        try {
            backgroundImage = ImageIO.read(new File("graphics/background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Dodanie panelu z obrazkiem
        backgroundPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, -20, null);
            }
        };
        backgroundPanel.setLayout(null);
        add(backgroundPanel);
        backgroundPanel.add(walkaT1);
        backgroundPanel.add(walkaT2);
        backgroundPanel.add(walkaT3);
        backgroundPanel.add(ewolucja);
        backgroundPanel.add(rozpocznij);
        backgroundPanel.add(zakoncz);
        //Soundtrack

        try {
            File soundtrackFile = new File("graphics/soundtrack.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundtrackFile);
            soundtrack = AudioSystem.getClip();
            soundtrack.open(audioInputStream);
            soundtrack.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        baseWSpawn();
        baseGSpawn();
        baseInfo();
        jednostkiInfo();
    }

    public void baseGSpawn() {
        switch (era) {
            case 1 -> bazaGracza = stworzBaze(100, "graphics/t1_base_gracz.png", 150, 0, 550, 150, 150);
            case 2 -> bazaGracza = stworzBaze(150, "graphics/t2_base_gracz.png", 300, -25, 500, 200, 300);
            case 3 -> bazaGracza = stworzBaze(200, "graphics/t3_base_gracz.png", 450, -40, 400, 250, 350);
        }
        if (hpBazyGracza != null && bazaGracza != null) {
            hpBazyGracza.setText("Punkty życia bazy: " + bazaGracza.hp);
            pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);
        }
    }

    public void baseWSpawn() {
        switch (eraPrzeciwnika) {
            case 1 -> bazaPrzeciwnika = stworzBaze(100, "graphics/t1_base_wrog.png", 0, 1050, 550, 150, 150);
            case 2 -> bazaPrzeciwnika = stworzBaze(150, "graphics/t2_base_wrog.png", 0, 1050, 500, 200, 300);
            case 3 -> bazaPrzeciwnika = stworzBaze(200, "graphics/t3_base_wrog.png", 0, 1000, 400, 250, 350);
        }
        if (hpBazyWroga != null && bazaPrzeciwnika != null) {
            hpBazyWroga.setText("Punkty życia bazy: " + bazaPrzeciwnika.hp
            );
        }
    }

    private Baza stworzBaze(int hp, String grafika, int pieniadze, int x, int y, int szerokosc, int wysokosc) {
        Baza baza = new Baza(hp, grafika, pieniadze);
        baza.setBounds(x, y, szerokosc, wysokosc);
        baza.setOpaque(false);
        backgroundPanel.add(baza);
        return baza;
    }

    private void usunBaze(Baza baza) {
        if (baza == null) {
            return;
        }
        backgroundPanel.remove(baza);
        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }

    public void baseInfo() {
        pieniadze = new JLabel("Pieniądze: " + bazaGracza.pieniadze);
        pieniadze.setBounds(20, 150, 200, 50);
        backgroundPanel.add(pieniadze);
        pieniadze.setVisible(false);
        hpBazyGracza = new JLabel("Punkty życia bazy: " + bazaGracza.hp);
        hpBazyGracza.setBounds(20, 120, 200, 50);
        backgroundPanel.add(hpBazyGracza);
        hpBazyGracza.setVisible(false);
        hpBazyWroga = new JLabel("Punkty życia bazy: " + bazaPrzeciwnika.hp);
        hpBazyWroga.setBounds(1020, 120, 200, 50);
        backgroundPanel.add(hpBazyWroga);
        hpBazyWroga.setVisible(false);
    }

    public void jednostkiInfo() {
        kosztT1 = new JLabel("Koszt: " + 50);
        kosztT1.setBounds(15, 0, 100, 50);
        backgroundPanel.add(kosztT1);
        kosztT1.setVisible(false);
        kosztT2 = new JLabel("Koszt: " + 100);
        kosztT2.setBounds(315, 0, 100, 50);
        backgroundPanel.add(kosztT2);
        kosztT2.setVisible(false);
        kosztT3 = new JLabel("Koszt: " + 150);
        kosztT3.setBounds(615, 0, 100, 50);
        backgroundPanel.add(kosztT3);
        kosztT3.setVisible(false);
        kosztE = new JLabel("Koszt: " + 400);
        kosztE.setBounds(915, 0, 100, 50);
        backgroundPanel.add(kosztE);
        kosztE.setVisible(false);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == rozpocznij) {
            rozpocznijGre();
        } else if (source == zakoncz) {
            dispose();
        } else if (source == walkaT1) {
            rekrutujJednostke(1);
        } else if (source == walkaT2) {
            rekrutujJednostke(2);
        } else if (source == walkaT3) {
            rekrutujJednostke(3);
        } else if (source == ewolucja) {
            ewoluuj();
        }
    }

    private void rekrutujJednostke(int typ) {
        if (!czyMoznaRekrutowacGracza()) {
            return;
        }
        switch (era) {
            case 1 -> {
                switch (typ) {
                    case 1 -> utworzWojownika(5, 50, "graphics/t1_m_gracz.png",
                            140, 600, 175, 150, 25);

                    case 2 -> utworzWojownika(10, 100, "graphics/t1_w_gracz.png",
                            140, 575, 175, 150, 25);

                    case 3 -> utworzWojownika(20, 150, "graphics/t1_s_gracz.png",
                            140, 550, 175, 150, 25);
                }
            }

            case 2 -> {
                switch (typ) {
                    case 1 -> utworzWojownika(10, 100, "graphics/t2_m_gracz.png",
                            140, 585, 100, 125, 50);

                    case 2 -> utworzWojownika(20, 150, "graphics/t2_w_gracz.png",
                            140, 585, 100, 125, 50);

                    case 3 -> utworzWojownika(30, 200, "graphics/t2_s_gracz.png",
                            140, 550, 200, 175, 50);
                }
            }

            case 3 -> {
                switch (typ) {
                    case 1 -> utworzWojownika(20, 150, "graphics/t3_m_gracz.png",
                            140, 575, 115, 150, 75);

                    case 2 -> utworzWojownika(30, 200, "graphics/t3_w_gracz.png",
                            140, 575, 90, 150, 75);

                    case 3 -> utworzWojownika(40, 250, "graphics/t3_s_gracz.png",
                            140, 570, 90, 150, 75);
                }
            }
        }
    }

    private void utworzWojownika(int damage, int koszt, String sciezka, int x, int y, int width, int height, int nagroda) {
        if (bazaGracza.pieniadze < koszt) {
            return;
        }
        Wojownik wojownik = new Wojownik(damage, koszt, sciezka);
        rekrutujWojownikaGracza(wojownik, x, y, width, height, nagroda);
    }

    private void rekrutujWojownikaGracza(Wojownik wojownik, int x, int y, int width, int height, int nagrodaDojsciaDoBazy) {
        ostatniaRekrutacjaGracza = System.currentTimeMillis();
        bazaGracza.odejmijPieniandze(wojownik.koszt);
        pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);
        wojownik.setOpaque(false);
        wojownik.setVisible(true);
        wojownik.setBounds(x, y, width, height);
        listaWojownikow.add(wojownik);
        backgroundPanel.add(wojownik);
        Timer timer = createTimerForPlayer(wojownik, nagrodaDojsciaDoBazy);
        timer.start();
    }

    private void rozpocznijGre() {
        ostatniaRekrutacjaGracza = 0;
        ostatniaRekrutacjaWroga = 0;

        era = 1;
        eraPrzeciwnika = 1;

        usunBaze(bazaPrzeciwnika);
        baseWSpawn();

        usunBaze(bazaGracza);;
        baseGSpawn();

        bazaGracza.hp = 100;
        bazaGracza.pieniadze = 150;
        bazaPrzeciwnika.hp = 100;

        aktualizujInformacjeBaz();
        ustawWidocznoscInterfejsu();

        koniecGry.setVisible(false);

        uruchomTimeryGry();
    }

    private void aktualizujInformacjeBaz() {
        hpBazyGracza.setText("Punkty życia bazy: " + bazaGracza.hp);
        hpBazyWroga.setText("Punkty życia bazy: " + bazaPrzeciwnika.hp);
        pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);
    }

    private void ustawWidocznoscInterfejsu() {
        rozpocznij.setVisible(false);
        zakoncz.setVisible(false);

        walkaT1.setVisible(true);
        walkaT2.setVisible(true);
        walkaT3.setVisible(true);
        ewolucja.setVisible(true);

        hpBazyGracza.setVisible(true);
        hpBazyWroga.setVisible(true);
        pieniadze.setVisible(true);

        kosztE.setVisible(true);
        kosztT1.setVisible(true);
        kosztT2.setVisible(true);
        kosztT3.setVisible(true);
    }

    private void ewoluuj() {
        if (era >= 3) {
            return;
        }

        int kosztEwolucji = era == 1 ? 400 : 600;

        if (bazaGracza.pieniadze < kosztEwolucji) {
            return;
        }

        bazaGracza.odejmijPieniandze(kosztEwolucji);
        pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);

        era++;

        usunBaze(bazaGracza);;
        baseGSpawn();

        aktualizujKoszty();

        if (era == 3) {
            ewolucja.setVisible(false);
            kosztE.setVisible(false);
        }
    }

    private void aktualizujKoszty() {
        if (era == 2) {
            kosztT1.setText("Koszt: 100");
            kosztT2.setText("Koszt: 150");
            kosztT3.setText("Koszt: 200");
            kosztE.setText("Koszt: 600");

        } else if (era == 3) {
            kosztT1.setText("Koszt: 150");
            kosztT2.setText("Koszt: 200");
            kosztT3.setText("Koszt: 250");
        }
    }

    private void uruchomTimeryGry() {
        Random rand = new Random();

        int czasEra2 = rand.nextInt(20000) + 40000;
        int czasEra3 = rand.nextInt(80000) + 80000;

        Timer timerEra2 = new Timer(czasEra2, e -> {
            eraPrzeciwnika = 2;
            usunBaze(bazaPrzeciwnika);
            baseWSpawn();
        });

        Timer timerEra3 = new Timer(czasEra3, e -> {
            eraPrzeciwnika = 3;
            usunBaze(bazaPrzeciwnika);
            baseWSpawn();
        });

        timerEra2.setRepeats(false);
        timerEra3.setRepeats(false);

        Timer timerPrzeciwnik = new Timer(3800, e -> {
            if (bazaGracza.hp > 0 && bazaPrzeciwnika.hp > 0) {
                OperacjePrzeciwnika();
            }

            koniecGry(bazaGracza, bazaPrzeciwnika, timerEra2, timerEra3);
        });

        Timer timerPieniadze = new Timer(5000, e -> {
            bazaGracza.dodajPieniadze(100);
            pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);
        });

        timerEra2.start();
        timerEra3.start();
        timerPrzeciwnik.start();
        timerPieniadze.start();
    }

    private Timer createTimerForPlayer(Wojownik wojownik, int nagrodaDojsciaDoBazy) {
        Timer timer = new Timer(25, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                wojownik.przesunTest(5, bazaPrzeciwnika, hpBazyWroga, backgroundPanel, listaWrogow, nagrodaDojsciaDoBazy, bazaGracza, pieniadze);
                if (!wojownik.aktywny) {
                    listaWojownikow.remove(wojownik);
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        return timer;
    }

    //Działania przeciwnika
    public void OperacjePrzeciwnika() {
        if (!czyMoznaRekrutowacWroga()) {
            return;
        }
        Random rand = new Random();
        int los = rand.nextInt(5);
        // 3 i 4 oznaczają brak rekrutacji
        if (los >= 3) {
            return;
        }
        switch (eraPrzeciwnika) {
            case 1:
                switch (los) {
                    case 0 -> rekrutujWroga(5, 50, "graphics/t1_m_wrog.png", 1000, 600, 175, 150);
                    case 1 -> rekrutujWroga(10, 100, "graphics/t1_w_wrog.png", 1000, 575, 175, 150);
                    case 2 -> rekrutujWroga(20, 100, "graphics/t1_s_wrog.png", 1000, 550, 175, 150);
                }
                break;

            case 2:
                switch (los) {
                    case 0 -> rekrutujWroga(10, 50, "graphics/t2_m_wrog.png", 1000, 575, 175, 150);
                    case 1 -> rekrutujWroga(15, 100, "graphics/t2_w_wrog.png", 1000, 575, 175, 150);
                    case 2 -> rekrutujWroga(30, 100, "graphics/t2_s_wrog.png", 1000, 550, 240, 175);
                }
                break;

            case 3:
                switch (los) {
                    case 0 -> rekrutujWroga(20, 50, "graphics/t3_m_wrog.png", 1000, 575, 115, 150);
                    case 1 -> rekrutujWroga(30, 100, "graphics/t3_w_wrog.png", 1000, 575, 90, 150);
                    case 2 -> rekrutujWroga(40, 100, "graphics/t3_s_wrog.png", 1000, 570, 90, 150);
                }
                break;
        }
    }

    private void rekrutujWroga(int damage, int koszt, String sciezka, int x, int y, int szerokosc, int wysokosc) {
        WojownikWrog wrog = new WojownikWrog(damage, koszt, sciezka, hpBazyGracza, backgroundPanel);
        wrog.setOpaque(false);
        wrog.setBounds(x, y, szerokosc, wysokosc);
        listaWrogow.add(wrog);
        backgroundPanel.add(wrog);
        ostatniaRekrutacjaWroga = System.currentTimeMillis();
        Timer timer = createTimerForEnemies(wrog);
        timer.start();
    }

    private Timer createTimerForEnemies(WojownikWrog wojownikWrog) {
        return new Timer(25, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                wojownikWrog.przesunTest(5, bazaGracza, hpBazyGracza, backgroundPanel, listaWojownikow);
                if (!wojownikWrog.aktywny) {
                    listaWrogow.remove(wojownikWrog);
                    ((Timer) e.getSource()).stop();
                }
            }
        });
    }

    public void koniecGry(Baza bazaGracza, Baza bazaPrzeciwnika, Timer timer1, Timer timer2) {
        if (bazaGracza.hp <= 0) {
            pieniadze.setVisible(false);
            hpBazyWroga.setVisible(false);
            hpBazyGracza.setVisible(false);
            walkaT2.setVisible(false);
            walkaT1.setVisible(false);
            walkaT3.setVisible(false);
            ewolucja.setVisible(false);
            kosztT1.setVisible(false);
            kosztT2.setVisible(false);
            kosztT3.setVisible(false);
            kosztE.setVisible(false);
            rozpocznij.setVisible(true);
            zakoncz.setVisible(true);
            koniecGry.setText("Koniec gry, porażka!");
            koniecGry.setBounds(520, 0, 250, 250);
            backgroundPanel.add(koniecGry);
            koniecGry.setVisible(true);
            timer1.restart();
            timer2.restart();
        } else if (bazaPrzeciwnika.hp <= 0) {
            pieniadze.setVisible(false);
            hpBazyWroga.setVisible(false);
            hpBazyGracza.setVisible(false);
            walkaT2.setVisible(false);
            walkaT1.setVisible(false);
            walkaT3.setVisible(false);
            ewolucja.setVisible(false);
            kosztT1.setVisible(false);
            kosztT2.setVisible(false);
            kosztT3.setVisible(false);
            kosztE.setVisible(false);
            rozpocznij.setVisible(true);
            zakoncz.setVisible(true);
            koniecGry.setText("Koniec gry, zwycięstwo!");
            koniecGry.setBounds(520, 0, 250, 250);
            backgroundPanel.add(koniecGry);
            koniecGry.setVisible(true);
            timer1.restart();
            timer2.restart();
        }
    }

    private boolean czyMoznaRekrutowacGracza() {
        long teraz = System.currentTimeMillis();
        return teraz - ostatniaRekrutacjaGracza >= COOLDOWN_REKRUTACJI;
    }

    private boolean czyMoznaRekrutowacWroga() {
        long teraz = System.currentTimeMillis();
        return teraz - ostatniaRekrutacjaWroga >= COOLDOWN_REKRUTACJI;
    }
}
