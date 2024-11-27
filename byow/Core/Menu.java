package byow.Core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class Menu {
    private int WIDTH = 80;
    private int HEIGHT = 50;

    public Menu() {
        StdDraw.setCanvasSize(this.WIDTH * 16, this.HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.WIDTH);
        StdDraw.setYscale(0, this.HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public void showMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT * 2 / 3, "CS61B: THE GAME");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT * 1 / 3, "New Game (N)");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT * 1 / 3 - 3, "Load Game (L)");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT * 1 / 3 - 6, "Quit (Q)");
        StdDraw.show();
    }
}
