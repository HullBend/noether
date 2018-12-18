package math.matrix;

public final class Trlpack {

    public static void trlpack(int mc, boolean unit, int L_start, double[] L, int incRowL, int incColL,
            double[] buffer) {
        final int mp = mc / BlockSizes.MR;
        final int mr_ = mc % BlockSizes.MR;

        int buffer_start = 0;

        for (int i = 0; i < mp; ++i) {
            trlpack_MRxk((i + 1) * BlockSizes.MR, unit, L_start, L, incRowL, incColL, buffer, buffer_start);
            buffer_start += (i + 1) * BlockSizes.MR * BlockSizes.MR;
            L_start += BlockSizes.MR * incRowL;
        }

        if (mr_ > 0) {
            for (int j = 0; j < mp * BlockSizes.MR; ++j) {
                for (int i = 0; i < mr_; ++i) {
                    buffer[buffer_start + i] = L[L_start + i * incRowL];
                }
                for (int i = mr_; i < BlockSizes.MR; ++i) {
                    buffer[buffer_start + i] = 0.0;
                }
                buffer_start += BlockSizes.MR;
                L_start += incColL;
            }
            for (int j = 0; j < mr_; ++j) {
                for (int i = 0; i < j; ++i) {
                    buffer[buffer_start + i] = 0.0;
                }
                buffer[buffer_start + j] = (unit) ? 1.0 : L[L_start + j * incRowL];
                for (int i = j + 1; i < mr_; ++i) {
                    buffer[buffer_start + i] = L[L_start + i * incRowL];
                }
                for (int i = mr_; i < BlockSizes.MR; ++i) {
                    buffer[buffer_start + i] = 0.0;
                }
                buffer_start += BlockSizes.MR;
                L_start += incColL;
            }
        }
    }

    private static void trlpack_MRxk(int k, boolean unit, int L_start, double[] L, int incRowL, int incColL,
            double[] buffer, int buffer_start) {
        for (int j = 0; j < k - BlockSizes.MR; ++j) {
            for (int i = 0; i < BlockSizes.MR; ++i) {
                buffer[buffer_start + i] = L[L_start + i * incRowL];
            }
            buffer_start += BlockSizes.MR;
            L_start += incColL;
        }
        for (int j = 0; j < BlockSizes.MR; ++j) {
            for (int i = 0; i < j; ++i) {
                buffer[buffer_start + i] = 0.0;
            }
            buffer[buffer_start + j] = (unit) ? 1.0 : L[L_start + j * incRowL];
            for (int i = j + 1; i < BlockSizes.MR; ++i) {
                buffer[buffer_start + i] = L[L_start + i * incRowL];
            }
            buffer_start += BlockSizes.MR;
            L_start += incColL;
        }
    }

    private Trlpack() {
        throw new AssertionError();
    }
}
