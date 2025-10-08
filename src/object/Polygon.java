package object;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private List<Point> body = new ArrayList<>();
    private Color barva = Color.CYAN;

    public Polygon(List<Point> body) {
        this.body = body;
    }

    public void vykresliPolygon (Graphics g) {
        if (body.size() > 1) {
            for (int i = 1; i < body.size(); i++) {
                g.setColor(barva);
                g.drawLine(body.get(i-1).getX(), body.get(i-1).getY(), body.get(i).getX(), body.get(i).getY());
            }
        }
        if (body.size() > 2) {
            g.setColor(barva);
            g.drawLine(body.getFirst().getX(), body.getFirst().getY(), body.getLast().getX(), body.getLast().getY());
        }
        if (body.size() > 3) {
            g.setColor(Color.BLACK);
            g.drawLine(body.getFirst().getX(), body.getFirst().getY(), body.get(body.size()-2).getX(), body.get(body.size()-2).getY());

        }
    }

    public void addPoint(Point p) {
        body.add(p);
    }

    public Point getPoint(int index) {
        return body.get(index);
    }

    public int getSize() {
        return body.size();
    }

    public void addPointToIndex(int index, Point p) {
        body.add(index, p);
    }

    public void removePoint(int index) {
        body.remove(index);
    }

    public Point getFirstPoint() {
        return body.getFirst();
    }

    public Point getLastPoint() {
        return body.getLast();
    }
}

