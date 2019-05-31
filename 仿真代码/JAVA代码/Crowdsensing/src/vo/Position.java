package vo;

public class Position {
    private int x;
    private int y;
    private int regionNum;

    public Position(int x, int y,int regionNum) {
        this.x = x;
        this.y = y;
        this.regionNum = regionNum;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRegionNum() {
        return regionNum;
    }

    public void setRegionNum(int regionNum) {
        this.regionNum = regionNum;
    }

    @Override
    public String toString() {
        return "" + regionNum ;
    }
}
