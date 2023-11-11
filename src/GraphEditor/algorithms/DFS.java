package GraphEditor.algorithms;

import GraphEditor.Panel;
import GraphEditor.utils.Graph;
import GraphEditor.models.Node;

import java.util.*;

public class DFS {
    private Graph graph;
    private Panel panel;

    public DFS(Graph graph, Panel panel){
        this.graph = graph;
        this.panel = panel;
    }

    public List<Node> dfs(Node root, boolean draw) throws InterruptedException {
        //returns DFS from root
        List<Node> paths = new ArrayList<>();
        List<Boolean> visited = new ArrayList<Boolean>(Collections.nCopies(graph.getNodeList().size(), false));

        Stack<Node> toVisit = new Stack<>();

        toVisit.push(root);
        while(!toVisit.isEmpty()) {
            Node top = toVisit.peek();
            boolean foundNeighbour = false;
            for(var neigh : top.getAdjacencyList()) {
                if(!visited.get(neigh.getNumber() - 1)) {
                    toVisit.push(neigh);
                    visited.set(neigh.getNumber() - 1, true);
                    foundNeighbour = true;
                    if(draw) {
                        for(var edge : graph.getEdgeList()) {
                            if(edge.getStartNode() == top && edge.getEndNode() == neigh) {

                                edge.highlightEdge();
                                panel.repaint();
                                Thread.sleep(500);
                                edge.unhighlightEdge();
                                panel.repaint();
                                Thread.sleep(500);
                            }
                        }
                    }
                    break;
                }
            }
            if(foundNeighbour)
                continue;

            toVisit.pop();
        }
        return paths;
    }

    public boolean isAcyclic(boolean paint) {
        List<Integer> visited = new ArrayList<Integer>(Collections.nCopies(graph.getNodeList().size(), 0));
        for(var node : graph.getNodeList()) {
            if(visited.get(node.getNumber() - 1) == 0) {
                Stack<Node> frontier = new Stack<>();
                frontier.push(node);

                while(!frontier.empty()) {
                    Node top = frontier.peek();
                    frontier.pop();
                    visited.set(top.getNumber() - 1, visited.get(top.getNumber() - 1) + 1);
                    if(visited.get(top.getNumber() - 1) >= visited.size() + 1) {
                        if(paint) {
                            try {
                                for(var nodes : graph.getNodeList()) {
                                    nodes.error();
                                }
                                panel.repaint();
                                Thread.sleep(500);
                                for(var nodes : graph.getNodeList()) {
                                    nodes.unhighlight();
                                }
                                panel.repaint();
                                Thread.sleep(500);
                                for(var nodes : graph.getNodeList()) {
                                    nodes.error();
                                }
                                panel.repaint();
                                Thread.sleep(500);
                                for(var nodes : graph.getNodeList()) {
                                    nodes.unhighlight();
                                }
                                panel.repaint();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return false;
                    }

                    for(var neighbours : top.getAdjacencyList())
                        frontier.push(neighbours);
                }
            }
        }

        if(paint) {
            try {
                for(var nodes : graph.getNodeList()) {
                    nodes.good();
                }
                panel.repaint();
                Thread.sleep(500);
                for(var nodes : graph.getNodeList()) {
                    nodes.unhighlight();
                }
                panel.repaint();
                Thread.sleep(500);
                for(var nodes : graph.getNodeList()) {
                    nodes.good();
                }
                panel.repaint();
                Thread.sleep(500);
                for(var nodes : graph.getNodeList()) {
                    nodes.unhighlight();
                }
                panel.repaint();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
