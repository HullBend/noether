package math.matrix;

public final class Mtrumm {

    public static void mtrumm(int mc, int nc, double alpha, double[] A_, double[] B_, int B_start, double[] B,
            int incRowB, int incColB, double[] AB, double[] workC) {

        final int mp = (mc + BlockSizes.MR - 1) / BlockSizes.MR;
        final int np = (nc + BlockSizes.NR - 1) / BlockSizes.NR;

        final int mr_ = mc % BlockSizes.MR;
        final int nr_ = nc % BlockSizes.NR;

        int mr, nr;
        int kc;

        for (int j = 0; j < np; ++j) {
            nr = (j != np - 1 || nr_ == 0) ? BlockSizes.NR : nr_;

            int ia = 0;
            for (int i = 0; i < mp; ++i) {
                mr = (i != mp - 1 || mr_ == 0) ? BlockSizes.MR : mr_;
                kc = Math.max(mc - i * BlockSizes.MR, 0);

                if (mr == BlockSizes.MR && nr == BlockSizes.NR) {
                    Ugemm.ugemm(kc, alpha, (ia * BlockSizes.MR), // A_ start
                            A_, (j * mc * BlockSizes.NR + i * BlockSizes.MR * BlockSizes.NR), // B_
                                                                                                // start
                            B_, 0.0, // beta
                            (B_start + i * BlockSizes.MR * incRowB + j * BlockSizes.NR * incColB), // B
                            // start
                            B, incRowB, incColB, AB);
                } else {
                    // Call the buffered micro kernel
                    Ugemm.ugemm(mr, nr, kc, alpha, (ia * BlockSizes.MR), // A_
                                                                            // start
                            A_, (j * mc * BlockSizes.NR + i * BlockSizes.MR * BlockSizes.NR), // B_
                                                                                                // start
                            B_, 0.0, // beta
                            (B_start + i * BlockSizes.MR * incRowB + j * BlockSizes.NR * incColB), // B
                            // start
                            B, incRowB, incColB, AB, workC);
                }
                ia += kc;
            }
        }
    }

    private Mtrumm() {
        throw new AssertionError();
    }
}
