package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Room {
    //leftX is [1,73]
    public int leftX;
    //leftY is [1,23]
    public int leftY;
    //rightX is [2,78]
    public int rightX;
    //rightY is [2,28]
    public int rightY;
    //width and height of room is [2,6]
    public int width;
    public int height;
    private Random r;

    public Room(Random random) {
        r = random;
        leftX = r.nextInt(73) + 1;
        leftY = r.nextInt(23) + 1;
        width = r.nextInt(5) + 2;
        height = r.nextInt(5) + 2;
        rightX = leftX + width - 1;
        rightY = leftY + height - 1;
    }

    public void paintRoom(Room room, TETile[][] world) {
        for (int i = room.leftX; i <= room.rightX; i++) {
            for (int j = room.leftY; j <= room.rightY; j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }
    }
}
