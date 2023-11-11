package GraphEditor.listeners;

import GraphEditor.Panel;
import GraphEditor.models.Edge;
import GraphEditor.models.Node;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MouseListener implements javax.swing.event.MouseInputListener {
    private Panel panel;
    private int highlightedNode;
    private int node_diam = 30;

    private boolean isDragging = false;
    private Node movingNode;

    private Node start;

    public MouseListener(Panel panel) {
        this.panel = panel;
        highlightedNode = -1;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        panel.setPointStart(e.getPoint());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        panel.setFocusable(true);
        panel.requestFocusInWindow();

        boolean clickedOnNode = false;
        int whichNode = -1;

        if(!isDragging)
        {
            for (var node : panel.getGraph().getNodeList()) {
                if (node.getDistance(new Point(e.getPoint().x - node_diam / 4, e.getPoint().y - node_diam / 4)) < node_diam - 5) {
                    clickedOnNode = true;
                    whichNode = node.getNumber();
                }
            }

            if (clickedOnNode) {
                if (highlightedNode == -1) {
                    start = panel.getGraph().getNodeList().get(whichNode - 1);
                    start.highlight();
                    highlightedNode = whichNode;
                } else if (highlightedNode == whichNode) {
                    panel.getGraph().getNodeList().get(whichNode - 1).unhighlight();
                    start = null;
                    highlightedNode = -1;
                } else {
                    if(!panel.getGraph().addEdge(highlightedNode, whichNode)){
                        panel.getGraph().removeEdge(highlightedNode, whichNode);
                    }
                    panel.getGraph().getNodeList().get(highlightedNode - 1).unhighlight();

                    start = null;
                    highlightedNode = -1;
                }
            } else {
                if(start != null) {
                    panel.getGraph().getNodeList().get(highlightedNode - 1).unhighlight();
                    start = null;
                    highlightedNode = -1;
                }
                panel.getGraph().addNode(e.getX(), e.getY());
            }
        }
        /*for(var node : panel.getGraph().getNodeList())
        {
            System.out.print(node.getNumber() + ":: ");
            for (var node2 : node.getAdjacencyList())
                System.out.print(node2.getNumber() + " ");
            System.out.println();
        }
        System.out.println();*/
        panel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(start != null && !isDragging && start.getNumber() == highlightedNode){
            if(start.getDistance(e.getPoint()) < 30) {
                isDragging = true;
                panel.repaint();
            }
        } else if(start != null && start.getNumber() == highlightedNode) {
            isDragging = true;

           for(var node : panel.getGraph().getNodeList()) {
                if(node != start &&
                        node.getDistance(new Point(e.getX() - start.getNode_diam() / 2, e.getY() - start.getNode_diam() / 2)) < node_diam){

                    double x = e.getX() - node.getCoordX() - start.getNode_diam() / 2;
                    double y = e.getY() - node.getCoordY() - start.getNode_diam() / 2;

                    if(x == 0) {
                        x = 1;
                    }
                    if(y == 0) {
                        y = 1;
                    }

                    double ipoLen = Math.sqrt(x * x + y * y);

                    int newX = (int) (node.getCoordX() + x / ipoLen * (node_diam));
                    int newY = (int) (node.getCoordY() + y / ipoLen * (node_diam));

                    //verify if not overlapping
                    for(var node2 : panel.getGraph().getNodeList()) {
                        if(node2!=start) {
                            if(node2!=node) {
                                if(node2.getDistance(new Point(newX, newY)) < node_diam)
                                {
                                    return;
                                }
                            }
                        }
                    }

                    panel.getGraph().getNode(highlightedNode - 1).setCoordX(newX);
                    panel.getGraph().getNode(highlightedNode - 1).setCoordY(newY);
                    panel.repaint();
                    return;
                }

            }

            panel.getGraph().getNode(highlightedNode - 1).setCoordX(e.getX() - start.getNode_diam() / 2);
            panel.getGraph().getNode(highlightedNode - 1).setCoordY(e.getY() - start.getNode_diam() / 2);
            panel.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public void setHighlightedNode(int highlightedNode) {
        this.highlightedNode = highlightedNode;
    }
}