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


    public int getCodewordWidth(boolean flushIfFull){ //modified
      if(code == L){ //means codebook is full
        if(W < maxW){ //means codebook can be extended AND is full
          return W+1; //since codebook CAN be extended we return new width
        } else { //when codebook is full but CANT be extended
          if(flushIfFull){ //if the flushIfFull boolean returns true, this means we are flushing the codebook after it is full and cant be extended
            return minW; //flushing means returning to the minimum value the codeword width could be which is 9 bits
          } else{ //returns maxW when flushIfFull isnt true, since we dont flush - meaning we keep the codeword width where it is now, which is the max value since it's full
            return maxW;
          }
        }
      } else { //simply returns W, which is the current codeword width, when the codebook isnt full
        return W;
      }
    }

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

        if(code == L){ //if codebook is full
          if(W < maxW){ //if codebook can be extended AND is full
            W++; //increment codeword width since codebook can be extended
            L = 1<<W; //updates L by doing a left bitwise shift since that is the same thing as updating L with 2^W.
          } else if (flushIfFull){ //when flushIfFull returns true, we reset dictionary because that means codebook is full AND cant be extended
            initialize();
          }
        }
        if(code < L){
            haveRoom = true;
        }

        if(haveRoom){
            codebook[code] = str;
            code++;
        }
    }

    public String getString(int codeword) {
        return codebook[codeword];
    }

}
