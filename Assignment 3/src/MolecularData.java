import java.util.*;

// Class representing molecular data
public class MolecularData {

    // Private fields
    private final List<Molecule> molecules; // List of molecules

    // Constructor
    public MolecularData(List<Molecule> molecules) {
        this.molecules = molecules;
    }

    // Getter for molecules
    public List<Molecule> getMolecules() {
        return molecules;
    }

    // Method to identify molecular structures
    // Return the list of different molecular structures identified from the input data
    public List<MolecularStructure> identifyMolecularStructures() {
        ArrayList<MolecularStructure> structures = new ArrayList<>();

        // Create a map to track visited molecules
        Set<Molecule> visited = new HashSet<>();

        // Iterate through each molecule in the data
        for (Molecule molecule : molecules) {
            // If the molecule has not been visited, start a new molecular structure
            if (!visited.contains(molecule)) {
                MolecularStructure structure = new MolecularStructure();
                depthFirstSearch(molecule, visited, structure);
                structures.add(structure);
            }
        }

        // Combine molecular structures if one is a subset of another
        combineSubsets(structures);

        return structures;
    }

    // Method to remove subsets from the list of molecular structures
    // Combine molecular structures if one is a subset of another
    private void combineSubsets(List<MolecularStructure> structures) {
        for (int i = 0; i < structures.size(); i++) {
            MolecularStructure current = structures.get(i);
            for (int j = i + 1; j < structures.size(); j++) {
                MolecularStructure other = structures.get(j);
                if (current.isSubsetOf(other)) {
                    // Add molecules from current to other, avoiding duplicates
                    for (Molecule molecule : current.getMolecules()) {
                        if (!other.hasMolecule(molecule.getId())) {
                            other.addMolecule(molecule);
                        }
                    }
                    // Remove current structure from the list
                    structures.remove(i);
                    i--; // Adjust index after removal
                    break; // Exit inner loop to recheck against other structures
                } else if (other.isSubsetOf(current)) {
                    // Add molecules from other to current, avoiding duplicates
                    for (Molecule molecule : other.getMolecules()) {
                        if (!current.hasMolecule(molecule.getId())) {
                            current.addMolecule(molecule);
                        }
                    }
                    // Remove other structure from the list
                    structures.remove(j);
                    j--; // Adjust index after removal
                }
            }
        }
    }

    // Depth-first search to identify molecular structures

    private void depthFirstSearch(Molecule molecule, Set<Molecule> visited, MolecularStructure structure) {
        // Add the current molecule to the structure
        visited.add(molecule);
        structure.addMolecule(molecule);

        // Recursively visit bonded molecules
        for (String bondId : molecule.getBonds()) {
            Molecule bondedMolecule = findMoleculeById(bondId);
            if (bondedMolecule != null && !visited.contains(bondedMolecule)) {
                depthFirstSearch(bondedMolecule, visited, structure);
            }
        }

        // Check if there are indirectly bonded molecules and mark them as visited
        for (String bondId : molecule.getBonds()) {
            Molecule bondedMolecule = findMoleculeById(bondId);
            if (bondedMolecule != null && !structure.hasMolecule(bondedMolecule.getId())) {
                depthFirstSearch(bondedMolecule, visited, structure);
            }
        }
    }


    // Helper method to find a molecule by ID
    private Molecule findMoleculeById(String id) {
        for (Molecule molecule : molecules) {
            if (molecule.getId().equals(id)) {
                return molecule;
            }
        }
        return null;
    }


    // Method to print given molecular structures
    public void printMolecularStructures(List<MolecularStructure> molecularStructures, String species) {
        System.out.println(molecularStructures.size() + " molecular structures have been discovered in " + species + ".");
        for (int i = 0; i < molecularStructures.size(); i++) {
            MolecularStructure structure = molecularStructures.get(i);
            System.out.print("Molecules in Molecular Structure " + (i + 1) + ": ");
            System.out.println(structure.toString()); // Automatically sorts molecules by ID
        }
    }


    // Method to identify anomalies given a source and target molecular structure
    // Returns a list of molecular structures unique to the targetStructure only
    public static ArrayList<MolecularStructure> getVitalesAnomaly(List<MolecularStructure> sourceStructures, List<MolecularStructure> targetStructures) {
        ArrayList<MolecularStructure> anomalyList = new ArrayList<>();
        // Iterate through target structures
        for (MolecularStructure targetStructure : targetStructures) {
            boolean found = false;
            // Check if target structure exists in source structures
            for (MolecularStructure sourceStructure : sourceStructures) {
                if (targetStructure.equals(sourceStructure)) {
                    found = true;
                    break;
                }
            }
            // If target structure is not found in source structures, add it to anomaly list
            if (!found) {
                anomalyList.add(targetStructure);
            }
        }

        return anomalyList;
    }

    // Method to print Vitales anomalies
    public void printVitalesAnomaly(List<MolecularStructure> molecularStructures) {
        System.out.println("Molecular structures unique to Vitales individuals:");
        for (MolecularStructure structure : molecularStructures) {
            System.out.println(structure.toString()); // Automatically sorts molecules by ID
        }
    }
}
