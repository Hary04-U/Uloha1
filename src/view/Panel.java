package view;

import object.Line;
import object.Point;
import object.Polygon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Panel extends JPanel {

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

    public Panel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        raster = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = new Point(e.getX(), e.getY());
                drag = false;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (polygon) {
                    if (drag) {
                        if (body.isEmpty()) {
                            body.add(startPoint);
                            body.add(new Point(e.getX(), e.getY()));
                        } else {
                            body.add(new Point(e.getX(), e.getY()));
                        }
                    } else {
                        body.add(new Point(e.getX(), e.getY()));
                    }
                } else {
                    if (drag) {
                        if (helpPoint != null) {
                            usecka = new Line(helpPoint, endPoint);
                            usecka.vykresliLine(usecka.getStartPoint(), usecka.getEndPoint(), raster, barva);

                            helpPoint = null;
                        } else {
                            usecka = new Line(startPoint, endPoint);
                            usecka.vykresliLine(usecka.getStartPoint(), usecka.getEndPoint(), raster, barva);
                        }
                    } else {
                        if (helpPoint != null) {
                            endPoint = new Point(e.getX(), e.getY());
                            usecka = new Line(helpPoint, endPoint);
                            usecka.vykresliLine(usecka.getStartPoint(), usecka.getEndPoint(), raster, barva);
                            helpPoint = null;
                        } else {
                            helpPoint = startPoint;
                            raster.setRGB(helpPoint.getX(), helpPoint.getY(), barva.getRGB());
                        }
                    }

                }
                repaint();
                drag = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0) {
                    int dx = e.getX() - startPoint.getX();
                    int dy = e.getY() - startPoint.getY();

                    double angle = Math.atan2(dy, dx);
                    double step = Math.toRadians(15);
                    double snapped = Math.round(angle / step) * step;

                    double length = Math.sqrt(dx * dx + dy * dy);
                    int newX = startPoint.getX() + (int)(length * Math.cos(snapped));
                    int newY = startPoint.getY() + (int)(length * Math.sin(snapped));

                    endPoint = new Point(newX, newY);
                } else {
                    endPoint = new Point(e.getX(), e.getY());
                }
                repaint();
                drag = true;
            }
        });

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P && !polygon) {
                    polygon = true;
                    rezim = "Polygon režim zapnut";
                } else if (e.getKeyCode() == KeyEvent.VK_P && polygon){
                    polygon = false;
                    rezim = "";
                    Polygon aktualPolygon = new Polygon(new ArrayList<>(body));
                    polygony.add(aktualPolygon);
                    System.out.println(polygony);
                    body.clear();
                }
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    raster.getGraphics().setColor(Color.BLACK);
                    raster.getGraphics().fillRect(0, 0, raster.getWidth(), raster.getHeight());
                    if (!body.isEmpty()) {
                        body.clear();
                    }
                    if (!polygony.isEmpty()) {
                        polygony.clear();
                    }
                }
                repaint();
            }
        });
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
}
