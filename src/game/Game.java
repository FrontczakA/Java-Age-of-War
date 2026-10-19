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

public class Game extends JFrame implements ActionListener {
    public static final int DEFAULT_WIDTH = 1200;
    public static final int DEFAULT_HEIGHT = 770;
    public Image backgroundImage;
    public JButton start;
    public JButton end;
    public JButton fightT1;
    public JButton fightT2;
    public JButton fightT3;
    public JButton evolution;
    public Clip soundtrack;
    public Base playerBase;
    public Base enemyBase;
    public JLabel money;
    public JLabel playerBaseHp;
    public JLabel enemyBaseHp;
    public JLabel costT1;
    public JLabel costT2;
    public JLabel costT3;
    public JLabel costE;
    public JPanel backgroundPanel;
    public JLabel gameOver = new JLabel();
    ArrayList<Unit> unitList = new ArrayList<>();
    ArrayList<EnemyUnit> enemyUnitList = new ArrayList<>();
    public int age = 1;
    public int enemyAge = 1;
    private long playerLastRecruitment = 0;
    private long enemyLastRecruitment = 0;
    private static final long RECRUITMENT_COOLDOWN = 750;

    public Game() {
        setTitle("Age of War Java");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        // Buttons
        start = new JButton("Start game");
        end = new JButton("End game");
        fightT1 = new JButton("Recruit weak warrior");
        fightT2 = new JButton("Recruit basic warrior");
        fightT3 = new JButton("Recruit strong warrior");
        evolution = new JButton("Advance to next age");
        start.setBounds(460, 150, 250, 100);
        end.setBounds(460, 300, 250, 100);
        fightT3.setBounds(615, 50, 250, 50);
        fightT2.setBounds(315, 50, 250, 50);
        fightT1.setBounds(15, 50, 250, 50);
        evolution.setBounds(915, 50, 250, 50);
        fightT1.setVisible(false);
        fightT2.setVisible(false);
        fightT3.setVisible(false);
        evolution.setVisible(false);
        start.addActionListener(this);
        end.addActionListener(this);
        fightT1.addActionListener(this);
        fightT2.addActionListener(this);
        fightT3.addActionListener(this);
        evolution.addActionListener(this);
        // Load Background
        try {
            backgroundImage = ImageIO.read(new File("graphics/background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Adding a panel with an image
        backgroundPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, -20, null);
            }
        };
        backgroundPanel.setLayout(null);
        add(backgroundPanel);
        backgroundPanel.add(fightT1);
        backgroundPanel.add(fightT2);
        backgroundPanel.add(fightT3);
        backgroundPanel.add(evolution);
        backgroundPanel.add(start);
        backgroundPanel.add(end);
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
        spawnEnemyBase();
        spawnPlayerBase();
        baseInfo();
        unitInfo();
    }

    public void spawnPlayerBase() {
        switch (age) {
            case 1 -> playerBase = createBase(100, "graphics/t1_base_gracz.png", 150, 0, 550, 150, 150);
            case 2 -> playerBase = createBase(150, "graphics/t2_base_gracz.png", 300, -25, 500, 200, 300);
            case 3 -> playerBase = createBase(200, "graphics/t3_base_gracz.png", 450, -40, 400, 250, 350);
        }
        if (playerBaseHp != null && playerBase != null) {
            playerBaseHp.setText("Base HP: " + playerBase.hp);
            money.setText("Money: " + playerBase.money);
        }
    }

    public void spawnEnemyBase() {
        switch (enemyAge) {
            case 1 -> enemyBase = createBase(100, "graphics/t1_base_wrog.png", 0, 1050, 550, 150, 150);
            case 2 -> enemyBase = createBase(150, "graphics/t2_base_wrog.png", 0, 1050, 500, 200, 300);
            case 3 -> enemyBase = createBase(200, "graphics/t3_base_wrog.png", 0, 1000, 400, 250, 350);
        }
        if (enemyBaseHp != null && enemyBase != null) {
            enemyBaseHp.setText("Base HP: " + enemyBase.hp
            );
        }
    }

    private Base createBase(int hp, String image, int money, int x, int y, int width, int height) {
        Base base = new Base(hp, image, money);
        base.setBounds(x, y, width, height);
        base.setOpaque(false);
        backgroundPanel.add(base);
        return base;
    }

    private void removeBase(Base base) {
        if (base == null) {
            return;
        }
        backgroundPanel.remove(base);
        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }

    public void baseInfo() {
        money = new JLabel("Money: " + playerBase.money);
        money.setBounds(20, 150, 200, 50);
        backgroundPanel.add(money);
        money.setVisible(false);
        playerBaseHp = new JLabel("Base HP: " + playerBase.hp);
        playerBaseHp.setBounds(20, 120, 200, 50);
        backgroundPanel.add(playerBaseHp);
        playerBaseHp.setVisible(false);
        enemyBaseHp = new JLabel("Base HP: " + enemyBase.hp);
        enemyBaseHp.setBounds(1020, 120, 200, 50);
        backgroundPanel.add(enemyBaseHp);
        enemyBaseHp.setVisible(false);
    }

    public void unitInfo() {
        costT1 = new JLabel("Cost: " + 50);
        costT1.setBounds(15, 0, 100, 50);
        backgroundPanel.add(costT1);
        costT1.setVisible(false);
        costT2 = new JLabel("Cost: " + 100);
        costT2.setBounds(315, 0, 100, 50);
        backgroundPanel.add(costT2);
        costT2.setVisible(false);
        costT3 = new JLabel("Cost: " + 150);
        costT3.setBounds(615, 0, 100, 50);
        backgroundPanel.add(costT3);
        costT3.setVisible(false);
        costE = new JLabel("Cost: " + 400);
        costE.setBounds(915, 0, 100, 50);
        backgroundPanel.add(costE);
        costE.setVisible(false);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == start) {
            startGame();
        } else if (source == end) {
            dispose();
        } else if (source == fightT1) {
            recruitPlayerUnit(1);
        } else if (source == fightT2) {
            recruitPlayerUnit(2);
        } else if (source == fightT3) {
            recruitPlayerUnit(3);
        } else if (source == evolution) {
            evolve();
        }
    }

    private void recruitPlayerUnit(int typ) {
        if (!isPlayerAllowedToRecruit()) {
            return;
        }
        switch (age) {
            case 1 -> {
                switch (typ) {
                    case 1 -> tryRecruitPlayerUnit(5, 50, "graphics/t1_m_gracz.png", 140, 600, 175, 150, 25);
                    case 2 -> tryRecruitPlayerUnit(10, 100, "graphics/t1_w_gracz.png", 140, 575, 175, 150, 25);
                    case 3 -> tryRecruitPlayerUnit(20, 150, "graphics/t1_s_gracz.png", 140, 550, 175, 150, 25);
                }
            }
            case 2 -> {
                switch (typ) {
                    case 1 -> tryRecruitPlayerUnit(10, 100, "graphics/t2_m_gracz.png", 140, 585, 100, 125, 50);
                    case 2 -> tryRecruitPlayerUnit(20, 150, "graphics/t2_w_gracz.png", 140, 585, 100, 125, 50);
                    case 3 -> tryRecruitPlayerUnit(30, 200, "graphics/t2_s_gracz.png", 140, 550, 200, 175, 50);
                }
            }
            case 3 -> {
                switch (typ) {
                    case 1 -> tryRecruitPlayerUnit(20, 150, "graphics/t3_m_gracz.png", 140, 575, 115, 150, 75);
                    case 2 -> tryRecruitPlayerUnit(30, 200, "graphics/t3_w_gracz.png", 140, 575, 90, 150, 75);
                    case 3 -> tryRecruitPlayerUnit(40, 250, "graphics/t3_s_gracz.png", 140, 570, 90, 150, 75);
                }
            }
        }
    }

    private void tryRecruitPlayerUnit(int damage, int cost, String path, int x, int y, int width, int height, int reward) {
        if (playerBase.money < cost) {
            return;
        }
        Unit unit = new Unit(damage, cost, path);
        spawnPlayerUnit(unit, x, y, width, height, reward);
    }

    private void spawnPlayerUnit(Unit unit, int x, int y, int width, int height, int reward) {
        playerLastRecruitment = System.currentTimeMillis();
        playerBase.subtractMoney(unit.cost);
        money.setText("Money: " + playerBase.money);
        unit.setOpaque(false);
        unit.setVisible(true);
        unit.setBounds(x, y, width, height);
        unitList.add(unit);
        backgroundPanel.add(unit);
        Timer timer = createTimerForPlayer(unit, reward);
        timer.start();
    }

    private void startGame() {
        playerLastRecruitment = 0;
        enemyLastRecruitment = 0;
        age = 1;
        enemyAge = 1;
        removeBase(enemyBase);
        spawnEnemyBase();
        removeBase(playerBase);
        spawnPlayerBase();
        playerBase.hp = 100;
        playerBase.money = 150;
        enemyBase.hp = 100;
        updateBaseInfo();
        setInterfaceVisibility();
        gameOver.setVisible(false);
        startGameTimer();
    }

    private void updateBaseInfo() {
        playerBaseHp.setText("Base HP: " + playerBase.hp);
        enemyBaseHp.setText("Base HP: " + enemyBase.hp);
        money.setText("Money: " + playerBase.money);
    }

    private void setInterfaceVisibility() {
        start.setVisible(false);
        end.setVisible(false);
        fightT1.setVisible(true);
        fightT2.setVisible(true);
        fightT3.setVisible(true);
        evolution.setVisible(true);
        playerBaseHp.setVisible(true);
        enemyBaseHp.setVisible(true);
        money.setVisible(true);
        costE.setVisible(true);
        costT1.setVisible(true);
        costT2.setVisible(true);
        costT3.setVisible(true);
    }

    private void evolve() {
        if (age >= 3) {
            return;
        }
        int evolveCost = age == 1 ? 400 : 600;
        if (playerBase.money < evolveCost) {
            return;
        }
        playerBase.subtractMoney(evolveCost);
        money.setText("Money: " + playerBase.money);
        age++;
        removeBase(playerBase);
        ;
        spawnPlayerBase();
        updateCosts();
        if (age == 3) {
            evolution.setVisible(false);
            costE.setVisible(false);
        }
    }

    private void updateCosts() {
        if (age == 2) {
            costT1.setText("Cost: 100");
            costT2.setText("Cost: 150");
            costT3.setText("Cost: 200");
            costE.setText("Cost: 600");

        } else if (age == 3) {
            costT1.setText("Cost: 150");
            costT2.setText("Cost: 200");
            costT3.setText("Cost: 250");
        }
    }

    private void startGameTimer() {
        Random rand = new Random();
        int ageTwoTimerRand = rand.nextInt(20000) + 40000;
        int ageThreeTimerRand = rand.nextInt(80000) + 80000;
        Timer ageTwoTimer = new Timer(ageTwoTimerRand, e -> {
            enemyAge = 2;
            removeBase(enemyBase);
            spawnEnemyBase();
        });
        Timer ageThreeTimer = new Timer(ageThreeTimerRand, e -> {
            enemyAge = 3;
            removeBase(enemyBase);
            spawnEnemyBase();
        });
        ageTwoTimer.setRepeats(false);
        ageThreeTimer.setRepeats(false);
        Timer enemyTimer = new Timer(3800, e -> {
            if (playerBase.hp > 0 && enemyBase.hp > 0) {
                enemyActions();
            }
            gameOver(playerBase, enemyBase, ageTwoTimer, ageThreeTimer);
        });
        Timer cashTimer = new Timer(5000, e -> {
            playerBase.addMoney(100);
            money.setText("Money: " + playerBase.money);
        });
        ageTwoTimer.start();
        ageThreeTimer.start();
        enemyTimer.start();
        cashTimer.start();
    }

    private Timer createTimerForPlayer(Unit unit, int reward) {
        return new Timer(25, e -> {
            unit.move(5, enemyBase, enemyBaseHp, backgroundPanel, enemyUnitList, reward, playerBase, money);
            if (!unit.active) {
                unitList.remove(unit);
                ((Timer) e.getSource()).stop();
            }
        });
    }

    //Enemy actions
    public void enemyActions() {
        if (!isEnemyAllowedToRecruit()) {
            return;
        }
        Random rand = new Random();
        int random = rand.nextInt(5);
        // 3 and 4 means no recruitment
        if (random >= 3) {
            return;
        }
        switch (enemyAge) {
            case 1:
                switch (random) {
                    case 0 -> spawnEnemyUnit(5, 50, "graphics/t1_m_wrog.png", 1000, 600, 175, 150);
                    case 1 -> spawnEnemyUnit(10, 100, "graphics/t1_w_wrog.png", 1000, 575, 175, 150);
                    case 2 -> spawnEnemyUnit(20, 100, "graphics/t1_s_wrog.png", 1000, 550, 175, 150);
                }
                break;

            case 2:
                switch (random) {
                    case 0 -> spawnEnemyUnit(10, 50, "graphics/t2_m_wrog.png", 1000, 575, 175, 150);
                    case 1 -> spawnEnemyUnit(15, 100, "graphics/t2_w_wrog.png", 1000, 575, 175, 150);
                    case 2 -> spawnEnemyUnit(30, 100, "graphics/t2_s_wrog.png", 1000, 550, 240, 175);
                }
                break;

            case 3:
                switch (random) {
                    case 0 -> spawnEnemyUnit(20, 50, "graphics/t3_m_wrog.png", 1000, 575, 115, 150);
                    case 1 -> spawnEnemyUnit(30, 100, "graphics/t3_w_wrog.png", 1000, 575, 90, 150);
                    case 2 -> spawnEnemyUnit(40, 100, "graphics/t3_s_wrog.png", 1000, 570, 90, 150);
                }
                break;
        }
    }

    private void spawnEnemyUnit(int damage, int cost, String path, int x, int y, int width, int height) {
        EnemyUnit enemy = new EnemyUnit(damage, cost, path, playerBaseHp, backgroundPanel);
        enemy.setOpaque(false);
        enemy.setBounds(x, y, width, height);
        enemyUnitList.add(enemy);
        backgroundPanel.add(enemy);
        enemyLastRecruitment = System.currentTimeMillis();
        Timer timer = createTimerForEnemies(enemy);
        timer.start();
    }

    private Timer createTimerForEnemies(EnemyUnit enemyUnit) {
        return new Timer(25, e -> {
            enemyUnit.move(5, playerBase, playerBaseHp, backgroundPanel, unitList);
            if (!enemyUnit.active) {
                enemyUnitList.remove(enemyUnit);
                ((Timer) e.getSource()).stop();
            }
        });
    }

    public void gameOver(Base playerBase, Base enemyBase, Timer timer1, Timer timer2) {
        if (playerBase.hp <= 0) {
            gameOverSettings("Game over, defeat!");
            timer1.restart();
            timer2.restart();
        } else if (enemyBase.hp <= 0) {
            gameOverSettings("Game over, victory!");
            timer1.restart();
            timer2.restart();
        }
    }

    private void gameOverSettings(String message) {
        money.setVisible(false);
        enemyBaseHp.setVisible(false);
        playerBaseHp.setVisible(false);
        fightT2.setVisible(false);
        fightT1.setVisible(false);
        fightT3.setVisible(false);
        evolution.setVisible(false);
        costT1.setVisible(false);
        costT2.setVisible(false);
        costT3.setVisible(false);
        costE.setVisible(false);
        start.setVisible(true);
        end.setVisible(true);
        gameOver.setText(message);
        gameOver.setBounds(520, 0, 250, 250);
        backgroundPanel.add(gameOver);
        gameOver.setVisible(true);
    }

    private boolean isPlayerAllowedToRecruit() {
        long now = System.currentTimeMillis();
        return now - playerLastRecruitment >= RECRUITMENT_COOLDOWN;
    }

    private boolean isEnemyAllowedToRecruit() {
        long now = System.currentTimeMillis();
        return now - enemyLastRecruitment >= RECRUITMENT_COOLDOWN;
    }
}
