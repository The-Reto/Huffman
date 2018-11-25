package fileOutput;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileCompressor {

    DataInputStream in;
    DataOutputStream out;
    File myFile;
    public boolean debug = false;

    public FileCompressor(String fileName) {
        this(new File(fileName));
    }

    FileCompressor(File myFile) {
        this.myFile = myFile;
    }

    public void compress(){
        System.out.println("Starting by reading original File");
        try {
            in = new DataInputStream(new FileInputStream(myFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HuffmanTree.OuterNode[] byteCounter = new HuffmanTree.OuterNode[256];
        ArrayList<Byte> bytes = new ArrayList<>();

        for (int i = 0; i < byteCounter.length; i++){
            if (byteCounter[i] == null) byteCounter[i] = new HuffmanTree.OuterNode((byte) i);
        }

        try {
            while (true) {
                int i = in.readUnsignedByte();
                bytes.add((byte) i);
                byteCounter[i].count();
            }
        } catch (EOFException e) {
            System.out.println("Reached end of File - starting compression");
        } catch (IOException e) {
            System.out.println("Could not read the file!");
        }

        Arrays.sort(byteCounter);
        int objs = 0;
        while (objs < byteCounter.length && byteCounter[objs].freq > 0) objs++;
        byteCounter = Arrays.copyOfRange(byteCounter, 0, objs);

        HuffmanTree tree = new HuffmanTree(byteCounter);

        if (debug) {
            for(HuffmanTree.OuterNode b :byteCounter) {
                System.out.print(b.b + " <-> " + b.freq + " : ");
                for (int i : b.code) System.out.print(i);
                System.out.println();
            }
        }

        try {
            out = new DataOutputStream(new FileOutputStream(myFile.getName()+".compr"));
            out.writeInt(bytes.size());
            out.writeInt(objs);
            for (int i = 0; i < byteCounter.length && byteCounter[i].freq != 0; i++) {
                out.writeByte(byteCounter[i].b);
                out.writeInt(byteCounter[i].freq);
            }
            BitWriter bitWriter = new BitWriter(out);
            for (byte b : bytes) {
                for (HuffmanTree.OuterNode n : byteCounter) {
                    if (b == n.b) {
                        Collections.reverse(n.code);
                        bitWriter.write(n.code);
                        Collections.reverse(n.code);
                        break;
                    }
                }
            }
            bitWriter.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {

        }
        System.out.println("Compression Successful!");
    }
}
