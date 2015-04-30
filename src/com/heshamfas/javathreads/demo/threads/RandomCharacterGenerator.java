package com.heshamfas.javathreads.demo.threads;

import com.heshamfas.javathreads.demo.CharacterEventHandler;
import com.heshamfas.javathreads.demo.ICharacterListener;
import com.heshamfas.javathreads.demo.ICharacterSource;

import java.util.Random;

/**
 * Created by 458326 on 4/28/15.
 */
public class RandomCharacterGenerator extends Thread implements ICharacterSource {

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


 /*ICharacterSource interface*/

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
        handler.fireNewCharacter(this, (int)chars[random.nextInt(chars.length)]);
    }

    @Override
    public void run() {
        super.run();
        for(;;){
            nextCharacter();
            try {
                Thread.sleep(getPauseTime());
            }catch (InterruptedException ie){
                return;
            }
        }
    }

    }

