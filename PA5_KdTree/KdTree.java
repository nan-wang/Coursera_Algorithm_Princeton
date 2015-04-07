public class KdTree {
    private Node root;
    private Point2D nearestP = new Point2D(0, 0);
    private double nearestD = 1.0;
    
    private static class Node {
        private Point2D p;      // the point
        // the axis-aligned rectangle corresponding to this node
        private RectHV rect;    
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int N;
        
        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
            this.N = 1;
        }
    }
    
    public KdTree() {                             
        // construct an empty set of points
    }
    
    public boolean isEmpty() {         
        // is the set empty?
        return root == null;
    }
    
    public int size() {
        // number of points in the set
        return size(root);
    }
    
    private int size(Node x) {
        if (x == null) return 0;
        return x.N;
    }
    
    public void insert(Point2D newPoint) {
        // add the point to the set (if it is not already in the set)
        if (newPoint == null)
            throw new java.lang.NullPointerException("invalid argument.");
//        RectHV newRect = new RectHV(0, 0, 1, 1);
        root = insert(root, newPoint, 0, root, 0, false);
    }
    
    private Node insert(Node h, Point2D newPoint, int levelIdx, 
                        Node hParent, double parentValue,
                        boolean leftOrLower) {
        if (h == null) {
            RectHV newRect;
            if (levelIdx == 0) {
                newRect = new RectHV(0, 0, 1, 1);
            }
            else {
                if (leftOrLower) {
                    if (levelIdx % 2 == 0) {
                        newRect = new RectHV(hParent.rect.xmin(), 
                                             hParent.rect.ymin(),
                                             hParent.rect.xmax(), 
                                             parentValue);
                    }
                    else {
                        newRect = new RectHV(hParent.rect.xmin(),
                                             hParent.rect.ymin(),
                                             parentValue,
                                             hParent.rect.ymax());
                    }
                }
                else {
                    if (levelIdx % 2 == 0) {
                        newRect = new RectHV(hParent.rect.xmin(), 
                                             parentValue,
                                             hParent.rect.xmax(),
                                             hParent.rect.ymax());
                    }
                    else {
                        newRect = new RectHV(parentValue, 
                                             hParent.rect.ymin(),
                                             hParent.rect.xmax(),
                                             hParent.rect.ymax());
                    }
                }
            }
            return new Node(newPoint, newRect);
        }
        double rootValue, curValue;
        if (levelIdx % 2 == 0) {
            rootValue = h.p.x();
            curValue = newPoint.x();
        }
        else {
            rootValue = h.p.y();
            curValue = newPoint.y();
        }
        if (curValue < rootValue) {
            h.lb = insert(h.lb, newPoint, levelIdx + 1, h, rootValue, true);          
        }
        if (curValue >= rootValue) {
            if (!h.p.equals(newPoint)) {
                h.rt = insert(h.rt, newPoint, levelIdx + 1, h, rootValue, false);
            }
        }
        h.N = size(h.lb) + size(h.rt) + 1;
//        System.out.printf("%s has N = %d\n", h.p, h.N);
        return h;
    }
    
    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null)
            throw new java.lang.NullPointerException("invalid argument.");
        Node searchNode = root;
        int levelIdx = 0;
        double curValue, searchValue;
        while (searchNode != null) {
            if (levelIdx % 2 == 0) {
                curValue = searchNode.p.x();
                searchValue = p.x();
            }
            else {
                curValue = searchNode.p.y();
                searchValue = p.y();
            }
            if (searchValue == curValue) {
                if (searchNode.p.equals(p))
                    return true;
                else
                    searchNode = searchNode.rt;
            }
            if (searchValue < curValue)
                searchNode = searchNode.lb;
            if (searchValue > curValue)
                searchNode = searchNode.rt;
            levelIdx += 1;
        }
        return false;
    }
    
    public void draw() {
        // draw all points to standard draw
        Node curNode = root;
        int levelIdx = 0;
        while (curNode != null) {
            curNode = draw(curNode, levelIdx);
        }
        StdDraw.setPenRadius(.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        root.rect.draw();
    }
    
    private Node draw(Node curNode, int levelIdx) {
        if (curNode != null) {
            draw(curNode.lb, levelIdx+1);
            draw(curNode.rt, levelIdx+1);
            StdDraw.setPenRadius(.01);
            RectHV rectToDraw;
            if (levelIdx % 2 == 0) {
                rectToDraw = new RectHV(curNode.rect.xmin(),
                                               curNode.rect.ymin(),
                                               curNode.p.x(),
                                               curNode.rect.ymax());
                StdDraw.setPenColor(StdDraw.RED);
            }
            else {
                rectToDraw = new RectHV(curNode.rect.xmin(),
                                               curNode.rect.ymin(),
                                               curNode.rect.xmax(),
                                               curNode.p.y());
                StdDraw.setPenColor(StdDraw.BLUE);
            }
            rectToDraw.draw();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.03);
            curNode.p.draw();
        }
        return null;
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle
        if (rect == null)
            throw new java.lang.NullPointerException("invalid argument.");
        Queue<Point2D> queue = new Queue<Point2D>();
        range(root, rect, 0, queue);
        return queue;
    }
    
    private void range(Node h, RectHV rect, int levelIdx, Queue<Point2D> queue) {
        if (h != null) {
            if (rect.contains(h.p))
                queue.enqueue(h.p);
            boolean intersected = false;
            boolean leftOrLower = false;
            if (levelIdx % 2 == 0) {
                if (rect.xmin() < h.p.x() && rect.xmax() >= h.p.x())
                    intersected = true;
                if (rect.xmax() < h.p.x())
                    leftOrLower = true;
            }
            else {
                if (rect.ymin() < h.p.y() && rect.ymax() >= h.p.y())
                    intersected = true;
                if (rect.ymax() < h.p.y())
                    leftOrLower = true;
            }
            if (intersected) {
                range(h.lb, rect, levelIdx+1, queue);
                range(h.rt, rect, levelIdx+1, queue);
            }
            else {
                if (leftOrLower) 
                    range(h.lb, rect, levelIdx+1, queue);
                else
                    range(h.rt, rect, levelIdx+1, queue);
            }
        }
    }
    
    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null)
            throw new java.lang.NullPointerException("invalid argument.");
        nearestD = 2.0;
        nearestP = null;
        nearest(root, p, 0);
        return this.nearestP;
    }
    
    private void nearest(Node h, Point2D p, int levelIdx) {
        if (h != null) {
            double curD = h.p.distanceTo(p);
            boolean leftOrLower = false;
            double distToRect;
            if (curD < nearestD) {
                nearestP = h.p;
                nearestD = curD;
            }
            if (levelIdx % 2 == 0) {
                if (p.x() < h.p.x()) {
                    leftOrLower = true;
                }
            }
            else {
                if (p.y() < h.p.y()) {
                    leftOrLower = true;
                }
            }
            if (leftOrLower) {
                nearest(h.lb, p, levelIdx+1);
                if (h.rt != null) {
                    distToRect = h.rt.rect.distanceTo(p);
                    if (distToRect <= nearestD) {
                        nearest(h.rt, p, levelIdx+1);
                    }
                }
            }
            else {
                nearest(h.rt, p, levelIdx+1);
                if (h.lb != null) {
                    distToRect = h.lb.rect.distanceTo(p);
                    if (distToRect <= nearestD) {
                        nearest(h.lb, p, levelIdx+1);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
        // java KdTreeGenerator N
        int N = Integer.parseInt(args[0]);
        KdTree kdTree = new KdTree();
        for (int i = 0; i < N; i++) {
            double x = Math.random();
            double y = Math.random();
            System.out.printf("%8.6f %8.6f\n", x, y);
            Point2D p = new Point2D(x, y);
            kdTree.insert(p);
        }
        StdDraw.show(0);
        StdDraw.clear();
        kdTree.draw();
        // test range search
        RectHV rect = new RectHV(0.0, 0.0, .5, .5);
        rect.draw();
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(.03);
        for (Point2D p : kdTree.range(rect)) {
            p.draw(); 
        }
        // test nearest search
        Point2D p = new Point2D(0.5, 0.5);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(.05);
        p.draw();
        Point2D nearestP = kdTree.nearest(p);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(.05);
        nearestP.draw();
        StdDraw.show(50);
        // test size()
        System.out.printf("The size of kdTree is %d.\n", kdTree.size());
    }
}
