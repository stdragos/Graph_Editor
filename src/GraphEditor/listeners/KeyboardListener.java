package GraphEditor.listeners;

import GraphEditor.Panel;
import GraphEditor.models.Edge;
import GraphEditor.models.Node;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KeyboardListener implements KeyListener {
    Panel panel;

    public KeyboardListener(Panel panel) {
        this.panel = panel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            panel.getGraph().setDirected(!panel.getGraph().isDirected());

            List<Edge> edgeList = panel.getGraph().getEdgeList();
            edgeList.clear();

            if(panel.getGraph().isDirected()) {
                for(var node1 : panel.getGraph().getNodeList()) {
                    for(var node2 : node1.getAdjacencyList()) {
                        edgeList.add(new Edge(node1, node2));
                    }
                }
            }
            else {
                for(var node1 : panel.getGraph().getNodeList()) {
                    for(var node2 : node1.getAdjacencyList()) {
                        boolean existsOrReverse = false;
                        for(var edge : edgeList) {
                            if(edge.getStartNode() == node1 && edge.getEndNode() == node2) {
                                existsOrReverse = true;
                                break;
                            }
                            if(edge.getStartNode() == node2 && edge.getEndNode() == node1) {
                                existsOrReverse = true;
                                break;
                            }
                        }
                        if(!existsOrReverse)
                            edgeList.add(new Edge(node1, node2));
                        if(!node2.getAdjacencyList().contains(node1)) {
                            node2.getAdjacencyList().add(node1);
                        }
                    }
                }
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_A) {
            new Thread(() -> {
                panel.getGraph().dfs.isAcyclic(true);
                panel.mouseListener.setStart(null);
                panel.mouseListener.setHighlightedNode(-1);
            }).start();

        }

        if(e.getKeyCode() == KeyEvent.VK_T) {

            new Thread(() -> {
                List<Node> sol = new ArrayList<>();
                if (panel.getDfs().isAcyclic(true)) {
                    sol = panel.getTopologicalSort().topologicalSort();

                    int panelWidth = (int) panel.getBounds().getWidth();
                    int panelHeight = (int) panel.getBounds().getHeight();

                    int nodeDistance = sol.get(0).getNode_diam() * 2;

                    //topologicalSort will display nodes in a line
                    int halfLineLength = (sol.size() * sol.get(0).getNode_diam() + nodeDistance * (sol.size() - 1)) / 2;

                    int startingY = panelHeight / 2 - 15;

                    int currentX = panelWidth / 2 - halfLineLength;

                    for (var node : sol) {
                        node.setCoordX(currentX);
                        currentX += node.getNode_diam() + nodeDistance;
                        node.setCoordY(startingY);
                    }

                    panel.repaint();
                }
                panel.mouseListener.setStart(null);
                panel.mouseListener.setHighlightedNode(-1);
            }).start();
        }

        if(e.getKeyCode() == KeyEvent.VK_D) {
            if(panel.mouseListener.getStart() != null){
                new Thread(() -> {
                    try {
                        panel.getDfs().dfs(panel.mouseListener.getStart(), true);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }).start();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_Q) {
            if(!panel.getGraph().getNodeList().isEmpty()) {
                try {
                    System.out.println(panel.getDfs().isQuasiStronglyConn());
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_E) {
            if(panel.mouseListener.getStart() != null) {
                panel.getGraph().removeNode(panel.mouseListener.getStart());
                panel.mouseListener.setHighlightedNode(-1);
                panel.mouseListener.setStart(null);
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_R) {
            panel.mouseListener.setHighlightedNode(-1);
            panel.mouseListener.setStart(null);

            if(!panel.getGraph().getNodeList().isEmpty())
            {
                new Thread(()->{
                try {
                    Node root = null;
                    if(panel.getGraph().dfs.isAcyclic(false))
                    {
                        if(panel.getGraph().dfs.isQuasiStronglyConn()) {
                            root = panel.getRootFinder().identifyRoot();
                            if(root != null) {
                                root.rootColor();
                                panel.repaint();
                                Thread.sleep(1500);
                                root.unhighlight();
                                panel.repaint();
                            } else {
                                panel.getGraph().nodeBlinkError();
                            }
                        } else {
                            panel.getGraph().nodeBlinkError();
                        }
                    } else{
                        panel.getGraph().nodeBlinkError();
                    }
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }}).start();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_C) {
            panel.mouseListener.setHighlightedNode(-1);
            panel.mouseListener.setStart(null);

            new Thread(() -> {
                try {
                    Random rand = new Random();
                    if(panel.getGraph().isDirected()) {
                        panel.getGraph().nodeBlinkError();
                        return;
                    }

                    List<List<Node>> ssc = panel.getDfs().connectedComponents();

                    for(var line : ssc) {
                        float r = rand.nextFloat();
                        float g = rand.nextFloat();
                        float b = rand.nextFloat();
                        Color randomColor = new Color(r, g, b);
                        for(var node : line) {
                            node.setNodeColor(randomColor);
                        }
                    }
                    panel.repaint();
                    Thread.sleep(100);
                    for(var node : panel.getGraph().getNodeList()) {
                        node.unhighlight();
                    }

                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

            }).start();
        }

        if(e.getKeyCode() == KeyEvent.VK_X) {
            List<List<Node>> ssc = panel.getKosaraju().getSSC();

            List<Edge> newEdgeList = new ArrayList<>();
            List<Node> newNodeList = new ArrayList<>();

            int startingDiam = 20;
            int increasingDiam = 20;

            int startingY = (int) (panel.getBounds().getHeight() / 2);
            int startingX = (int) (panel.getBounds().getWidth() / 2 - 100 * (ssc.size() / 2));

            int currSSC = 1;

            for(var line : ssc) {
                newNodeList.add(new Node(startingX, startingY - (startingDiam + increasingDiam * line.size()) / 2, currSSC));
                newNodeList.get(currSSC - 1).setNode_diam(startingDiam + increasingDiam * line.size());
                newNodeList.get(currSSC - 1).setNumberToPrint("");
                for(var node : line) {
                    newNodeList.get(currSSC - 1).addNumberToPrint(node.getNumber());
                }
                ++currSSC;
                startingX += startingDiam + increasingDiam * line.size() + 100;
            }

            for(int i = 0; i < newNodeList.size() - 1; ++i) {
                for(int j = i + 1; j < newNodeList.size(); ++j) {
                    boolean addedEdge = false;
                    for(var node1 : newNodeList.get(i).getNumbersHeld()) {
                        for(var node2 : newNodeList.get(j).getNumbersHeld()) {
                            for(var edge : panel.getGraph().getEdgeList()) {
                                if(edge.getStartNode().getNumber() == node1 &&
                                        edge.getEndNode().getNumber() == node2) {
                                    newEdgeList.add(new Edge(newNodeList.get(i), newNodeList.get(j)));
                                    addedEdge = true;
                                    break;
                                }
                                if(edge.getStartNode().getNumber() == node2 &&
                                        edge.getEndNode().getNumber() == node1) {
                                    newEdgeList.add(new Edge(newNodeList.get(j), newNodeList.get(i)));
                                    addedEdge = true;
                                    break;
                                }
                            }
                            if(addedEdge)
                                break;
                        }
                        if(addedEdge)
                            break;
                    }
                }
            }

            panel.getGraph().setEdgeList(newEdgeList);
            panel.getGraph().setNodeList(newNodeList);
            panel.repaint();
        }
        panel.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
