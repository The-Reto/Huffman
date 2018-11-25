package fileOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class HuffmanTree {

    Node root;

    HuffmanTree(OuterNode arr[]) {
        ArrayList<Node> heap = new ArrayList<>(Arrays.asList(arr));
        while (heap.size() > 1) {
            if (heap.get(heap.size() - 1).freq > 0) {
                Node a = heap.remove(heap.size()-1);
                Node b = heap.remove(heap.size()-1);
                InnerNode n = new InnerNode(a,b);
                heap.add(n);
                heap.sort(new Comparator<Node>() {
                    @Override
                    public int compare(Node node, Node t1) {
                        return t1.freq - node.freq;
                    }
                });
            } else {
                heap.remove(heap.size() - 1);
            }
        }
        this.root = heap.get(0);
    }

    public Byte get(ArrayList<Integer> code) {
        return root.get(code, 0);
    }

    static abstract class Node implements Comparable {
        int freq;

        Node() {
            freq=0;
        }

        @Override
        public int compareTo(Object o) {
            return ((Node) o).freq - freq;
        }

        abstract void addCode(Integer codlet);

        public abstract Byte get(ArrayList<Integer> code, int i);
    }

    static class InnerNode extends Node {
        Node children[] = new Node[2];

        InnerNode(Node a, Node b) {
            this.freq = a.freq + b.freq;
            children[0] = a;
            children[1] = b;
            a.addCode(0);
            b.addCode(1);
        }

        void addCode(Integer codlet) {
            children[0].addCode(codlet);
            children[1].addCode(codlet);
        }

        public Byte get(ArrayList<Integer> code, int i) {
            if (i == code.size()) return null;
            else return children[code.get(i)].get(code, i+1);
        }
    }

    static class OuterNode extends Node {
        byte b;
        ArrayList<Integer> code = new ArrayList<>();

        OuterNode(byte b) {
            super();
            this.b = b;
        }

        OuterNode(byte n, int freq) {
            super();
            this.b = n;
            this.freq = freq;
        }

        void count() {
            freq++;
        }

        void addCode(Integer codlet) {
            code.add(codlet);
        }

        public Byte get(ArrayList<Integer> code, int i) {
            //if (i == code.size()) return null;
            return b;
        }
    }
}
