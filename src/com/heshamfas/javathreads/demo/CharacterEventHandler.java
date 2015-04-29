package com.heshamfas.javathreads.demo;
import java.util.*;

/**
 * Created by 458326 on 4/3/15.
 *  helper class that fires the events when appropriate:
 */
public class CharacterEventHandler {
    private Vector listeners = new Vector();// note here that the victor doesn't hava any type

    public void addCharacterListner(CharacterListener cl){
        listeners.add(cl);

    }
    public void removeCharacterListener(CharacterListener cl){
        listeners.remove(cl);
    }

    public void fireNewCharacter(CharacterSource source, int c) {
        //firing the event
        CharacterEvent ce = new CharacterEvent(source, c);
        //notifying all listeners
        CharacterListener[] cl = (CharacterListener[]) listeners.toArray(new CharacterListener[0]);// converting to array and specifying type

        for (int i = 0; i < cl.length; i++) {
            cl[i].newCharacter(ce);
        }
    }
  /*
   http://stackoverflow.com/questions/174093/toarraynew-myclass0-or-toarraynew-myclassmylist-size

   As of ArrayList in Java 5, the array will be filled already if it has the right size (or is bigger). Consequently

    MyClass[] arr = myList.toArray(new MyClass[myList.size()]);
    will create one array object, fill it and return it to "arr". On the other hand

    MyClass[] arr = myList.toArray(new MyClass[0]);
    will create two arrays. The second one is an array of MyClass with length 0. So there is an object creation for an object that will be thrown away immediately. As far as the source code suggests the compiler / JIT cannot optimize this one so that it is not created. Additionally, using the zero-length object results in casting(s) within the toArray() - method.

    See the source of ArrayList.toArray():

    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }
    Use the first method so that only one object is created and avoid (implicit but nevertheless expensive) castings.

*/

}
