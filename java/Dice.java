package pack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

class Dice extends JLabel {
    public Dice() {
        super("Roll Dice", SwingConstants.CENTER);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int roll = new Random().nextInt(6) + 1;
                setText("Dice: " + roll);
            }
        });
    }
}
