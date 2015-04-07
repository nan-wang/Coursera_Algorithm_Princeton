public class PointSET {
    private SET<Point2D> setOfPoints;
    
    public PointSET() {                             
        // construct an empty set of points
        setOfPoints = new SET<Point2D>();
    }
    
    public boolean isEmpty() {         
        // is the set empty?
        return setOfPoints.isEmpty();
    }
    
    public int size() {
        // number of points in the set
        return setOfPoints.size();
    }
    
    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new java.lang.NullPointerException("invalid argument.");
        if (!setOfPoints.contains(p)) {
            setOfPoints.add(p);
        }
    }
    
    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null)
            throw new java.lang.NullPointerException("invalid argument.");
        return setOfPoints.contains(p);
    }
    
    public void draw() {
        // draw all points to standard draw
        for (Point2D eachPoint : setOfPoints) {
            eachPoint.draw();
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle
        if (rect == null)
            throw new java.lang.NullPointerException("invalid argument.");
        SET<Point2D> result = new SET<Point2D>();
        for (Point2D eachPoint : setOfPoints) {
            if (rect.contains(eachPoint)) {
                result.add(eachPoint);
            }
        }
        return result;
    }
    
    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null)
            throw new java.lang.NullPointerException("invalid argument.");
        double shortestDistance = 2;
        Point2D result = null;
        for (Point2D eachPoint : setOfPoints) {
            double curDistance = eachPoint.distanceSquaredTo(p);
            if (curDistance < shortestDistance) {
                shortestDistance = curDistance;
                result = eachPoint;
            } 
        }
        return result;
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
        // java KdTreeGenerator N
        int N = Integer.parseInt(args[0]);
        PointSET pointSet = new PointSET();
        for (int i = 0; i < N; i++) {
            double x = Math.random();
            double y = Math.random();
            System.out.printf("%8.6f %8.6f\n", x, y);
            Point2D p = new Point2D(x, y);
            pointSet.insert(p);
        }
        StdDraw.show(0);
        StdDraw.clear();
        StdDraw.setPenRadius(.03);
        pointSet.draw();
        // test range search
        RectHV rect = new RectHV(0.0, 0.0, .5, .5);
        rect.draw();
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(.03);
        for (Point2D p : pointSet.range(rect)) {
            p.draw(); 
        }
        // test nearest search
        Point2D p = new Point2D(0.5, 0.5);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(.05);
        p.draw();
        Point2D nearestP = pointSet.nearest(p);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(.05);
        nearestP.draw();
        StdDraw.show(50);
        // java KdTreeVisualizer
//        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
//        StdDraw.show(0);
//        PointSET pointSet = new PointSET();
//        while (true) {
//            if (StdDraw.mousePressed()) {
//                double x = StdDraw.mouseX();
//                double y = StdDraw.mouseY();
//                System.out.printf("%8.6f %8.6f\n", x, y);
//                Point2D p = new Point2D(x, y);
//                if (rect.contains(p)) {
//                    StdOut.printf("%8.6f %8.6f\n", x, y);
//                    pointSet.insert(p);
//                    StdDraw.clear();
//                    pointSet.draw();
//                }
//            }
//            StdDraw.show(50);
//        }
    }
}