package pack;

import java.awt.*;

class Trap extends Building {
    public Trap(Point position) {
        super(Color.RED, position);
        this.visible = false; // This building is hidden until discovered
    }
}
