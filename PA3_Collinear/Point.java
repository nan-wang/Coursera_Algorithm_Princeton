/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new Comparator<Point>() {
        public int compare(Point p1, Point p2) {
            double s1 = slopeTo(p1);
            double s2 = slopeTo(p2);
            if (s1 < s2) return -1;
            if (s1 > s2) return 1;
            return 0;
        }
    };

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        if (that.y == this.y && that.x == this.x)
            return Double.NEGATIVE_INFINITY;
        if (that.y == this.y) return +0.0;
        if (that.x == this.x) return Double.POSITIVE_INFINITY;
        return (that.y * 1.0 - this.y * 1.0) / (that.x * 1.0 - this.x * 1.0);
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (this.y < that.y) return -1;
        if (this.y > that.y) return 1;
        if (this.x < that.x) return -1;
        if (this.x > that.x) return 1;
        return 0;
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }


    // unit test
    public static void main(String[] args) {
        Point a = new Point(3, 4);
        Point b = new Point(3, 5);
        Point c = new Point(4, 4);
        StdOut.printf("a:c %d\t%.2f\n", a.compareTo(c), a.slopeTo(c));
        StdOut.printf("c:a %d\t%.2f\n", c.compareTo(a), c.slopeTo(a));
        StdOut.printf("a:b %d\t%.2f\n", a.compareTo(b), a.slopeTo(b));
        StdOut.printf("b:a %d\t%.2f\n", b.compareTo(a), b.slopeTo(a));
        StdOut.printf("a:a %d\t%.2f\n", a.compareTo(a), a.slopeTo(a));
        StdOut.printf("b:c %d\t%.2f\n", b.compareTo(c), b.slopeTo(c));
        StdOut.printf("c:b %d\t%.2f\n", c.compareTo(b), c.slopeTo(b));
        
        Point[] pointList = {a, b, c};
        StdOut.printf("\nbefore sorting:\n");
        for (Point each : pointList)
            StdOut.printf(each.toString());
        java.util.Arrays.sort(pointList);
        StdOut.printf("\nafter sorting:\n");
        for (Point each : pointList)
            StdOut.printf(each.toString());
    }
}
