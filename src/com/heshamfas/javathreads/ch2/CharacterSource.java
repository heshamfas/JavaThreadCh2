package com.heshamfas.javathreads.ch2;

/**
 * Created by 458326 on 4/3/15.
 */
public interface CharacterSource {
    public void addCharacterListener(CharacterListener cl);
    public void removeCharacterListener(CharacterListener cl);
    public void nextCharacter();
}
