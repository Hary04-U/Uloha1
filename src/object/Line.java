package object;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Line {
    private Point startPoint;
    private Point endPoint;

    public Line(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void vykresliLine (Point startPoint, Point endPoint, BufferedImage raster, Color c) {
        int rozdilX = endPoint.getX() - startPoint.getX();
        int rozdilY = endPoint.getY() - startPoint.getY();

        if (rozdilX == 0 && rozdilY == 0) {
            raster.setRGB(startPoint.getX(), startPoint.getY(), c.getRGB());
        }

        if (Math.abs(rozdilX) >= Math.abs(rozdilY)) {
            double k = (double) rozdilY / rozdilX;
            double q = startPoint.getY() - k*startPoint.getX();
            int startX = Math.min(startPoint.getX(), endPoint.getX());
            int endX = Math.max(startPoint.getX(), endPoint.getX());
            for (int x = startX; x <= endX; x++) {
                int y = (int) Math.round(k * x + q);
                raster.setRGB(x, y, c.getRGB());
            }
        } else {
            double k = (double) rozdilX / rozdilY;
            double q = startPoint.getX() - k*startPoint.getY();
            int startY = Math.min(startPoint.getY(), endPoint.getY());
            int endY = Math.max(startPoint.getY(), endPoint.getY());
            for (int y = startY; y <= endY; y++) {
                int x = (int) Math.round(k * y + q);
                raster.setRGB(x, y, c.getRGB());
            }
        }
    }

    public void vykresliRedLine (Point startPoint, Point endPoint, BufferedImage raster) {
        int rozdilX = endPoint.getX() - startPoint.getX();
        int rozdilY = endPoint.getY() - startPoint.getY();

        if (rozdilX == 0 && rozdilY == 0) {
            raster.setRGB(startPoint.getX(), startPoint.getY(), Color.RED.getRGB());
        }

        if (Math.abs(rozdilX) >= Math.abs(rozdilY)) {
            double k = (double) rozdilY / rozdilX;
            double q = startPoint.getY() - k*startPoint.getX();
            int startX = Math.min(startPoint.getX(), endPoint.getX());
            int endX = Math.max(startPoint.getX(), endPoint.getX());
            for (int x = startX; x <= endX; x++) {
                int y = (int) Math.round(k * x + q);
                raster.setRGB(x, y, Color.RED.getRGB());
            }
        } else {
            double k = (double) rozdilX / rozdilY;
            double q = startPoint.getX() - k*startPoint.getY();
            int startY = Math.min(startPoint.getY(), endPoint.getY());
            int endY = Math.max(startPoint.getY(), endPoint.getY());
            for (int y = startY; y <= endY; y++) {
                int x = (int) Math.round(k * y + q);
                raster.setRGB(x, y, Color.RED.getRGB());
            }
        }
    }
}