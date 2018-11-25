package fileOutput;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FIleDecompressor {

    DataInputStream in;
    DataOutputStream out;
    File myFile;
    boolean debug = false;

    public FIleDecompressor(String fileName) {
        this(new File(fileName));
    }

    FIleDecompressor(File myFile) {
        this.myFile = myFile;
    }

    public void decompress(){
        System.out.println("Starting by reading compressed File");
        try {
            in = new DataInputStream(new FileInputStream(myFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            //in.readInt();
            System.out.println("Building Huffman-tree");
            int bytesInOriginal = in.readInt();
            HuffmanTree.OuterNode table[] = new HuffmanTree.OuterNode[in.readInt()];
            for (int i = 0; i < table.length; i++) {
                table[i] = new HuffmanTree.OuterNode(in.readByte(), in.readInt());
            }
            Arrays.sort(table);
            HuffmanTree tree = new HuffmanTree(table);

            if (debug) {
                for(HuffmanTree.OuterNode b :table) {
                    System.out.print(b.b + " <-> " + b.freq + " : ");
                    for (int i : b.code) System.out.print(i);
                    System.out.println();
                }
            }
            System.out.println("Starting decoding");
            BitReader reader = new BitReader(in);
            out = new DataOutputStream(new FileOutputStream(myFile.getName().replace(".compr","")));
            while (!reader.eof && 0 < bytesInOriginal--) {
                ArrayList<Integer> code = new ArrayList<>();
                Byte b = null;
                while ((b = tree.get(code)) == null) {
                    code.add(reader.nextBit());
                }
                out.writeByte(b);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Uncompressing Successful!");
    }
}
