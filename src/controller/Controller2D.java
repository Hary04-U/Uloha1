package controller;

import object.Line;
import object.Point;
import object.Polygon;
import view.Pane;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Controller2D implements Controller {
    private Pane panel;
    private BufferedImage raster;

    public Controller2D(Pane panel) {
        this.panel = panel;
        this.raster = panel.getRaster();
        listeners();
    }

    @Override
    public void listeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                panel.setStartPoint(new Point(e.getX(), e.getY()));
                panel.setDrag(false);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (panel.isPolygon()) {
                    if (panel.isDrag()) {
                        if (panel.getBody().isEmpty()) {
                            panel.getBody().add(panel.getStartPoint());
                            panel.getBody().add(new Point(e.getX(), e.getY()));
                        } else {
                            panel.getBody().add(new Point(e.getX(), e.getY()));
                        }
                    } else {
                        panel.getBody().add(new Point(e.getX(), e.getY()));
                    }
                } else {
                    if (panel.isDrag()) {
                        if (panel.getHelpPoint() != null) {
                            Line usecka = new Line(panel.getHelpPoint(), panel.getEndPoint());
                            usecka.vykresliLine(usecka.getStartPoint(), usecka.getEndPoint(), raster, panel.getBarva());

                            panel.setHelpPoint(null);
                        } else {
                            Line usecka = new Line(panel.getStartPoint(), panel.getEndPoint());
                            usecka.vykresliLine(usecka.getStartPoint(), usecka.getEndPoint(), raster, panel.getBarva());
                        }
                    } else {
                        if (panel.getHelpPoint() != null) {
                            Point endPoint = new Point(e.getX(), e.getY());
                            Line usecka = new Line(panel.getHelpPoint(), endPoint);
                            usecka.vykresliLine(usecka.getStartPoint(), usecka.getEndPoint(), raster, panel.getBarva());
                            panel.setHelpPoint(null);
                        } else {
                            panel.setHelpPoint(panel.getStartPoint());
                            raster.setRGB(panel.getHelpPoint().getX(), panel.getHelpPoint().getY(), panel.getBarva().getRGB());
                        }
                    }

                }
                panel.repaint();
                panel.setDrag(false);
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0) {
                    int dx = e.getX() - panel.getStartPoint().getX();
                    int dy = e.getY() - panel.getStartPoint().getY();

                    double angle = Math.atan2(dy, dx);
                    double step = Math.toRadians(15);
                    double snapped = Math.round(angle / step) * step;

                    double length = Math.sqrt(dx * dx + dy * dy);
                    int newX = panel.getStartPoint().getX() + (int)(length * Math.cos(snapped));
                    int newY = panel.getStartPoint().getY() + (int)(length * Math.sin(snapped));

                    panel.setEndPoint(new Point(newX, newY));
                } else {
                    panel.setEndPoint(new Point(e.getX(), e.getY()));
                }
                panel.repaint();
                panel.setDrag(true);
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P && !(panel.isPolygon())) {
                    panel.setPolygon(true);
                    panel.setRezim("Polygon režim zapnut");
                } else if (e.getKeyCode() == KeyEvent.VK_P && panel.isPolygon()){
                    panel.setPolygon(false);
                    panel.setRezim("");
                    Polygon aktualPolygon = new Polygon(new ArrayList<>(panel.getBody()));
                    panel.getPolygony().add(aktualPolygon);
                    panel.getBody().clear();
                }
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    raster.getGraphics().setColor(Color.BLACK);
                    raster.getGraphics().fillRect(0, 0, raster.getWidth(), raster.getHeight());
                    if (!panel.getBody().isEmpty()) {
                        panel.getBody().clear();
                    }
                    if (!panel.getPolygony().isEmpty()) {
                        panel.getPolygony().clear();
                    }
                }
                panel.repaint();
            }
        });
    }
}
