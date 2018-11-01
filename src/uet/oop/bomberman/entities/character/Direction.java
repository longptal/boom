package uet.oop.bomberman.entities.character;

public enum Direction {
    nill, left, up, right, down;

    // make start with -1;
    public int getIndex() {
        return ordinal() - 1;
    }

    public static Direction getDirectionFromInt(int i) {
        switch (i) {
            case 0:
                return up;
            case 1:
                return right;
            case 2:
                return down;
            case 3:
                return left;
            case -1:
                return nill;
            default:
                return nill;
        }
    }
    public static int getIntFromDirection(Direction d) {
        switch (d) {
            case up:
                return 0;
            case right:
                return 1;
            case down:
                return 2;
            case left:
                return 3;
            case nill:
                return -1;
            default:
                return -1;
        }
    }

}
