package view;

import model.object.Line;
import model.object.Point;
import model.object.Polygon;
import model.rasterizer.LineRasterizer;
import model.rasterizer.PolygonRasterizer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.awt.Color.*;

public class Pane extends JPanel {

    private final BufferedImage raster;
    private final BufferedImage previewRaster;
    private final LineRasterizer liner;
    private final PolygonRasterizer polygonLiner;
    private final ArrayList<Point> body = new ArrayList<>();
    private final ArrayList<Polygon> polygony = new ArrayList<>();
    private final ArrayList<Line> lines = new ArrayList<>();
    private Point startPoint;
    private Point endPoint = null;
    private Point helpPoint = null;
    private Point editPoint = null;
    private Line line;
    private String rezim = "";
    private boolean drag = false;
    private boolean polygon = false;
    private boolean edit = false;
    private boolean interpolate = false;
    private boolean gradient = false;
    private int bodyIndex;
    private Color barva = CYAN;
    private Color barvaG = null;
    private Color vychoziBarva = barva;
    private int pixelRadius = 3;

    public Pane(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        raster = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        previewRaster = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        liner = new LineRasterizer(raster);
        polygonLiner = new PolygonRasterizer(liner);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!polygony.isEmpty()) {
            for (Polygon poly : polygony) {
                polygonLiner.vykresliPolygon(raster, poly);
            }
        }

        if (!lines.isEmpty()) {
            for (Line line : lines) {
                liner.vykresliLineDDA(line, raster);
            }
        }
        System.out.println(barva + " " + barvaG);

        g.drawImage(raster, 0, 0, null);

        Graphics2D gPreview = previewRaster.createGraphics();
        gPreview.setComposite(AlphaComposite.Clear);
        gPreview.fillRect(0, 0, previewRaster.getWidth(), previewRaster.getHeight());
        gPreview.dispose();

        g.setColor(WHITE);
        g.drawString(rezim, 10, 20);
        if (polygon && !interpolate && !gradient) {
            g.drawString("Polygon -> funkce zapnuta", 10, 20);
        } else if (polygon && (interpolate || gradient)) {
            g.drawString("Polygon -> funkce zapnuta", 10, 40);
        }

        if (drag) {
            if (polygon) {
                if (edit) {
                    if (body.size() == 1) {
                        line = new Line(editPoint, editPoint, RED, barvaG);
                        liner.vykresliLineDDA(line, previewRaster);
                    } else if (body.size() == 2) {
                        if (bodyIndex == 0) {
                            line = new Line(editPoint, body.getLast(), RED, barvaG);
                            liner.vykresliLineDDA(line, previewRaster);
                        } else if (bodyIndex == 1) {
                            line = new Line(editPoint, body.getFirst(), RED, barvaG);
                            liner.vykresliLineDDA(line, previewRaster);
                        }
                    } else if (body.size() > 2) {
                        if (bodyIndex == body.size() - 1) {
                            line = new Line(editPoint, body.get(bodyIndex - 1), RED, barvaG);
                            liner.vykresliLineDDA(line, previewRaster);
                            line = new Line(editPoint, body.getFirst(), RED, barvaG);
                            liner.vykresliLineDDA(line, previewRaster);
                        } else if (bodyIndex == 0) {
                            line = new Line(editPoint, body.get(bodyIndex + 1), RED, barvaG);
                            liner.vykresliLineDDA(line, previewRaster);
                            line = new Line(editPoint, body.getLast(), RED, barvaG);
                            liner.vykresliLineDDA(line, previewRaster);
                        } else {
                            line = new Line(editPoint, body.get(bodyIndex - 1), RED, barvaG);
                            liner.vykresliLineDDA(line, previewRaster);
                            line = new Line(editPoint, body.get(bodyIndex + 1), RED, barvaG);
                            liner.vykresliLineDDA(line, previewRaster);
                        }
                    }
                } else {
                    if (endPoint != null) {
                        if (body.size() > 1) {
                            line = new Line(body.getLast(), endPoint, RED, barvaG);
                            liner.vykresliLineDDA(line, previewRaster);
                            line = new Line(body.getFirst(), endPoint, RED, barvaG);
                            liner.vykresliLineDDA(line, previewRaster);
                        } else if (body.size() == 1) {
                            line = new Line(body.getFirst(), endPoint, RED, barvaG);
                            liner.vykresliLineDDA(line, previewRaster);
                        } else {
                            if (helpPoint == null) {
                                line = new Line(startPoint, endPoint, RED, barvaG);
                                liner.vykresliLineDDA(line, previewRaster);
                            } else {
                                line = new Line(helpPoint, endPoint, RED, barvaG);
                                liner.vykresliLineDDA(line, previewRaster);
                            }
                        }
                    }
                }
            } else {
                if (helpPoint == null) {
                    line = new Line(startPoint, endPoint, RED, barvaG);
                    liner.vykresliLineDDA(line, previewRaster);
                } else {
                    line = new Line(helpPoint, endPoint, RED, barvaG);
                    liner.vykresliLineDDA(line, previewRaster);
                }
            }
            g.drawImage(previewRaster, 0, 0, null);
        }

        if (polygon) {
            Color barvaPolygonG = null;
            Color barvaPolygon = YELLOW;
            if (body.size() == 1) {
                line = new Line(body.getFirst(), body.getFirst(), barvaPolygon, barvaPolygonG);
                liner.vykresliLineDDA(line, previewRaster);
            }
            if (body.size() > 1) {
                for (int i = 1; i < body.size(); i++) {
                    line = new Line(body.get(i-1), body.get(i), barvaPolygon, barvaPolygonG);
                    liner.vykresliLineDDA(line, previewRaster);
                }
            }
            if (body.size() > 2) {
                if (drag && !edit) {
                    barvaPolygon = GRAY;
                }
                line = new Line(body.getFirst(), body.getLast(), barvaPolygon, barvaPolygonG);
                liner.vykresliLineDDA(line, previewRaster);
            }

            if (edit) {
                if (drag) {
                    barvaPolygon = GRAY;
                } else {
                    barvaPolygon = YELLOW;
                }
                if (body.size() > 2) {
                    if (bodyIndex == body.size() - 1) {
                        line = new Line(body.getLast(), body.get(bodyIndex - 1), barvaPolygon, barvaPolygonG);
                        liner.vykresliLineDDA(line, previewRaster);
                        line = new Line(body.getLast(), body.getFirst(), barvaPolygon, barvaPolygonG);
                        liner.vykresliLineDDA(line, previewRaster);
                    } else if (bodyIndex == 0) {
                        line = new Line(body.getFirst(), body.get(bodyIndex + 1), barvaPolygon, barvaPolygonG);
                        liner.vykresliLineDDA(line, previewRaster);
                        line = new Line(body.getFirst(), body.getLast(), barvaPolygon, barvaPolygonG);
                        liner.vykresliLineDDA(line, previewRaster);
                    } else {
                        line = new Line(body.get(bodyIndex), body.get(bodyIndex - 1), barvaPolygon, barvaPolygonG);
                        liner.vykresliLineDDA(line, previewRaster);
                        line = new Line(body.get(bodyIndex), body.get(bodyIndex + 1), barvaPolygon, barvaPolygonG);
                        liner.vykresliLineDDA(line, previewRaster);
                    }
                }
            }
            g.drawImage(previewRaster, 0, 0, null);
        }
    }

    //GETTERY
    public BufferedImage getRaster() { return raster; }
    public Point getStartPoint() { return startPoint; }
    public Point getEndPoint() { return endPoint; }
    public Point getHelpPoint() { return helpPoint; }
    public Point getEditPoint() { return editPoint; }
    public Line getLine() { return line; }
    public boolean isDrag() { return drag; }
    public boolean isPolygon() { return polygon; }
    public boolean isEdit() { return edit; }
    public boolean isInterpolate() { return interpolate; }
    public boolean isGradient() { return gradient; }
    public int getBodyIndex() { return bodyIndex; }
    public Color getBarva() { return barva; }
    public Color getBarvaG() { return barvaG; }
    public Color getVychoziBarva() { return vychoziBarva; }
    public int getPixelRadius() { return pixelRadius; }
    public ArrayList<Point> getBody() { return body; }
    public ArrayList<Polygon> getPolygony() { return polygony; }
    public ArrayList<Line> getLines() { return lines; }

    //SETTERY
    public void setStartPoint(Point startPoint) { this.startPoint = startPoint; }
    public void setEndPoint(Point endPoint) {this.endPoint = endPoint; }
    public void setHelpPoint(Point helpPoint) { this.helpPoint = helpPoint; }
    public void setEditPoint(Point editPoint) { this.editPoint = editPoint; }
    public void setLine(Line line) { this.line = line; }
    public void setDrag(boolean drag) { this.drag = drag; }
    public void setEdit(boolean edit) { this.edit = edit; }
    public void setBodyIndex(int bodyIndex) { this.bodyIndex = bodyIndex; }
    public void setBarva(Color barva) { this.barva = barva; }
    public void setBarvaG(Color barvaG) { this.barvaG = barvaG; }
    public void setPolygon(Boolean polygon) { this.polygon = polygon; }
    public void setRezim(String rezim) {
        interpolate = false;
        gradient = false;

        switch (rezim) {
            case "Interpolace":
                interpolate = true;
                this.rezim = "Interpolace -> funkce zapnuta";
                break;
            case "Gradient":
                gradient = true;
                this.rezim = "Gradient -> funkce zapnuta";
                break;
            case "":
                this.rezim = "";
                break;
        }
    }


}
