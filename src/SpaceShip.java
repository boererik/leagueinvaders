import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpaceShip extends Character{
    private boolean moveR;
    private boolean moveL;
    private boolean isDestroyed;

    public SpaceShip(int x, int y, int speed) {
        super(x,y,speed);
        moveL = false;
        moveR = false;
        isDestroyed = false;
        BufferedImage pic;
        try {
            pic = ImageIO.read(this.getClass().getResource("Pics/ziggs.png"));
            super.setImg(pic);
        } catch (IOException e) {
            System.out.println("Image could not be read");
            System.exit(1);
        }
    }

    public void moveSpaceShip() {
        if (this.moveR){
            this.setX(this.getX() + this.getSpeed());
        }
        if (this.moveL){
            this.setX(this.getX() - this.getSpeed());
        }
    }

    public void setDestroyed() {
        isDestroyed = true;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public boolean getMoveR() {
        return moveR;
    }

    public boolean getMoveL() {
        return moveR;
    }

    public void setMoveR(boolean mv) {
        this.moveR = mv;
    }

    public void setMoveL(boolean mv) {
        this.moveL = mv;
    }
}
