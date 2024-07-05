import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileOperations {
    public static int[] readFlowDurationsFromCSV(String filePath) {
        List<Integer> flowDurations = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean skipFirstLine = true;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (skipFirstLine) {
                    skipFirstLine = false;
                    continue; // Skip the first line containing column names
                }
                if (parts.length >= 7) {
                    try {
                        int flowDuration = Integer.parseInt(parts[6].trim());
                        flowDurations.add(flowDuration);
                    } catch (NumberFormatException e) {
                        System.err.println("Error converting to integer: " + parts[6]);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        // Convert List<Integer> to int[]
        int[] flowDurationsArray = new int[flowDurations.size()];
        for (int i = 0; i < flowDurations.size(); i++) {
            flowDurationsArray[i] = flowDurations.get(i);
        }

        return flowDurationsArray;
    }
}