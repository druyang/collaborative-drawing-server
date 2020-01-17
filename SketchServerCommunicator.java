import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * Handles communication between the server and one client, for SketchServer
 *
 * @author Matthew Roth and Andrw Yang, CS 10 19F
 */
public class SketchServerCommunicator extends Thread {
	private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private SketchServer server;			// handling communication for

	public SketchServerCommunicator(Socket sock, SketchServer server) {
		this.sock = sock;
		this.server = server;
	}

	/**
	 * Sends a message to the client
	 * @param msg
	 */
	public void send(String msg) {
		out.println(msg);
	}
	
	/**
	 * Keeps listening for and handling (your code) messages from the client
	 */
	public void run() {
		try {
			System.out.println("someone connected");
			
			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			// Tell the client the current state of the world
			// TODO: YOUR CODE HERE
			String current = "c";
			for (Integer id : server.getSketch().getShapeTree().navigableKeySet()) {
				current+=" "+server.getSketch().getShapeTree().get(id).toString().substring(0, 1) + " " + id + " " + server.getSketch().getShapeTree().get(id).toString().substring(2);
			}
			send(current);

			// Keep getting and handling messages from the client
			// TODO: YOUR CODE HERE
			String line;
			while ((line = in.readLine()) != null) {
				Command command = new Command(line);
				command.parseCommand();
				// handle the different types of commands
				if (command.commandType.equals("a")) {
					int id = server.getId();
					if (command.shapeType.equals("ellipse")) {
						Ellipse newShape = new Ellipse(command.x1, command.y1, command.x2, command.y2,
								new Color(command.color));
						server.getSketch().addShape(id, newShape);
						server.broadcast("a"+" "+newShape.toString());
					} else if (command.shapeType.equals("rectangle")) {
						Rectangle newShape = new Rectangle(command.x1, command.y1,
								command.x2, command.y2, new Color(command.color));
						server.getSketch().addShape(id, newShape);
						server.broadcast("a"+" "+newShape.toString());
					} else if (command.shapeType.equals("segment")) {
						Segment newShape = new Segment(command.x1, command.y1,
								command.x2, command.y2, new Color(command.color));
						server.getSketch().addShape(id, newShape);
						server.broadcast("a"+" "+ newShape.toString());
					} else if (command.shapeType.equals("freehand")) {
						Polyline newShape = new Polyline(command.points, new Color(command.color));
						server.getSketch().addShape(id, newShape);
						server.broadcast("a"+" "+ newShape.toString());
					}
					server.incrementID();

				}
				else if (command.commandType.equals("m")) {
					server.getSketch().getShapeTree().get(command.id).moveBy(command.dx, command.dy);
					server.broadcast(line);
				}
				else if (command.commandType.equals("r")) {
					server.getSketch().getShapeTree().get(command.id).setColor(new Color(command.color));
					server.broadcast(line);
				}
				else if (command.commandType.equals("d")) {
					server.getSketch().getShapeTree().remove(command.id);
					server.broadcast(line);
				}
			}

			// Clean up -- note that also remove self from server's list so it doesn't broadcast here
			server.removeCommunicator(this);
			out.close();
			in.close();
			sock.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
