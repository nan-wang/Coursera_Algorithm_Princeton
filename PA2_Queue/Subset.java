public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> stringList = new RandomizedQueue<String>();
        String[] strList = StdIn.readAllStrings();
        int[] idxList = new int[strList.length];
        for (int i = 0; i < strList.length; i++)
            idxList[i] = i;
        StdRandom.shuffle(idxList);
        for (int i = 0; i < k; i++)
            stringList.enqueue(strList[idxList[i]]);
        
        for (String eachStr : stringList)
            StdOut.printf("%s\n", eachStr);
    }
}