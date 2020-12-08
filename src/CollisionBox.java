import java.awt.Polygon;
import java.awt.geom.Area;

public class CollisionBox {

    private int px;
    private int py;
    private float rotationAngle;
    private int width;
    private int height;

    public CollisionBox(int px, int py, float rotationAngle, int width, int height) {
        this.px = px;
        this.py = py;
        this.rotationAngle = rotationAngle;
        this.width = width;
        this.height = height;

    }

    // clockwise from origin corner
    public int[][] getCornerLocations() {

        float centerX = px + width / 2.0f;
        float centerY = py + height / 2.0f;
        int diagonal = (int) Math.round(
                Math.sqrt(Math.pow((px - (px + width)), 2) + Math.pow((py - (py + height)), 2)));

        float diagonalAngle = (float) Math.atan2(width, height);


        int[] c1 = { (int) (centerX - Math.cos(rotationAngle - diagonalAngle) * (diagonal / 2.0f)),
                (int) (centerY - Math.sin(rotationAngle - diagonalAngle) * (diagonal / 2.0f)) };

        int[] c2 = { (int) (centerX + Math.cos(rotationAngle + diagonalAngle) * (diagonal / 2.0f)),
                (int) (centerY + Math.sin(rotationAngle + diagonalAngle) * (diagonal / 2.0f)) };

        int[] c3 = { (int) (centerX + Math.cos(rotationAngle - diagonalAngle) * (diagonal / 2.0f)),
                (int) (centerY + Math.sin(rotationAngle - diagonalAngle) * (diagonal / 2.0f)) };

        int[] c4 = { (int) (centerX - Math.cos(rotationAngle + diagonalAngle) * (diagonal / 2.0f)),
                (int) (centerY - Math.sin(rotationAngle + diagonalAngle) * (diagonal / 2.0f)) };

        int[][] cornerLocations = { c1, c2, c3, c4 };

        return cornerLocations;
    }

    public boolean isIntersecting(CollisionBox that) {

        int[][] thisCorners = this.getCornerLocations();
        Polygon thisPolygon = new Polygon(
                new int[] { thisCorners[0][0], thisCorners[1][0], thisCorners[2][0],
                        thisCorners[3][0] },
                new int[] { thisCorners[0][1], thisCorners[1][1], thisCorners[2][1],
                        thisCorners[3][1] },
                4);

        int[][] thatCorners = that.getCornerLocations();
        Polygon thatPolygon = new Polygon(
                new int[] { thatCorners[0][0], thatCorners[1][0], thatCorners[2][0],
                        thatCorners[3][0] },
                new int[] { thatCorners[0][1], thatCorners[1][1], thatCorners[2][1],
                        thatCorners[3][1] },
                4);

        Area thisArea = new Area(thisPolygon);
        Area thatArea = new Area(thatPolygon);

        thisArea.intersect(thatArea);

        return !thisArea.isEmpty();

    }

    public void update(int px, int py, float rotationAngle) {
        this.px = px;
        this.py = py;
        this.rotationAngle = rotationAngle;

    }

}
