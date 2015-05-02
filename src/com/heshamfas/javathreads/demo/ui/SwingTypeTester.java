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
    private JButton startButton;
    private JButton stopButton;
    private JButton quitButton;
    private CharacterEventHandler handler;

    public SwingTypeTester(){
        initComponents();
    }

    private void initComponents(){
        handler = new CharacterEventHandler();
        /*
        This will set the character source as this SwingType Tester
        Therefore the displayCanvas will be a listener to.
        Random Character Generator.
        This class SwingTypeTester has also FeadbackCanvas
        which will Listen to keypress events which will call the
        event handler to fire a new character event.
        */
        /*displayCanvas = new CharacterDisplayCanvas(this);*/
        displayCanvas = new CharacterDisplayCanvas(); // her
        feedbackCanvas = new CharacterDisplayCanvas(this);
        startButton = new JButton();
        stopButton = new JButton()   ;
        quitButton = new JButton();
        add(displayCanvas, BorderLayout.NORTH);
        add(feedbackCanvas, BorderLayout.CENTER);
        JPanel p = new JPanel();
        startButton.setText("start");
        stopButton.setText("stop");
        quitButton.setText("quit");
        p.add(startButton);
        p.add(stopButton);
        p.add(quitButton);
        add(p, BorderLayout.SOUTH);

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
                if (c != KeyEvent.CHAR_UNDEFINED) {
                    newCharacter((int) c);
                }
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                producer = new RandomCharacterGenerator();
                displayCanvas.registerCharacterSource(producer);
                producer.start();
                startButton.setEnabled(false);
                feedbackCanvas.setEnabled(true);
                feedbackCanvas.requestFocus();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                producer.setDone();
                feedbackCanvas.setEnabled(false);
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
        handler.addCharacterListener(cl);;

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
