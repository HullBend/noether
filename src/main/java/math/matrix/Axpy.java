package math.matrix;

//
// Compute Y += alpha*X
//
public final class Axpy {

    public static void axpy(int n, double alpha, double[] x, int xOff, int incX, double[] y, int yOff, int incY) {
        if (n <= 0 || alpha == 0.0) {
            return;
        }

        if (incX == 1 && incY == 1) {
            // code for both increments equal to 1

            for (int i = 0; i < n; ++i) {
                y[i + yOff] += alpha * x[i + xOff];
            }

        } else {
            // code for unequal increments or
            // equal increments greater than 1
            if (incX <= 0 || incY <= 0) {
                throw new IllegalArgumentException("illegal non-positive incrememts: incX=" + incX + ", incY=" + incY);
            }
            for (int i = 0; i < n; ++i) {
                y[i * incY + yOff] += alpha * x[i * incX + xOff];
            }
        }
    }

    private Axpy() {
        throw new AssertionError();
    }
}
