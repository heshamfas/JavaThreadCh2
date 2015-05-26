package com.heshamfas.javathreads.demo.ui;

import com.heshamfas.javathreads.demo.CharacterEvent;
import com.heshamfas.javathreads.demo.ICharacterListener;
import com.heshamfas.javathreads.demo.ICharacterSource;

import javax.swing.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by root on 5/26/15.
 */
public class ScoreLabel extends JLabel implements ICharacterListener {

    private volatile int score =0;
    private int char2Type = -1;
    private ICharacterSource generator = null, typist = null;
    private Lock scoreLock = new ReentrantLock();

    public ScoreLabel (ICharacterSource generator, ICharacterSource typist){
        this.generator= generator;
        this.typist = typist;

        if(generator!=null){
            generator.addCharacterListener(this);
        }
        if(typist!=null){
            typist.addCharacterListener(this);
        }
    }
    public ScoreLabel(){
        this(null,null);
    }

    public synchronized void resetGenerator(ICharacterSource newGenerator){
        try{
            scoreLock.lock();
            if(generator!=null){
            generator.removeCharacterListener(this);
            generator = newGenerator;
            if(generator!= null){
                generator.addCharacterListener(this);
            }
        }
        }finally {
            scoreLock.unlock();
        }
    }

    public  void resetTypist(ICharacterSource newTypist){
        try{

            scoreLock.lock();

        if(typist!=null){
            typist.removeCharacterListener(this);
            typist = newTypist;
            if(typist!= null){
                typist.addCharacterListener(this);
            }
        }
        }finally {
            scoreLock.unlock();
        }
    }

    public  void setScore() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setText(Integer.toString(score));
            }
        });
    }


    @Override
    public void newCharacter(CharacterEvent ce) {
        //previous character not typed correctly: 1- point penalty
     /*   try{
            scoreLock.lock();*/
        if(ce.source == generator){
            synchronized (this) {
                if (char2Type != -1) {
                    score--;
                    setScore();
                }
                char2Type = ce.character;
            }
        }
        //if character is extraneous: 1- point penalty
        // if character ddoes not match: 1- point penalty
        else {
            synchronized (this) {
                if (char2Type != ce.character) {
                    score--;
                } else {
                    score++;
                    char2Type = -1;
                }
                setScore();
            }
        }
 /*   }finally {
            scoreLock.unlock();
        }*/
    }

}
