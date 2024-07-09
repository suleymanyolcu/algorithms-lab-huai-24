import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Main class
 */
// FREE CODE HERE
public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java Main <demandSchedule.dat>");
            return;
        }
       /** MISSION POWER GRID OPTIMIZATION BELOW **/

        System.out.println("##MISSION POWER GRID OPTIMIZATION##");
        String demandScheduleFile = args[0];
        ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour = readDemandSchedule(demandScheduleFile);

        if (amountOfEnergyDemandsArrivingPerHour == null) {
            System.out.println("Error reading demand schedule file.");
            return;
        }
        PowerGridOptimization powerGridOptimization = new PowerGridOptimization(amountOfEnergyDemandsArrivingPerHour);
        OptimalPowerGridSolution optimalSolution = powerGridOptimization.getOptimalPowerGridSolutionDP();

        int totalDemand = amountOfEnergyDemandsArrivingPerHour.stream().mapToInt(Integer::intValue).sum();
        int satisfiedDemand = optimalSolution.getmaxNumberOfSatisfiedDemands();
        int unsatisfiedDemand = totalDemand - satisfiedDemand;

        System.out.println("The total number of demanded gigawatts: " + totalDemand);
        System.out.println("Maximum number of satisfied gigawatts: " + satisfiedDemand);
        System.out.print("Hours at which the battery bank should be discharged: ");
        ArrayList<Integer> liste = optimalSolution.getHoursToDischargeBatteriesForMaxEfficiency();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < liste.size(); i++) {
            sb.append(liste.get(i));
            if (i < liste.size() - 1) {
                sb.append(", ");
            }
        }
        String result = sb.toString();
        System.out.println(result);
        System.out.println("The number of unsatisfied gigawatts: " + unsatisfiedDemand);
        System.out.println("##MISSION POWER GRID OPTIMIZATION COMPLETED##");

        /** MISSION ECO-MAINTENANCE BELOW **/

        System.out.println("##MISSION ECO-MAINTENANCE##");
        String esvMaintenanceFile = args[1];
        int numAvailableESVs;
        int esvCapacity;
        ArrayList<Integer> maintenanceTaskEnergyDemands = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(esvMaintenanceFile))) {
            String line = br.readLine();
            String[] esvInfo = line.trim().split("\\s+");
            numAvailableESVs = Integer.parseInt(esvInfo[0]);
            esvCapacity = Integer.parseInt(esvInfo[1]);

            line = br.readLine();
            String[] tasks = line.trim().split("\\s+");
            for (String task : tasks) {
                maintenanceTaskEnergyDemands.add(Integer.parseInt(task));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        OptimalESVDeploymentGP esvDeploymentGP = new OptimalESVDeploymentGP(maintenanceTaskEnergyDemands);
        int minNumESVsToDeploy = esvDeploymentGP.getMinNumESVsToDeploy(numAvailableESVs, esvCapacity);

        if (minNumESVsToDeploy == -1) {
            System.out.println("Warning: Mission Eco-Maintenance Failed.");
        } else {
            System.out.println("The minimum number of ESVs to deploy: " + minNumESVsToDeploy);
            ArrayList<ArrayList<Integer>> maintenanceTasksAssignedToESVs = esvDeploymentGP.getMaintenanceTasksAssignedToESVs();
            for (int i = 0; i < maintenanceTasksAssignedToESVs.size(); i++) {
                System.out.println("ESV " + (i + 1) + " tasks: " + maintenanceTasksAssignedToESVs.get(i));
            }
        }

        System.out.println("##MISSION ECO-MAINTENANCE COMPLETED##");
    }
    private static ArrayList<Integer> readDemandSchedule(String filename) {
        ArrayList<Integer> demandSchedule = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+");
                for (String token : tokens) {
                    demandSchedule.add(Integer.parseInt(token));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return demandSchedule;
    }
}
