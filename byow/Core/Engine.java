package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;


public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    public static final int WHEIGHT = 30;
    public static final int FORTY_FIVE = 45;
    public static final int TEN = 10;
    int tileX = -1;
    int tileY = -1;

    private class ReturnArgs {
        private World world;
        private long seed;

        public ReturnArgs(World w, long s) {
            this.world = w;
            this.seed = s;
        }
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        Menu menu = new Menu();
        menu.showMenu();
        PlayMusic p = new PlayMusic();
        p.playLoop("proj3/bgm.wav");
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char inputKey = StdDraw.nextKeyTyped();
                if (inputKey == 'N' | inputKey == 'n') {
                    SeedPage seedPage = new SeedPage(WIDTH, HEIGHT);
                    World world = seedPage.startWorld();
                    ter.initialize(WIDTH, HEIGHT);
                    p.stop();
                    p.playLoop("proj3/bgm2.wav");
                    ter.renderFrame(world.getWholeWorld(), world, "");
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char c = StdDraw.nextKeyTyped();
                            if (c == ':') {
                                while (true) {
                                    if (StdDraw.hasNextKeyTyped()) {
                                        c = StdDraw.nextKeyTyped();
                                        if (c == 'q' || c == 'Q') {
                                            saveFile(seedPage.getSeed(), world);
                                            System.exit(0);
                                        } else {
                                            break;
                                        }
                                    }
                                }
                            }
                            if (c != '：' && c != '：' && c != 'q' && c != 'Q') {
                                world.changeWorld(c);
                                int x = (int) StdDraw.mouseX();
                                int y = (int) StdDraw.mouseY();
                                ter.renderFrame(world.getWholeWorld(), world, drawHUD(world, x, y, true));
                            }
                        }
                        drawHUD(world, (int) StdDraw.mouseX(), (int) StdDraw.mouseY(), false);
                    }
                } else if (inputKey == 'L' | inputKey == 'l') {
                    ReturnArgs returnArgs = loadFile();
                    World world = returnArgs.world;
                    long seed = returnArgs.seed;
                    ter.initialize(WIDTH, HEIGHT);
                    p.stop();
                    p.playLoop("proj3/bgm2.wav");
                    ter.renderFrame(world.getWholeWorld(), world, "");
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char c = StdDraw.nextKeyTyped();
                            if (c == ':') {
                                while (true) {
                                    if (StdDraw.hasNextKeyTyped()) {
                                        c = StdDraw.nextKeyTyped();
                                        if (c == 'q' || c == 'Q') {
                                            saveFile(seed, world);
                                            System.exit(0);
                                        } else {
                                            break;
                                        }
                                    }
                                }
                            }
                            if (c != '：' && c != '：' && c != 'q' && c != 'Q') {
                                world.changeWorld(c);
                                int x = (int) StdDraw.mouseX();
                                int y = (int) StdDraw.mouseY();
                                ter.renderFrame(world.getWholeWorld(), world, drawHUD(world, x, y, true));
                            }
                        }
                        drawHUD(world, (int) StdDraw.mouseX(), (int) StdDraw.mouseY(), false);
                    }
                } else if (inputKey == 'Q' | inputKey == 'q') {
                    System.exit(0);
                }
            }
        }
    }

    public String drawHUD(World world, int x, int y, Boolean isKeyTyped) {
        TETile[][] wholeWorld = world.getWholeWorld();
        String tileType = "Tile: nothing";
        if (x < WIDTH && y < WHEIGHT) {
            if (wholeWorld[x][y] == Tileset.WALL) {
                tileType = "Tile: wall";
            } else if (wholeWorld[x][y] == Tileset.FLOOR) {
                tileType = "Tile: floor";
            } else if (wholeWorld[x][y] == Tileset.LOCKED_DOOR) {
                tileType = "Tile: door";
            } else if (wholeWorld[x][y] == Tileset.USER) {
                tileType = "Tile: avatar";
            } else if (wholeWorld[x][y] == Tileset.BEAN) {
                tileType = "Tile: bean";
            }
        } else {
            x = 0;
            y = 0;
        }
        if (isKeyTyped) {
            return tileType;
        }
        if (tileX == -1 || (wholeWorld[x][y] != wholeWorld[tileX][tileY])) {
            ter.renderFrame(world.getWholeWorld(), world, "");
            StdDraw.text(TEN, FORTY_FIVE, tileType);
            StdDraw.pause(TEN);
            StdDraw.show();
        }
        tileX = x;
        tileY = y;
        return null;
    }

    public void saveFile(long seed, World world) {
        Out out = new Out("gameStatus.txt");
        out.println(seed);
        out.println(world.getPosUser()[0] + "\t" + world.getPosUser()[1]);
        out.println(world.getNumOfLeftBean());
        if (world.getNumOfLeftBean() != 0) {
            for (int i = 0; i < world.getLeftBean().size() - 1; i++) {
                out.print(world.getLeftBean().get(i) + "\t");
            }
            out.print(world.getLeftBean().get(world.getLeftBean().size() - 1));
        }
    }

    public ReturnArgs loadFile() {
        In in = null;
        try {
            in = new In("gameStatus.txt");
        } catch (IllegalArgumentException e) {
            System.exit(0);
        }
        long seed = 0;
        int xUser = 0;
        int yUser = 0;
        int numOfBeanLoaded = 0;
        ArrayList<Integer> listOfBeanLoaded = new ArrayList<>();
        if (!in.isEmpty()) {
            String stringSeed = in.readLine();
            seed = Long.parseLong(stringSeed);
            String pos = in.readLine();
            String[] splitOfPos = pos.split("\t");
            xUser = Integer.parseInt(splitOfPos[0]);
            yUser = Integer.parseInt(splitOfPos[1]);
            String numBeanString = in.readLine();
            numOfBeanLoaded = Integer.parseInt(numBeanString);
            if (numOfBeanLoaded != 0) {
                String beans = in.readLine();
                String[] splitOfBeans = beans.split("\t");
                for (int i = 0; i < numOfBeanLoaded * 2; i++) {
                    Integer a = Integer.parseInt(splitOfBeans[i]);
                    listOfBeanLoaded.add(a);
                }
            }
        }
        return new ReturnArgs(new World(WIDTH, WHEIGHT, seed, xUser, yUser, numOfBeanLoaded, listOfBeanLoaded), seed);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        int curChar = 0;
        if (input.charAt(curChar) == 'N' || input.charAt(curChar) == 'n') {
            String seedStr = "";
            for (curChar = 1; ; curChar++) {
                if (input.charAt(curChar) == 'S' || input.charAt(curChar) == 's') {
                    break;
                }
                seedStr += input.charAt(curChar);
            }
            long seed = Long.parseLong(seedStr);
            World world = new World(WIDTH, WHEIGHT, seed);
            while (curChar < input.length() - 1) {
                curChar++;
                if (input.charAt(curChar) == ':') {
                    curChar++;
                    if (input.charAt(curChar) == 'Q' || input.charAt(curChar) == 'q') {
                        saveFile(seed, world);
                        break;
                    } else {
                        curChar--;
                    }
                } else {
                    world.changeWorld(input.charAt(curChar));
                }
            }
            TETile[][] finalWorldFrame = world.getWholeWorld();
            return finalWorldFrame;
        } else if (input.charAt(curChar) == 'L' || input.charAt(curChar) == 'l') {
            ReturnArgs returnArgs = loadFile2();
            World world = returnArgs.world;
            long seed = returnArgs.seed;
            while (curChar < input.length() - 1) {
                curChar++;
                if (input.charAt(curChar) == ':') {
                    curChar++;
                    if (input.charAt(curChar) == 'Q' || input.charAt(curChar) == 'q') {
                        saveFile(seed, world);
                        break;
                    } else {
                        curChar--;
                    }
                } else {
                    world.changeWorld(input.charAt(curChar));
                }
            }
            TETile[][] finalWorldFrame = world.getWholeWorld();
            return finalWorldFrame;
        }
        return null;
    }

    public ReturnArgs loadFile2() {
        In in = null;
        try {
            in = new In("gameStatus.txt");
        } catch (IllegalArgumentException e) {
            System.exit(0);
        }
        long seed = 0;
        int xUser = 0;
        int yUser = 0;
        int numOfBeanLoaded = 0;
        ArrayList<Integer> listOfBeanLoaded = new ArrayList<>();
        if (!in.isEmpty()) {
            String stringSeed = in.readLine();
            seed = Long.parseLong(stringSeed);
            String pos = in.readLine();
            String[] splitOfPos = pos.split("\t");
            xUser = Integer.parseInt(splitOfPos[0]);
            yUser = Integer.parseInt(splitOfPos[1]);
            String numBeanString = in.readLine();
            numOfBeanLoaded = Integer.parseInt(numBeanString);
            if (numOfBeanLoaded != 0) {
                String beans = in.readLine();
                String[] splitOfBeans = beans.split("\t");
                for (int i = 0; i < numOfBeanLoaded * 2; i++) {
                    Integer a = Integer.parseInt(splitOfBeans[i]);
                    listOfBeanLoaded.add(a);
                }
            }
        } else {
            System.exit(0);
        }
        return new ReturnArgs(new World(WIDTH, WHEIGHT, seed, xUser, yUser, numOfBeanLoaded, listOfBeanLoaded), seed);
    }
}
