package math.matrix;

public final class Trmm {

    public static void trmm(boolean leftSide, boolean lowerTriang, boolean transA, boolean unitDiag, int m, int n,
            double alpha, double[] A, int _a_offset, int lda, double[] B, int _b_offset, int ldb) {

        if (leftSide) {
            if (lowerTriang) {
                if (!transA) {
                    Trlmm.trlmm(m, n, alpha, unitDiag, _a_offset, A, 1, lda, _b_offset, B, 1, ldb);
                } else {
                    Trumm.trumm(m, n, alpha, unitDiag, _a_offset, A, lda, 1, _b_offset, B, 1, ldb);
                }
            } else {
                if (!transA) {
                    Trumm.trumm(m, n, alpha, unitDiag, _a_offset, A, 1, lda, _b_offset, B, 1, ldb);
                } else {
                    Trlmm.trlmm(m, n, alpha, unitDiag, _a_offset, A, lda, 1, _b_offset, B, 1, ldb);
                }
            }
        } else {
            if (lowerTriang) {
                if (!transA) {
                    Trumm.trumm(n, m, alpha, unitDiag, _a_offset, A, lda, 1, _b_offset, B, ldb, 1);
                } else {
                    Trlmm.trlmm(n, m, alpha, unitDiag, _a_offset, A, 1, lda, _b_offset, B, ldb, 1);
                }
            } else {
                if (!transA) {
                    Trlmm.trlmm(n, m, alpha, unitDiag, _a_offset, A, lda, 1, _b_offset, B, ldb, 1);
                } else {
                    Trumm.trumm(n, m, alpha, unitDiag, _a_offset, A, 1, lda, _b_offset, B, ldb, 1);
                }
            }
        }
    }

    private Trmm() {
        throw new AssertionError();
    }
}
