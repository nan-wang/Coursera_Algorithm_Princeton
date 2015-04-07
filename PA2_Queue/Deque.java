import java.util.Iterator;
    
public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int N;
    
    private class Node {
        private Item item;
        private Node next;
        private Node pre;
        public Node() {
            next = null;
            pre = null;
        }
    }
    
    public Deque() {
        first = null;
        last = null;
        N = 0;
    }
    
    public boolean isEmpty() {
        return (size() == 0);
    }
    
    public int size() {
        return N;
    }
    
    public void addFirst(Item item) {
        if (item == null)
            throw new java.lang.NullPointerException("added a null item.");
        Node oldfirst = first;
        first = new Node();
        first.item = item;       
        if (isEmpty()) {
            last = first;
        }
        else {
            first.next = oldfirst;
            oldfirst.pre = first;
        }
        N++;
    }
    
    public void addLast(Item item) {
        if (item == null)
            throw new java.lang.NullPointerException("added a null item.");
        Node oldlast = last;
        last = new Node();
        last.item = item;
        if (isEmpty()) {
            first = last;
        }
        else {
            last.pre = oldlast;
            oldlast.next = last;
        }
        N++;
    }
    
    public Item removeFirst() {
        if (isEmpty())
            throw new java.util.NoSuchElementException(
                "removed from an empty deque.");
        Item item = first.item;
        first = first.next;
        N--;
        if (!isEmpty()) {
            first.pre = null;
        }
        else {
            first = null;
            last = null;
        }
        return item;  
    }
    
    public Item removeLast() {
        if (isEmpty())
            throw new java.util.NoSuchElementException(
                "removed from an empty deque.");
        Item item = last.item;
        last = last.pre;
        N--;
        if (!isEmpty()) {
            last.next = null;
        }
        else {
            first = null;
            last = null;
        }
        return item;
    }
    
    public Iterator<Item> iterator() {
        return new ListIterator();
    }
    
    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() {
            return (current != null);
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException("called remove().");
        }
        public Item next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException(
                    "called next() on the last item.");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    
    public static void main(String[] args) {
        // test string +L-L
        String[] testString = {"to", "be", "or", "not", "-", "to", "be", "-", "-"};
        // expected output "notbeto"
        Deque<String> dequeStr = new Deque<String>();
        for (String eachStr : testString) {
            if (eachStr.equals("-"))
                StdOut.print(dequeStr.removeLast());
            else
                dequeStr.addLast(eachStr);
        }
        // test int +F-F "243"
        StdOut.print("\nTest 2: array of integers\n");
        int[] testArray = {1, 2, 0, 3, 4, 0, 0};
        Deque<Integer> dequeInt = new Deque<Integer>();
        if (dequeInt.isEmpty()) StdOut.print("create an empty deque.\n");
        for (int eachInt : dequeInt) {
            StdOut.printf("%d\t", eachInt);
        }        
        for (int eachInt : testArray) {
            if (eachInt == 0)
                StdOut.printf("%d\t", dequeInt.removeFirst());
            else
                dequeInt.addFirst(eachInt);
        }
        StdOut.print("\nThe number of elements is: ");
        StdOut.print(dequeInt.size());
        StdOut.print("\nThey are:\n");
        for (int eachInt : dequeInt) {
            StdOut.printf("%d\t", eachInt);
        }
        // test empty
        StdOut.print("\nTest 2: array of integers\n");
        Deque<Integer> emptyDeque = new Deque<Integer>();
        
        try {
            emptyDeque.removeFirst();
        } catch (java.util.NoSuchElementException err) {
            StdOut.print(err);
        }
        StdOut.print("\n");
        try {
            emptyDeque.removeLast();
        } catch (java.util.NoSuchElementException err) {
            StdOut.print(err);
        }
        StdOut.print("\n");
        try {
            emptyDeque.addFirst(null);
        } catch (java.lang.NullPointerException err) {
            StdOut.print(err);
        }
        StdOut.print("\n");
        try {
            emptyDeque.addLast(null);
        } catch (java.lang.NullPointerException err) {
            StdOut.print(err);
        }
    }
}