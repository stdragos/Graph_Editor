package GraphEditor.algorithms;

import GraphEditor.Panel;
import GraphEditor.models.Edge;
import GraphEditor.models.Node;
import GraphEditor.utils.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Boruvka {
    Panel panel;
    Graph graph;

    public Boruvka(Panel panel, Graph graph) {
        this.panel = panel;
        this.graph = graph;
    }

    private Node getRoot(Node node, List<Node> representativeElements) {
        while(representativeElements.get(node.getNumber() - 1) != node) {
            node = representativeElements.get(representativeElements.get(node.getNumber() - 1).getNumber() - 1);
        }
        return node;
    }

    private void unite(Node root1, Node root2, List<Node> representativeElements, List<Integer> ranks) {
        //Node root1 = getRoot(node1, representativeElements);
        //Node root2 = getRoot(node2, representativeElements);
        if (ranks.get(root1.getNumber() - 1) < ranks.get(root2.getNumber() - 1)) {
            representativeElements.set(root1.getNumber() - 1, root2);
        }
        else if (ranks.get(root1.getNumber() - 1) > ranks.get(root2.getNumber() - 1)) {
            representativeElements.set(root2.getNumber() - 1, root1);
        }
        else {
            representativeElements.set(root2.getNumber() - 1, root1);
            ranks.set(root1.getNumber() - 1, ranks.get(root1.getNumber() - 1) + 1);
        }
    }

    public List<Edge> getMinTree() {
        int forests = graph.getNodeList().size();
        List<Node> representativeElements = new ArrayList<>(graph.getNodeList());
        List<Integer> ranks = new ArrayList<>(Collections.nCopies(graph.getNodeList().size(), 1));
        List<Edge> newEdges = new ArrayList<>();

        while(forests > 1) {
            List<Edge> cheapestEdge = new ArrayList<>(Collections.nCopies(graph.getNodeList().size(), null));

            for(var edge : graph.getEdgeList()) {
                Node root1 = getRoot(edge.getStartNode(), representativeElements);
                Node root2 = getRoot(edge.getEndNode(), representativeElements);

                if(root1 != root2) {
                    if(cheapestEdge.get(root1.getNumber() - 1) == null || cheapestEdge.get(root1.getNumber() - 1).getWeight() > edge.getWeight()) {
                            cheapestEdge.set(root1.getNumber() - 1, edge);
                    }
                    if(cheapestEdge.get(root2.getNumber() - 1) == null || cheapestEdge.get(root2.getNumber() - 1).getWeight() > edge.getWeight()) {
                            cheapestEdge.set(root2.getNumber() - 1, edge);
                    }
                }
            }

            for(var node : graph.getNodeList()) {
                if(cheapestEdge.get(node.getNumber() - 1) != null) {
                    Edge edge = cheapestEdge.get(node.getNumber() - 1);
                    Node root1 = getRoot(edge.getStartNode(), representativeElements);
                    Node root2 = getRoot(edge.getEndNode(), representativeElements);
                    if(root1 != root2) {
                        newEdges.add(edge);
                        unite(root1, root2, representativeElements, ranks);
                        --forests;
                    }
                }
            }
        }

        return newEdges;
    }
}

