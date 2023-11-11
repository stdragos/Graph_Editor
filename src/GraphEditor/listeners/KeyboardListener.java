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
                        //Node start = new Node(panel.mouseListener.getStart());
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
                            /*System.out.println("test");*/
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
            Random rand = new Random();
            List<List<Node>> ssc = panel.getKosaraju().getSSC();
            new Thread(() -> {
                try {
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
    public void keyReleased(KeyEvent e) {

    }
}
