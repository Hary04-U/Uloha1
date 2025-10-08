package view;

import object.Line;
import object.Point;
import object.Polygon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Pane extends JPanel {

    private BufferedImage raster;
    private Point startPoint;
    private Point endPoint;
    private Point helpPoint = null;
    private Line usecka;
    private String rezim = "";
    private boolean drag = false;
    private boolean polygon = false;
    private Color barva = Color.CYAN;
    private ArrayList<Point> body = new ArrayList<>();
    private ArrayList<Polygon> polygony = new ArrayList<>();

    public Pane(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        raster = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(raster, 0, 0, null);
        g.setColor(Color.WHITE);
        g.drawString(rezim, 10, 20);

        // Preview čáry červenou barvou
        if (drag) {
            g.setColor(Color.RED);
            if (polygon && body.size() > 1) {
                g.drawLine(body.getLast().getX(), body.getLast().getY(), endPoint.getX(), endPoint.getY());
                g.drawLine(body.getFirst().getX(), body.getFirst().getY(), endPoint.getX(), endPoint.getY());
            } else if (polygon && body.size() == 1) {
                g.drawLine(body.getFirst().getX(), body.getFirst().getY(), endPoint.getX(), endPoint.getY());
            } else {
                if (helpPoint == null) {
                    g.drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
                } else {
                    g.drawLine(helpPoint.getX(), helpPoint.getY(), endPoint.getX(), endPoint.getY());
                }
            }
        }

        // Zobrazení aktualně tvořeného polygonu
        if (polygon) {
            g.setColor(Color.YELLOW);
            if (body.size() == 1) {
                g.drawLine(body.getFirst().getX(), body.getFirst().getY(), body.getFirst().getX(), body.getFirst().getY());
            }
            if (body.size() > 1) {
                for (int i = 1; i < body.size(); i++) {
                    g.drawLine(body.get(i-1).getX(), body.get(i-1).getY(), body.get(i).getX(), body.get(i).getY());
                }
            }
            if (body.size() > 2) {
                g.drawLine(body.getFirst().getX(), body.getFirst().getY(), body.getLast().getX(), body.getLast().getY());
            }
        }

        // Vykreslení všech vytvořených polygonů (abych pořád nevytvářel jen jeden)
        if (!polygony.isEmpty()) {
            for (Polygon poly : polygony) {
                poly.vykresliPolygon(g);
            }
        }
    }

    public BufferedImage getRaster() {
        return raster;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public Point getHelpPoint() {
        return helpPoint;
    }

    public Line getUsecka() {
        return usecka;
    }

    public String getRezim() {
        return rezim;
    }

    public boolean isDrag() {
        return drag;
    }

    public void setRaster(BufferedImage raster) {
        this.raster = raster;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public void setHelpPoint(Point helpPoint) {
        this.helpPoint = helpPoint;
    }

    public void setUsecka(Line usecka) {
        this.usecka = usecka;
    }

    public void setRezim(String rezim) {
        this.rezim = rezim;
    }

    public void setDrag(boolean drag) {
        this.drag = drag;
    }

    public void setPolygon(boolean polygon) {
        this.polygon = polygon;
    }

    public void setBarva(Color barva) {
        this.barva = barva;
    }

    public void setBody(ArrayList<Point> body) {
        this.body = body;
    }

    public void setPolygony(ArrayList<Polygon> polygony) {
        this.polygony = polygony;
    }

    public boolean isPolygon() {
        return polygon;
    }

    public Color getBarva() {
        return barva;
    }

    public ArrayList<Point> getBody() {
        return body;
    }

    public ArrayList<Polygon> getPolygony() {
        return polygony;
    }
}
