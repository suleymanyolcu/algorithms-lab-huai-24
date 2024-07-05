import java.util.Arrays;
import java.util.Random;

public class Experiments {
    public static int[] sizes = {500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 250000};

    public static int[] takeSubset(int[] array, int subsetSize) {
        return Arrays.copyOf(array, subsetSize);
    }

    public static double[] getTimeInformationForSort(int[] data, String algorithm) {
        double[] sortTimes = new double[sizes.length]; // Array to store timing information

        // Perform sorting experiments on given data
        for (int i = 0; i < sizes.length; i++) {
            int[] subset = takeSubset(data, sizes[i]);
            double totalTime = 0;
            for (int j = 0; j < 10; j++) {
                int[] copy = Arrays.copyOf(subset, subset.length); // Create a copy of the subset to avoid modifying the original data
                long startTime = 0;
                long endTime = 0;
                switch (algorithm) {
                    case "insertion":
                        startTime = System.currentTimeMillis();
                        Algorithms.insertionSort(copy);
                        endTime = System.currentTimeMillis();
                        break;
                    case "merge":
                        startTime = System.currentTimeMillis();
                        Algorithms.mergeSort(copy);
                        endTime = System.currentTimeMillis();
                        break;
                    case "counting":
                        startTime = System.currentTimeMillis();
                        int max = findMax(copy); // Find max value for counting sort
                        Algorithms.countingSort(copy, max);
                        endTime = System.currentTimeMillis();
                        break;
                    // Add cases for other sorting algorithms if needed
                    default:
                        throw new IllegalArgumentException("Invalid sorting algorithm");
                }
                totalTime += (endTime - startTime);
            }
            double averageTime = totalTime / 10.0;
            switch (algorithm) {
                case "insertion":
                    System.out.println("Insertion Sort Took Time: " + averageTime + " for size: " + sizes[i]);
                    break;
                case "merge":
                    System.out.println("Merge Sort Took Time: " + averageTime + " for size: " + sizes[i]);
                    break;
                case "counting":
                    System.out.println("Count Sort Took Time: " + averageTime + " for size: " + sizes[i]);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sorting algorithm");

            }
            sortTimes[i] = averageTime;
        }

        return sortTimes;
    }

    public static double[] getTimeInformationForSearch(int[] data, String algorithm) {
        double[] searchTimes = new double[sizes.length]; // Array to store timing information

        // Perform search experiments on given data
        for (int i = 0; i < sizes.length; i++) {
            int[] subset = takeSubset(data, sizes[i]);
            double totalTime = 0;
            Random rand = new Random();
            for (int j = 0; j < 1000; j++) {
                int randomIndex = rand.nextInt(subset.length);
                int randomNumber = subset[randomIndex];
                long startTime = 0;
                long endTime = 0;
                switch (algorithm) {
                    case "linear":
                        startTime = System.nanoTime();
                        Algorithms.linearSearch(subset, randomNumber);
                        endTime = System.nanoTime();
                        break;
                    case "binary":
                        Arrays.sort(subset); // Ensure data is sorted for binary search
                        startTime = System.nanoTime();
                        Algorithms.binarySearch(subset, randomNumber);
                        endTime = System.nanoTime();
                        break;
                    // Add cases for other search algorithms if needed
                    default:
                        throw new IllegalArgumentException("Invalid search algorithm");
                }
                totalTime += (endTime - startTime);
            }
            double averageTime = totalTime / 1000.0;
            switch (algorithm) {
                case "linear":
                    System.out.println("Linear Search Took Time: " + averageTime + " for size: " + sizes[i]);
                    break;
                case "binary":
                    System.out.println("Binary Search Took Time: " + averageTime + " for size: " + sizes[i]);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid searching algorithm");

            }
            searchTimes[i] = averageTime;
        }

        return searchTimes;
    }
    private static int findMax(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int num : arr) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }


}