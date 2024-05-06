import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Main extends JFrame implements ActionListener {

    private Timer timer;
    private Timer carCounterTimer;
    private int[] carPositions;
    private int carSpeed = 5;
    private int carCount = 10;
    private int laneHeight = 70;
    private int carHeight = 60;
    private Image carImage;
    private Image policeImage;
    private cechyAut[] auta;
    private JLabel carCountLabel;
    private JLabel[] warehouseLabels;

    private int carCounter = 10; // Początkowa liczba aut
    private final int carCounterIncrement = 10; // Liczba aut do dodania co 30 sekund
    private int policeX; // Pozycja policjanta pozioma
    private int policeY; // Pozycja policjanta pionowa
    private int policeSpeed = 6; // Prędkość policjanta

    public Main(cechyAut[] auta) {
        this.auta = auta;

        setTitle("Jeżdżące auta");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Ustawienie na pełny ekran
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        carPositions = new int[carCount];
        for (int i = 0; i < carCount; i++) {
            carPositions[i] = -i * 300;
        }

        carImage = new ImageIcon("car.png").getImage();
        policeImage = new ImageIcon("police.png").getImage();

        timer = new Timer(50, this);
        timer.start();

        // Dodanie etykiety na liczbę aut
        carCountLabel = new JLabel("Liczba aut: " + carCounter);
        carCountLabel.setForeground(Color.WHITE);
        carCountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel panel = new JPanel();
        panel.setBackground(new Color(135, 206, 250)); // Jasno niebieski kolor tła
        panel.add(carCountLabel);
        getContentPane().add(panel, BorderLayout.NORTH);

        // Dodanie etykiet na liczby w magazynach
        warehouseLabels = new JLabel[3];
        for (int i = 0; i < warehouseLabels.length; i++) {
            warehouseLabels[i] = new JLabel(getWarehouseName(i) + ": " + generateRandomNumber(1000, 10000));
            warehouseLabels[i].setForeground(Color.WHITE);
            warehouseLabels[i].setFont(new Font("Arial", Font.BOLD, 16));
            panel.add(warehouseLabels[i]);
        }

        // Ustawienie timera do zwiększania liczby aut co 30 sekund
        carCounterTimer = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carCounter += carCounterIncrement;
                carCountLabel.setText("Liczba aut: " + carCounter);
                // Zaktualizuj liczby w magazynach
                for (int i = 0; i < warehouseLabels.length; i++) {
                    warehouseLabels[i].setText(getWarehouseName(i) + ": " + generateRandomNumber(1000, 10000));
                }
                // Zaktualizuj dane dla każdego auta
                for (cechyAut auto : auta) {
                    auto.updateData();
                }
                repaint();
            }
        });
        carCounterTimer.start();

        // Ustawienie pozycji początkowej policjanta na środku ekranu
        policeX = getWidth()+500;
        policeY = getHeight()+500;

        // Ustawienie timera do poruszania się policjanta co 200 ms
        Timer policeTimer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movePolice();
                checkCollisions();
                repaint();
            }
        });
        policeTimer.start();
    }

    // Metoda do poruszania się policjanta
    private void movePolice() {
        Random random = new Random();
        int moveX = random.nextInt(3) - 1; // Losowy ruch poziomy od -1 do 1
        int moveY = random.nextInt(3) - 1; // Losowy ruch pionowy od -1 do 1

        // Sprawdzenie granic ekranu dla ruchu policjanta
        if (policeX + moveX >= 0 && policeX + moveX <= getWidth() - policeImage.getWidth(null)) {
            policeX += moveX * policeSpeed;
        }
        if (policeY + moveY >= 0 && policeY + moveY <= getHeight() - policeImage.getHeight(null)) {
            policeY += moveY * policeSpeed;
        }
    }

    // Metoda do sprawdzania kolizji z autami
    private void checkCollisions() {
        Rectangle policeRect = new Rectangle(policeX, policeY, policeImage.getWidth(null), policeImage.getHeight(null));

        for (int i = 0; i < carCount; i++) {
            Rectangle carRect = new Rectangle(carPositions[i], laneHeight * (i + 1) - carHeight / 2, carImage.getWidth(null), carImage.getHeight(null));

            if (policeRect.intersects(carRect)) {
                // Jeśli jest kolizja, usuń auto i zmniejsz liczbę aut
                for (int j = i; j < carCount - 1; j++) {
                    carPositions[j] = carPositions[j + 1];
                }
                carCount--;

                // Zaktualizuj etykietę z liczbą aut
                carCounter--;
                carCountLabel.setText("Liczba aut: " + carCounter);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < carCount; i++) {
            carPositions[i] += carSpeed;
            if (carPositions[i] > getWidth()) {
                carPositions[i] = -carImage.getWidth(this);
            }
        }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < carCount; i++) {
            g.drawImage(carImage, carPositions[i], laneHeight * (i + 1) - carHeight / 2, this);

            // Rysuj informacje o samochodzie nad pasem
            if (i < auta.length) {
                g.setColor(Color.BLACK);
                g.drawString(auta[i].marka + " " + auta[i].numerRejestracyjny, carPositions[i] + 10, laneHeight * (i + 1) - carHeight / 2 - 10);
            }
        }

        // Rysowanie policjanta
        g.drawImage(policeImage, policeX, policeY, this);
    }

    private String getWarehouseName(int index) {
        switch (index) {
            case 0:
                return "Warszawa";
            case 1:
                return "Gdańsk";
            case 2:
                return "Wrocław";
            default:
                return "";
        }
    }

    private int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static void main(String[] args) {
        cechyAut[] auta = new cechyAut[10];
        for (int i = 0; i < auta.length; i++) {
            auta[i] = new cechyAut();
        }

        SwingUtilities.invokeLater(() -> {
            Main main = new Main(auta);
            main.setVisible(true);
        });
    }
}

class cechyAut {
    String marka;
    String numerRejestracyjny;
    int rokProdukcji;
    boolean isWorking;

    public cechyAut() {
        updateData();
    }

    // Metoda do aktualizacji danych dla auta
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

        this.rokProdukcji = random.nextInt(23) + 2000;
        this.isWorking = random.nextBoolean();
    }
}
