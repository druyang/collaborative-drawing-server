import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A multi-segment Shape, with straight lines connecting "joint" points -- (x1,y1) to (x2,y2) to (x3,y3) ...
 * 
 * @author Matthew Roth and Andrw Yang, CS 10 19F
 */
public class Polyline implements Shape {
	// TODO: YOUR CODE HERE
	ArrayList<Point> points = new ArrayList<>();
	Color color;

	public Polyline(Point p1, Color color) {
		points.add(p1); this.color = color;
	}
	public Polyline(ArrayList<Point> points, Color color) {
		this.points = points; this.color = color;
	}
	@Override
	public void moveBy(int dx, int dy) {
		for (Point point : points) {
			point.x = (int)point.getX() + dx;
			point.y = (int)point.getY() + dy;
		}
	}

	@Override
	public Color getColor() {return color;}

	@Override
	public void setColor(Color color) {this.color = color;}
	
	@Override
	public boolean contains(int x, int y) {
		Point curr, next;
		for (int i = 0; i < points.size() - 1; i++) {
			curr = points.get(i);
			next = points.get(i + 1);
			// give a little margin of error for "contains"
			if (Segment.pointToSegmentDistance(x, y, (int)curr.getX(), (int)curr.getY(), (int)next.getX(), (int)next.getY()) <= 5) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void draw(Graphics g) {
		Segment segment;
		Point curr, next;
		for (int i = 0; i < points.size() - 1; i++) {
			curr = points.get(i); next = points.get(i + 1);
			segment = new Segment((int)curr.getX(), (int)curr.getY(), (int)next.getX(), (int)next.getY(), color);
			segment.draw(g);
		}
	}

	@Override
	public String toString() {
		String string = "f ";
		string+=color.getRGB();
		for (Point point : points) {
			string+= " " + (int)point.getX() + " " + (int)point.getY();
		}
		return string;
	}
}
