import java.util.Arrays;

public class Algorithms {
    public static void insertionSort(int[] array) {
        int n = array.length;

        for (int j = 1; j < n; j++) {
            int key = array[j];
            int i = j - 1;

            while (i >= 0 && array[i] > key) {
                array[i + 1] = array[i];
                i = i - 1;
            }

            array[i + 1] = key;
        }
    }

    public static void mergeSort(int[] array) {
        int n = array.length;
        if (n <= 1) {
            return;
        }
        int mid = n / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, n);
        mergeSort(left);
        mergeSort(right);
        merge(array, left, right);
    }

    private static void merge(int[] array, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                array[k] = left[i];
                i++;
            } else {
                array[k] = right[j];
                j++;
            }
            k++;
        }
        while (i < left.length) {
            array[k] = left[i];
            i++;
            k++;
        }
        while (j < right.length) {
            array[k] = right[j];
            j++;
            k++;
        }
    }

    public static int[] countingSort(int[] array, int k) {
        int[] count = new int[k + 1];
        int[] output = new int[array.length];
        for (int j : array) {
            count[j]++;
        }
        for (int i = 1; i <= k; i++) {
            count[i] += count[i - 1];
        }
        for (int i = array.length - 1; i >= 0; i--) {
            int j = array[i];
            count[j]--;
            output[count[j]] = array[i];
        }
        return output;
    }


    public static int linearSearch(int[] array, int x) {
        int size = array.length;
        for (int i = 0; i < size; i++) {
            if (array[i] == x) {
                return i; // Element found, return its index
            }
        }
        return -1; // Element not found
    }

    public static int binarySearch(int[] array, int x) {
        int low = 0;
        int high = array.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (array[mid] == x) {
                return mid; // Element found at index mid
            } else if (array[mid] < x) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1; // Element not found
    }

}
