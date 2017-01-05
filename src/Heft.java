import javax.swing.*;
import SpriteCtrl.*;
import entitylib.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.BitSet;

public class Heft extends Canvas {
    public int page; // This handles the KeyPress event to determine what a key does on a page;
    public PhysEntity slime0;
    boolean repaintInProgress = false;
    protected HeftKeyListener listener;

    Heft(){
        setIgnoreRepaint(true); // lets me handle the repaint requests
        page = 1; // setting the page to be 1, I assume we will have 4 or more frames: 1. Main Menu 2. About 3. Death 4. Game

        listener = new HeftKeyListener();
        addKeyListener(listener);
        setFocusable(true);

        // We can set the graphics reference to null until I generate the object, alternatively I can also set the reference, it doesn't really matter
        slime0 = new PhysEntity("Slime","C:\\Users\\steve\\OneDrive\\Documents\\Java\\Roguelike\\Sprites\\slime_ss.png",
                68, 2, 70, 135, 10, true, 2, 550, 450, 1100, 900, 0, true); // has only a death animation
        slime0.startAI();
        Chrono chrono = new Chrono(this);
        new Timer(10, chrono).start();
    }

    public class HeftKeyListener implements KeyListener{
        private BitSet keyBits = new BitSet(256);

        @Override
        public void keyTyped(KeyEvent e) {
            // dont care
        }

        @Override
        public void keyPressed(final KeyEvent e) {
            char keyChar = e.getKeyChar();
            keyBits.set(keyChar);
        }

        @Override
        public void keyReleased(final KeyEvent e) {
            char keyChar = e.getKeyChar();
            keyBits.clear(keyChar);
        }

        public boolean isKeyPressed(final char keyChar){
            return keyBits.get(keyChar);
        }
    }

    public void HeftRepaint(){
        // I'm not sure if this is necessary anymore due to how Swing works
        if(repaintInProgress) return; // so to not waste memory

        repaintInProgress = true;

        int indicator = 0;

        if(listener.isKeyPressed('w') || listener.isKeyPressed('W')){
            slime0.moveUp();
            indicator++;
        }
        if(listener.isKeyPressed('a') || listener.isKeyPressed('A')){
            slime0.moveLeft();
            indicator++;
        }
        if(listener.isKeyPressed('s') || listener.isKeyPressed('S')){
            slime0.moveDown();
            indicator++;
        }
        if(listener.isKeyPressed('d') || listener.isKeyPressed('D')){
            slime0.moveRight();
            indicator++;
        }
        if(indicator == 0){
            slime0.setNeutral();
        }

        Dimension size = getSize(); // get size of canvas

        BufferStrategy strategy = getBufferStrategy();
        Graphics graphics = strategy.getDrawGraphics();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, size.width, size.height); // refreshing the screen

        slime0.generate(graphics);

        if(graphics != null) graphics.dispose();

        strategy.show(); // moves to the next buffer

        Toolkit.getDefaultToolkit().sync(); // sync blitter page

        repaintInProgress = false;

    }
}

