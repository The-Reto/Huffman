package fileOutput;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BitWriter {
    DataOutputStream out;
    byte current;
    int pos;

    BitWriter(DataOutputStream out) {
        this.out = out;
        this.current = 0;
        this.pos = 0;
    }

    void write(ArrayList<Integer> toWrite) {
        for (Integer i : toWrite) this.write(i);
    }

    private void write(Integer i) {
        if(i == 1) {
            current += Math.pow(2, 7-pos);
        }
        pos++;

        if (pos == 7) {
            pos = 0;
            try {
                out.writeByte(current);
            } catch (IOException e) {
                e.printStackTrace();
            }
            current = 0;
        }
    }

    public void close() {
        try {
            out.writeByte(current);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
