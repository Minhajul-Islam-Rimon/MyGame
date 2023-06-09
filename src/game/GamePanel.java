package game;

import javax.swing.JPanel;

import entities.Enemy;
import entities.Entity;
import entities.Player;
import entities.Projectile;
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import ui.UiPlane;
import utils.Taglist;
import utils.Vector2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.ListIterator;

public class GamePanel extends JPanel {

    int x = 200, y = 200;
    public boolean isPaused = false;
    public Player player;
    public Vector2 currentMousePos = new Vector2(0, 0);
    public ArrayList<Entity> entityList = new ArrayList<Entity>();
    public ArrayList<Entity> listBuffer = new ArrayList<Entity>();
    public UiPlane uiPlane;

    public GamePanel() {
        addKeyListener(new KeyboardInputs(this));
        addMouseMotionListener(new MouseInputs(this));
        uiPlane = new UiPlane(this);
        player = new Player(new Vector2(500, 500), 75 , Color.BLACK, this);
        entityList.add(player);
        entityList.add(new Enemy(new Vector2(100, 100), 20, Color.blue, this));


    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2d = (Graphics2D)graphics;
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        update(graphics);
    }




    public void checkCollision(Entity a) {

        for(int i = 0; i < entityList.size(); i++) {
            Entity current = entityList.get(i);
            if(a != current)
                if(Vector2.sqDistance(a.pos, current.pos) <= Math.pow(a.radius + current.radius, 2)) {
                    a.onCollisionEnter(current);
                }
        }

    }

    public void update(Graphics graphics) {
        if(listBuffer.size() != 0) {
            for(int i = 0; i < listBuffer.size(); i++ ) {
                entityList.add(listBuffer.get(i));
            }
            listBuffer.clear();
        }
        checkDeath();
        player.setClosestEnemy(entityList);
        for(int i = 0; i < entityList.size(); i++) {
            Entity current = entityList.get(i);
            if(!isPaused)
                current.update();
            checkCollision(current);
            current.render(graphics);
        }
        uiPlane.update();
        uiPlane.render(graphics);
    }

    public void checkDeath() {
        if(player.stats.currentHealth <= 0)
            isPaused = true;
    }
    public void instantiate(Entity object) {
        listBuffer.add(object);
        // entityList.add(object);
    }

    public void destroy(Entity object) {
        entityList.remove(object);
    }

    public void spawnEnemy() {
        float x = (float) Math.random() * 1700;
        float y = (float) Math.random() * 1000;
        Vector2 newPos = new Vector2(x,y);
        entityList.add(new Enemy(newPos, 30, getBackground(), this));
    }

    public void reset() {
        for(Entity x: entityList) {
            if(x.tagName == Taglist.enemy)
                entityList.remove(x);
        }
        player.pos = new Vector2(500, 500);
        player.stats.fullHeal();

    }


}



