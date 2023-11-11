package GraphEditor.algorithms;

import GraphEditor.Panel;
import GraphEditor.models.Node;
import GraphEditor.utils.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class RootFinder {
    private Graph graph;
    private Panel panel;

    public RootFinder(Graph graph, Panel panel) {
        this.graph = graph;
        this.panel = panel;
    }

    public Node identifyRoot() {
        List<Boolean> visited = new ArrayList<>(Collections.nCopies(graph.getNodeList().size(), false));

        for(var node : graph.getNodeList()) {
            Stack<Node> frontier = new Stack<>();
            frontier.push(node);

            while(!frontier.isEmpty()) {
                boolean foundNeighbour = false;

                for(var neighbour : node.getAdjacencyList()) {
                    if(!visited.get(neighbour.getNumber() - 1)) {
                        frontier.push(neighbour);
                        visited.set(neighbour.getNumber() - 1, true);
                        foundNeighbour = true;
                        break;
                    }
                }
                if(foundNeighbour)
                    continue;

                frontier.pop();
            }
        }

        boolean foundRoot = false;
        Node root = null;

        for(int i = 0; i < visited.size(); ++i) {
            if(!visited.get(i) && !foundRoot) {
                foundRoot = true;
                root = graph.getNodeList().get(i);
            } else if (!visited.get(i) && foundRoot) {
                return null;
            }
        }

        return root;
    }
}
