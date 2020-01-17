import java.awt.*;
import java.util.ArrayList;
/**
 * Command.java is a class used for parsing command Strings
 *
 * @author Andrw Yang
 */

public class Command {
    // instance variables used for keeping data from various commands
    String commandString; Integer id; String shapeType, commandType; int dx, dy, color, x1, y1, x2, y2; ArrayList<Point> points;
    Sketch currentSketch; int lastID;

    public Command(String commandString) {
        this.commandString = commandString;
    }

    /**
     * Parse command, keep data in instance variables
     */
    public void parseCommand() {
        String[] commandArray = commandString.split(" ");
        commandType = commandArray[0];
        // command format for adding: a + shapeToString()
        if (commandArray[0].equals("a")) {
            if (!commandArray[1].equals("f")) {
                if (commandArray[1].equals("e")) {
                    shapeType = "ellipse";
                } else if (commandArray[1].equals("r")) {
                    shapeType = "rectangle";
                } else if (commandArray[1].equals("s")) {
                    shapeType = "segment";
                }
                x1 = Integer.parseInt(commandArray[1 + 1]);
                y1 = Integer.parseInt(commandArray[1 + 2]);
                x2 = Integer.parseInt(commandArray[1 + 3]);
                y2 = Integer.parseInt(commandArray[1 + 4]);
                color = Integer.parseInt(commandArray[1 + 5]);
            }
            // handle special case of freehand mode with special toString() method
            else if (commandArray[1].equals("f")) {
                shapeType = "freehand";
                color = Integer.parseInt(commandArray[1 + 1]);
                points = new ArrayList<>();
                for (int idx = 1 + 2; idx < commandArray.length; idx += 2) {
                    points.add(new Point(Integer.parseInt(commandArray[idx]), Integer.parseInt(commandArray[idx + 1])));
                }
            }
        }
        // for moving, keep track of shape id, dx, and dy
        else if (commandArray[0].equals("m")) {
            id = Integer.parseInt(commandArray[1]);
            dx = Integer.parseInt(commandArray[2]);
            dy = Integer.parseInt(commandArray[3]);
        }
        // for recoloring, keep track of shape id and new color
        else if (commandArray[0].equals("r")) {
            id = Integer.parseInt(commandArray[1]);
            color = Integer.parseInt(commandArray[2]);
        }
        // for deleting, keep track of shape id
        else if (commandArray[0].equals("d")) {
            id = Integer.parseInt(commandArray[1]);
        }
        // for informing new client on state of sketch, create a new sketch
        else if (commandArray[0].equals("c")) {
            if (commandArray.length > 1) {
                currentSketch = new Sketch();
                int shapeID = Integer.parseInt(commandArray[2]);
                int i = 1;
                // keep going until every string in command array has been seen
                while (i < commandArray.length) {
                    // handle case of freehand
                    if (commandArray[i].equals("f")) {
                        int n = i + 3;
                        color = Integer.parseInt(commandArray[i + 2]);
                        points = new ArrayList<>();
                        // keep adding points until new shape is reached
                        while (n < commandArray.length && !commandArray[n].equals("e") && !commandArray[n].equals("r") &&
                                !commandArray[n].equals("f") && !commandArray[n].equals("s")) {

                            points.add(new Point(Integer.parseInt(commandArray[n]), Integer.parseInt(commandArray[n + 1])));
                            n += 2;
                        }
                        currentSketch.getShapeTree().put(shapeID, new Polyline(points, new Color(color)));
                        if (n + 1 < commandArray.length) {
                            shapeID = Integer.parseInt(commandArray[n + 1]);
                            i = n;
                        } else {
                            // keep track of lastID for new client
                            lastID = shapeID;
                            break;
                        }
                    } else {
                        if (commandArray[i].equals("e")) {
                            currentSketch.getShapeTree().put(shapeID, new Ellipse(Integer.parseInt(commandArray[i + 2]),
                                    Integer.parseInt(commandArray[i + 3]), Integer.parseInt(commandArray[i + 4]),
                                    Integer.parseInt(commandArray[i + 5]),
                                    new Color(Integer.parseInt(commandArray[i + 6]))));
                        } else if (commandArray[i].equals("r")) {
                            currentSketch.getShapeTree().put(shapeID, new Rectangle(Integer.parseInt(commandArray[i + 2]),
                                    Integer.parseInt(commandArray[i + 3]), Integer.parseInt(commandArray[i + 4]),
                                    Integer.parseInt(commandArray[i + 5]),
                                    new Color(Integer.parseInt(commandArray[i + 6]))));

                        } else if (commandArray[i].equals("s")) {
                            currentSketch.getShapeTree().put(shapeID, new Segment(Integer.parseInt(commandArray[i + 2]),
                                    Integer.parseInt(commandArray[i + 3]), Integer.parseInt(commandArray[i + 4]),
                                    Integer.parseInt(commandArray[i + 5]),
                                    new Color(Integer.parseInt(commandArray[i + 6]))));

                        }
                        // go to next shape
                        if (i + 8 < commandArray.length) {
                            shapeID = Integer.parseInt(commandArray[i + 8]);
                            i += 7;
                        } else {
                            // keep track of lastID for new client
                            lastID = shapeID;
                            break;
                        }
                    }

                }
            }
        }
    }
}
