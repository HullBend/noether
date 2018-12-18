package math.matrix;

//
// Compute A *= alpha
//
public final class Gescal {

	public static void gescal(int m, int n, double alpha, int A_start, double[] A, int incRowA, int incColA) {
		if (alpha == 1.0 || m <= 0 || n <= 0) {
			return;
		}

		if (incRowA == 1) {
			for (int j = 0; j < n; ++j) {
				Scal.scal(m, alpha, A, A_start + j * incColA, 1);
			}
		} else if (incColA == 1) {
			for (int i = 0; i < m; ++i) {
				Scal.scal(n, alpha, A, A_start + i * incRowA, 1);
			}
		} else {
			if (alpha != 0.0) {
				for (int j = 0; j < n; ++j) {
					int base_A = A_start + j * incColA;
					for (int i = 0; i < m; ++i) {
						A[base_A + i * incRowA] *= alpha;
					}
				}
			} else {
				for (int j = 0; j < n; ++j) {
					int base_A = A_start + j * incColA;
					for (int i = 0; i < m; ++i) {
						A[base_A + i * incRowA] = 0.0;
					}
				}
			}
		}
	}

	private Gescal() {
		throw new AssertionError();
	}
}
