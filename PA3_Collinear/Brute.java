public class Brute {
    public static void main(String[] args) {
        // prepare for plotting
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
        StdDraw.setPenRadius(0.01);
        
        // read the input
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Point[] pointList = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            pointList[i] = new Point(x, y);
            pointList[i].draw();
        }
     
        // find the collinear points
        java.util.Arrays.sort(pointList);
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                for (int k = j+1; k < N; k++) {
                    double slopeIj = pointList[i].slopeTo(pointList[j]);
                    double slopeIk = pointList[i].slopeTo(pointList[k]);
                    if (slopeIj == slopeIk) {
                        for (int l = k+1; l < N; l++) {
                            if (pointList[i].slopeTo(pointList[l]) == slopeIj) {
                                StdOut.printf("%s -> %s -> %s -> %s\n",
                                             pointList[i], pointList[j],
                                             pointList[k], pointList[l]);
                                pointList[i].drawTo(pointList[l]);
                            }
                        }
                    }
                }
            }
        }
        StdDraw.show(0);
        StdDraw.setPenRadius();
    }
}