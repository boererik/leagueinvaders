public class Bomb extends Character{
    private boolean moveForward;
    private boolean isActive;

    public Bomb(int x, int y, int speed) {
        super(x, y, speed);
        moveForward = false;
        isActive = true;
    }

    public void moveLaser() {
        if (this.getY() > -30) {
            this.setY(this.getY() - this.getSpeed());
        }
    }

    public void setMoveForward(boolean set) {
        this.moveForward = set;
    }

    public boolean isActive () {
        return isActive;
    }

    public void setInActive() {
        this.isActive = false;
    }

}
