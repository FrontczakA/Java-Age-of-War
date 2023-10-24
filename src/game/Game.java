package game;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.sound.sampled.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
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
    public JLabel koniecGry= new JLabel();
    ArrayList<Wojownik> listaWojownikow = new ArrayList<>();
    ArrayList<WojownikWrog> listaWrogow = new ArrayList<>();
    public int era = 1;
    public int eraPrzeciwnika = 1;

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

        add(walkaT1);
        add(walkaT2);
        add(walkaT3);
        add(ewolucja);
        add(rozpocznij);
        add(zakoncz);

        rozpocznij.addActionListener(this);
        zakoncz.addActionListener(this);
        walkaT1.addActionListener(this);
        walkaT2.addActionListener(this);
        walkaT3.addActionListener(this);
        ewolucja.addActionListener(this);

        // Wczytanie obrazka tła
        try {
            backgroundImage = ImageIO.read(getClass().getResource("grafiki/background.jpg"));
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

        //Soundtrack

        try {
            URL soundtrackUrl = getClass().getResource("grafiki/soundtrack.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundtrackUrl);
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
        if (era == 1) {
            String baseG = "grafiki/t1_base_gracz.png";

            bazaGracza = new Baza(100, baseG, 150);
            bazaGracza.setBounds(0, 550, 150, 150);
            bazaGracza.setOpaque(false);
            backgroundPanel.add(bazaGracza);
        } else if (era == 2) {
            String baseG = "grafiki/t2_base_gracz.png";

            bazaGracza = new Baza(150, baseG, 300);
            bazaGracza.setBounds(-25, 500, 200, 300);
            bazaGracza.setOpaque(false);
            backgroundPanel.add(bazaGracza);
            hpBazyGracza.setText("Punkty życia bazy: " + bazaGracza.hp);
            pieniadze.setText("Pieniadze: " + bazaGracza.pieniadze);

        } else if (era == 3) {
            String baseG = "grafiki/t3_base_gracz.png";

            bazaGracza = new Baza(200, baseG, 450);
            bazaGracza.setBounds(-40, 400, 250, 350);
            bazaGracza.setOpaque(false);
            backgroundPanel.add(bazaGracza);
            hpBazyGracza.setText("Punkty życia bazy: " + bazaGracza.hp);
            pieniadze.setText("Pieniadze: " + bazaGracza.pieniadze);

        }
    }

    public void baseGRemove() {
        bazaGracza.setVisible(false);
        backgroundPanel.remove(bazaGracza);
    }

    public void baseWRemove() {
        bazaPrzeciwnika.setVisible(false);
        backgroundPanel.remove(bazaPrzeciwnika);
    }
    public void baseWSpawn() {

    if(eraPrzeciwnika==1) {
        String baseW = "grafiki/t1_base_wrog.png";

        bazaPrzeciwnika = new Baza(100, baseW, 0);
        bazaPrzeciwnika.setBounds(1050, 550, 150, 150);
        bazaPrzeciwnika.setOpaque(false);
        backgroundPanel.add(bazaPrzeciwnika);
    }
    else if(eraPrzeciwnika == 2){
        String baseW = "grafiki/t2_base_wrog.png";

        bazaPrzeciwnika = new Baza(150, baseW,0);
        bazaPrzeciwnika.setBounds(1050, 500, 200, 300);
        bazaPrzeciwnika.setOpaque(false);
        backgroundPanel.add(bazaPrzeciwnika);
        hpBazyWroga.setText("Punkty życia bazy: " + bazaPrzeciwnika.hp);
    }
    else if(eraPrzeciwnika == 3){
        String baseW = "grafiki/t3_base_wrog.png";

        bazaPrzeciwnika = new Baza(200, baseW,0);
        bazaPrzeciwnika.setBounds(1000, 400, 250, 350);
        bazaPrzeciwnika.setOpaque(false);
        backgroundPanel.add(bazaPrzeciwnika);
        hpBazyWroga.setText("Punkty życia bazy: " + bazaPrzeciwnika.hp);
    }

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

            era = 1;
            eraPrzeciwnika = 1;

            baseWRemove();
            baseWSpawn();
            baseGRemove();
            baseGSpawn();

            bazaGracza.hp = 100;
            bazaGracza.pieniadze = 150;
            bazaPrzeciwnika.hp = 100;
            hpBazyGracza.setText("Punkty życia bazy: "+ bazaGracza.hp);
            hpBazyWroga.setText("Punkty życia bazy: "+ bazaPrzeciwnika.hp);
            pieniadze.setText("Pieniadze: "+ bazaGracza.pieniadze);
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

            //Przejscie przeciwnika do nowej ery w przedziale od 20 do 60 sekundy od rozpoczecia gry
            Random rand = new Random();
            int liczba1 = rand.nextInt(20000) + 40000;

            Timer timerPrzeciwnikEra2 = new Timer(liczba1, g -> {
                eraPrzeciwnika = 2;
                baseWRemove();
                baseWSpawn();
            });

            timerPrzeciwnikEra2.start();

            //Przejscie przeciwnika do nowej ery w przedziale od 80 do 160 sekundy od rozpoczecia gry
            int liczba2 = rand.nextInt(80000) + 80000;

            Timer timerPrzeciwnikEra3 = new Timer(liczba2, g -> {
                eraPrzeciwnika = 3;
                baseWRemove();
                baseWSpawn();
            });
            timerPrzeciwnikEra3.start();

            timerPrzeciwnikEra3.setRepeats(false);
            timerPrzeciwnikEra2.setRepeats(false);

            //Obsluga przeciwnika
            Timer timerPrzeciwnik = new Timer(3800, g -> {
                if(bazaGracza.hp>0&&bazaPrzeciwnika.hp>0){
                    OperacjePrzeciwnika();
                }
                koniecGry(bazaGracza,bazaPrzeciwnika,timerPrzeciwnikEra2,timerPrzeciwnikEra3);
            });
            timerPrzeciwnik.setRepeats(true);
            timerPrzeciwnik.start();

            koniecGry.setVisible(false);

            //Dodawanie pieniedzy co 5 sekund

            Timer timer = new Timer(5000, g -> {
                bazaGracza.dodajPieniadze(100);
                pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);
            });
            timer.start();


        }
        else if (source == zakoncz) {
            dispose();
    }

        else if (source == walkaT1) {
                if (era == 1) {

                                Wojownik wojownik = new Wojownik(5,50, "grafiki\\t1_m_gracz.png");
                                listaWojownikow.add(wojownik);

                        if((bazaGracza.pieniadze-wojownik.koszt)>=0){
                                bazaGracza.odejmijPieniandze(wojownik.koszt);
                                pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);

                                wojownik.setOpaque(false);
                                wojownik.setVisible(true);
                                wojownik.setBounds(140, 600, 175, 150);

                                backgroundPanel.add(wojownik);

                                //Ruch jednostki
                                Timer timer = new Timer(25 , new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {

                                        wojownik.przesunTest( 5, bazaPrzeciwnika, hpBazyWroga, backgroundPanel, listaWrogow, 25 , bazaGracza, pieniadze);
                                        if(wojownik.getX() == 1000)
                                        {
                                             listaWojownikow.remove(wojownik);
                                        }
                                    }
                                });


                                timer.start();



                                 }
                        else {}

                }


            else if (era == 2){

                        Wojownik wojownik = new Wojownik(10,100,"grafiki\\t2_m_gracz.png");
                        listaWojownikow.add(wojownik);

                        if((bazaGracza.pieniadze-wojownik.koszt)>=0) {
                            bazaGracza.odejmijPieniandze(wojownik.koszt);
                            pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);

                            wojownik.setOpaque(false);
                            wojownik.setVisible(true);
                            wojownik.setBounds(140, 585, 100, 125);

                            backgroundPanel.add(wojownik);
                            Timer timer = new Timer(25, new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    wojownik.przesunTest( 5, bazaPrzeciwnika, hpBazyWroga, backgroundPanel, listaWrogow, 50 , bazaGracza, pieniadze);
                                    if(wojownik.getX() == 1000)
                                    {
                                        listaWojownikow.remove(wojownik);
                                    }
                                }
                            });
                            timer.start();
                        }
                else{}

            }

                else if (era == 3){
                        Wojownik wojownik = new Wojownik(20,150,"grafiki\\t3_m_gracz.png");
                        listaWojownikow.add(wojownik);

                        if((bazaGracza.pieniadze-wojownik.koszt)>=0) {
                            bazaGracza.odejmijPieniandze(wojownik.koszt);
                            pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);

                            wojownik.setOpaque(false);
                            wojownik.setVisible(true);
                            wojownik.setBounds(140, 575, 115, 150);

                            backgroundPanel.add(wojownik);
                            Timer timer = new Timer(25, new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    wojownik.przesunTest( 5, bazaPrzeciwnika, hpBazyWroga, backgroundPanel, listaWrogow, 75 , bazaGracza, pieniadze);
                                    if(wojownik.getX() == 1000)
                                    {
                                        listaWojownikow.remove(wojownik);
                                    }
                                }
                            });
                            timer.start();

                        }
                        else{}

                }

        }

        else if (source == walkaT2){

            if(era==1){
                    Wojownik wojownik = new Wojownik(10,100,"grafiki\\t1_w_gracz.png");
                    listaWojownikow.add(wojownik);

                    if((bazaGracza.pieniadze-wojownik.koszt)>=0) {
                        bazaGracza.odejmijPieniandze(wojownik.koszt);
                        pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);

                        wojownik.setOpaque(false);
                        wojownik.setVisible(true);
                        wojownik.setBounds(140, 575, 175, 150);

                        backgroundPanel.add(wojownik);

                        Timer timer = new Timer(25, new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                wojownik.przesunTest( 5, bazaPrzeciwnika, hpBazyWroga, backgroundPanel, listaWrogow, 25, bazaGracza, pieniadze);
                                if(wojownik.getX() == 1000)
                                {
                                    listaWojownikow.remove(wojownik);
                                }
                            }
                        });

                        timer.start();


                        }
                else{}

                }
            else if(era == 2){
                    Wojownik wojownik = new Wojownik(20,150,"grafiki\\t2_w_gracz.png");
                    listaWojownikow.add(wojownik);

                         if((bazaGracza.pieniadze-wojownik.koszt)>=0) {
                        bazaGracza.odejmijPieniandze(wojownik.koszt);
                        pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);

                        wojownik.setOpaque(false);
                        wojownik.setVisible(true);
                        wojownik.setBounds(140, 585, 100, 125);

                        backgroundPanel.add(wojownik);

                        Timer timer = new Timer(25, new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                wojownik.przesunTest( 5, bazaPrzeciwnika, hpBazyWroga, backgroundPanel, listaWrogow, 50, bazaGracza, pieniadze);
                                if(wojownik.getX() == 1000)
                                {
                                    listaWojownikow.remove(wojownik);
                                }
                            }
                        });
                        timer.start();


                        }
                else {}


                }
            else if (era == 3){
                    Wojownik wojownik = new Wojownik(30,200,"grafiki\\t3_w_gracz.png");
                    listaWojownikow.add(wojownik);

                    if((bazaGracza.pieniadze-wojownik.koszt)>=0) {
                        bazaGracza.odejmijPieniandze(wojownik.koszt);
                        pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);

                        wojownik.setOpaque(false);
                        wojownik.setVisible(true);
                        wojownik.setBounds(140, 575, 90, 150);

                        backgroundPanel.add(wojownik);
                        Timer timer = new Timer(25, new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                wojownik.przesunTest( 5, bazaPrzeciwnika, hpBazyWroga, backgroundPanel, listaWrogow, 75 , bazaGracza, pieniadze);
                                if(wojownik.getX() == 1000)
                                {
                                    listaWojownikow.remove(wojownik);
                                }
                            }
                        });
                        timer.start();


                    }
                    else{}

            }

            }

            else if (source == walkaT3){

            if(era==1){
                    Wojownik wojownik = new Wojownik(20,150,"grafiki\\t1_s_gracz.png");
                    listaWojownikow.add(wojownik);

                    if((bazaGracza.pieniadze-wojownik.koszt)>=0) {
                        bazaGracza.odejmijPieniandze(wojownik.koszt);
                        pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);

                        wojownik.setOpaque(false);
                        wojownik.setVisible(true);
                        wojownik.setBounds(140, 550, 175, 150);

                        backgroundPanel.add(wojownik);

                        Timer timer = new Timer(25, new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                wojownik.przesunTest( 5, bazaPrzeciwnika, hpBazyWroga, backgroundPanel, listaWrogow, 25, bazaGracza, pieniadze);
                                if(wojownik.getX() == 1000)
                                {
                                    listaWojownikow.remove(wojownik);
                                }
                            }
                        });
                        timer.start();


                     }
                else {}

                }
            else if(era == 2){
                    Wojownik wojownik = new Wojownik( 30, 200, "grafiki\\t2_s_gracz.png");
                    listaWojownikow.add(wojownik);

                    if ((bazaGracza.pieniadze - wojownik.koszt) >= 0) {
                        bazaGracza.odejmijPieniandze(wojownik.koszt);
                        pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);

                        wojownik.setOpaque(false);
                        wojownik.setVisible(true);
                        wojownik.setBounds(140, 550, 200, 175);

                        backgroundPanel.add(wojownik);

                        Timer timer = new Timer(25, new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                wojownik.przesunTest( 5, bazaPrzeciwnika, hpBazyWroga, backgroundPanel, listaWrogow, 50, bazaGracza, pieniadze);
                                if(wojownik.getX() == 1000)
                                {
                                    listaWojownikow.remove(wojownik);
                                }
                            }
                        });

                        timer.start();


                    } else {}


            }
            else if (era == 3){
                    Wojownik wojownik = new Wojownik(40,250,"grafiki\\t3_s_gracz.png");
                    listaWojownikow.add(wojownik);

                    if((bazaGracza.pieniadze-wojownik.koszt)>=0) {
                        bazaGracza.odejmijPieniandze(wojownik.koszt);
                        pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);

                        wojownik.setOpaque(false);
                        wojownik.setVisible(true);
                        wojownik.setBounds(140, 570, 90, 150);

                        backgroundPanel.add(wojownik);
                        Timer timer = new Timer(25, new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                wojownik.przesunTest( 5, bazaPrzeciwnika, hpBazyWroga, backgroundPanel, listaWrogow, 75 , bazaGracza, pieniadze);
                                if(wojownik.getX() == 1000)
                                {
                                    listaWojownikow.remove(wojownik);
                                }
                            }
                        });
                        timer.start();


                          }
                    else{}
            }

            }
            else if (source == ewolucja){
                if(era == 1){
                if((bazaGracza.pieniadze-400)>=0){
                        bazaGracza.odejmijPieniandze(400);
                        pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);
                        era = 2;
                        baseGRemove();
                        baseGSpawn();
                        kosztT3.setText("Koszt :" + 200 );
                        kosztT2.setText("Koszt :" + 150 );
                        kosztT1.setText("Koszt :" + 100 );
                        kosztE.setText("Koszt :" + 600 );
                }
                else{}
                }
                if(era == 2){
                    if((bazaGracza.pieniadze-600)>=0){
                        bazaGracza.odejmijPieniandze(600);
                        pieniadze.setText("Pieniądze: " + bazaGracza.pieniadze);
                        era = 3;
                        ewolucja.setVisible(false);
                        baseGRemove();
                        baseGSpawn();
                        kosztT3.setText("Koszt :" + 250 );
                        kosztT2.setText("Koszt :" + 200 );
                        kosztT1.setText("Koszt :" + 150 );
                        kosztE.setVisible(false);
                    }else{}
            }

        }
    }


    //Działania przeciwnika
    public void OperacjePrzeciwnika() {



        Random rand = new Random();
        int liczbaPrzeciwnik = rand.nextInt(5);
        System.out.println(liczbaPrzeciwnik);

        if (eraPrzeciwnika == 1) {
            if (liczbaPrzeciwnik == 0) {
                WojownikWrog wojownikWrog = new WojownikWrog( 5, 50, "grafiki\\t1_m_wrog.png", hpBazyGracza, backgroundPanel);
                listaWrogow.add(wojownikWrog);

                wojownikWrog.setOpaque(false);
                wojownikWrog.setVisible(true);
                wojownikWrog.setBounds(1000, 600, 175, 150);

                backgroundPanel.add(wojownikWrog);

                Timer timer = new Timer(25, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        wojownikWrog.przesunTest(5, bazaGracza, hpBazyGracza, backgroundPanel, listaWojownikow);
                        if(wojownikWrog.getX()==100){
                            listaWrogow.remove(wojownikWrog);
                        }
                    }
                });
                timer.start();

                liczbaPrzeciwnik = rand.nextInt(5);
            }
            if (liczbaPrzeciwnik == 1) {
                WojownikWrog wojownikWrog = new WojownikWrog(10, 100, "grafiki\\t1_w_wrog.png", hpBazyGracza, backgroundPanel);
                listaWrogow.add(wojownikWrog);

                wojownikWrog.setOpaque(false);
                wojownikWrog.setVisible(true);
                wojownikWrog.setBounds(1000, 575, 175, 150);

                backgroundPanel.add(wojownikWrog);

                Timer timer = new Timer(25, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        wojownikWrog.przesunTest(5, bazaGracza, hpBazyGracza, backgroundPanel, listaWojownikow);
                        if(wojownikWrog.getX()==100){
                            listaWrogow.remove(wojownikWrog);
                        }
                    }
                });
                timer.start();

                liczbaPrzeciwnik = rand.nextInt(5);
            }
            if (liczbaPrzeciwnik == 2) {
                WojownikWrog wojownikWrog = new WojownikWrog( 20, 100, "grafiki\\t1_s_wrog.png", hpBazyGracza, backgroundPanel);
                listaWrogow.add(wojownikWrog);

                wojownikWrog.setOpaque(false);
                wojownikWrog.setVisible(true);
                wojownikWrog.setBounds(1000, 550, 175, 150);

                backgroundPanel.add(wojownikWrog);

                Timer timer = new Timer(25, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        wojownikWrog.przesunTest(5, bazaGracza, hpBazyGracza, backgroundPanel, listaWojownikow);
                        if(wojownikWrog.getX()==100){
                            listaWrogow.remove(wojownikWrog);
                        }
                    }
                });
                timer.start();

                liczbaPrzeciwnik = rand.nextInt(5); //liczbaPrzeciwnik losowa od 0 do 9
            }

            if (liczbaPrzeciwnik == 3) {
                liczbaPrzeciwnik = rand.nextInt(5);
            }
            if (liczbaPrzeciwnik == 4) {
                liczbaPrzeciwnik = rand.nextInt(5);
            }
            else{}
        }
        if (eraPrzeciwnika == 2) {
            if (liczbaPrzeciwnik == 0) {
                WojownikWrog wojownikWrog = new WojownikWrog( 10, 50, "grafiki\\t2_m_wrog.png", hpBazyGracza, backgroundPanel);
                listaWrogow.add(wojownikWrog);

                wojownikWrog.setOpaque(false);
                wojownikWrog.setVisible(true);
                wojownikWrog.setBounds(1000, 575, 175, 150);

                backgroundPanel.add(wojownikWrog);

                Timer timer = new Timer(25, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        wojownikWrog.przesunTest(5, bazaGracza, hpBazyGracza, backgroundPanel, listaWojownikow);
                        if(wojownikWrog.getX()==100){
                            listaWrogow.remove(wojownikWrog);

                        }
                    }
                });
                timer.start();

                liczbaPrzeciwnik = rand.nextInt(5);
            }
            if (liczbaPrzeciwnik == 1) {
                WojownikWrog wojownikWrog = new WojownikWrog( 15, 100, "grafiki\\t2_w_wrog.png", hpBazyGracza, backgroundPanel);
                listaWrogow.add(wojownikWrog);

                wojownikWrog.setOpaque(false);
                wojownikWrog.setVisible(true);
                wojownikWrog.setBounds(1000, 575, 175, 150);

                backgroundPanel.add(wojownikWrog);

                Timer timer = new Timer(25, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        wojownikWrog.przesunTest(5, bazaGracza, hpBazyGracza, backgroundPanel, listaWojownikow);
                        if(wojownikWrog.getX()==100){
                            listaWrogow.remove(wojownikWrog);
                        }
                    }
                });
                timer.start();

                liczbaPrzeciwnik = rand.nextInt(5);
            }
            if (liczbaPrzeciwnik == 2) {
                WojownikWrog wojownikWrog = new WojownikWrog( 30, 100, "grafiki\\t2_s_wrog.png", hpBazyGracza, backgroundPanel);
                listaWrogow.add(wojownikWrog);

                wojownikWrog.setOpaque(false);
                wojownikWrog.setVisible(true);
                wojownikWrog.setBounds(1000, 550, 240, 175);

                backgroundPanel.add(wojownikWrog);

                Timer timer = new Timer(25, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        wojownikWrog.przesunTest(5, bazaGracza, hpBazyGracza, backgroundPanel, listaWojownikow);
                        if(wojownikWrog.getX()==100){
                            listaWrogow.remove(wojownikWrog);

                        }
                    }
                });
                timer.start();

                liczbaPrzeciwnik = rand.nextInt(5); //liczbaPrzeciwnik losowa od 0 do 9
            }

            if (liczbaPrzeciwnik == 3) {
                liczbaPrzeciwnik = rand.nextInt(5);
            }
            if (liczbaPrzeciwnik == 4) {
                liczbaPrzeciwnik = rand.nextInt(5);
            }
            else{}
        }

        if (eraPrzeciwnika == 3) {
            if (liczbaPrzeciwnik == 0) {
                WojownikWrog wojownikWrog = new WojownikWrog( 20, 50, "grafiki\\t3_m_wrog.png", hpBazyGracza, backgroundPanel);
                listaWrogow.add(wojownikWrog);

                wojownikWrog.setOpaque(false);
                wojownikWrog.setVisible(true);
                wojownikWrog.setBounds(1000, 575, 115, 150);

                backgroundPanel.add(wojownikWrog);

                Timer timer = new Timer(25, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        wojownikWrog.przesunTest(5, bazaGracza, hpBazyGracza, backgroundPanel, listaWojownikow);
                        if(wojownikWrog.getX()==100){
                            listaWrogow.remove(wojownikWrog);

                        }
                    }
                });
                timer.start();

                liczbaPrzeciwnik = rand.nextInt(5);
            }
            if (liczbaPrzeciwnik == 1) {
                WojownikWrog wojownikWrog = new WojownikWrog( 30, 100, "grafiki\\t3_w_wrog.png", hpBazyGracza, backgroundPanel);
                listaWrogow.add(wojownikWrog);

                wojownikWrog.setOpaque(false);
                wojownikWrog.setVisible(true);
                wojownikWrog.setBounds(1000, 575, 90, 150);

                backgroundPanel.add(wojownikWrog);

                Timer timer = new Timer(25, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        wojownikWrog.przesunTest(5, bazaGracza, hpBazyGracza, backgroundPanel, listaWojownikow);
                        if(wojownikWrog.getX()==100){
                            listaWrogow.remove(wojownikWrog);
                        }
                    }
                });
                timer.start();

                liczbaPrzeciwnik = rand.nextInt(5);
            }
            if (liczbaPrzeciwnik == 2) {
                WojownikWrog wojownikWrog = new WojownikWrog( 40, 100, "grafiki\\t3_s_wrog.png", hpBazyGracza, backgroundPanel);
                listaWrogow.add(wojownikWrog);

                wojownikWrog.setOpaque(false);
                wojownikWrog.setVisible(true);
                wojownikWrog.setBounds(1000, 570, 90, 150);

                backgroundPanel.add(wojownikWrog);

                Timer timer = new Timer(25, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        wojownikWrog.przesunTest(5, bazaGracza, hpBazyGracza, backgroundPanel, listaWojownikow);
                        if(wojownikWrog.getX()==100){
                            listaWrogow.remove(wojownikWrog);

                        }
                    }
                });
                timer.start();

                liczbaPrzeciwnik = rand.nextInt(5); //liczbaPrzeciwnik losowa od 0 do 9
            }

            if (liczbaPrzeciwnik == 3) {
                liczbaPrzeciwnik = rand.nextInt(5);
            }
            if (liczbaPrzeciwnik == 4) {
                liczbaPrzeciwnik = rand.nextInt(5);
            }
            else{}
        }

    }

    public void koniecGry(Baza bazaGracza,Baza bazaPrzeciwnika, Timer timer1, Timer timer2){
        if(bazaGracza.hp <= 0 ){
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
        }
        else if(bazaPrzeciwnika.hp <= 0){
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


};
