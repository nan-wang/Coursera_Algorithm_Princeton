import java.util.Iterator;

public class Solver {
    private NodeList solution = null;
    private MinPQ<Node> searchQueue;
    private MinPQ<Node> searchQueueTwin;
    private boolean solvable;
    private int numMove = -1;
    
    private class Node implements Comparable<Node> {
        private int priority;
        private Board preBoard;
        private Board curBoard;
        private int curMove;
                
        public Node(Board newBoard, Board oldBoard, int numMoves) {
            curBoard = newBoard;
            preBoard = oldBoard;
            priority = numMoves + curBoard.manhattan();
            curMove = numMoves;
        }
        
        public int getCurMove() {
            return curMove;
        }
        
        public Board getPreBoard() {
            return preBoard;
        }
        
        public Board getCurBoard() {
            return curBoard;
        }
        
        public int getPriority() {
            return priority;
        }
        
        public int compareTo(Node that) {
            if (this.priority > that.priority) 
                return 1;
            if (this.priority < that.priority)
                return -1;
            if (this.curBoard.manhattan() > that.getCurBoard().manhattan())
                return 1;
            if (this.curBoard.manhattan() < that.getCurBoard().manhattan())
                return -1;
            return 0;
        }    
    }
    
    private class NodeList implements Iterable<Board> {
        private ST<String, BoardNode> st = new ST<String, BoardNode>();
        private String last = "";
        
        private class BoardNode {
            private Board board;
            private BoardNode preBoardNode;
            private int boardPriority;
            
            public Board getBoard() {
                return board;
            }
            
            public BoardNode getPreBoardNode() {
                return preBoardNode;
            }
            
            public int getPriority() {
                return boardPriority;
            }
            
            public void setBoard(Board newBoard) {
                board = newBoard;
            }
            
            public void setPreBoardNode(BoardNode newBoardNode) {
                preBoardNode = newBoardNode;
            }
            
            public void setPriority(int newPriority) {
                boardPriority = newPriority;
            }
        }
        
        public void pushToST(Node newNode) {
            String newKey = newNode.getCurBoard().toString();
            boolean updateKey = false;
            
            String newPreKey = newNode.getPreBoard().toString();            
            if (st.contains(newKey)) {
                if (st.get(newKey).getPreBoardNode() == null)
                    updateKey = true;
                else {
                    String oldPreKey = 
                        st.get(newKey).getPreBoardNode().getBoard().toString();
                    if (st.get(oldPreKey).getPriority()
                            > st.get(newPreKey).getPriority())
                        updateKey = true;
                }
            }
            else
                updateKey = true;
            
            if (updateKey) {
                BoardNode newBoardNode = new BoardNode();
                newBoardNode.setPreBoardNode(
                    st.get(newNode.getPreBoard().toString()));
                newBoardNode.setBoard(newNode.getCurBoard());
                newBoardNode.setPriority(newNode.getPriority());
                // update
                st.put(newBoardNode.getBoard().toString(), newBoardNode);
                last = newBoardNode.getBoard().toString();
            }
        }
        
        public Iterator<Board> iterator() {
            return new StIterator();
        }
        
        private class StIterator implements Iterator<Board> {
            private BoardNode curBoardNode = st.get(last);
            public boolean hasNext() {
                return curBoardNode != null;
            }
            public void remove() {
                throw new java.lang.UnsupportedOperationException(
                    "called remove().");
            }
            public Board next() {
                Board curBoard = curBoardNode.getBoard();
                curBoardNode = curBoardNode.getPreBoardNode();
                return curBoard;
            }
        }
    }
    
    public Solver(Board initial) {
        solvable = false;
        solution = new NodeList();
        searchQueue = new MinPQ<Node>();
        searchQueueTwin = new MinPQ<Node>();
        
        // initialize the first search node
        Node searchNode = new Node(initial, initial, 0);
        Node searchNodeTwin = new Node(initial.twin(), initial.twin(), 0);
        searchQueue.insert(searchNode);
        searchQueueTwin.insert(searchNodeTwin);
        while ((!searchNode.getCurBoard().isGoal())
                   && (!searchNodeTwin.getCurBoard().isGoal())) {
//            StdOut.printf("Step %d:\t", moveCount);
//            StdOut.printf("The dequeue board is: %d-%d\n%s\n", 
//            searchNode.getCurBoard().manhattan(), searchNode.priority, 
//            searchNode.getCurBoard());
//            StdOut.printf("The boards in the PQ:\n");
//            for (Node each : searchQueue) {
//                StdOut.printf("%d-%d\n%s\n", each.getCurBoard().manhattan(),
//            each.priority, each.getCurBoard());
//            }
            
            searchNode = searchQueue.delMin();
            solution.pushToST(searchNode);
            // insert the neighbors
            for (Board eachNeighbor : searchNode.getCurBoard().neighbors()) {
                if (!eachNeighbor.equals(searchNode.getPreBoard())) {
                    Node eachNode = new Node(
                        eachNeighbor, searchNode.getCurBoard(), 
                        searchNode.getCurMove() + 1);
                    searchQueue.insert(eachNode);
                }
            }
            
            searchNodeTwin = searchQueueTwin.delMin();
            for (Board eachNeighbor : searchNodeTwin.getCurBoard().neighbors()) {
                if (!eachNeighbor.equals(searchNodeTwin.getPreBoard())) {
                    Node eachNode = new Node(
                        eachNeighbor, searchNodeTwin.getCurBoard(),
                        searchNodeTwin.getCurMove() + 1);
                    searchQueueTwin.insert(eachNode);
                }
            }
        }
        solvable = searchNode.getCurBoard().isGoal();
        if (solvable && numMove == -1) {
            for (Board each : solution)
                numMove++;
        }
        if (solvable && numMove == -1)
            numMove = 0;
        if (!solvable)
            solution = null;
    }
    

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return numMove;
    }

    public Iterable<Board> solution() {
        if (solution != null) {
            Stack<Board> reverseSolution = new Stack<Board>();
            for (Board each : solution)
                reverseSolution.push(each);
            return reverseSolution;
        }
        else
            return null;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++) 
            for (int j = 0; j < N; j++) 
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

    Solver solver = new Solver(initial);

    if (!solver.isSolvable()) {
        StdOut.println("No solution possible");
        StdOut.printf("The moves is %d.\n", solver.moves());
    }
    else {
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
    }
    }
}