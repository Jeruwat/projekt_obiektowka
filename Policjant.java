import java.awt.*;
import java.util.Random;

class Policjant extends Drawable {
    String ranga;
    int moc;

    public Policjant(String ranga, int moc, int width, int height, int x, int y, Image image) {
        super(x, y, width, height, image);
        this.ranga = ranga;
        this.moc = moc;
    }

    public void move(int maxWidth, int maxHeight) {
        Random random = new Random();
        int moveX = (random.nextInt(3) - 1) * moc; // Ruch poziomy od -moc do moc
        int moveY = (random.nextInt(3) - 1) * moc; // Ruch pionowy od -moc do moc

        // Sprawdzenie granic ekranu dla ruchu policjanta
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
    }
}