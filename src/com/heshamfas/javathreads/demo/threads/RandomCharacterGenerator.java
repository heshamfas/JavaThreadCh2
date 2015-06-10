package com.heshamfas.javathreads.demo.threads;

import com.heshamfas.javathreads.demo.CharacterEventHandler;
import com.heshamfas.javathreads.demo.ICharacterListener;
import com.heshamfas.javathreads.demo.ICharacterSource;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by 458326 on 4/28/15.
 */
public class RandomCharacterGenerator extends Thread implements ICharacterSource {

    private static char[] chars;
    private volatile boolean done = true;
    private static String charArray = "abcdefghijklmnopqrstuvwxyz0123456789";
    static {
        chars = charArray.toCharArray();
    }

    Random random;
    CharacterEventHandler handler;
    private Lock lock = new ReentrantLock();
    private Condition cv = lock.newCondition();

    public RandomCharacterGenerator(){
        random = new Random();
        handler = new CharacterEventHandler();
        this.setName("RandomCharacterGenerator");
    }

public int getPauseTime(){
    return (int) (Math.max(1000,5000 * random.nextDouble()));
}


 /*ICharacterSource interface*/

    @Override
    public void addCharacterListener(ICharacterListener cl) {
        handler.addCharacterListener(cl);
    }

    @Override
    public void removeCharacterListener(ICharacterListener cl) {
        handler.removeCharacterListener(cl);
    }

    @Override
    public void nextCharacter() {
        char aChar= chars[random.nextInt(chars.length)];
        handler.fireNewCharacter(this, (int)aChar);
        System.out.println("character is: " + aChar);

    }

    public void setDone(boolean done){
        try{
            lock.lock();
            this.done = done;
            if(!done){
                cv.signal();
            }
        }finally {
            lock.unlock();
        }

    }
    @Override
    public void run() {
        super.run();
        try {
        lock.lock();
            System.out.println("inside character generator and done is : " + done);
            while (!done) {
                try {
                    if(done){
                        cv.await();
                    }else {
                        nextCharacter();
                        cv.await(getPauseTime(), TimeUnit.MILLISECONDS);
                    }

                } catch (InterruptedException ie) {
                    return;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    }

