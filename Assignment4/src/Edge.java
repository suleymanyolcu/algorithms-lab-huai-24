public class Edge {
    private String destination;
    private double weight;

    public Edge(String destination, double weight) {
        this.destination = destination;
        this.weight = weight;
    }

    public String getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }
}
