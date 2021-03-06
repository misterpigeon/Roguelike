
/**
 * Created by User on 2016-12-25.
 */
import java.awt.*;
import java.io.*;
import java.net.URL;

import entitylib.*;

import javax.swing.*;

public class GameFrame extends JFrame{
    GameFrame(){
        super("Heft");
        Heft heft = new Heft();
        heft.setVisible(true);
        add(heft, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100,900);
        setVisible(true);
        heft.createBufferStrategy(2);
        setResizable(false);
    }

    public static void main(String [] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GameFrame();
            }
        });
    }
}

