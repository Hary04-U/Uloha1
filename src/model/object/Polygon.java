package model.object;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private List<Point> body = new ArrayList<>();
    private Color barva;
    private Color barvaG;

    public Polygon(List<Point> body, Color barva, Color barvaG) {
        this.body = body;
        this.barva = barva;
        this.barvaG = barvaG;
    }

    public List<Point> getBody() {
        return body;
    }

    public Color getBarva() {
        return barva;
    }

    public Color getBarvaG() {
        return barvaG;
    }
}

