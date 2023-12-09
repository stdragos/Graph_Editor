package GraphEditor.algorithms;

import GraphEditor.Panel;
import GraphEditor.models.Edge;
import GraphEditor.models.Node;
import GraphEditor.utils.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class GenericalMinTree {
    private Panel panel;
    private Graph graph;

    GenericalMinTree(Panel panel, Graph graph) {
        this.panel = panel;
        this.graph = graph;
    }

    public List<Edge> getMinTree() {
        List<List<Node>> components = new ArrayList<>();
        List<List<Edge>> newEdges = new ArrayList<>();
        for (var node : graph.getNodeList()) {
            List<Node> tmp1 = new ArrayList<>();
            tmp1.add(node);
            components.add(tmp1);

            List<Edge> tmp2 = new ArrayList<>();
            newEdges.add(tmp2);
        }

        for (int k = 0; k < graph.getNodeList().size() - 1; ++k) {
            int currentComponent = 0;
            for (int i = 0; i < components.size(); ++i) {
                if (components.get(i).size() != 0) {
                    currentComponent = i;
                    break;
                }
            }

                Stack<Edge> currentEdges = new Stack<>();
                for(var node : components.get(currentComponent)) {
                    //de adaugat in currentEdges edgeurile care nu leaga cu alte noduri din aceeasi componenta
                    /*for(var entry : node.getWeightList().entrySet()) {
                        for()
                    }*/
                }

            }
        return null;
    }

}
