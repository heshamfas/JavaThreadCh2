package com.heshamfas.javathreads.demo.ui;

import com.heshamfas.javathreads.demo.CharacterEvent;
import com.heshamfas.javathreads.demo.ICharacterListener;
import com.heshamfas.javathreads.demo.ICharacterSource;

import javax.swing.*;

/**
 * Created by Hesham on 5/24/2015.
 */
public class ScoreLabel extends JLabel implements ICharacterListener {
    private volatile int score=0;
    private int char2Type = -1;
    private ICharacterSource generator = null, typist = null;

    public ScoreLabel(ICharacterSource generator, ICharacterSource typist){
        this.generator = generator;
        this.typist = typist;

        if(generator != null){
           generator.addCharacterListener(this);
        }
        if(typist!=null){
          typist.addCharacterListener(this);
        }
    }
    public ScoreLabel(){this(null,null);}

    public synchronized void resetGenerator(ICharacterSource newGenerator){
        if(generator!=null){
            generator.removeCharacterListener(this);
            generator = newGenerator;
            if(generator != null){
                generator.addCharacterListener(this);
            }
        }
    }
    public synchronized void resetScore(){
        score =0;
        char2Type = -1;
        setScore();
    }

    private synchronized void setScore(){
      //this method will be explained later in chapter 7
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setText(Integer.toString(score));
            }
        });
    }
    @Override
    public void newCharacter(CharacterEvent ce) {
        //previous character not typed correctly : 1 - point penalty
        if(ce.source == generator){
            if(char2Type != -1){// the user didn't type any characters in the time allowed
                score--;
                setScore();
            }
            char2Type = ce.character;
            //If character is extraneous: 1- point penalty
            // If Character doesn't match 1- point penalty
        }else {
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
