import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

public class Sketch {
     TreeMap<Integer, Shape> shapeTree;

    public Sketch() {
        shapeTree = new TreeMap<>();
    }

    // don't want this to be interrupted by different threads
    public synchronized void addShape(Integer id, Shape shape) {
        shapeTree.put(id, shape);
    }

    // same here, always need to get same shapeTree of master-sketch
    public synchronized TreeMap<Integer, Shape> getShapeTree() {return shapeTree;}
}
