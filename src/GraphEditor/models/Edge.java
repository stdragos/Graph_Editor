package GraphEditor.models;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Edge {
    private Node startNode;
    private Node endNode;

    private boolean directed = false;

    private Color edgeColor = new Color(216, 156, 74);
    private final Color highlightColor =  new Color(255, 0, 0);
    private final Color basicColor = new Color(216, 156, 74);


    public Edge(Node startNode, Node endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public void drawEdge(Graphics g) {
        Point B = new Point(startNode.getCoordX() + startNode.getNode_diam() / 2, startNode.getCoordY() + startNode.getNode_diam() / 2);
        Point A = new Point(endNode.getCoordX() + endNode.getNode_diam() / 2, endNode.getCoordY() + endNode.getNode_diam() / 2);

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform initAT = g2d.getTransform();

        double angle = Math.atan2(B.y - A.y, B.x - A.x);

        AffineTransform affineTransform = AffineTransform.getTranslateInstance(B.x, B.y);
        affineTransform.concatenate(AffineTransform.getRotateInstance( angle - Math.PI));
        g2d.setTransform(affineTransform);

        Polygon arrowHead = new Polygon();

        g2d.setColor(edgeColor);
        g2d.setStroke(new BasicStroke(3));

        int lineLength = (int)Point2D.distance(A.getX(), A.getY(), B.getX(), B.getY());

        g2d.drawLine(0, 0, lineLength, 0);

        if(directed)
        {
            g2d.setColor(edgeColor);
            arrowHead.addPoint(10 + lineLength - endNode.getNode_diam() / 2 - 5, 0);
            arrowHead.addPoint(-10 + lineLength - endNode.getNode_diam() / 2 - 5, -10);
            arrowHead.addPoint(-10 + lineLength - endNode.getNode_diam() / 2 - 5, 10);
            g2d.fill(arrowHead);
        }

        g2d.setTransform(initAT);
        g2d.setStroke(new BasicStroke(1));
    }


    public Node getEndNode() {
        return endNode;
    }

    public boolean isDirected() {
        return directed;
    }

    public Node getStartNode() {
        return startNode;
    }


    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public void highlightEdge() {
        this.edgeColor = this.highlightColor;
    }

    public void unhighlightEdge() {
        this.edgeColor = this.basicColor;
    }
}
