package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class World {
    private int widthOfWholeWorld;
    private int lengthOfWholeWorld;
    private TETile[][] wholeWorld;
    private Boolean isFocused;
    private TETile[][] focusedWorld;
    private ArrayList<Room> listOfRoom;
    //numOfRoom is [4,8]
    private int numOfRoom;
    private Random r;
    private int xUser;
    private int yUser;
    private int remainingBean;
    private ArrayList<Integer[]> listOfBean;


    public World(int w, int l, long SEED) {
        r = new Random(SEED);
        this.widthOfWholeWorld = w;
        this.lengthOfWholeWorld = l;
        this.wholeWorld = this.generateBlankWorld(widthOfWholeWorld, lengthOfWholeWorld);
        this.focusedWorld = this.generateBlankWorld(widthOfWholeWorld, lengthOfWholeWorld);
        this.isFocused = false;
        numOfRoom = r.nextInt(5) + 4;
        listOfRoom = new ArrayList<>();
        this.remainingBean = 3;
        this.listOfBean = new ArrayList<>();
        this.generateFinishedWorld();
    }

    public World(int w, int l, long SEED, int xUserLoaded, int yUserLoaded, int numOfBeansLoaded, ArrayList<Integer> listOfBeanLoaded) {
        r = new Random(SEED);
        this.widthOfWholeWorld = w;
        this.lengthOfWholeWorld = l;
        this.wholeWorld = this.generateBlankWorld(widthOfWholeWorld, lengthOfWholeWorld);
        this.focusedWorld = this.generateBlankWorld(widthOfWholeWorld, lengthOfWholeWorld);
        this.isFocused = false;
        numOfRoom = r.nextInt(5) + 4;
        listOfRoom = new ArrayList<>();
        this.remainingBean = numOfBeansLoaded;
        this.listOfBean = new ArrayList<>();
        this.xUser = xUserLoaded;
        this.yUser = yUserLoaded;
        this.placeAllRooms();
        this.placeAllHallways();
        this.addWall();
        this.addDoor();
        wholeWorld[xUserLoaded][yUserLoaded] = Tileset.USER;
        if (numOfBeansLoaded != 0) {
            for (int i = 0; i < listOfBeanLoaded.size(); i = i + 2) {
                wholeWorld[listOfBeanLoaded.get(i)][listOfBeanLoaded.get(i + 1)] = Tileset.BEAN;
                Integer[] bean = new Integer[2];
                bean[0] = listOfBeanLoaded.get(i);
                bean[1] = listOfBeanLoaded.get(i + 1);
                listOfBean.add(bean);
            }
            if (listOfBean.size() == 1) {
                for (int i = 0; i < 2; i++) {
                    Integer[] bean = new Integer[2];
                    bean[0] = -1;
                    bean[1] = -1;
                    listOfBean.add(bean);
                }
            } else if (listOfBean.size() == 2) {
                Integer[] bean = new Integer[2];
                bean[0] = -1;
                bean[1] = -1;
                listOfBean.add(bean);
            }
        }
    }


    public TETile[][] generateBlankWorld(int w, int l) {
        TETile[][] t = new TETile[w][l];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < l; j++) {
                t[i][j] = Tileset.NOTHING;
            }
        }
        return t;
    }

    public void placeOneRoom() {
        Boolean bool = false;
        Room room = new Room(r);
        while (!bool) {
            bool = true;
            for (int j = room.leftX; j <= room.rightX; j++) {
                for (int k = room.leftY; k <= room.rightY; k++) {
                    if (wholeWorld[j][k] != Tileset.NOTHING
                            || wholeWorld[min(j + 2, 79)][k] != Tileset.NOTHING || wholeWorld[max(j - 2, 0)][k] != Tileset.NOTHING
                            || wholeWorld[j][min(k + 2, 29)] != Tileset.NOTHING || wholeWorld[j][max(k - 2, 0)] != Tileset.NOTHING
                    ) {
                        bool = false;
                        room = new Room(r);
                        break;
                    }
                }
                if (!bool) {
                    break;
                }
            }
        }
        room.paintRoom(room, wholeWorld);
        listOfRoom.add(room);
    }

    public void placeAllRooms() {
        int i = 0;
        while (i < numOfRoom) {
            this.placeOneRoom();
            i++;
        }
    }

    public void placeAllHallways() {
        for (int i = 0; i < numOfRoom - 1; i++) {
            placeOneHallway(i, i + 1);
        }
        placeOneHallway(numOfRoom - 1, 0);
    }

    public void placeOneHallway(int roomOneIndex, int roomTwoIndex) {
        Room roomOne = listOfRoom.get(roomOneIndex);
        Room roomTwo = listOfRoom.get(roomTwoIndex);
        int widthDifference = Math.max(roomOne.leftX - roomTwo.rightX, roomTwo.leftX - roomOne.rightX);
        int heightDifference = Math.max(roomOne.leftY - roomTwo.rightY, roomTwo.leftY - roomOne.rightY);
        if (widthDifference >= heightDifference) {
            Room roomLeft = roomOne;
            Room roomRight = roomTwo;
            if (roomOne.leftX - roomTwo.rightX > roomTwo.leftX - roomOne.rightX) {
                roomLeft = roomTwo;
                roomRight = roomOne;
            }
            int outLeftY = r.nextInt(roomLeft.height) + roomLeft.leftY;
            int outRightY = r.nextInt(roomRight.height) + roomRight.leftY;
            int inflectionPointX = 3 + roomLeft.rightX;
            if (widthDifference > 2) {
                inflectionPointX += r.nextInt(widthDifference - 2);
            }
            horExtension(roomLeft.rightX + 1, inflectionPointX, outLeftY);
            verExtension(inflectionPointX, Math.min(outLeftY, outRightY), Math.max(outLeftY, outRightY));
            horExtension(inflectionPointX, roomRight.leftX - 1, outRightY);
        } else {
            Room roomDown = roomOne;
            Room roomUp = roomTwo;
            if (roomOne.leftY - roomTwo.rightY > roomTwo.leftY - roomOne.rightY) {
                roomDown = roomTwo;
                roomUp = roomOne;
            }
            int outDownX = r.nextInt(roomDown.width) + roomDown.leftX;
            int outUpX = r.nextInt(roomUp.width) + roomUp.leftX;
            int inflectionPointY = 3 + roomDown.rightY;
            if (heightDifference > 2) {
                inflectionPointY += r.nextInt(heightDifference - 2);
            }
            verExtension(outDownX, roomDown.rightY + 1, inflectionPointY);
            horExtension(Math.min(outDownX, outUpX), Math.max(outDownX, outUpX), inflectionPointY);
            verExtension(outUpX, inflectionPointY, roomUp.leftY - 1);
        }
    }

    public void horExtension(int leftX, int rightX, int Y) {
        for (int i = leftX; i <= rightX; i++) {
            wholeWorld[i][Y] = Tileset.FLOOR;
        }
    }

    public void verExtension(int X, int downY, int upY) {
        for (int i = downY; i <= upY; i++) {
            wholeWorld[X][i] = Tileset.FLOOR;
        }
    }

    public void addWall() {
        for (int i = 0; i < widthOfWholeWorld; i++) {
            for (int j = 0; j < lengthOfWholeWorld; j++) {
                if (wholeWorld[i][j] != Tileset.FLOOR) {
                    if (wholeWorld[max(0, i - 1)][j] == Tileset.FLOOR || wholeWorld[i][min(j + 1, 29)] == Tileset.FLOOR
                            || wholeWorld[min(79, i + 1)][j] == Tileset.FLOOR || wholeWorld[i][max(0, j - 1)] == Tileset.FLOOR
                            || wholeWorld[max(0, i - 1)][min(29, j + 1)] == Tileset.FLOOR || wholeWorld[min(79, i + 1)][min(29, j + 1)] == Tileset.FLOOR
                            || wholeWorld[max(0, i - 1)][max(j - 1, 0)] == Tileset.FLOOR || wholeWorld[min(79, i + 1)][max(0, j - 1)] == Tileset.FLOOR) {
                        wholeWorld[i][j] = Tileset.WALL;
                    }
                }
            }
        }
    }

    public void addDoor() {
        int doorX;
        int doorY = 0;
        while (true) {
            doorX = r.nextInt(widthOfWholeWorld);
            int numOfWall = 0;
            for (int i = 0; i < lengthOfWholeWorld; i++) {
                if (wholeWorld[doorX][i] == Tileset.WALL) {
                    numOfWall++;
                }
            }
            if (numOfWall == 0) {
                continue;
            }
            int randomY = r.nextInt(numOfWall) + 1;
            for (int i = 0; i < lengthOfWholeWorld; i++) {
                if (wholeWorld[doorX][i] == Tileset.WALL) {
                    doorY = i;
                    randomY--;
                }
                if (randomY == 0) {
                    break;
                }
            }
            break;
        }
        wholeWorld[doorX][doorY] = Tileset.LOCKED_DOOR;
    }

    public void addUser() {
        this.xUser = listOfRoom.get(0).leftX + 1;
        this.yUser = listOfRoom.get(0).leftY + 1;
        wholeWorld[xUser][yUser] = Tileset.USER;
    }

    public void addBean() {
        Integer[] bean1 = new Integer[2];
        bean1[0] = listOfRoom.get(1).leftX + 1;
        bean1[1] = listOfRoom.get(1).leftY + 1;
        Integer[] bean2 = new Integer[2];
        bean2[0] = listOfRoom.get(2).leftX + 1;
        bean2[1] = listOfRoom.get(2).leftY + 1;
        Integer[] bean3 = new Integer[2];
        bean3[0] = listOfRoom.get(3).leftX + 1;
        bean3[1] = listOfRoom.get(3).leftY + 1;
        wholeWorld[bean1[0]][bean1[1]] = Tileset.BEAN;
        wholeWorld[bean2[0]][bean2[1]] = Tileset.BEAN;
        wholeWorld[bean3[0]][bean3[1]] = Tileset.BEAN;
        listOfBean.add(bean1);
        listOfBean.add(bean2);
        listOfBean.add(bean3);
    }

    public void deleteBean(int x, int y) {
        for (int i = 0; i < 3; i++) {
            if (listOfBean.get(i)[0] == x && listOfBean.get(i)[1] == y) {
                listOfBean.get(i)[0] = -1;
                listOfBean.get(i)[1] = -1;
            }
        }
    }

    public int getNumOfLeftBean() {
        return this.remainingBean;
    }

    public ArrayList<Integer> getLeftBean() {
        ArrayList<Integer> listOfLeftBean = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (listOfBean.get(i)[0] != -1 && listOfBean.get(i)[1] != -1) {
                listOfLeftBean.add(listOfBean.get(i)[0]);
                listOfLeftBean.add(listOfBean.get(i)[1]);
            }
        }
        return listOfLeftBean;
    }


    public int getRemainingBean() {
        return this.remainingBean;
    }

    public void generateFinishedWorld() {
        this.placeAllRooms();
        this.placeAllHallways();
        this.addWall();
        this.addDoor();
        this.addUser();
        this.addBean();
    }

    public TETile[][] getWholeWorld() {
        if (isFocused) {
            return focusedWorld;
        }
        return wholeWorld;
    }

    public int[] getPosUser() {
        int[] pos = new int[2];
        pos[0] = xUser;
        pos[1] = yUser;
        return pos;
    }

    public void changeWorld(Character c) {
        PlayMusic p = new PlayMusic();
        if (c == 'A' || c == 'a') {
            if (wholeWorld[xUser - 1][yUser] == Tileset.WALL || wholeWorld[xUser - 1][yUser] == Tileset.LOCKED_DOOR){
                p.playSingle("proj3/hitWall.wav");
            }
            else  {
                if (wholeWorld[xUser - 1][yUser] == Tileset.BEAN) {
                    p.playSingle("proj3/bean.wav");
                    this.remainingBean--;
                    this.deleteBean(xUser - 1, yUser);
                }
                wholeWorld[xUser - 1][yUser] = Tileset.USER;
                wholeWorld[xUser][yUser] = Tileset.FLOOR;
                this.xUser -= 1;
                p.playSingle("proj3/walk.wav");
            }
        } else if (c == 'W' || c == 'w') {
            if (wholeWorld[xUser][yUser+1] == Tileset.WALL || wholeWorld[xUser][yUser+1] == Tileset.LOCKED_DOOR){
                p.playSingle("proj3/hitWall.wav");
            }
            else  {
                if (wholeWorld[xUser][yUser+1] == Tileset.BEAN) {
                    p.playSingle("proj3/bean.wav");
                    this.remainingBean--;
                    this.deleteBean(xUser, yUser+1);
                }
                wholeWorld[xUser][yUser+1] = Tileset.USER;
                wholeWorld[xUser][yUser] = Tileset.FLOOR;
                this.yUser += 1;
                p.playSingle("proj3/walk.wav");
            }
        } else if (c == 'D' || c == 'd') {
            if (wholeWorld[xUser+1][yUser] == Tileset.WALL || wholeWorld[xUser+1][yUser] == Tileset.LOCKED_DOOR){
                p.playSingle("proj3/hitWall.wav");
            }
            else  {
                if (wholeWorld[xUser+1][yUser] == Tileset.BEAN) {
                    p.playSingle("proj3/bean.wav");
                    this.remainingBean--;
                    this.deleteBean(xUser+1, yUser);
                }
                wholeWorld[xUser+1][yUser] = Tileset.USER;
                wholeWorld[xUser][yUser] = Tileset.FLOOR;
                this.xUser += 1;
                p.playSingle("proj3/walk.wav");
            }
        } else if (c == 'S' || c == 's') {
            if (wholeWorld[xUser][yUser-1] == Tileset.WALL || wholeWorld[xUser][yUser-1] == Tileset.LOCKED_DOOR){
                p.playSingle("proj3/hitWall.wav");
            }
            else  {
                if (wholeWorld[xUser][yUser-1] == Tileset.BEAN) {
                    p.playSingle("proj3/bean.wav");
                    this.remainingBean--;
                    this.deleteBean(xUser, yUser-1);
                }
                wholeWorld[xUser][yUser-1] = Tileset.USER;
                wholeWorld[xUser][yUser] = Tileset.FLOOR;
                this.yUser -= 1;
                p.playSingle("proj3/walk.wav");
            }
        } else if (c == 'C' || c == 'c') {
            isFocused = !isFocused;
        } else {
            return;
        }
        if (isFocused) {
            setFocusWorld();
        }
    }

    public void setFocusWorld() {
        for (int i = 0; i < widthOfWholeWorld; i++) {
            for (int j = 0; j < lengthOfWholeWorld; j++) {
                focusedWorld[i][j] = Tileset.NOTHING;
            }
        }
        for (int i = max(xUser - 4, 0); i < min(xUser + 4, widthOfWholeWorld - 1); i++) {
            for (int j = max(yUser - 4, 0); j < min(yUser + 4, lengthOfWholeWorld - 1); j++) {
                focusedWorld[i][j] = wholeWorld[i][j];
            }
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(80, 30);
        World world = new World(80, 30, 2978);
        world.generateFinishedWorld();
        ter.renderFrame(world.getWholeWorld(), world, "");
    }
}
