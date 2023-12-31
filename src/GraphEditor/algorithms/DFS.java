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
        List<Node> paths = new ArrayList<>();
        List<Boolean> visited = new ArrayList<Boolean>(Collections.nCopies(graph.getNodeList().size(), false));

        Stack<Node> toVisit = new Stack<>();
        visited.set(root.getNumber() - 1, true);

        if(draw) {
            root.setHighlightDFS();
            panel.repaint();
            Thread.sleep(500);
        }

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
                            if(edge.getStartNode() == top && edge.getEndNode() == neigh ||
                                    (!graph.isDirected() && edge.getStartNode() == neigh && edge.getEndNode() == top)) {
                                edge.highlightEdge();
                                neigh.setHighlightDFS();
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

            paths.add(top);
            toVisit.pop();
        }

        for(var node : graph.getNodeList())
            node.resetHighlightDFS();
        return paths;
    }

    public boolean isAcyclic(boolean paint) {
        List<Integer> visited = new ArrayList<Integer>(Collections.nCopies(graph.getNodeList().size(), 0));
        for(var node : graph.getNodeList()) {
            if(visited.get(node.getNumber() - 1) == 0) {
                Queue<Node> frontier = new LinkedList<>();
                frontier.add(node);

                while(!frontier.isEmpty()) {
                    Node top = frontier.poll();
                    visited.set(top.getNumber() - 1, visited.get(top.getNumber() - 1) + 1);
                    if(visited.get(top.getNumber() - 1) >= 2 * visited.size()) {
                        if(paint) {
                            try {
                                graph.nodeBlinkError();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return false;
                    }

                    frontier.addAll(top.getAdjacencyList());
                }
            }
        }

        if(paint) {
            try {
                graph.nodeBlinkGood();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    public boolean isQuasiStronglyConn() throws InterruptedException {
        for(var node : graph.getNodeList()) {
            List<Node> path = dfs(node, false);
            if (path.size() == graph.getNodeList().size()) {
                return true;
            }
        }
        return false;
    }

    public List<List<Node>> connectedComponents() {
        List<List<Node>> CC = new ArrayList<>();
        List<Boolean> visited = new ArrayList<Boolean>(Collections.nCopies(graph.getNodeList().size(), false));
        Stack<Node> frontier = new Stack<>();
        int currentCC = 0;
        for(var node : graph.getNodeList()) {
            if(!visited.get(node.getNumber() - 1)) {
                frontier.push(node);
                visited.set(node.getNumber() - 1, true);

                List<Node> tmp = new ArrayList<>();
                CC.add(tmp);

                while(!frontier.isEmpty()) {
                    Node top = frontier.peek();
                    boolean foundNeighbour = false;
                    for(var neigh : top.getAdjacencyList()) {
                        if(!visited.get(neigh.getNumber() - 1)) {
                            frontier.push(neigh);
                            visited.set(neigh.getNumber() - 1, true);
                            foundNeighbour = true;
                            break;
                        }
                    }
                    if(foundNeighbour)
                        continue;

                    CC.get(currentCC).add(top);
                    frontier.pop();

                }
                ++currentCC;
            }
        }
        return CC;
    }
}
