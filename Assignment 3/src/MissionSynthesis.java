import java.util.*;

// Class representing the Mission Synthesis
public class MissionSynthesis {

    // Private fields
    private final List<MolecularStructure> humanStructures; // Molecular structures for humans
    private final ArrayList<MolecularStructure> diffStructures; // Anomalies in Vitales structures compared to humans

    // Constructor
    public MissionSynthesis(List<MolecularStructure> humanStructures, ArrayList<MolecularStructure> diffStructures) {
        this.humanStructures = humanStructures;
        this.diffStructures = diffStructures;
    }

    // Method to synthesize bonds for the serum
    public List<Bond> synthesizeSerum() {

        List<Molecule> selectedMolecules = selectLowestBondMolecules(humanStructures);
        selectedMolecules.addAll(selectLowestBondMolecules(diffStructures));

        // Initialize a priority queue to store potential bonds
        PriorityQueue<Bond> pq = new PriorityQueue<>(Comparator.comparingDouble(Bond::getWeight));
        // Add all potential bonds between selected molecules to the priority queue
        for (int i = 0; i < selectedMolecules.size(); i++) {
            for (int j = i + 1; j < selectedMolecules.size(); j++) {
                Molecule molecule1 = selectedMolecules.get(i);
                Molecule molecule2 = selectedMolecules.get(j);
                double bondStrength = (molecule1.getBondStrength() + molecule2.getBondStrength()) / 2.0;
                pq.add(new Bond(molecule1, molecule2, bondStrength));
                //System.out.println(molecule1.getId() + "," + molecule2.getId() + "," + bondStrength);
            }
        }

        // Initialize an empty list to store selected bonds for the serum
        List<Bond> serumBonds = new ArrayList<>();

        // Iterate through all potential bonds and add them to the serum if they don't form cycles
        while (!pq.isEmpty() && serumBonds.size() < selectedMolecules.size() - 1) {
            Bond bond = pq.poll();
            Molecule from = bond.getFrom();
            Molecule to = bond.getTo();

            // Check if adding this bond forms a cycle
            if (!formsCycle(serumBonds, from, to)) {
                // Add the bond to the selected bonds for the serum
                serumBonds.add(bond);
            }
        }

        return serumBonds;
    }

    // Method to check if adding a bond forms a cycle
    private boolean formsCycle(List<Bond> serumBonds, Molecule from, Molecule to) {
        Set<Molecule> visited = new HashSet<>();
        Queue<Molecule> queue = new LinkedList<>();
        queue.offer(from);

        // Perform BFS to check for a cycle
        while (!queue.isEmpty()) {
            Molecule current = queue.poll();
            visited.add(current);
            for (Bond bond : serumBonds) {
                Molecule next = null;
                if (bond.getFrom().equals(current)) {
                    next = bond.getTo();
                } else if (bond.getTo().equals(current)) {
                    next = bond.getFrom();
                }
                if (next != null && !visited.contains(next)) {
                    if (next.equals(to)) {
                        return true; // Cycle detected
                    }
                    queue.offer(next);
                }
            }
        }

        return false; // No cycle detected
    }


    // Method to select molecules with the lowest bond strength from each molecular structure
    private List<Molecule> selectLowestBondMolecules(List<MolecularStructure> structures) {
        List<Molecule> selectedMolecules = new ArrayList<>();
        for (MolecularStructure structure : structures) {
            // Add the molecule with the lowest bond strength to the selected list
            if (!structure.getMolecules().isEmpty()) {
                selectedMolecules.add(structure.getMoleculeWithWeakestBondStrength());
            }
        }
        return selectedMolecules;
    }

    // Method to print the synthesized bonds
    public void printSynthesis(List<Bond> serum) {
        System.out.println("Typical human molecules selected for synthesis: " + getMoleculeIds(humanStructures));
        System.out.println("Vitales molecules selected for synthesis: " + getMoleculeIds(diffStructures));
        System.out.println("Synthesizing the serum...");

        double totalBondStrength = 0;

        for (Bond bond : serum) {
            totalBondStrength += bond.getWeight();
            System.out.println("Forming a bond between " + bond.getFrom().getId() + " - " + bond.getTo().getId() + " with strength " + String.format("%.2f", bond.getWeight()));
        }

        System.out.println("The total serum bond strength is " + String.format("%.2f", totalBondStrength));
    }

    // Helper method to get molecule IDs from molecular structures
    private String getMoleculeIds(List<MolecularStructure> structures) {
        List<String> ids = new ArrayList<>();
        for (MolecularStructure structure : structures) {
            ids.add(structure.getMoleculeWithWeakestBondStrength().getId());
        }
        return ids.toString();
    }
}
