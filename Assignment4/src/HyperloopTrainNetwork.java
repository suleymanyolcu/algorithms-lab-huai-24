import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HyperloopTrainNetwork implements Serializable {
    static final long serialVersionUID = 11L;
    public double averageTrainSpeed;
    public final double averageWalkingSpeed = 1000 / 6.0;;
    public int numTrainLines;
    public Station startPoint;
    public Station destinationPoint;
    public List<TrainLine> lines;
    public DirectedEdgeGraph createGraph() {
        DirectedEdgeGraph graph = new DirectedEdgeGraph();
        for (TrainLine line : lines) {
            for (Station station : line.trainLineStations) {
                String stationName = station.description;
                double distance = calculateDistance(startPoint.coordinates, station.coordinates);
                double weight = distance / averageWalkingSpeed;
                graph.addEdge(startPoint.description, stationName, weight);
            }
        }
        for (TrainLine line : lines) {
            List<Station> stations = line.trainLineStations;
            for (int i = 0; i < stations.size() - 1; i++) {
                Station sourceStation = stations.get(i);
                Station destinationStation = stations.get(i + 1);
                String sourceName = sourceStation.description;
                String destinationName = destinationStation.description;
                double distance = calculateDistance(sourceStation.coordinates, destinationStation.coordinates);
                double weight = distance / averageTrainSpeed;
                graph.addEdge(sourceName, destinationName, weight);
                // Assuming two-way train travel
                graph.addEdge(destinationName, sourceName, weight);
            }
        }
        for (int i = 0; i < lines.size(); i++) {
            for (int j = i + 1; j < lines.size(); j++) {
                List<Station> stations1 = lines.get(i).trainLineStations;
                List<Station> stations2 = lines.get(j).trainLineStations;
                for (Station station1 : stations1) {
                    for (Station station2 : stations2) {
                        String stationName1 = station1.description;
                        String stationName2 = station2.description;
                        double distance = calculateDistance(station1.coordinates, station2.coordinates);
                        double weight = distance / averageWalkingSpeed;
                        graph.addEdge(stationName1, stationName2, weight);
                        graph.addEdge(stationName2, stationName1, weight); // Assuming two-way walking
                    }
                }
            }
        }
        for (TrainLine line : lines) {
            for (Station station : line.trainLineStations) {
                String stationName = station.description;
                double distance = calculateDistance(station.coordinates, destinationPoint.coordinates);
                double weight = distance / averageWalkingSpeed;
                graph.addEdge(stationName, destinationPoint.description, weight);
            }
        }
        double distanceToDestination = calculateDistance(startPoint.coordinates, destinationPoint.coordinates);
        double weightToDestination = distanceToDestination / averageWalkingSpeed;
        graph.addEdge(startPoint.description, destinationPoint.description, weightToDestination);


        return graph;
    }

    private double calculateDistance(Point point1, Point point2) {
        int dx = point2.x - point1.x;
        int dy = point2.y - point1.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Method with a Regular Expression to extract integer numbers from the fileContent
     * @return the result as int
     */
    public int getIntVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+)");
        Matcher m = p.matcher(fileContent);
        m.find();
        return Integer.parseInt(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract string constants from the fileContent
     * @return the result as String
     */
    public String getStringVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*\"([^\"]+)\"");
        Matcher m = p.matcher(fileContent);
        m.find();
        return m.group(1);
    }

    /**
     * Write the necessary Regular Expression to extract floating point numbers from the fileContent
     * Your regular expression should support floating point numbers with an arbitrary number of
     * decimals or without any (e.g. 5, 5.2, 5.02, 5.0002, etc.).
     * @return the result as Double
     */
    public Double getDoubleVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+(?:\\.[0-9]*)?)");
        Matcher m = p.matcher(fileContent);
        m.find();
        return Double.parseDouble(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract a Point object from the fileContent
     * points are given as an x and y coordinate pair surrounded by parentheses and separated by a comma
     * @return the result as a Point object
     */
    public Point getPointVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "\\s*=\\s*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
        Matcher m = p.matcher(fileContent);
        m.find();
        int x = Integer.parseInt(m.group(1));
        int y = Integer.parseInt(m.group(2));
        return new Point(x, y);
    }

    /**
     * Function to extract the train lines from the fileContent by reading train line names and their 
     * respective stations.
     * @return List of TrainLine instances
     */
    public static List<TrainLine> getTrainLines(String fileContent) {
        List<TrainLine> trainLines = new ArrayList<>();
        Pattern p = Pattern.compile("train_line_name\\s*=\\s*\"(.*?)\"\\s*train_line_stations\\s*=\\s*(.*)");
        Matcher m = p.matcher(fileContent);
        while (m.find()) {
            String trainLineName = m.group(1);
            String stationsStr = m.group(2).trim();
            List<Station> stations = extractStations(stationsStr, trainLineName);
            trainLines.add(new TrainLine(trainLineName, stations));
        }
        return trainLines;
    }

    private static List<Station> extractStations(String stationsStr, String trainLineName) {
        List<Station> stations = new ArrayList<>();
        Pattern stationPattern = Pattern.compile("(\\d+\\s*,\\s*\\d+)");
        Matcher stationMatcher = stationPattern.matcher(stationsStr);
        int number = 1;
        while (stationMatcher.find()) {
            String[] coordinates = stationMatcher.group().split("\\s*,\\s*");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            stations.add(new Station(new Point(x, y), trainLineName+" Line Station "+ number));
            number++;
        }
        return stations;
    }

    public String readFileContent(String filename) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(filename));
            return new String(encoded);
        } catch (IOException e) {
            e.printStackTrace();
            return ""; //
        }
    }
    /**
     * Function to populate the given instance variables of this class by calling the functions above.
     */

    public void readInput(String filename) {
        String fileContent = readFileContent(filename);
        this.numTrainLines = getIntVar("num_train_lines", fileContent);
        this.startPoint = new Station(getPointVar("starting_point", fileContent), "Starting Point");
        this.destinationPoint = new Station(getPointVar("destination_point", fileContent), "Final Destination");
        this.averageTrainSpeed = getDoubleVar("average_train_speed", fileContent)*100/6.0;
        this.lines = getTrainLines(fileContent);

    }
}
