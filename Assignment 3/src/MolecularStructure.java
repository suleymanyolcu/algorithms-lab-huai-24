import java.util.*;
import java.util.stream.Collectors;

// Class representing a molecular structure
public class MolecularStructure {
    
    // Private field
    private final List<Molecule> molecules = new ArrayList<>(); // List of molecules in the structure

    // Method to check if a molecule exists in the structure
    public boolean hasMolecule(String moleculeId) {
        return molecules.stream().map(Molecule::getId).collect(Collectors.toList()).contains(moleculeId);
    }

    // Method to add a molecule to the structure
    public void addMolecule(Molecule molecule) {
        molecules.add(molecule);
    }

    // Getter for molecules in the structure
    public List<Molecule> getMolecules() {
        return molecules;
    }

    // Method to get the easiest-to-bond molecule in the structure
    public Molecule getMoleculeWithWeakestBondStrength() {
        return molecules.stream().min(Comparator.comparing(Molecule::getBondStrength)).orElse(null);
    }
    // Method to check if this structure is a subset of another structure
    public boolean isSubsetOf(MolecularStructure other) {
        for (Molecule molecule : molecules) {
            if (other.hasMolecule(molecule.getId())) {
                return true;
            }
        }
        return false;
    }
    // Override equals method to compare structures
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MolecularStructure other = (MolecularStructure) obj;
        return this.molecules.equals(other.molecules);
    }

    // Override toString method to provide a string representation of the structure
    @Override
    public String toString() {
        ArrayList<Molecule> sortedMolecules = new ArrayList<>(molecules);
        Collections.sort(sortedMolecules);
        return sortedMolecules.toString();
    }
}
