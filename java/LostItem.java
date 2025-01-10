package pack;

import java.awt.*;

class LostItem extends Building {
    private boolean hasGivenMoney = false;

    public LostItem(Point position) {
        super(Color.BLUE, position);
        this.visible = false; // This building is hidden until discovered
    }

    public boolean hasGivenMoney() {
        return hasGivenMoney;
    }

    public void giveMoney() {
        this.hasGivenMoney = true;
    }
}
