import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class DisplayPanel extends JPanel implements ActionListener {
    private int carSpeed = 5;
    private int carCount = 10;
    private int laneHeight = 70;
    private int carHeight = 60;
    private int[] carPositions;
    private Image carImage;
    private cechyAut[] auta;
    private Policjant[] policjanci;
    private Timer timer;
    private Main mainFrame;
    private Papamobil papamobil;
    private int totalBottles = 0;
    private Finanse finanse;

    public DisplayPanel(cechyAut[] auta, int policeCount, Main mainFrame, Finanse finanse) {
        this.auta = auta;
        this.mainFrame = mainFrame;
        this.finanse = finanse;

        carPositions = new int[carCount];
        for (int i = 0; i < carCount; i++) {
            carPositions[i] = -i * 300;
        }

        carImage = new ImageIcon("car.png").getImage();
        policjanci = new Policjant[policeCount];
        Image policeImage = new ImageIcon("police.png").getImage();

        // Inicjalizacja policjantów z określonymi współrzędnymi
        for (int i = 0; i < policeCount; i++) {
            int x = Integer.parseInt(JOptionPane.showInputDialog("Podaj współrzędną x dla Policjant " + (i + 1)));
            int y = Integer.parseInt(JOptionPane.showInputDialog("Podaj współrzędną y dla Policjant " + (i + 1)));
            policjanci[i] = new Policjant("Ranga " + (i + 1), i + 1, policeImage.getWidth(null), policeImage.getHeight(null), x, y, policeImage);
        }

        // Inicjalizacja Papamobila z określonymi współrzędnymi
        int papamobilX = Integer.parseInt(JOptionPane.showInputDialog("Podaj współrzędną x dla Papamobil"));
        int papamobilY = Integer.parseInt(JOptionPane.showInputDialog("Podaj współrzędną y dla Papamobil"));
        papamobil = new Papamobil(papamobilX, papamobilY);

        timer = new Timer(50, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < carCount; i++) {
            carPositions[i] += carSpeed;
            if (carPositions[i] > getWidth()) {
                carPositions[i] = -carImage.getWidth(this);
                totalBottles += auta[i].rokProdukcji; // Dodanie butelek do całkowitej liczby
                finanse.updateTotalEarnings(totalBottles);
            }
        }
        for (Policjant policjant : policjanci) {
            policjant.move(getWidth(), getHeight());
            checkCollision(policjant);
        }
        papamobil.move(getWidth(), getHeight());
        checkPapamobilCollision();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < carCount; i++) {
            g.drawImage(carImage, carPositions[i], laneHeight * (i + 1) - carHeight / 2, this);

            if (i < auta.length) {
                g.setColor(Color.BLACK);
                g.drawString(auta[i].marka + " " + auta[i].numerRejestracyjny, carPositions[i] + 10, laneHeight * (i + 1) - carHeight / 2 - 10);
            }
        }

        for (Policjant policjant : policjanci) {
            policjant.draw(g);
        }

        papamobil.draw(g);

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Łączne zarobki: " + String.format("%.2f", finanse.getCapital()) + " zł", 10, 40);
    }

    private void checkCollision(Policjant policjant) {
        for (int i = 0; i < carCount; i++) {
            if (new Rectangle(policjant.x, policjant.y, policjant.width, policjant.height)
                    .intersects(new Rectangle(carPositions[i], laneHeight * (i + 1) - carHeight / 2, carImage.getWidth(null), carImage.getHeight(null)))) {
                removeCar(i);
                mainFrame.decrementCarCount();
                break;
            }
        }
    }

    private void checkPapamobilCollision() {
        for (int i = 0; i < policjanci.length; i++) {
            if (new Rectangle(papamobil.x, papamobil.y, papamobil.width, papamobil.height)
                    .intersects(new Rectangle(policjanci[i].x, policjanci[i].y, policjanci[i].width, policjanci[i].height))) {
                removePolicjant(i);
                break;
            }
        }
    }

    public void removeCar(int index) {
        int[] newCarPositions = new int[carCount - 1];
        cechyAut[] newAuta = new cechyAut[carCount - 1];
        for (int i = 0, j = 0; i < carCount; i++) {
            if (i != index) {
                newCarPositions[j] = carPositions[i];
                newAuta[j] = auta[i];
                j++;
            }
        }
        carCount--;
        carPositions = newCarPositions;
        auta = newAuta;
    }

    public void removePolicjant(int index) {
        Policjant[] newPolicjanci = new Policjant[policjanci.length - 1];
        for (int i = 0, j = 0; i < policjanci.length; i++) {
            if (i != index) {
                newPolicjanci[j] = policjanci[i];
                j++;
            }
        }
        policjanci = newPolicjanci;
    }

    public void calculateTotalBottles() {
        totalBottles = 0;
        for (int i = 0; i < carCount; i++) {
            totalBottles += auta[i].rokProdukcji;
        }
    }

    public int getTotalBottles() {
        return totalBottles;
    }
}