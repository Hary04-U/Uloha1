package view;

import controller.Controller2D;

public class Main {
    public static void main(String[] args) {
        Window window = new Window(800, 600);
        new Controller2D(window.getPanel());
    }
}
