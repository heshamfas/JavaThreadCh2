package com.heshamfas.javathreads.demo.ui;

import com.heshamfas.javathreads.demo.CharacterEventHandler;
import com.heshamfas.javathreads.demo.ICharacterListener;
import com.heshamfas.javathreads.demo.ICharacterSource;
import com.heshamfas.javathreads.demo.threads.RandomCharacterGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by 458326 on 4/28/15.
 */
public class SwingTypeTester extends JFrame implements ICharacterSource {

    protected RandomCharacterGenerator producer;
    private CharacterDisplayCanvas displayCanvas;
    private CharacterDisplayCanvas feedbackCanvas;
    private JButton quitButton;
    private JButton startButton;
    private CharacterEventHandler handler;

    public SwingTypeTester(){
        initComponents();
    }

    private void initComponents(){
        handler = new CharacterEventHandler();
        displayCanvas = new CharacterDisplayCanvas(this);
        feedbackCanvas = new CharacterDisplayCanvas(this);
        quitButton = new JButton();
        startButton = new JButton();
        add(displayCanvas, BorderLayout.NORTH);
        JPanel p = new JPanel();
        startButton.setText("start");
        quitButton.setText("quit");
        p.add(startButton);
        p.add(quitButton);
        add(p,BorderLayout.SOUTH);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                    quit();

            }
        });


        feedbackCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                char c = e.getKeyChar();
                if(c != KeyEvent.CHAR_UNDEFINED){
                    newCharacter((int) c);
                }
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                producer = new RandomCharacterGenerator();
                displayCanvas.setCharacterSource(producer);
                producer.start();
                startButton.setEnabled(false);
                feedbackCanvas.setEnabled(true);
                feedbackCanvas.requestFocus();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });

        pack();
    }

    private void quit(){
        System.exit(0);
    }

    public void newCharacter(int c){
        handler.fireNewCharacter(this, c);
        //
    }


    /*ICharacterSource */
    @Override
    public void addCharacterListener(ICharacterListener cl) {
        handler.addCharacterListner(cl);

    }

    @Override
    public void removeCharacterListener(ICharacterListener cl) {
            handler.removeCharacterListener(cl);
    }

    @Override
    public void nextCharacter() {
        throw new IllegalStateException("We don't produce on demand");
    }


}
