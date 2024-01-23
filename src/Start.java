//Boér Erik 3914 gazdinfó1
import javax.swing.*;
public class Start extends JFrame {
    public Start() {
        add(new Board());
        setTitle("League Invaders");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1480,800);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }
    public static void main(String[] args) {
        new Start();
    }
}
