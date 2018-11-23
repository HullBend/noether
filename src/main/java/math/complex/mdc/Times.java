package math.complex.mdc;

final class Times {

    /**
     * Computes the product of a Z and a Zmat.
     * 
     * @param z
     *            The complex scalar
     * @param A
     *            The Zmat
     * @return zA
     */
    static Zmat o(Z z, Zmat A) {
        Zmat B = new Zmat(A.nrow, A.ncol);
        for (int i = 0; i < A.nrow; i++)
            for (int j = 0; j < A.ncol; j++) {
                B.re[i][j] = z.re * A.re[i][j] - z.im * A.im[i][j];
                B.im[i][j] = z.im * A.re[i][j] + z.re * A.im[i][j];
            }
        return B;
    }

    /**
     * Computes the product of two Zmats.
     * 
     * @param A
     *            The first Zmat
     * @param B
     *            The second Zmat
     * @return AB
     * @exception ZException
     *                for unconformity
     */
    static Zmat o(Zmat A, Zmat B) throws ZException {
        if (A.ncol != B.nrow)
            throw new ZException("Unconformity in product");
        Zmat C = new Zmat(A.nrow, B.ncol);
        for (int i = 0; i < A.nrow; i++)
            for (int k = 0; k < A.ncol; k++)
                for (int j = 0; j < B.ncol; j++) {
                    C.re[i][j] = C.re[i][j] + A.re[i][k] * B.re[k][j] - A.im[i][k] * B.im[k][j];
                    C.im[i][j] = C.im[i][j] + A.im[i][k] * B.re[k][j] + A.re[i][k] * B.im[k][j];
                }
        return C;
    }

    /**
     * Computes A<sup>H</sup>A, where A is a Zmat.
     * 
     * @param A
     *            The Zmat
     * @return A<sup>H</sup>A
     */
    static Zmat aha(Zmat A) {
        Zmat C = new Zmat(A.ncol, A.ncol, true);
        for (int k = 0; k < A.nrow; k++) {
            for (int i = 0; i < A.ncol; i++) {
                C.re[i][i] = C.re[i][i] + A.re[k][i] * A.re[k][i] + A.im[k][i] * A.im[k][i];
                C.im[i][i] = 0.;
                for (int j = i + 1; j < A.ncol; j++) {
                    C.re[i][j] = C.re[i][j] + A.re[k][i] * A.re[k][j] + A.im[k][i] * A.im[k][j];
                    C.im[i][j] = C.im[i][j] + A.re[k][i] * A.im[k][j] - A.im[k][i] * A.re[k][j];
                }
            }
        }
        for (int i = 0; i < A.ncol; i++) {
            for (int j = i + 1; j < A.ncol; j++) {
                C.re[j][i] = C.re[i][j];
                C.im[j][i] = -C.im[i][j];
            }
        }
        return C;
    }

    /**
     * Computes AA<sup>H</sup>, where A is a Zmat.
     * 
     * @param A
     *            The Zmat
     * @return AA<sup>H</sup>
     */
    static Zmat aah(Zmat A) {
        Zmat C = new Zmat(A.nrow, A.nrow, true);
        for (int i = 0; i < A.nrow; i++) {
            for (int k = 0; k < A.ncol; k++) {
                C.re[i][i] = C.re[i][i] + A.re[i][k] * A.re[i][k] + A.im[i][k] * A.im[i][k];
            }
            C.im[i][i] = 0.;
            for (int j = i + 1; j < A.nrow; j++) {
                for (int k = 0; k < A.ncol; k++) {
                    C.re[i][j] = C.re[i][j] + A.re[i][k] * A.re[j][k] + A.im[i][k] * A.im[j][k];
                    C.im[i][j] = C.im[i][j] - A.re[i][k] * A.im[j][k] + A.im[i][k] * A.re[j][k];
                }
                C.re[j][i] = C.re[i][j];
                C.im[j][i] = -C.im[i][j];
            }
        }
        return C;
    }

    /**
     * Computes the product of a Z and a Zdiagmat.
     * 
     * @param z
     *            The complex scalar
     * @param D
     *            The Zdiagmat
     * @return zD
     */
    static Zdiagmat o(Z z, Zdiagmat D) {
        Zdiagmat B = new Zdiagmat(D);
        for (int i = 0; i < D.order; i++) {
            B.re[i] = z.re * D.re[i] - z.im * D.im[i];
            B.im[i] = z.im * D.re[i] + z.re * D.im[i];
        }
        return B;
    }

    /**
     * Computes the product of two Zdiagmats.
     * 
     * @param D1
     *            The first Zdiagmat
     * @param D2
     *            The second Zdiagmat
     * @return D1*D2
     * @exception ZException
     *                for unconformity
     */
    static Zdiagmat o(Zdiagmat D1, Zdiagmat D2) throws ZException {
        if (D1.order != D2.order) {
            throw new ZException("Unconformity in product");
        }
        Zdiagmat D3 = new Zdiagmat(D1.order);
        for (int i = 0; i < D3.order; i++) {
            D3.re[i] = D1.re[i] * D2.re[i] - D1.im[i] * D2.im[i];
            D3.im[i] = D1.re[i] * D2.im[i] + D1.im[i] * D2.re[i];
        }
        return D3;
    }

    /**
     * Computes the product of a Zdiagmat and a Zmat.
     * 
     * @param D
     *            The Zdiagmat
     * @param A
     *            The Zmat
     * @return DA
     * @exception ZException
     *                for unconformity
     */
    static Zmat o(Zdiagmat D, Zmat A) throws ZException {
        if (D.order != A.nrow) {
            throw new ZException("Unconformity in product.");
        }
        Zmat B = new Zmat(A.nrow, A.ncol);
        for (int i = 0; i < A.nrow; i++) {
            for (int j = 0; j < A.nc; j++) {
                B.re[i][j] = D.re[i] * A.re[i][j] - D.im[i] * A.im[i][j];
                B.im[i][j] = D.re[i] * A.im[i][j] + D.im[i] * A.re[i][j];
            }
        }
        return B;
    }

    /**
     * Computes the product of a Zmat and a Zdiagmat.
     * 
     * @param A
     *            The Zgmat
     * @param D
     *            The Zdiagmat
     * @return AD
     * @exception ZException
     *                for unconformity
     */
    static Zmat o(Zmat A, Zdiagmat D) throws ZException {
        if (D.order != A.ncol) {
            throw new ZException("Unconformity in product.");
        }
        Zmat B = new Zmat(A.nrow, A.ncol);
        for (int i = 0; i < A.nrow; i++) {
            for (int j = 0; j < A.ncol; j++) {
                B.re[i][j] = D.re[j] * A.re[i][j] - D.im[j] * A.im[i][j];
                B.im[i][j] = D.re[j] * A.im[i][j] + D.im[j] * A.re[i][j];
            }
        }
        return B;
    }
}