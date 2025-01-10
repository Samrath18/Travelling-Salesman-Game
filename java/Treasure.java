package pack;

import java.awt.*;

class Treasure extends Building {
    private String name;
    private int value;

    public Treasure(Point position, String name, int value) {
        super(Color.GREEN, position);
        this.name = name;
        this.value = value;
        this.visible = false; // This building is hidden until discovered

    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
