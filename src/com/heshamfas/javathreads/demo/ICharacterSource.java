package com.heshamfas.javathreads.demo;

/**
 * Created by 458326 on 4/3/15.
 */
public interface ICharacterSource {
    public void addCharacterListener(ICharacterListener cl);
    public void removeCharacterListener(ICharacterListener cl);
    public void nextCharacter();
}
