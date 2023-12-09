package GraphEditor.algorithms;

import GraphEditor.Panel;
import GraphEditor.models.Edge;
import GraphEditor.models.Node;
import GraphEditor.utils.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class GenericalMinTree {
    private Panel panel;
    private Graph graph;

    public GenericalMinTree(Panel panel, Graph graph) {
        this.panel = panel;
        this.graph = graph;
    }

    public List<Edge> getMinTree() {
        List<List<Node>> components = new ArrayList<>();
        List<List<Edge>> newEdges = new ArrayList<>();
        for (var node : graph.getNodeList()) {
            List<Node> tmp1 = new ArrayList<>();
            tmp1.add(node);
            components.add(tmp1);

            List<Edge> tmp2 = new ArrayList<>();
            newEdges.add(tmp2);
        }



        for (int k = 0; k < graph.getNodeList().size() - 1; ++k) {
            int currentComponent = 0;
            for (int i = 0; i < components.size(); ++i) {
                if (components.get(i).size() != 0) {
                    currentComponent = i;
                    break;
                }
            }

                Edge minEdge = new Edge(null, null);
                minEdge.setWeight(Integer.MAX_VALUE);
                Node fromWhichNode = null;

                for(var node : components.get(currentComponent)) {
                    for(var entry : node.getWeightList().entrySet()) {
                        Edge edge = entry.getValue();

                        if(edge.getStartNode() != node) {
                            if(!components.get(currentComponent).contains(edge.getStartNode())) {
                                if(minEdge.getWeight() > edge.getWeight()) {
                                    minEdge = edge;
                                    fromWhichNode = node;
                                }
                            }
                        }
                        else {
                            if(!components.get(currentComponent).contains(edge.getEndNode())) {
                                if(minEdge.getWeight() > edge.getWeight()) {
                                    minEdge = edge;
                                    fromWhichNode = node;
                                }
                            }
                        }
                    }
                }

                if(minEdge.getStartNode() != null) {
                    Node nodeToSearchFor = null;
                    if (minEdge.getStartNode() != fromWhichNode)
                        nodeToSearchFor = minEdge.getStartNode();
                    else nodeToSearchFor = minEdge.getEndNode();

                    //merge components
                    int whichComponent = 0;
                    for (int i = 0; i < components.size(); ++i) {
                        if (components.get(i).contains(nodeToSearchFor)) {
                            for (var node : components.get(i)) {
                                components.get(currentComponent).add(node);
                            }
                            components.get(i).clear();
                            whichComponent = i;
                            break;
                        }
                    }

                    for (var line : components) {
                        for (var elem : line) {
                            System.out.print(elem.getNumber() + " ");
                        }
                        System.out.println();
                    }

                    if (!newEdges.get(whichComponent).isEmpty()) {
                        for (var edge : newEdges.get(whichComponent)) {
                            newEdges.get(currentComponent).add(edge);
                        }
                    }

                    newEdges.get(currentComponent).add(minEdge);

                }
                if (k == graph.getNodeList().size() - 2) {
                    return newEdges.get(currentComponent);
                }
            }
        return null;
    }

}
