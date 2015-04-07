//import java.java-algs4.*;
public class Percolation {
    private WeightedQuickUnionUF sites; // used for tracking the connection
    private WeightedQuickUnionUF fullStatus; // used for checking isFull()
    private int width;
    private int numSites;
    private int topIdx;
    private int bottomIdx;
    private boolean [] openStatus;
       
    public Percolation(int N) {
        if (N <= 0)
            throw new IllegalArgumentException("invalid size");
        width = N;
        numSites = width*width + 2;
        sites = new WeightedQuickUnionUF(numSites);
        fullStatus = new WeightedQuickUnionUF(numSites-1);
        topIdx = numSites - 2;
        bottomIdx = numSites - 1;
        openStatus = new boolean [numSites-2];
    }
    
    private int getIndex(int i, int j) {
        return (i-1) * width + j -1;
    }
     
    public void open(int i, int j) {
        if (i < 1 || i > width || j < 1 || j > width)
            throw new IndexOutOfBoundsException("invalid index");
        int siteIdx = getIndex(i, j);
        openStatus[siteIdx] = true;
        // check neighbour at the top
        if (i != 1) {
            if (isOpen(i-1, j)) {
                sites.union(siteIdx, siteIdx-width);
                fullStatus.union(siteIdx, siteIdx-width);
            }
        }
        else {
            fullStatus.union(topIdx, siteIdx);
            sites.union(topIdx, siteIdx);
        }
        // check neighbour at the bottom
        if (i != width) {
            if (isOpen(i+1, j)) {
                sites.union(siteIdx, siteIdx+width);
                fullStatus.union(siteIdx, siteIdx+width);
            }
        }
        else 
            sites.union(bottomIdx, siteIdx);
        // check neighbour at the left
        if (j != 1) {
            if (isOpen(i, j-1)) {
                sites.union(siteIdx, siteIdx-1);
                fullStatus.union(siteIdx, siteIdx-1);
            }
        }
        // check neighbour at the right
        if (j != width) {
            if (isOpen(i, j+1)) {
                sites.union(siteIdx, siteIdx+1);
                fullStatus.union(siteIdx, siteIdx+1);
            }
        }
    }
    
    public boolean isOpen(int i, int j) {
        if (i < 1 || i > width || j < 1 || j > width)
            throw new IndexOutOfBoundsException("invalid index");
        int siteIdx = getIndex(i, j);
        return (openStatus[siteIdx]);
    }
    
    public boolean isFull(int i, int j) {
        if (i < 1 || i > width || j < 1 || j > width)
            throw new IndexOutOfBoundsException("invalid index");
        int siteIdx = getIndex(i, j);
        return (fullStatus.connected(siteIdx, topIdx));
    }
    
    public boolean percolates() {
        return sites.connected(topIdx, bottomIdx);
    }   
//            
//    public void printOpenStatus() {
//        System.out.println("=====openStatus");
//        for (int i=1; i<= width; i++) {
//            for (int j=1; j <= width; j++) {
//                if (isOpen(i, j))
//                    System.out.print("O ");
//                else
//                    System.out.print("X ");
//            }
//            System.out.print("\n");
//        }
//    }
//
//    public void printFullStatus() {
//        System.out.println("=====fullStatus");
//        for (int i=1; i<= width; i++) {
//            for (int j=1; j <= width; j++) {
//                if (isFull(i, j))
//                    System.out.print("F ");
//                else
//                    System.out.print("N ");
//            }
//            System.out.print("\n");
//        }
//    }
//    
//    public void printIdxWQUF() {
//        System.out.println("=====idxWQUF");
//        for (int i=1; i <= width; i++) {
//            for (int j=1; j <= width; j++) {
//                System.out.print(sites.find(getIndex(i, j))+" ");
//            }
//            System.out.print("\n");
//        }
//    }
}

