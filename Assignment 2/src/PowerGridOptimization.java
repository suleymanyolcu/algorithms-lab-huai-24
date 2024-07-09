import java.util.ArrayList;

/**
 * This class accomplishes Mission POWER GRID OPTIMIZATION
 */
public class PowerGridOptimization {
    private ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour;

    public PowerGridOptimization(ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour) {
        this.amountOfEnergyDemandsArrivingPerHour = amountOfEnergyDemandsArrivingPerHour;
    }

    public ArrayList<Integer> getAmountOfEnergyDemandsArrivingPerHour() {
        return amountOfEnergyDemandsArrivingPerHour;
    }

    /**
     * Function to implement the given dynamic programming algorithm
     * SOL(0) <- 0
     * HOURS(0) <- [ ]
     * For{j <- 1...N}
     * SOL(j) <- max_{0<=i<j} [ (SOL(i) + min[ E(j), P(j − i) ] ]
     * HOURS(j) <- [HOURS(i), j]
     * EndFor
     *
     * @return OptimalPowerGridSolution
     */
    public OptimalPowerGridSolution getOptimalPowerGridSolutionDP() {
        int n = amountOfEnergyDemandsArrivingPerHour.size();
        int[] sol = new int[n + 1];
        ArrayList<ArrayList<Integer>> hours = new ArrayList<>();

        sol[0] = 0;
        hours.add(new ArrayList<>());

        for (int j = 1; j <= n; j++) {
            int maxSatisfied = 0;
            int bestHour = 0;
            for (int i = 0; i < j; i++) {
                int satisfied = sol[i] + Math.min(amountOfEnergyDemandsArrivingPerHour.get(j - 1), getEfficiency(j - i));
                if (satisfied > maxSatisfied) {
                    maxSatisfied = satisfied;
                    bestHour = i;
                }
            }
            sol[j] = maxSatisfied;
            ArrayList<Integer> bestHours = new ArrayList<>(hours.get(bestHour));
            bestHours.add(j);
            hours.add(bestHours);
        }
        return new OptimalPowerGridSolution(sol[n], hours.get(n));
    }
    private int getEfficiency(int hours) {
        return hours * hours;
    }
}
