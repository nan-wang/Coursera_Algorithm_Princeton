public class Fast {
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
        boolean isRepeated;
        for (int curIdx = 0; curIdx < N; curIdx++) {
            // find the tallest
            java.util.Arrays.sort(pointList, curIdx, N);
//            for(Point each : point_list)
//                StdOut.printf("%s ", each);
//            StdOut.printf("\n");
            Point p = pointList[curIdx];
            // reorder the rest according to slope
            java.util.Arrays.sort(pointList, curIdx+1, N, p.SLOPE_ORDER);
            // find the duplicated items
            int repeatCounter = 0;
            int i = curIdx + 1;
            while (i < N) {
                repeatCounter = 1;
                double curSlope = p.slopeTo(pointList[i]);
                while ((i+repeatCounter < N) 
                       && p.slopeTo(pointList[i + repeatCounter]) == curSlope) {
                    repeatCounter++;
                }
                isRepeated = false;
                if (repeatCounter >= 3) {
                    // check whether it is repeated
                    for (int preIdx = 0; preIdx < curIdx && !isRepeated; preIdx++) {
                        if (pointList[preIdx].slopeTo(p) == curSlope)
                            isRepeated = true;
                    }
                    if (!isRepeated) {
                        // print
                        StdOut.printf("%s", p);
                        for (int j = 0; j < repeatCounter; j++) {
                            StdOut.printf(" -> %s", pointList[i+j]);
                        }
                        StdOut.printf("\n");
                        // plot
                        p.drawTo(pointList[i+repeatCounter-1]);
                    }
                }
                i = i + repeatCounter;           
            }
        }
        StdDraw.show(0);
        StdDraw.setPenRadius();
    }
}