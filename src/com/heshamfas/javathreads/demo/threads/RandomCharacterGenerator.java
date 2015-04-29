package com.heshamfas.javathreads.demo.threads;

import com.heshamfas.javathreads.demo.CharacterEventHandler;
import com.heshamfas.javathreads.demo.CharacterListener;
import com.heshamfas.javathreads.demo.CharacterSource;

import java.util.Random;

/**
 * Created by 458326 on 4/28/15.
 */
public class RandomCharacterGenerator extends Thread implements CharacterSource {

    private static char[] chars;
    private static String charArray = "abcdefghijklmnopqrstuvwxyz0123456789";

    static {
        chars = charArray.toCharArray();
    }

    Random random;
    CharacterEventHandler handler;

    public RandomCharacterGenerator(){
        random = new Random();
        handler = new CharacterEventHandler();
    }

public int getPauseTime(){
    return (int) (Math.max(1000,5000 * random.nextDouble()));
}


 /*CharacterSource interface*/

    @Override
    public void addCharacterListener(CharacterListener cl) {
        handler.addCharacterListner(cl);
    }

    @Override
    public void removeCharacterListener(CharacterListener cl) {
        handler.removeCharacterListener(cl);
    }

    @Override
    public void nextCharacter() {
        handler.fireNewCharacter(this, (int)chars[random.nextInt(chars.length)]);
    }

    public void run(){
        for(;;){
            try {
                Thread.sleep(getPauseTime());
            }catch (InterruptedException ie){
                return;
            }
            }
        }
    }

