/*************************************************************************
 *  Compilation:  javac LZWmod.java
 *  Execution:    java LZWmod - < input.txt > output.lzw  (compress input.txt
 *                                                         into output.lzw)
 *  Execution:    java LZWmod + < output.lzw > input.rec  (expand output.lzw
 *                                                         into input.rec)
 *  Dependencies: BinaryStdIn.java BinaryStdOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 *************************************************************************/
 import java.util.Arrays;
 import java.util.List;


public class LZW {
    private static final int R = 256;        // alphabet size
    private static boolean flushIfFull = false;

    public static void compress() {
        CompressionCodeBookInterface codebook =
            new DLBCodeBook(9, 16); //changed from 12,12 to 9,16 because that is the codeword width range we are looking at now

        BinaryStdOut.write(flushIfFull); //adds a 1-bit flag at the beginning of the compressed file which determines whether or not to reset the codebook when running out of codewords

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            if(!codebook.advance(c)){ //found longest match
                int codeword = codebook.getCodeWord();
                BinaryStdOut.write(codeword, codebook.getCodewordWidth());
                codebook.add(flushIfFull);
                codebook.advance(c);
            }
        }
        int codeword = codebook.getCodeWord();
        BinaryStdOut.write(codeword, codebook.getCodewordWidth());

        BinaryStdOut.write(R, codebook.getCodewordWidth());
        BinaryStdOut.close();
    }


    public static void expand() {
        flushIfFull = BinaryStdIn.readBoolean(); //program reads the 1-bit flag to determine whether or not to reset the codebook

        ExpansionCodeBookInterface codebook = new ArrayCodeBook(9, 16); //changed from 12,12 to 9,16 because that is the codeword size range we are looking at

        int codeword = BinaryStdIn.readInt(codebook.getCodewordWidth(flushIfFull));
        String val = codebook.getString(codeword);

        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(codebook.getCodewordWidth(flushIfFull));

            if (codeword == R) break;
            String s = codebook.getString(codeword);
            if (codebook.size() == codeword) s = val + val.charAt(0); // special case hack

            codebook.add(val + s.charAt(0), flushIfFull);
            val = s;

        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
        List<String> argList = Arrays.asList(args);
        if(argList.contains("n")) flushIfFull=false; //added command-line argument that allows the user to choose n for "do nothing" ONLY during compression
        if(argList.contains("r")) flushIfFull=true; //added command-line argument that allows the user to choose r for "reset", to reset codebook ONLY during compression when all the codewords have been used
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");

    }

}
