package com.heshamfas.javathreads.demo.ui;

import com.heshamfas.javathreads.demo.CharacterEvent;
import com.heshamfas.javathreads.demo.ICharacterListener;
import com.heshamfas.javathreads.demo.ICharacterSource;

import javax.swing.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


// Created by Hesham on 5/24/2015.

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


    public void resetGenerator(ICharacterSource newGenerator){
        try {
            scoreLock.lock();
            if (generator != null) {
                generator.removeCharacterListener(this);
                generator = newGenerator;
                if (generator != null) {
                    generator.addCharacterListener(this);
                }
            }
        }finally {
            scoreLock.unlock();
        }
    }

    public synchronized void resetTypist(ICharacterSource newTypist){
        try
        {
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
    public void resetScore(){
        try{
            scoreLock.lock();
            score = 0;
            char2Type = -1;
            setScore();
        }finally {
            scoreLock.unlock();
        }
    }

    public synchronized void setScore() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
               setText("score is " + Integer.toString(score));
            }
        });
    }
    @Override
    public  void newCharacter(CharacterEvent ce) {
        try{

            scoreLock.lock();

        //previous character not typed correctly: 1- point penalty
        if(ce.source == generator){
            if(char2Type!=-1){
                System.out.println("char2Type is: " + char2Type);
                score--;
                setScore();
            }
            char2Type = ce.character;

        }
        //if character is extraneous: 1- point penalty
        // if character ddoes not match: 1- point penalty
        else {
            System.out.println("typed character is: " + char2Type);
            if(char2Type != ce.character){
                score--;
            }else {
                score++;
                char2Type= -1;
            }
            setScore();
        }

    }finally {
            scoreLock.unlock();
        }
    }

    }
