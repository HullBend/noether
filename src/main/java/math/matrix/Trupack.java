package math.matrix;

public final class Trupack {

    public static void trupack(int mc, boolean unit, int U_start, double[] U, int incRowU, int incColU,
            double[] buffer) {
        final int mp = mc / BlockSizes.MR;
        final int mr_ = mc % BlockSizes.MR;

        int buffer_start = 0;

        for (int i = 0; i < mp; ++i) {
            trupack_MRxk(mc - i * BlockSizes.MR, unit, U_start, U, incRowU, incColU, buffer, buffer_start);
            buffer_start += (mc - i * BlockSizes.MR) * BlockSizes.MR;
            U_start += BlockSizes.MR * (incRowU + incColU);
        }
        if (mr_ > 0) {
            for (int j = 0; j < mr_; ++j) {
                for (int i = 0; i < j; ++i) {
                    buffer[buffer_start + i] = U[U_start + i * incRowU];
                }
                buffer[buffer_start + j] = (unit) ? 1.0 : U[U_start + j * incRowU];
                for (int i = j + 1; i < BlockSizes.MR; ++i) {
                    buffer[buffer_start + i] = 0.0;
                }
                buffer_start += BlockSizes.MR;
                U_start += incColU;
            }
        }
    }

    private static void trupack_MRxk(int k, boolean unit, int U_start, double[] U, int incRowU, int incColU,
            double[] buffer, int buffer_start) {

        for (int j = 0; j < BlockSizes.MR; ++j) {
            for (int i = 0; i < j; ++i) {
                buffer[buffer_start + i] = U[U_start + i * incRowU];
            }
            buffer[buffer_start + j] = (unit) ? 1.0 : U[U_start + j * incRowU];
            for (int i = j + 1; i < BlockSizes.MR; ++i) {
                buffer[buffer_start + i] = 0.0;
            }
            buffer_start += BlockSizes.MR;
            U_start += incColU;
        }
        for (int j = 0; j < k - BlockSizes.MR; ++j) {
            for (int i = 0; i < BlockSizes.MR; ++i) {
                buffer[buffer_start + i] = U[U_start + i * incRowU];
            }
            buffer_start += BlockSizes.MR;
            U_start += incColU;
        }
    }

    private Trupack() {
        throw new AssertionError();
    }
}
