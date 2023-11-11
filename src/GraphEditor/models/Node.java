package GraphEditor.models;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private int coordX;
    private int coordY;
    private int number;

    private Node parent = null;

    private List<Node> adjacencyList;

    private Color nodeColor = new Color(169, 169, 169);
    private final Color highlightColor =  new Color(13, 135, 222);
    private final Color basicColor = new Color(169, 169, 169);
    private final Color rootColor = new Color(164, 43, 201);

    private int node_diam = 30;

    public Node(int coordX, int coordY, int number) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.number = number;
        this.adjacencyList = new ArrayList<>();
    }

    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public int getNumber() {
        return number;
    }

    public double getDistance(Point coord) {
        return Point2D.distance(coordX, coordY, coord.getX(), coord.getY());
    }

    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Point getPoint() {
        return new Point(this.coordX, this.coordY);
    }

    public void highlight() {
        this.nodeColor = highlightColor;
    }

    public void unhighlight() {
        this.nodeColor = basicColor;
    }

    public void error() {
        this.nodeColor = Color.RED;
    }

    public void good() {
        this.nodeColor = Color.GREEN;
    }

    public int getNode_diam() {
        return node_diam;
    }

    public boolean isAdjacent(Node a) {
        for(var node : this.adjacencyList) {
            if(a == node)
                return true;
        }

        return false;
    }

    public void insertAdjacent(Node a) {
        this.adjacencyList.add(a);
    }

    public void removeAdjacent(Node a) {
        this.adjacencyList.remove(a);
    }

    public void drawNode(Graphics g) {
        g.setColor(nodeColor);
        Font font = new Font("Arial", Font.BOLD, 15);
        g.setFont(font);
        g.fillOval(coordX, coordY, node_diam, node_diam);
        g.setColor(Color.black);
        g.drawOval(coordX, coordY, node_diam, node_diam);
        g.setColor(Color.black);
        if(this.number < 10)
            g.drawString(((Integer)this.number).toString(), coordX+12, coordY + 20);
        else
            g.drawString(((Integer)this.number).toString(), coordX+8, coordY + 20);
    }

    public List<Node> getAdjacencyList() {
        return adjacencyList;
    }

    public int getAdjacencyNumber() {
        return adjacencyList.size();
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void rootColor() {
        this.nodeColor = rootColor;
    }
}
