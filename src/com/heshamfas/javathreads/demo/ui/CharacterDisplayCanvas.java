package com.heshamfas.javathreads.demo.ui;

import com.heshamfas.javathreads.demo.CharacterEvent;
import com.heshamfas.javathreads.demo.ICharacterListener;
import com.heshamfas.javathreads.demo.ICharacterSource;
import javax.swing.*;
import java.awt.*;

/**
 * Created by 458326 on 4/3/15.
 */
public class CharacterDisplayCanvas extends JComponent implements ICharacterListener {
    protected FontMetrics fm;
    protected char[] tmpChar = new char[1];
    protected int fontHeight;

           //component setting
    public CharacterDisplayCanvas() {
        setFont(new Font("Arial", Font.BOLD, 18));
        fm =getFontMetrics(getFont());
        fontHeight = fm.getHeight();
    }

    public CharacterDisplayCanvas(ICharacterSource cs) {
        this();
        registerCharacterSource(cs);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((fm.getMaxAscent() + 10), (fm.getMaxAdvance() + 10));
    }


    @Override
    protected synchronized void paintComponent(Graphics gc) {
        super.paintComponent(gc);
        Dimension d = getSize();
        gc.clearRect(0, 0, d.width, d.height);
        if (tmpChar[0] == 0) {
            return;
        }
        int charWidth = fm.charWidth((int) tmpChar[0]);
        gc.drawChars(tmpChar, 0, 1, (d.width - charWidth) / 2, fontHeight);
    }

    // program specific methods//

    protected void registerCharacterSource(ICharacterSource cs) {
        cs.addCharacterListener(this);
    }
    //
    @Override
    public void newCharacter(CharacterEvent ce) {
        tmpChar[0] = (char) ce.character;
        repaint();
    }
}
