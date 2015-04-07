public class Board {
    private final int[][] board;
    private Stack<Board> neighborList = null;
    private final int zeroIdx;
    private final int selectedIdx;
     
    public Board(int[][] blocks) {
        int width = blocks[0].length;
        board = new int[width][width];
        int tmpRow = 0;
        int tmpCol = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = blocks[i][j];
                if (board[i][j] == 0) {
                    tmpRow = i;
                    tmpCol = j;
                }
            }
        }
        // find the empty
        zeroIdx = tmpRow * width + tmpCol;
        // find the twin
        tmpRow = StdRandom.uniform(width);
        tmpCol = StdRandom.uniform(width-1);
        while (blocks[tmpRow][tmpCol] == 0
               || blocks[tmpRow][tmpCol + 1] == 0) {
            tmpCol = StdRandom.uniform(width-1);
            tmpRow = StdRandom.uniform(width);
        }
        selectedIdx = tmpRow * width + tmpCol;
    }

    public int dimension() {
        return board[0].length;
    }

    public int hamming() {
        int results = 0;
        int width = dimension();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (board[i][j] != 0) {
                    if (board[i][j] != i * width + j + 1) {
                        results++;
                    }
                }
            }
        }
        return results;
    }

    public int manhattan() {
        int result = 0;
        int expRow = 0;
        int expCol = 0;
        int width = dimension();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (board[i][j] != 0) {
                    expRow = (board[i][j] - 1) / width;
                    expCol = (board[i][j] - 1) % width;
                    result += abs(expRow - i) + abs(expCol - j);
                }
            }
        }
        return result;
    }
    
    private int abs(int val) {
        if (val < 0)
            return -val;
        else
            return val;
    }

    public boolean isGoal() {
        int width = dimension();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (i == (width - 1) && j == (width -1)) {
                    if (board[i][j] != 0) {
                        return false;
                    }
                }
                else {
                    if (board[i][j] != i * width + j + 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Board twin() {
        int[][] twinBoard = copyBoard();
        int width = dimension();
        int selectedRow = selectedIdx / width;
        int selectedCol = selectedIdx % width;
        int tmp = twinBoard[selectedRow][selectedCol];
        twinBoard[selectedRow][selectedCol] =
            twinBoard[selectedRow][selectedCol + 1];
        twinBoard[selectedRow][selectedCol + 1] = tmp;
        return new Board(twinBoard);
    }

    public boolean equals(Object y) {
        int width = dimension();
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) return false;
        try {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < width; j++) {
                    if (that.board[i][j] != board[i][j]) {
                        return false;
                    }
                }
            }
        }
        catch (ClassCastException exception) {
            return false;
        }
        return true;
    }
    
    // copy `board` to a new `Board`
    private int[][] copyBoard() {
        int width = dimension();
        int[][] newBoard = new int[width][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }

    public Iterable<Board> neighbors() {
        if (neighborList == null) {
            neighborList = new Stack<Board>();
            int width = dimension();
            int zeroRowIdx = zeroIdx / width;
            int zeroColIdx = zeroIdx % width;
            // find neighbors


            if (zeroColIdx != width-1) {
                int[][] leftNeighbor = copyBoard();
                leftNeighbor[zeroRowIdx][zeroColIdx] = 
                    leftNeighbor[zeroRowIdx][zeroColIdx+1];
                leftNeighbor[zeroRowIdx][zeroColIdx+1] = 0;
                neighborList.push(new Board(leftNeighbor));
            } 
            if (zeroRowIdx != 0) {
                int[][] downNeighbor = copyBoard();
                downNeighbor[zeroRowIdx][zeroColIdx] = 
                    downNeighbor[zeroRowIdx-1][zeroColIdx];
                downNeighbor[zeroRowIdx-1][zeroColIdx] = 0;
                neighborList.push(new Board(downNeighbor));
            }
            if (zeroColIdx != 0) {
                int[][] rightNeighbor = copyBoard();
                rightNeighbor[zeroRowIdx][zeroColIdx] = 
                    rightNeighbor[zeroRowIdx][zeroColIdx-1];
                rightNeighbor[zeroRowIdx][zeroColIdx-1] = 0;
                neighborList.push(new Board(rightNeighbor));
            }
            if (zeroRowIdx != width-1) {
                int[][] upNeighbor = copyBoard();
                upNeighbor[zeroRowIdx][zeroColIdx] = 
                    upNeighbor[zeroRowIdx+1][zeroColIdx];
                upNeighbor[zeroRowIdx+1][zeroColIdx] = 0;
                neighborList.push(new Board(upNeighbor));
            }              
        }
        return neighborList;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        int width = dimension();
        output.append(width + "\n");
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                output.append(String.format("%2d ", board[i][j]));
            }
            output.append("\n");
        }
        return output.toString();
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.printf("This is the initial board.\n");
        StdOut.println(initial);
        StdOut.printf("hamming: %d\n", initial.hamming());
        StdOut.printf("manhattan: %d\n", initial.manhattan());
        
        StdOut.printf("These are its neighbors.\n");
        for (Board eachNeighbor : initial.neighbors()) {
            StdOut.println(eachNeighbor);
            StdOut.printf("hamming: %d\n", eachNeighbor.hamming());
            StdOut.printf("manhattan: %d\n", eachNeighbor.manhattan());
        }
        
        StdOut.printf("Here are %d twins.\n", initial.dimension());
        for (int i = 0; i < initial.dimension(); i++) {
            Board newTwin = initial.twin();
            StdOut.println(newTwin);
            StdOut.printf("hamming: %d\n", newTwin.hamming());
            StdOut.printf("manhattan: %d\n", newTwin.manhattan());
        }
        
        StdOut.printf("Is the initial board the same as below?\n");
        Board comparison = new Board(blocks);
        StdOut.println(comparison);
        if (initial.equals(comparison))
            StdOut.println("yes");
        else
            StdOut.println("no");
        comparison = initial.twin();
        StdOut.println(comparison);
        if (initial.equals(comparison))
            StdOut.println("yes");
        else
            StdOut.println("no");
    }
}
