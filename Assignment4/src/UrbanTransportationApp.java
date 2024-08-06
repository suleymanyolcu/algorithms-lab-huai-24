import java.io.Serializable;
import java.util.*;

class UrbanTransportationApp implements Serializable {
    static final long serialVersionUID = 99L;

    public HyperloopTrainNetwork readHyperloopTrainNetwork(String filename) {
        HyperloopTrainNetwork hyperloopTrainNetwork = new HyperloopTrainNetwork();
        hyperloopTrainNetwork.readInput(filename);
        return hyperloopTrainNetwork;
    }

    /**
     * Function calculate the fastest route from the user's desired starting point to 
     * the desired destination point, taking into consideration the hyperloop train
     * network. 
     * @return List of RouteDirection instances
     */
    public List<RouteDirection> getFastestRouteDirections(HyperloopTrainNetwork network) {
        DirectedEdgeGraph graph = network.createGraph();
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previousVertices = new HashMap<>();
        Set<String> visited = new HashSet<>();
        for (String vertex : graph.getAdjacencyList().keySet()) {
            distances.put(vertex, Double.POSITIVE_INFINITY);
            previousVertices.put(vertex, null);
        }
        distances.put(network.startPoint.description, 0.0);
        PriorityQueue<String> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        priorityQueue.add(network.startPoint.description);

        while (!priorityQueue.isEmpty()) {
            String currentVertex = priorityQueue.poll();
            visited.add(currentVertex);

            for (Edge edge : graph.getAdjacencyList().getOrDefault(currentVertex, Collections.emptyList())) {
                if (!visited.contains(edge.getDestination())) {
                    double newDistance = distances.get(currentVertex) + edge.getWeight();
                    Double destinationDistance = distances.get(edge.getDestination());
                    if (destinationDistance == null || newDistance < destinationDistance.doubleValue()) {
                        distances.put(edge.getDestination(), newDistance);
                        previousVertices.put(edge.getDestination(), currentVertex);
                        priorityQueue.add(edge.getDestination());
                    }
                }
            }
        }

        List<RouteDirection> routeDirections = new ArrayList<>();
        String currentVertex = network.destinationPoint.description;
        while (currentVertex != null && !currentVertex.equals(network.startPoint.description)) {
            String previousVertex = previousVertices.get(currentVertex);
            Double currentDistance = distances.get(currentVertex);
            Double previousDistance = distances.get(previousVertex);
            if (currentDistance != null && previousDistance != null) {
                double duration = currentDistance - previousDistance;
                boolean trainRide;
                if (isTrainStation(previousVertex,network) && isTrainStation(currentVertex,network)) {
                    trainRide = hasDirectTrainRide(previousVertex, currentVertex,network);
                } else {
                    trainRide = false;
                }
                routeDirections.add(0, new RouteDirection(previousVertex, currentVertex, duration, trainRide));
                currentVertex = previousVertex;
            } else {
                break;
            }
        }

        return routeDirections;
    }
    private boolean hasDirectTrainRide(String station1, String station2,HyperloopTrainNetwork network) {
        for (TrainLine line : network.lines) {
            List<Station> stations = line.trainLineStations;
            int index1 = -1, index2 = -1;
            for (int i = 0; i < stations.size(); i++) {
                if (stations.get(i).description.equals(station1)) {
                    index1 = i;
                }
                if (stations.get(i).description.equals(station2)) {
                    index2 = i;
                }
            }
            if (index1 != -1 && index2 != -1 && Math.abs(index1 - index2) == 1) {
                return true;
            }
        }
        return false;
    }
    private boolean isTrainStation(String vertex,HyperloopTrainNetwork network) {
        for (TrainLine line : network.lines) {
            for (Station station : line.trainLineStations) {
                if (station.description.equals(vertex)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Function to print the route directions to STDOUT
     */
    public void printRouteDirections(List<RouteDirection> directions) {
        double totalTime = 0;
        for(RouteDirection direction: directions){
            totalTime += direction.duration;
        }
        System.out.printf("The fastest route takes %.0f minute(s).\n", totalTime);
        System.out.println("Directions\n" +
                "----------");
        int counter = 1 ;
        for (RouteDirection direction : directions) {
            if (direction.trainRide) {
                System.out.printf("%d. Get on the train from \"%s\" to \"%s\" for %.2f minutes.\n",
                        counter, direction.startStationName, direction.endStationName, direction.duration);
            } else {
                System.out.printf("%d. Walk from \"%s\" to \"%s\" for %.2f minutes.\n",
                        counter, direction.startStationName, direction.endStationName, direction.duration);
            }
            counter++;
        }

    }
}
