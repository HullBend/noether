package math.matrix;

final class BlockSizes {

   static final int MR = 4; // 4
   static final int NR = 4; // 4

   static final int MC = 384; // 384
   static final int KC = 384; // 384
   static final int NC = 4096; // 4096 .. 16384

   private BlockSizes() {
      throw new AssertionError();
   }
}
