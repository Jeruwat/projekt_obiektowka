import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Main extends JFrame {

    private Timer carCounterTimer;
    private Timer loggingTimer;
    private int carCounter = 10; // Początkowa liczba aut
    private final int carCounterIncrement = 10; // Liczba aut do dodania co 30 sekund
    private JLabel carCountLabel;
    private JLabel[] warehouseLabels;
    private DisplayPanel displayPanel;
    private cechyAut[] auta;
    private Finanse finanse; // Nowa klasa do obsługi finansów
    private FileLogger fileLogger;

    public Main(cechyAut[] auta) {
        this.auta = auta;

        setTitle("Jeżdżące auta");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Ustawienie na pełny ekran
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        finanse = new Finanse();
        fileLogger = new FileLogger("simulation_log.txt"); // Initialize the FileLogger

        // Panel do wyświetlania aut i policjantów
        displayPanel = new DisplayPanel(auta, 5, this, finanse);

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
            int cost = generateRandomNumber(1000, 10000);
            warehouseLabels[i] = new JLabel(getWarehouseName(i) + ": " + cost);
            finanse.setWarehouseCost(i, cost);
            warehouseLabels[i].setForeground(Color.WHITE);
            warehouseLabels[i].setFont(new Font("Arial", Font.BOLD, 16));
            panel.add(warehouseLabels[i]);
        }

        getContentPane().add(displayPanel, BorderLayout.CENTER);

        // Ustawienie timera do zwiększania liczby aut co 30 sekund
        carCounterTimer = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carCounter += carCounterIncrement;
                carCountLabel.setText("Liczba aut: " + carCounter);
                // Zaktualizuj liczby w magazynach
                for (int i = 0; i < warehouseLabels.length; i++) {
                    int cost = generateRandomNumber(1000, 10000);
                    warehouseLabels[i].setText(getWarehouseName(i) + ": " + cost);
                    finanse.setWarehouseCost(i, cost);
                }
                // Zaktualizuj dane dla każdego auta
                for (cechyAut auto : auta) {
                    auto.updateData();
                }
                finanse.deductCarGenerationCost(carCounterIncrement); // Odejmowanie kosztów generowania aut
                displayPanel.repaint();
            }
        });
        carCounterTimer.start();

        // Ustawienie timera do zmiany ceny za butelkę co 30 sekund
        Timer priceChangeTimer = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Losowo zmień cenę za butelkę
                Random random = new Random();
                finanse.setBottlePrice(1 + random.nextDouble() * 4); // Cena od 1 do 5
                // Wyświetl nową cenę
                JOptionPane.showMessageDialog(null, "Nowa cena za butelkę: " + String.format("%.2f", finanse.getBottlePrice()) + " zł");
                finanse.updateTotalEarnings(displayPanel.getTotalBottles());
                displayPanel.repaint();
            }
        });
        priceChangeTimer.start();

        // Ustawienie timera do odejmowania kosztów produkcji co 30 sekund
        Timer costDeductionTimer = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finanse.deductProductionCosts();
                displayPanel.repaint();
            }
        });
        costDeductionTimer.start();

        // Ustawienie timera do logowania danych co 30 sekund
        loggingTimer = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logData();
            }
        });
        loggingTimer.start();
    }

    private void logData() {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Liczba aut: ").append(carCounter).append("\n");
        for (int i = 0; i < warehouseLabels.length; i++) {
            logMessage.append(getWarehouseName(i)).append(": ").append(finanse.getWarehouseCost(i)).append("\n");
        }
        logMessage.append("Cena za butelkę: ").append(finanse.getBottlePrice()).append("\n");
        logMessage.append("Łączne zarobki: ").append(finanse.getTotalEarnings()).append("\n");
        logMessage.append("Kapitał: ").append(finanse.getCapital()).append("\n");

        fileLogger.log(logMessage.toString());
    }

    public void decrementCarCount() {
        carCounter--;
        carCountLabel.setText("Liczba aut: " + carCounter);
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
