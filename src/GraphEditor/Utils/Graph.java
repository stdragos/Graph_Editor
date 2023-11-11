package GraphEditor.Utils;

import GraphEditor.Panel;
import GraphEditor.algorithms.DFS;
import GraphEditor.models.Edge;
import GraphEditor.models.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Graph {
    private List<Node> nodeList;
    private List<Edge> edgeList;

    boolean directed = false;
    boolean firstTimeDirected = true;

    public DFS dfs;

    Panel panel;

    public Graph(Panel panel) {
        this.panel = panel;
        nodeList = new ArrayList<>();
        edgeList = new ArrayList<>();
        dfs = new DFS(this, panel);
    }

    public void addNode(int x, int y) {
        boolean canAddNode = true;
        int diam = 0;
        for (Node node : this.nodeList) {
            diam = node.getNode_diam();
            if (panel.calculateDistance(new Point(x - diam / 2, y - diam / 2), node.getPoint()) < 30)
                canAddNode = false;
        }

        if (canAddNode) {
            Node node = new Node(x - diam / 2, y - diam / 2, this.nodeList.size() + 1);
            this.nodeList.add(node);

            panel.repaint();
        }
    }

    public boolean addEdge(int a, int b) {
        Node A = nodeList.get(a - 1);
        Node B = nodeList.get(b - 1);
        if(!directed && !A.isAdjacent(B) && !B.isAdjacent(A)) {
            Edge edge = new Edge(nodeList.get(a - 1), nodeList.get(b - 1));
            edgeList.add(edge);

            A.insertAdjacent(B);
            A.getAdjacencyList().sort(Comparator.comparingInt(Node::getNumber));
            B.insertAdjacent(A);
            B.getAdjacencyList().sort(Comparator.comparingInt(Node::getNumber));

            panel.repaint();
            return true;
        } else if(directed) {
            if(!A.isAdjacent(B)) {
                Edge edge = new Edge(nodeList.get(a - 1), nodeList.get(b - 1));
                edgeList.add(edge);

                A.insertAdjacent(B);
                A.getAdjacencyList().sort(Comparator.comparingInt(Node::getNumber));
                return true;
            }
        }
        return false;
    }

    public void removeNode(Node a) {
        nodeList.remove(a);
        edgeList.removeIf(edge -> edge.getStartNode() == a || edge.getEndNode() == a);
        for(var node : nodeList) {
            node.removeAdjacent(a);
        }
        for(int i = 0; i < nodeList.size(); ++i) {
            if(nodeList.get(i).getNumber() != i + 1) {
                nodeList.get(i).setNumber(i + 1);
            }
        }
    }

    public void removeEdge(int A, int B) {
        Node a = nodeList.get(A - 1);
        Node b = nodeList.get(B - 1);

        for(int i = 0; i < edgeList.size(); ++i) {
            if(edgeList.get(i).getStartNode() == a && edgeList.get(i).getEndNode() == b) {
                nodeList.get(a.getNumber() - 1).removeAdjacent(b);
                if(!directed) {
                    nodeList.get(b.getNumber() - 1).removeAdjacent(a);
                }
                edgeList.remove(edgeList.get(i));
                break;
            }
        }

        if(!directed) {
            for(int i = 0; i < edgeList.size(); ++i) {
                if(edgeList.get(i).getStartNode() == b && edgeList.get(i).getEndNode() == a) {
                    nodeList.get(a.getNumber() - 1).removeAdjacent(b);
                    if(!directed) {
                        nodeList.get(b.getNumber() - 1).removeAdjacent(a);
                    }
                    edgeList.remove(edgeList.get(i));
                    break;
                }
            }
        }

    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public Node getNode(int a) {
        return nodeList.get(a);
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
        if(firstTimeDirected) {
            firstTimeDirected = false;
            for(var edge : edgeList) {
                panel.getGraph().addEdge(edge.getEndNode().getNumber(), edge.getStartNode().getNumber());
            }
        }
    }

    public boolean isDirected() {
        return directed;
    }


}
