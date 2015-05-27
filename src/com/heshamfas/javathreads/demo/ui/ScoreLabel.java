package com.heshamfas.javathreads.demo.ui;

import com.heshamfas.javathreads.demo.CharacterEvent;
import com.heshamfas.javathreads.demo.ICharacterListener;
import com.heshamfas.javathreads.demo.ICharacterSource;

import javax.swing.*;


// Created by Hesham on 5/24/2015.

public class ScoreLabel extends JLabel implements ICharacterListener {

    private volatile int score =0;
    private int char2Type = -1;
    private ICharacterSource generator = null, typist = null;

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
        if(generator!=null){
            generator.removeCharacterListener(this);
            generator = newGenerator;
            if(generator!= null){
                generator.addCharacterListener(this);
            }
        }
    }

    public synchronized void resetTypist(ICharacterSource newTypist){
        if(typist!=null){
            typist.removeCharacterListener(this);
            typist = newTypist;
            if(typist!= null){
                typist.addCharacterListener(this);
            }
        }
    }

    public synchronized void setScore() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                setText(Integer.toString(score));
            }
        });
    }
    @Override
    public synchronized void newCharacter(CharacterEvent ce) {
        //previous character not typed correctly: 1- point penalty
        if(ce.source == generator){
            if(char2Type!=-1){
                score--;
                setScore();
            }
            char2Type = ce.character;

        }
        //if character is extraneous: 1- point penalty
        // if character ddoes not match: 1- point penalty
        else {
            if(char2Type != ce.character){
                score--;
            }else {
                score++;
                char2Type= -1;
            }
            setScore();
        }

    }
    }
