package GraphEditor.algorithms;

import GraphEditor.Panel;
import GraphEditor.models.Edge;
import GraphEditor.models.Node;
import GraphEditor.utils.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Kruskal {
    Panel panel;
    Graph graph;

    public Kruskal(Panel panel, Graph graph) {
        this.panel = panel;
        this.graph = graph;
    }

    private Node getRoot(Node node, HashMap<Node, Node> representativeElements) {
        while(representativeElements.get(node) != node) {
           node = representativeElements.get(node);
        }
        return node;
    }

    public List<Edge> getMinTree() {
        List<Edge> sortedEdges = new ArrayList<>(graph.getEdgeList());
        sortedEdges.sort((edge1, edge2) -> {
            return edge1.getWeight() - edge2.getWeight();
        });
        List<Edge> resultEdges = new ArrayList<>();
        HashMap<Node, Node> representativeElements = new HashMap<>(); // <representative, element>

        for(var node : graph.getNodeList()) {
            representativeElements.put(node, node);
        }
        for(var edge : sortedEdges) {
            Node node1 = getRoot(edge.getEndNode(), representativeElements);
            Node node2 = getRoot(edge.getStartNode(), representativeElements);
            if(node1 != node2) {
                resultEdges.add(edge);
                representativeElements.put(node1, node2);
            }
        }

        return resultEdges;
    }
}
