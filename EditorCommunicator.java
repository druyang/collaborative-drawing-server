import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * Handles communication to/from the server for the editor
 * 
 * @author Matthew Roth and Andrw Yang
 */
public class EditorCommunicator extends Thread {
	private PrintWriter out;		// to server
	private BufferedReader in;		// from server
	protected Editor editor;		// handling communication for

	/**
	 * Establishes connection and in/out pair
	 */
	public EditorCommunicator(String serverIP, Editor editor) {
		this.editor = editor;
		System.out.println("connecting to " + serverIP + "...");
		try {
			Socket sock = new Socket(serverIP, 4242);
			out = new PrintWriter(sock.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			System.out.println("...connected");
		}
		catch (IOException e) {
			System.err.println("couldn't connect");
			System.exit(-1);
		}
	}

	/**
	 * Sends message to the server
	 */
	public void send(String msg) {
		out.println(msg);
	}

	/**
	 * Keeps listening for and handling (your code) messages from the server
	 */
	public void run() {
		try {
			// Handle messages
			// TODO: YOUR CODE HERE
			String line; Command command;
			// should this keep running like the other?
			while ((line = in.readLine()) != null) {
				command = new Command(line);
				command.parseCommand();
				// handle the different types of commands
				if (command.commandType.equals("a")) {
					int id = editor.getId();
					if (command.shapeType.equals("ellipse")) {
						editor.getSketch().getShapeTree().put(id, new Ellipse(command.x1, command.y1,
								command.x2, command.y2, new Color(command.color)));
					}
					else if (command.shapeType.equals("rectangle")) {
						editor.getSketch().getShapeTree().put(id, new Rectangle(command.x1, command.y1,
								command.x2, command.y2, new Color(command.color)));

					}
					else if (command.shapeType.equals("segment")) {
						editor.getSketch().getShapeTree().put(id, new Segment(command.x1, command.y1,
								command.x2, command.y2, new Color(command.color)));
					}
					else if (command.shapeType.equals("freehand")) {
						editor.getSketch().getShapeTree().put(id, new Polyline(command.points, new Color(command.color)));
					}
					editor.repaint();
					editor.incrementID();
				}
				else if (command.commandType.equals("m")) {
					editor.getSketch().getShapeTree().get(command.id).moveBy(command.dx, command.dy);
					editor.repaint();
				}
				else if (command.commandType.equals("r")) {
					editor.getSketch().getShapeTree().get(command.id).setColor(new Color(command.color));
					editor.repaint();
				}
				else if (command.commandType.equals("d")) {
					editor.getSketch().getShapeTree().remove(command.id);
					editor.repaint();
				}
				else if (command.commandType.equals("c")) {
					if (command.currentSketch!=null) {
						editor.getSketch().shapeTree = command.currentSketch.getShapeTree();
						editor.id = command.lastID + 1;
						editor.repaint();
					}
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("server hung up");
		}
	}	

	// Send editor requests to the server
	// not sure what is supposed go here, editor requests to server are sent from editor.java
	// TODO: YOUR CODE HERE
	
}
