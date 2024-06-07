class Finanse {
    private double bottlePrice = 2.5; // Cena za 1 butelkę
    private double totalEarnings = 0; // Łączne zarobki
    private double[] warehouseCosts = new double[3]; // Koszty produkcji dla magazynów
    private double initialCapital = 1000000; // Początkowy kapitał firmy
    private double capital = initialCapital; // Aktualny kapitał

    public double getBottlePrice() {
        return bottlePrice;
    }

    public void setBottlePrice(double bottlePrice) {
        this.bottlePrice = bottlePrice;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }

    public double getCapital() {
        return capital;
    }

    public double getWarehouseCost(int index) {
        return warehouseCosts[index];
    }

    public void setWarehouseCost(int index, double cost) {
        warehouseCosts[index] = cost;
    }

    public void updateTotalEarnings(int totalBottles) {
        totalEarnings = totalBottles * bottlePrice;
        capital = initialCapital + totalEarnings;
    }

    public void deductProductionCosts() {
        double totalCosts = 0;
        for (double cost : warehouseCosts) {
            totalCosts += cost;
        }
        capital -= totalCosts;
    }

    public void deductCarGenerationCost(int carCount) {
        double costPerCar = 100; // Koszt generowania jednego auta
        capital -= carCount * costPerCar;
    }
}
