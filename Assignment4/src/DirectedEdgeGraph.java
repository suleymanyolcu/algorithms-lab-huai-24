import java.util.*;

public class DirectedEdgeGraph {
    private Map<String, List<Edge>> adjacencyList;

    public DirectedEdgeGraph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addEdge(String source, String destination, double weight) {
        adjacencyList.computeIfAbsent(source, k -> new ArrayList<>()).add(new Edge(destination, weight));
    }

    public Map<String, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }
}
