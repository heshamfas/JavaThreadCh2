package com.heshamfas.javathreads.demo.ui;

import com.heshamfas.javathreads.demo.CharacterEvent;
import com.heshamfas.javathreads.demo.ICharacterListener;
import com.heshamfas.javathreads.demo.ICharacterSource;

import java.awt.*;

/**
 * Created by Hesham on 5/19/2015.
 */
public class AnimatedCharacterDisplayCanvas  extends CharacterDisplayCanvas implements ICharacterListener, Runnable{
    private volatile boolean done = false;
    private int curX= 0 ;

    public AnimatedCharacterDisplayCanvas(ICharacterSource cs){
        super(cs);
    }

    @Override
    public synchronized void newCharacter (CharacterEvent characterEvent){
        curX = 0;
        tmpChar[0] = (char) characterEvent.character;
        repaint();
    }

    @Override
    protected synchronized void paintComponent(Graphics gc){
        Dimension d = getSize();
        gc.clearRect(0, 0, d.width, d.height);
        if(tmpChar[0]==0){
            return;
        }
        int charWidth = fm.charWidth(tmpChar[0]);
        gc.drawChars(tmpChar, 0,1, curX++, fontHeight);
    }
    public void setDone(boolean b){
        done = b;
    }
    @Override
    public void run() {
        while (!done){
            repaint();
            try{
                Thread.sleep(100);
            }catch (InterruptedException ie){
                return;
            }
        }

    }
}
