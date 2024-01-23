public class Enemy extends Character{
    private boolean moveR;
    private boolean moveL;
    private boolean isVisible;

    public Enemy(int x, int y, int speed) {
        super(x,y,speed);
        moveL = false;
        moveR = true;
        isVisible = true;
    }

    public void setMoveR(boolean moveR) {
        this.moveR = moveR;
    }

    public void setMoveL(boolean moveL) {
        this.moveL = moveL;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean getMoveR() {
        return moveR;
    }

    public boolean getMoveL() {
        return moveL;
    }

    public boolean isVisible() {
        return isVisible;
    }
}
