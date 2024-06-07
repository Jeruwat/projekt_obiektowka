import javax.swing.*;
import java.awt.*;
import java.util.Random;

class cechyAut extends Drawable {
    String marka;
    String numerRejestracyjny;
    int rokProdukcji;
    boolean isWorking;

    public cechyAut() {
        super(0, 0, 50, 20, new ImageIcon("car.png").getImage());
        updateData();
    }

    public void updateData() {
        Random random = new Random();
        String[] availableBrands = {"Toyota", "Ford", "BMW", "Audi", "Mercedes"};
        this.marka = availableBrands[random.nextInt(availableBrands.length)];

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            builder.append((char) (random.nextInt(26) + 'A'));
        }
        builder.append(" ");
        for (int i = 0; i < 4; i++) {
            builder.append(random.nextInt(10));
        }
        this.numerRejestracyjny = builder.toString();

        this.rokProdukcji = random.nextInt(1000);
        this.isWorking = random.nextBoolean();
        this.x = random.nextInt(800);
        this.y = random.nextInt(600);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString(marka + " " + numerRejestracyjny, x + 10, y - 10);
    }
}
