import java.util.*;
import java.io.*;

public class TrieIterative {
    // Instance Variables
    int[] wordCount;
    int[] prefixCount;
    ArrayList<Map<Character, Integer>> edges;
    int size;
    int maxSize;

    // Constructors
    public TrieIterative(int _maxSize) {
        // note here maxSize is the total number of nodes, not just how many
        // strings can be stored in the trie
        size = 1; // root is ""
        maxSize = _maxSize;
        wordCount = new int[maxSize];
        prefixCount = new int[maxSize];
        for (int i = 0 ; i < maxSize ; i++) {
            edges.add(new HashMap<Character, Integer>());
        }
    }

    // Functions

    void insert(String s) {
        if (size == maxSize) {
            return;
        }

        int currentNode = 0; // start from empty
        for (char c : s.toCharArray()) {
            prefixCount[currentNode]++;

            if (edges.get(currentNode).get(c) == null) {
                edges.get(currentNode).put(c, size);
                size++;
            }

            currentNode = edges.get(currentNode).get(c);
        }

        wordCount[currentNode]++;
    }

    boolean contains(String s) {
        int currentNode = 0;

        for (char c : s.toCharArray()) {
            if (edges.get(currentNode).get(c) == null) {
                return false;
            }

            currentNode = edges.get(currentNode).get(c);
        }

        return wordCount[currentNode] > 0;
    }

    int countPrefix(String p) {
        int currentNode = 0;

        for (char c : p.toCharArray()) {
            if (edges.get(currentNode).get(c) == null) {
                return 0;
            }

            currentNode = edges.get(currentNode).get(c);
        }

        return prefixCount[currentNode];
    }
}
