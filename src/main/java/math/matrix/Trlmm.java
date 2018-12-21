package math.matrix;

public final class Trlmm {

    public static void trlmm(int m, int n, double alpha, boolean unitDiag, int A_start, double[] A, int incRowA,
            int incColA, int B_start, double[] B, int incRowB, int incColB) {

        if (alpha == 0.0) {
            Gescal.gescal(m, n, 0.0, B_start, B, incRowB, incColB);
            return;
        }

        final int mb = (m + BlockSizes.MC - 1) / BlockSizes.MC;
        final int nb = (n + BlockSizes.NC - 1) / BlockSizes.NC;

        final int mc_ = m % BlockSizes.MC;
        final int nc_ = n % BlockSizes.NC;

        final double[] A_ = new double[BlockSizes.MC * BlockSizes.MC + BlockSizes.MR];
        final double[] B_ = new double[BlockSizes.MC * BlockSizes.NC + BlockSizes.NR];
        final double[] C_ = new double[BlockSizes.MR * BlockSizes.NR];
        final double[] AB = new double[BlockSizes.MR * BlockSizes.NR];

        for (int j = 0; j < nb; ++j) {
            int nc = (j != nb - 1 || nc_ == 0) ? BlockSizes.NC : nc_;

            for (int l = mb - 1; l >= 0; --l) {
                int kc = (l != mb - 1 || mc_ == 0) ? BlockSizes.MC : mc_;

                Gepack.gepack_B(kc, nc, (B_start + l * BlockSizes.MC * incRowB + j * BlockSizes.NC * incColB), // B
                                                                                                                // start
                        B, incRowB, incColB, B_);

                Trlpack.trlpack(kc, unitDiag, (A_start + l * BlockSizes.MC * (incRowA + incColA)), // A
                                                                                                    // start
                        A, incRowA, incColA, A_);

                Mtrlmm.mtrlmm(kc, nc, alpha, A_, B_,
                        (B_start + l * BlockSizes.MC * incRowB + j * BlockSizes.NC * incColB), // B
                                                                                                // start
                        B, incRowB, incColB, AB, C_);

                for (int i = l + 1; i < mb; ++i) {
                    int mc = (i != mb - 1 || mc_ == 0) ? BlockSizes.MC : mc_;

                    Gepack.gepack_A(mc, kc, (A_start + i * BlockSizes.MC * incRowA + l * BlockSizes.MC * incColA), // A
                                                                                                                    // start
                            A, incRowA, incColA, A_);

                    Mgemm.mgemm(mc, nc, kc, alpha, A_, B_, 1.0,
                            (B_start + i * BlockSizes.MC * incRowB + j * BlockSizes.NC * incColB), // B
                                                                                                    // start
                            B, incRowB, incColB, AB, C_);
                }
            }
        }

    }

    private Trlmm() {
        throw new AssertionError();
    }
}
