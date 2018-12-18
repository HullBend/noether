package math.matrix;

//
// Compute Y += alpha*X
//
public final class Geaxpy {

    public static void geaxpy(int m, int n, double alpha, int X_start, double[] X, int incRowX, int incColX,
            int Y_start, double[] Y, int incRowY, int incColY) {
        if (m <= 0 || n <= 0 || alpha == 0.0) {
            return;
        }

        if (incRowX == 1 && incRowY == 1) {
            //
            // X and Y are both column major
            //
            for (int j = 0; j < n; ++j) {
                Axpy.axpy(m, alpha, X, X_start + j * incColX, 1, Y, Y_start + j * incColY, 1);
            }
        } else if (incColX == 1 && incColY == 1) {
            //
            // X and Y are both row major
            //
            for (int i = 0; i < m; ++i) {
                Axpy.axpy(n, alpha, X, X_start + i * incRowX, 1, Y, Y_start + i * incRowY, 1);
            }
        } else {
            //
            // General case
            //
            for (int j = 0; j < n; ++j) {
                int base_Y = Y_start + j * incColY;
                int _incColX = j * incColX;
                for (int i = 0; i < m; ++i) {
                    Y[base_Y + i * incRowY] += alpha * X[i * incRowX + _incColX];
                }
            }
        }
    }

    private Geaxpy() {
        throw new AssertionError();
    }
}
