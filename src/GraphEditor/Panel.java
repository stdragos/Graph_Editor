package GraphEditor;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.geom.Point2D;

import GraphEditor.algorithms.*;
import GraphEditor.utils.Graph;
import GraphEditor.listeners.KeyboardListener;
import GraphEditor.listeners.MouseListener;
import GraphEditor.models.*;

public class Panel extends JPanel {
    private final Color backgroundColor = new Color(105, 142, 112);
    private int nodeNo = 1;
    Graph graph;
    TopologicalSort topologicalSort;
    DFS dfs;
    RootFinder rootFinder;
    Kosaraju kosaraju;
    GenericalMinTree genericalMinTree;
    Kruskal kruskal;
    Prim prim;

    private boolean directedUndirectedgraph = false; // false for undirected

    private boolean modifyingWeights = false;
    private Edge selectedEdge = null;

    Point pointStart;
    Point pointEnd;
    boolean isDragging;
    boolean isDraggingNode;
    Node whichNodeIsMoving;
    public volatile boolean workingThread = false;


    public MouseListener mouseListener;
    public KeyboardListener keyboardListener;

    public double calculateDistance(Point a, Point b) {
        return Point2D.distance(a.getX(), a.getY(), b.getX(), b.getY());
    }

    public Panel() {
        this.setBackground(backgroundColor);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.addMouseListener(mouseListener = new MouseListener(this));
        this.addMouseMotionListener(mouseListener);
        this.addKeyListener(keyboardListener = new KeyboardListener(this));

        graph = new Graph(this);
        topologicalSort = new TopologicalSort(graph);
        dfs = new DFS(graph, this);
        rootFinder = new RootFinder(graph, this);
        kosaraju = new Kosaraju(graph, this);
        genericalMinTree = new GenericalMinTree(this, graph);
        kruskal = new Kruskal(this, graph);
        prim = new Prim(this, graph);
    }

    public boolean isDragging() {
        return isDragging;
    }

    public boolean isDirectedUndirectedgraph() {
        return directedUndirectedgraph;
    }

    public boolean isDraggingNode() {
        return isDraggingNode;
    }

    public int getNodeNo() {
        return nodeNo;
    }

    public Node getWhichNodeIsMoving() {
        return whichNodeIsMoving;
    }

    public Point getPointEnd() {
        return pointEnd;
    }

    public Point getPointStart() {
        return pointStart;
    }

    public void setDirectedUndirectedgraph(boolean directedUndirectedgraph) {
        this.directedUndirectedgraph = directedUndirectedgraph;
    }

    public void setDraggingNode(boolean draggingNode) {
        isDraggingNode = draggingNode;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    public void setNodeNo(int nodeNo) {
        this.nodeNo = nodeNo;
    }

    public void setPointEnd(Point pointEnd) {
        this.pointEnd = pointEnd;
    }

    public void setPointStart(Point pointStart) {
        this.pointStart = pointStart;
    }

    public void setWhichNodeIsMoving(Node whichNodeIsMoving) {
        this.whichNodeIsMoving = whichNodeIsMoving;
    }

    public Graph getGraph() {
        return graph;
    }

    public TopologicalSort getTopologicalSort() {
        return topologicalSort;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(!graph.getEdgeList().isEmpty()) {
            for (var edge : graph.getEdgeList()) {
                edge.setDirected(graph.isDirected());
                edge.drawEdge(g);
            }
        }
        if(!graph.getNodeList().isEmpty()) {
            for (var node : graph.getNodeList()) {
                node.drawNode(g);
            }
        }
    }

    public void resetGraph() {
        graph = new Graph(this);
        topologicalSort = new TopologicalSort(graph);
        dfs = new DFS(graph, this);
        rootFinder = new RootFinder(graph, this);
        kosaraju = new Kosaraju(graph, this);
        genericalMinTree = new GenericalMinTree(this, graph);
        kruskal = new Kruskal(this, graph);
    }

    public DFS getDfs() {
        return dfs;
    }

    public RootFinder getRootFinder() {
        return rootFinder;
    }

    public Kosaraju getKosaraju() {
        return kosaraju;
    }

    public GenericalMinTree getGenericalMinTree() {
        return genericalMinTree;
    }

    public Kruskal getKruskal() {
        return kruskal;
    }

    public Prim getPrim() {
        return prim;
    }

    public boolean getModifyingWeights() {
        return this.modifyingWeights;
    }

    public void setModifyingWeights(boolean modifyingWeights) {
        this.modifyingWeights = modifyingWeights;
    }

    public Edge getSelectedEdge() {
        return selectedEdge;
    }

    public void setSelectedEdge(Edge selectedEdge) {
        this.selectedEdge = selectedEdge;
    }
}
