package GraphEditor.models;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Node {
    private int coordX;
    private int coordY;
    private int number;

    private String numberToPrint = "";
    private List<Integer> numbersHeld = new ArrayList<>();

    private Node parent = null;

    private List<Node> adjacencyList;
    private HashMap<Integer, Integer> weightList; //<nodeNumber, weight>

    private Color nodeColor = new Color(169, 169, 169);
    private final Color highlightColor =  new Color(13, 135, 222);
    private final Color basicColor = new Color(169, 169, 169);
    private final Color rootColor = new Color(151, 112, 32);

    Color currBorderColor = Color.black;
    Color basicBorderColor = Color.black;
    Color highlightBorderColor = new Color(118, 67, 197);
    int borderSize = 1;
    private int node_diam = 30;

    public Node(int coordX, int coordY, int number) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.number = number;
        this.adjacencyList = new ArrayList<>();
        this.numberToPrint += number;
        weightList = new HashMap<>();
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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(nodeColor);
        Font font = new Font("Arial", Font.BOLD, 15);
        g2d.setFont(font);
        g2d.fillOval(coordX, coordY, node_diam, node_diam);

        g2d.setStroke(new BasicStroke(borderSize));
        g2d.setColor(currBorderColor);
        g2d.drawOval(coordX, coordY, node_diam, node_diam);
        g2d.setStroke(new BasicStroke(1));

        g2d.setColor(Color.black);
        //circle center : coordX + node_diam / 2, coordY + node_diam / 2
        int numberLen = numberToPrint.length() * 8;
        int startingX = coordX + node_diam / 2 - numberLen / 2;
        int startingY = coordY + node_diam / 2 + 5;
        g2d.drawString(numberToPrint, startingX, startingY);
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

    public void setNodeColor(Color color) {
        this.nodeColor = color;
    }

    public void setNode_diam(int node_diam) {
        this.node_diam = node_diam;
    }

    public void addNumberToPrint(int number) {
        if(Objects.equals(this.numberToPrint, "")) {
            this.numberToPrint += number;
        } else {
            this.numberToPrint += "," + number;
        }
        numbersHeld.add(number);

    }

    public void setNumberToPrint(String numberToPrint) {
        this.numberToPrint = numberToPrint;

    }

    public void setHighlightDFS() {
        borderSize = 4;
        currBorderColor = highlightBorderColor;
    }

    public void resetHighlightDFS() {
        borderSize = 1;
        currBorderColor = basicBorderColor;
    }
    public List<Integer> getNumbersHeld() {
        return numbersHeld;
    }

    public HashMap<Integer, Integer> getWeightList() {
        return weightList;
    }

    public void addWeightNeighbour(Node node, int weight) {
        this.weightList.put(node.getNumber(), weight);
    }

    public void resetWeightList() {
        this.weightList = new HashMap<>();
    }
}
