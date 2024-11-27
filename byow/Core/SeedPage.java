package byow.Core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class SeedPage {
    int width;
    int height;
    long theSeed;

    public SeedPage(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public World startWorld() {
        drawFrame("");
        String inputString = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char inputKey = StdDraw.nextKeyTyped();
                if ('0' <= inputKey && inputKey <= '9') {
                    inputString += inputKey;
                    drawFrame(inputString);
                } else if (inputKey == 'S' | inputKey == 's') {
                    break;
                }
            }
        }
        long seed = Long.parseLong(inputString);
        this.theSeed= seed;
        World world = new World(80, 30, seed);
        return world;
    }

    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.text(width / 2, height / 2, "Please enter a seed:");
        StdDraw.text(width / 2, height / 3, s);
        StdDraw.show();
    }
    public long getSeed(){
        return this.theSeed;
    }

}
