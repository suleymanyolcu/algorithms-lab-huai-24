import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import java.io.IOException;
import java.util.Arrays;

class Main {
    public static void main(String[] args) throws IOException {

        int[] data = FileOperations.readFlowDurationsFromCSV(args[0]);
        int[] inputSizes = {500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 250000};
        int[] sortedData = Arrays.copyOf(data,data.length);
        Arrays.sort(sortedData);

        System.out.println("SORTING ALGORITHMS WITH RANDOM DATA");
        double[][] randomDataSortTimes = new double[3][10];
        randomDataSortTimes[0] = Experiments.getTimeInformationForSort(data, "insertion");
        randomDataSortTimes[1] = Experiments.getTimeInformationForSort(data, "merge");
        randomDataSortTimes[2] = Experiments.getTimeInformationForSort(data, "counting");
        showAndSaveChartSort("Tests on Random Data",inputSizes,randomDataSortTimes);

        System.out.println("SORTING ALGORITHMS WITH SORTED DATA");
        double[][] sortedDataSortTimes = new double[3][10];
        sortedDataSortTimes[0] = Experiments.getTimeInformationForSort(sortedData, "insertion");
        sortedDataSortTimes[1] = Experiments.getTimeInformationForSort(sortedData, "merge");
        sortedDataSortTimes[2] = Experiments.getTimeInformationForSort(sortedData, "counting");
        showAndSaveChartSort("Tests on Sorted Data",inputSizes,sortedDataSortTimes);

        System.out.println("SORTING ALGORITHMS WITH REVERSE SORTED DATA");
        reverse(sortedData);
        double[][] reverseSortedDataSortTimes = new double[3][10];
        reverseSortedDataSortTimes[0] = Experiments.getTimeInformationForSort(sortedData, "insertion");
        reverseSortedDataSortTimes[1] = Experiments.getTimeInformationForSort(sortedData, "merge");
        reverseSortedDataSortTimes[2] = Experiments.getTimeInformationForSort(sortedData, "counting");
        showAndSaveChartSort("Tests on Reverse Sorted Data",inputSizes,reverseSortedDataSortTimes);


        System.out.println("SEARCHING ALGORITHMS WITH RANDOM DATA");
        double[][] searchingTimes = new double[3][10];
        searchingTimes[0] = Experiments.getTimeInformationForSearch(data,"linear");
        System.out.println("SEARCHING ALGORITHMS WITH SORTED DATA");
        searchingTimes[1] = Experiments.getTimeInformationForSearch(sortedData,"linear");
        searchingTimes[2]=Experiments.getTimeInformationForSearch(sortedData,"binary");
        showAndSaveChartSearch("Searching Algorithm Tests",inputSizes,searchingTimes);
    }

    static void reverse(int[] array) {
        int start = 0;
        int end = array.length - 1;
        while (start < end) {
            int temp = array[start];
            array[start] = array[end];
            array[end] = temp;
            start++;
            end--;
        }
    }

    public static void showAndSaveChartSort(String title, int[] xAxis, double[][] yAxis) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle("Time in Milliseconds").xAxisTitle("Input Size").build();

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Add a plot for a sorting algorithm
        chart.addSeries("Insertion Sort", doubleX, yAxis[0]);
        chart.addSeries("Merge Sort", doubleX, yAxis[1]);
        chart.addSeries("Count Sort", doubleX, yAxis[2]);

        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }
    public static void showAndSaveChartSearch(String title, int[] xAxis, double[][] yAxis) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle("Time in Nanoseconds").xAxisTitle("Input Size").build();

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Add a plot for a sorting algorithm
        chart.addSeries("Linear Random Data ", doubleX, yAxis[0]);
        chart.addSeries("Linear Sorted Data", doubleX, yAxis[1]);
        chart.addSeries("Binary Sorted Data", doubleX, yAxis[2]);

        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }
}
