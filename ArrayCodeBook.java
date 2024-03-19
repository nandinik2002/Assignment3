/**
 * An implementation of ExpansionCodeBookInterface using an array.
 */

public class ArrayCodeBook implements ExpansionCodeBookInterface {
    private static final int R = 256;        // alphabet size
    private String[] codebook;
    private int W;       // current codeword width
    private int minW;    // minimum codeword width
    private int maxW;    // maximum codeword width
    private int L;       // maximum number of codewords with
                         // current codeword width (L = 2^W)
    private int code;    // next available codeword value

    public ArrayCodeBook(int minW, int maxW){
        this.maxW = maxW;
        this.minW = minW;
        initialize();
    }
    public int size(){
        return code;
    }


    public int getCodewordWidth(boolean flushIfFull){ //modify
      if(code == L){ //means codebook is full
        if(W < maxW){ //means codebook can be extended AND is full
          return W+1;
        } else { //
          if(flushIfFull){ //means we are flushing codebook after it is full and cant be extended
            return minW;
          } else{ //returns maxW when we dont flush
            return maxW;
          }
        }
      } else { //returns W when the codebook isnt full
        return W;
      }
    }
    //return w+1 if the codebook can still be extended and its full

    //if the codebook is full and cant be extended
    //- it can do 2 things - flush means W = minW(9);
    //if not flushing we return maxW(16)

    //return W if it isnt full

    private void initialize(){ //flushing
        codebook = new String[1 << maxW];
        W = minW;
        L = 1<<W;
        code = 0;
        // initialize symbol table with all 1-character strings
        for (int i = 0; i < R; i++)
            add("" + (char) i, false);
        add("", false); //R is codeword for EOF
    }

    public void add(String str, boolean flushIfFull) {
        boolean haveRoom = false;

        if(code == L){
          if(W < maxW){
            W++;
            L = 1<<W;
          } else if (flushIfFull){ //resets dictionary
            initialize();
          }
        }
        if(code < L){
            haveRoom = true;
        }
        //checking if the next code value is = 2^W then if it we take actions: increase W by 1 (update L accordingly because it doubles when W increases), if increasing W makes it equal to or greater than

        if(haveRoom){
            codebook[code] = str;
            code++;
        }
    }

    public String getString(int codeword) {
        return codebook[codeword];
    }

}
