package com.heshamfas.javathreads.demo;

/**
 * Created by 458326 on 4/3/15.
 */
public class CharacterEvent {
    public ICharacterSource source;
    public int character;

    public CharacterEvent(ICharacterSource cs, int c){
        source = cs;
        character = c;

    }
}
