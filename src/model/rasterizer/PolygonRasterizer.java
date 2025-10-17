package model.rasterizer;

import model.object.Line;
import model.object.Polygon;

import java.awt.image.BufferedImage;

public class PolygonRasterizer {

    private LineRasterizer liner;
    private Line usecka;

    public PolygonRasterizer(LineRasterizer liner) {
        this.liner = liner;
    }

    public void vykresliPolygon (BufferedImage raster, Polygon polygon) {
        if (polygon.getBody().size() > 1) {
            for (int i = 1; i < polygon.getBody().size(); i++) {
                usecka = new Line(polygon.getBody().get(i-1), polygon.getBody().get(i), polygon.getBarva(), polygon.getBarvaG());
                liner.vykresliLineDDA(usecka, raster);
            }
        }
        if (polygon.getBody().size() > 2) {
            usecka = new Line(polygon.getBody().getFirst(), polygon.getBody().getLast(), polygon.getBarva(), polygon.getBarvaG());
            liner.vykresliLineDDA(usecka, raster);
        }
    }
}
