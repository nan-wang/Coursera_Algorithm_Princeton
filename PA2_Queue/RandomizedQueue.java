import java.util.Iterator;
    
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] itemArray;
    private int N = 0;
    private int chosenIdx = 0;
    
    public RandomizedQueue() {
        itemArray = (Item[]) new Object[1];
    }
    
    public boolean isEmpty() {
        return N == 0;
    }
    
    public int size() {
        return N;
    }
    
    public void enqueue(Item item) {
        if (N == itemArray.length)
            resize(2 * itemArray.length);
        if (item == null)
            throw new java.lang.NullPointerException("added a null item.");
        itemArray[N++] = item;
    }
    
    public Item dequeue() {
        if (N == 0)
            throw new java.util.NoSuchElementException(
                "removed from an empty queue.\n");
        chosenIdx = StdRandom.uniform(N);
        Item chosenItem = itemArray[chosenIdx];
        itemArray[chosenIdx] = itemArray[--N];
        itemArray[N] = null;
        if (N > 0 && N == itemArray.length/4)
            resize(itemArray.length/2);
        return chosenItem;
    }
    
    public Item sample() {
        if (N == 0)
            throw new java.util.NoSuchElementException(
                "sampled from an empty queue.\n");
        chosenIdx = StdRandom.uniform(N);
        return itemArray[chosenIdx];
    }
    
    private void resize(int capacity) {
        Item[] newArray = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++)
            newArray[i] = itemArray[i];
        itemArray = newArray;
    }
    
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }
    
    private class ArrayIterator implements Iterator<Item> {
        private int i = 0;
        private int[] idxArray = new int[N];
        
        public ArrayIterator() {
            for (int idx = 0; idx < N; idx++)
                idxArray[idx] = idx;
            StdRandom.shuffle(idxArray);
        }
        
        public boolean hasNext() {
            return i != N;
        }
        
        public void remove() {
            throw new java.lang.UnsupportedOperationException("called remove().");
        }
        
        public Item next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException(
                    "called next() on the last item.");
            return itemArray[idxArray[i++]];
        }
    }
    
    public static void main(String[] args) {
        int[] intArray = {1, 2, 3, 4, 0};
        // test dequeue(), enqueue()
        RandomizedQueue<Integer> randQ = new RandomizedQueue<Integer>();
        try {
            randQ.dequeue();
        } catch (java.util.NoSuchElementException err) {
            StdOut.print(err);
        }
        try {
            randQ.sample();
        } catch (java.util.NoSuchElementException err) {
            StdOut.print(err);
        }
        for (int eachInt : intArray) {
            if (eachInt == 0)
                StdOut.printf("%d\t", randQ.dequeue());
            else
                randQ.enqueue(eachInt);
        }
        // test iterator
        for (int i = 0; i < 2; i++) {
            StdOut.printf("\nThe RandomizedQueue is:\n");
            try {
                for (int eachInt : randQ)
                    StdOut.printf("%d\t", eachInt);
            } catch (java.util.NoSuchElementException err) {
                StdOut.print(err);
            }
        }
        // test size()
        StdOut.printf("\nThere are %d items now.", randQ.size());
    }
}