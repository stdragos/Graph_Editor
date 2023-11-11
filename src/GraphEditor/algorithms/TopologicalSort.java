package GraphEditor.algorithms;

import GraphEditor.utils.Graph;
import GraphEditor.models.Node;

import java.util.*;

public class TopologicalSort {
    Graph graph;

    public TopologicalSort(Graph graph) {
        this.graph = graph;
    }

    public List<Node> topologicalSort() {
        Stack<Node> stackSolution = new Stack<>();
        List<Boolean> visited = new ArrayList<Boolean>(Collections.nCopies(graph.getNodeList().size(), false));

        Stack<Node> toVisit = new Stack<>();

        for(var node : graph.getNodeList()) {
            if(!visited.get(node.getNumber() - 1)) {
                toVisit.push(node);
                visited.set(node.getNumber() - 1, true);

                while(!toVisit.isEmpty()) {
                    Node top = toVisit.peek();
                    boolean foundNeighbour = false;
                    for(var neigh : top.getAdjacencyList()) {
                        if(!visited.get(neigh.getNumber() - 1)) {
                            toVisit.push(neigh);
                            visited.set(neigh.getNumber() - 1, true);
                            foundNeighbour = true;
                            break;
                        }
                    }
                    if(foundNeighbour)
                        continue;

                    toVisit.pop();
                    stackSolution.push(top);
                }
            }
        }

        List<Node> listSolution = new ArrayList<>();
        while(!stackSolution.isEmpty()) {
            listSolution.add(stackSolution.pop());
        }

        return listSolution;
    }
}
