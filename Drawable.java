import java.awt.*;

abstract class Drawable {
    int x, y;
    int width, height;
    Image image;

    public Drawable(int x, int y, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public abstract void draw(Graphics g);
}
