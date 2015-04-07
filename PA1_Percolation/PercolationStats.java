//import Percolation;
public class PercolationStats {
    private int numRepeats;
    private int width;
    private double [] thresholdArray;
    private double thresholdMean;
    private double thresholdStd;
    
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException("Bad N, T");
        width = N;
        numRepeats = T;
        thresholdArray = new double [T];
        for (int i = 0; i < T; i++) {       
            Percolation eachRun = new Percolation(N);
            double counter = 0.0;
            while (!eachRun.percolates()) {
                int colIdx = StdRandom.uniform(width) + 1;
                int rowIdx = StdRandom.uniform(width) + 1;
                if (!eachRun.isOpen(rowIdx, colIdx)) {
                   eachRun.open(rowIdx, colIdx);
                   counter += 1.0;
                }
//                System.out.println("choose "+rowIdx+", "+colIdx);
//                eachRun.printOpenStatus();
//                eachRun.printFullStatus();
//                eachRun.printIdxWQUF();
//                System.out.println("# Counter: ="+counter);
            }
            counter /= (width * width * 1.0);
            thresholdArray[i] = counter;
        }
    }
    
    public double mean() {
        thresholdMean = StdStats.mean(thresholdArray);
        return thresholdMean;
    }
    
    public double stddev() {
        thresholdStd = StdStats.stddev(thresholdArray);
        return thresholdStd;
    }
    
    public double confidenceLo() {
        thresholdMean = StdStats.mean(thresholdArray);
        thresholdStd = StdStats.stddev(thresholdArray);
        return thresholdMean - 1.96*thresholdStd/Math.sqrt(numRepeats);
    }
    
    public double confidenceHi() {
        thresholdMean = StdStats.mean(thresholdArray);
        thresholdStd = StdStats.stddev(thresholdArray);
        return thresholdMean + 1.96*thresholdStd/Math.sqrt(numRepeats);
    }
    
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats mc = new PercolationStats(N, T);
        System.out.println("mean\t\t\t= " + mc.mean());
        System.out.println("stddev\t\t\t= " + mc.stddev());
        System.out.println("95% confidence interval\t= " 
                           + mc.confidenceLo() + ", " + mc.confidenceHi());
    }
}