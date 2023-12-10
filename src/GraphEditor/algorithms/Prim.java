package GraphEditor.algorithms;

import GraphEditor.Panel;
import GraphEditor.models.Edge;
import GraphEditor.models.Node;
import GraphEditor.utils.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

class CostStruct {
    public Integer cost;
    public Node node;

    public CostStruct(Integer cost, Node node) {
        this.cost = cost;
        this.node = node;
    }
}


public class Prim {
    Panel panel;
    Graph graph;

    public Prim(Panel panel, Graph graph) {
        this.panel = panel;
        this.graph = graph;
    }

    public List<Edge> getMinTree() {
        List<CostStruct> costs = new ArrayList<>();
        List<Node> resultTree = new ArrayList<>();
        List<Edge> edges = new ArrayList<>(Collections.nCopies(graph.getNodeList().size(), null));
        List<Node> unusedNodes = new ArrayList<>(graph.getNodeList());
        List<Edge> resultEdges = new ArrayList<>();

        unusedNodes.sort((node1, node2) -> {
            return node1.getNumber() - node2.getNumber();
        });

        for (var node : unusedNodes) {
           costs.add(new CostStruct(Integer.MAX_VALUE, node));
        }

        costs.get(0).cost = 0;


        while (resultTree.size() != graph.getNodeList().size()) {
            int minCost = Integer.MAX_VALUE;
            Node minNode = null;
            for (var entry : costs) {
                if (minCost >= entry.cost && !resultTree.contains(entry.node)) {
                    minCost = entry.cost;
                    minNode = entry.node;
                }
            }

            resultTree.add(minNode);
            unusedNodes.remove(minNode);

            if(minNode.getNumber() != 1) {
                resultEdges.add(edges.get(minNode.getNumber() - 1));
            }

            for(var node : resultTree) {
                for(var neighbour : node.getAdjacencyList()) {
                    if(unusedNodes.contains(neighbour)) {
                        if(costs.get(neighbour.getNumber() - 1).cost >
                        node.getWeightList().get(neighbour.getNumber()).getWeight()) {
                            costs.get(neighbour.getNumber() - 1).cost = node.getWeightList().get(neighbour.getNumber()).getWeight();
                            edges.set(neighbour.getNumber() - 1, node.getWeightList().get(neighbour.getNumber()));
                        }
                    }
                }
            }
        }
        for(var entry : costs) {
            System.out.println(entry.node.getNumber() + " " + entry.cost);
        }
        return resultEdges;
    }
}
