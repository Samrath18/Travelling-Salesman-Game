package pack;

import java.awt.*;

class Building {
    Color color;
    Point position;
    boolean visible = true; // Assume all buildings are visible by default for simplicity

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Building(Color color, Point position) {
        this.color = color;
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public Point getPosition() {
        return position;
    }
}
