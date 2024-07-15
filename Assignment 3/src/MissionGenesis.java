import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Class representing the mission of Genesis
public class MissionGenesis {

    // Private fields
    private MolecularData molecularDataHuman; // Molecular data for humans
    private MolecularData molecularDataVitales; // Molecular data for Vitales

    // Getter for human molecular data
    public MolecularData getMolecularDataHuman() {
        return molecularDataHuman;
    }

    // Getter for Vitales molecular data
    public MolecularData getMolecularDataVitales() {
        return molecularDataVitales;
    }

    // Method to read XML data from the specified filename
    // This method should populate molecularDataHuman and molecularDataVitales fields once called
    public void readXML(String filename) {
        try {
            // Create a DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse the XML file
            Document doc = builder.parse(new File(filename));

            // Extract human and Vitales molecular data
            NodeList humanMolecules = doc.getElementsByTagName("HumanMolecularData");
            NodeList vitalesMolecules = doc.getElementsByTagName("VitalesMolecularData");

            molecularDataHuman = extractMolecularData(humanMolecules);
            molecularDataVitales = extractMolecularData(vitalesMolecules);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to extract molecular data from NodeList
    private MolecularData extractMolecularData(NodeList molecules) {
        try {
            // Initialize a list to hold molecules
            NodeList moleculeNodes = molecules.item(0).getChildNodes();
            List<Molecule> moleculeList = new ArrayList<>();

            // Iterate over molecule nodes
            for (int i = 0; i < moleculeNodes.getLength(); i++) {
                Node moleculeNode = moleculeNodes.item(i);
                if (moleculeNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element moleculeElement = (Element) moleculeNode;
                    String id = moleculeElement.getElementsByTagName("ID").item(0).getTextContent();
                    int bondStrength = Integer.parseInt(moleculeElement.getElementsByTagName("BondStrength").item(0).getTextContent());

                    NodeList bondNodes = moleculeElement.getElementsByTagName("Bonds").item(0).getChildNodes();
                    List<String> bonds = new ArrayList<>();
                    for (int j = 0; j < bondNodes.getLength(); j++) {
                        Node bondNode = bondNodes.item(j);
                        if (bondNode.getNodeType() == Node.ELEMENT_NODE) {
                            bonds.add(bondNode.getTextContent());
                        }
                    }

                    Molecule molecule = new Molecule(id, bondStrength, bonds);
                    moleculeList.add(molecule);
                }
            }

            return new MolecularData(moleculeList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
