import javax.swing.*;
import java.awt.*;
import java.util.Random;

class Papamobil extends Drawable {

    public Papamobil(int x, int y) {
        super(x, y, 100, 80, new ImageIcon("papamobil.png").getImage());
    }

    public void move(int maxWidth, int maxHeight) {
        Random random = new Random();
        int moveX = random.nextInt(11) - 5; // Ruch poziomy od -5 do 5
        int moveY = random.nextInt(11) - 5; // Ruch pionowy od -5 do 5

        if (x + moveX >= 0 && x + moveX <= maxWidth - width) {
            x += moveX;
        }
        if (y + moveY >= 0 && y + moveY <= maxHeight - height) {
            y += moveY;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
        g.setColor(Color.BLACK);
        g.drawString("Papamobil jest Dziełem Bożym", x, y - 10);
    }
}
