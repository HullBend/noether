package math.matrix;

import java.util.Arrays;

//
// Compute Y += alpha*X
//
public final class Axpy {

	// private static final int UNROLLING_FACTOR = 4;
	private static final int UNROLLING_FACTOR = 8;

	public static void axpy(int n, double alpha, double[] x, int xOff, int incX, double[] y, int yOff, int incY) {
		if (n <= 0 || alpha == 0.0) {
			return;
		}

		if (incX == 1 && incY == 1) {
			// code for both increments equal to 1
			int nb = n / UNROLLING_FACTOR;
			int nl = n % UNROLLING_FACTOR;

			int idx = 0;

			if (nb > 0) {
				for (int i = 0; i < nb; ++i) {

					y[idx + yOff] += alpha * x[idx++ + xOff];
					y[idx + yOff] += alpha * x[idx++ + xOff];
					y[idx + yOff] += alpha * x[idx++ + xOff];
					y[idx + yOff] += alpha * x[idx++ + xOff];
					if (UNROLLING_FACTOR == 8) {
						y[idx + yOff] += alpha * x[idx++ + xOff];
						y[idx + yOff] += alpha * x[idx++ + xOff];
						y[idx + yOff] += alpha * x[idx++ + xOff];
						y[idx + yOff] += alpha * x[idx++ + xOff];
					}
				}
			}

			if (nl == 0) {
				return;
			}

			final int end = nl + idx;
			for (int i = idx; i < end; ++i) {
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

	public static void main(String[] args) {
		double[] y = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, }; // 20
		double[] x = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21 }; // 20
		double[] y1 = { 1, 1, 1, 1, 1, 1, 1, 1, }; // 8
		double[] x1 = { 2, 3, 4, 5, 6, 7, 8, 9, }; // 8
		double[] y2 = { 1, 1, 1, 1, 1, 1, 1, }; // 7
		double[] x2 = { 2, 3, 4, 5, 6, 7, 8, }; // 7
		double alpha = 1.5;
		System.out.println("y : " + y.length);
		System.out.println("x : " + x.length);

		axpy(y.length, alpha, x, 0, 1, y, 0, 1);
		System.out.println(Arrays.toString(y));
	}

	private Axpy() {
		throw new AssertionError();
	}
}
