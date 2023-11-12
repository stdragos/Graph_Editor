package GraphEditor.algorithms;

import GraphEditor.Panel;
import GraphEditor.models.Node;
import GraphEditor.utils.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Kosaraju {
    private Panel panel;
    private Graph graph;
    List<List<Node>> reverseAdjacency = new ArrayList<>();

    public Kosaraju(Graph graph, Panel panel) {
        this.panel = panel;
        this.graph = graph;
    }

    private void transposeAdjacency() {
        reverseAdjacency.clear();
        for(int i = 0; i < graph.getNodeList().size(); ++i) {
            List<Node> tmp = new ArrayList<>();
            reverseAdjacency.add(tmp);
        }

        for(var node : graph.getNodeList()) {
            for(var neigh : node.getAdjacencyList()) {
                reverseAdjacency.get(neigh.getNumber() - 1).add(node);
            }
        }
    }

    public List<List<Node>> getSSC() {
        List<List<Node>> SCC = new ArrayList<>();
        List<Boolean> visited = new ArrayList<>(Collections.nCopies(graph.getNodeList().size(), false));
        Stack<Node> frontier = new Stack<>();
        Stack<Node> commonStack = new Stack<>();

        for (var node : graph.getNodeList()) {
            if (!visited.get(node.getNumber() - 1)) {

                frontier.push(node);
                visited.set(node.getNumber() - 1, true);

                while (!frontier.isEmpty()) {
                    Node top = frontier.peek();
                    boolean foundNeigh = false;
                    for (var neigh : top.getAdjacencyList()) {
                        if (!visited.get(neigh.getNumber() - 1)) {
                            frontier.push(neigh);
                            visited.set(neigh.getNumber() - 1, true);
                            foundNeigh = true;
                            break;
                        }
                    }
                    if(foundNeigh)
                        continue;
                    commonStack.push(top);
                    frontier.pop();
                }
            }
        }

        transposeAdjacency();
        visited.clear();
        visited = new ArrayList<>(Collections.nCopies(graph.getNodeList().size(), false));
        int currentSCC = -1;

        while(!commonStack.isEmpty()) {
                List<Node> tmp = new ArrayList<>();
                SCC.add(tmp);

                ++currentSCC;

                Node commonTop = commonStack.peek();
                commonStack.pop();
                if(!visited.get(commonTop.getNumber() - 1)) {

                    visited.set(commonTop.getNumber() - 1, true);
                    frontier.push(commonTop);

                    while (!frontier.isEmpty()) {
                        Node top = frontier.peek();
                        boolean foundNeigh = false;
                        for (var neigh : reverseAdjacency.get(top.getNumber() - 1)) {
                            if (!visited.get(neigh.getNumber() - 1)) {
                                frontier.push(neigh);
                                visited.set(neigh.getNumber() - 1, true);
                                foundNeigh = true;
                                break;
                            }
                        }
                        if(foundNeigh)
                            continue;
                        SCC.get(currentSCC).add(top);
                        frontier.pop();
                    }

            }

        }
        for(int i = 0; i < SCC.size(); ++i) {
            if(SCC.get(i).isEmpty()) {
                SCC.remove(i);
                --i;
            }
        }
        return SCC;
    }
}