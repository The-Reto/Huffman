package fileOutput;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

public class BitReader {

    DataInputStream in;
    byte current;
    int curr, pos;
    boolean eof = false;

    BitReader(DataInputStream in) {
        this.in = in;
        try {
            current = in.readByte();
        } catch (IOException e) {
            e.printStackTrace();
        }
        curr = current & 0xff;
        pos = 0;
    }

    int nextBit() {
        if(pos == 7){
            pos = 0;
            try {
                current = in.readByte();
            } catch (EOFException e) {
                eof = true;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            curr = current & 0xff;
        }
        if (curr >= Math.pow(2, 7-pos)) {
            curr -= Math.pow(2, 7-pos);
            pos++;
            return 1;
        }
        else {
            pos++;
            return 0;
        }
    }
}
