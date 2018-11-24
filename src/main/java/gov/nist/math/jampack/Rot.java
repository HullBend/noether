package gov.nist.math.jampack;

/**
 * Rot generates and manipulates plane rotations. Given a 2-vector components
 * are x and y, there is a unitary matrix P such that
 * 
 * <pre>
 *      P|x| =  |   c      s||x| = |z|
 *       |y|    |-conj(s)  c||y|   |0|
 * </pre>
 * 
 * The number c, which is always real, is the cosine of the rotation. The number
 * s, which may be complex is the sine of the rotation.
 * <p>
 * Comments: This suite will eventually contain methods for real rotations (two
 * are already in place). The only difference between real and complex rotations
 * is that si and zi are zero for the former. The final routines will do the
 * efficient thing.
 * 
 * @version Pre-alpha
 * @author G. W. Stewart
 */
public final class Rot {

    /** The cosine of the rotation */
    double c;
    /** The real part of the sine of the rotation */
    double sr;
    /** The imaginary part of the sine of the rotation */
    double si;
    /** The real part of the first component of the transformed vector */
    double zr;
    /** The imaginary part of the first component of the transformed vector */
    double zi;

    /**
     * Given a real 2-vectc, genc generates a real plane rotation P such that
     * 
     * <pre>
     *      P|x| =  | c  s||x| = |z|
     *       |y|    |-s  c||y|   |0|
     * </pre>
     * 
     * @param x
     *            The first component of the two vector
     * @param y
     *            The second component of the two vector
     * @param P
     *            The plane rotation
     */
    static void genc(double x, double y, Rot P) {
        P.si = 0.0;
        P.zi = 0.0;

        if (x == 0.0 & y == 0.0) {
            P.c = 1.0;
            P.sr = 0.0;
            P.zr = 0.0;
            return;
        }

        double s = Math.abs(x) + Math.abs(y);
        P.zr = s * Math.sqrt((x / s) * (x / s) + (y / s) * (y / s));
        P.c = x / P.zr;
        P.sr = y / P.zr;
    }

    /**
     * Given a real 2-vector, genr generates a plane rotation such that
     * 
     * <pre>
     *      |x y|P = |x y|| c  s||x| = |z 0|
     *                    |-s  c||y|
     * </pre>
     * 
     * @param x
     *            The first component of the 2-vector
     * @param y
     *            The second component of the 2-vector
     * @param P
     *            The rotation
     */
    static void genr(double x, double y, Rot P) {
        P.si = 0.0;
        P.zi = 0.0;

        double s = Math.abs(x) + Math.abs(y);

        if (s == 0.0) {
            P.c = 1.0;
            P.sr = 0.0;
            P.zr = 0.0;
            return;
        }

        P.zr = s * Math.sqrt((x / s) * (x / s) + (y / s) * (y / s));
        P.c = x / P.zr;
        P.sr = -y / P.zr;
    }

    /**
     * Multiplies columns (ii1:ii2,jj1) and A(ii2:ii2,jj1) of a Zmat (altered)
     * by a plane rotation.
     * 
     * @param A
     *            The Zmat (altered)
     * @param P
     *            The rotation
     * @param ii1
     *            The first index of the column range
     * @param ii2
     *            The second index of the column range
     * @param jj1
     *            The index of the first column
     * @param jj2
     *            The index of the second column
     */
    static void ap(Zmat A, Rot P, int ii1, int ii2, int jj1, int jj2) {
        double t1r, t1i, t2r, t2i;

        int i1 = ii1 - 1;
        int i2 = ii2 - 1;
        int j1 = jj1 - 1;
        int j2 = jj2 - 1;

        for (int i = i1; i <= i2; i++) {
            t1r = P.c * A.re[i][j1] - P.sr * A.re[i][j2] - P.si * A.im[i][j2];
            t1i = P.c * A.im[i][j1] - P.sr * A.im[i][j2] + P.si * A.re[i][j2];
            t2r = P.c * A.re[i][j2] + P.sr * A.re[i][j1] - P.si * A.im[i][j1];
            t2i = P.c * A.im[i][j2] + P.sr * A.im[i][j1] + P.si * A.re[i][j1];
            A.re[i][j1] = t1r;
            A.im[i][j1] = t1i;
            A.re[i][j2] = t2r;
            A.im[i][j2] = t2i;
        }
    }

    /**
     * Multiplies columns (ii1:ii2,jj1) and A(ii2:ii2,jj1) of a Zmat (altered)
     * by the conjugate transpose of plane rotation.
     * 
     * @param A
     *            The Zmat (altered)
     * @param P
     *            The rotation
     * @param ii1
     *            The first index of the column range
     * @param ii2
     *            The second index of the column range
     * @param jj1
     *            The index of the first column
     * @param jj2
     *            The index of the second column
     */
    static void aph(Zmat A, Rot P, int ii1, int ii2, int jj1, int jj2) {
        double t1r, t1i, t2r, t2i;

        int i1 = ii1 - 1;
        int i2 = ii2 - 1;
        int j1 = jj1 - 1;
        int j2 = jj2 - 1;

        for (int i = i1; i <= i2; i++) {
            t1r = P.c * A.re[i][j1] + P.sr * A.re[i][j2] + P.si * A.im[i][j2];
            t1i = P.c * A.im[i][j1] + P.sr * A.im[i][j2] - P.si * A.re[i][j2];
            t2r = P.c * A.re[i][j2] - P.sr * A.re[i][j1] + P.si * A.im[i][j1];
            t2i = P.c * A.im[i][j2] - P.sr * A.im[i][j1] - P.si * A.re[i][j1];
            A.re[i][j1] = t1r;
            A.im[i][j1] = t1i;
            A.re[i][j2] = t2r;
            A.im[i][j2] = t2i;
        }
    }

    /**
     * Given the real and imaginary parts of a 2-vector, genc generates a plane
     * rotation P such that
     * 
     * <pre>
     *      P|x| =  |   c      s||x| = |z|
     *       |y|    |-conj(s)  c||y|   |0|
     * </pre>
     * 
     * @param xr
     *            The real part of the first component of the 2-vector
     * @param xi
     *            The imaginary part of the first component of the 2-vector
     * @param yr
     *            The real part of the second component of the 2-vector
     * @param yi
     *            The imaginary part of the second component of the 2-vector
     * @param P
     *            The rotation (must be initialized)
     */
    static void genc(double xr, double xi, double yr, double yi, Rot P) {
        double s, absx, absxy;

        if (xr == 0.0 && xi == 0.0) {
            P.c = 0.0;
            P.sr = 1.0;
            P.si = 0.0;
            P.zr = yr;
            P.zi = yi;
            return;
        }
        s = Math.abs(xr) + Math.abs(xi);
        absx = s * Math.sqrt((xr / s) * (xr / s) + (xi / s) * (xi / s));
        s = Math.abs(s) + Math.abs(yr) + Math.abs(yi);
        absxy = s * Math.sqrt((absx / s) * (absx / s) + (yr / s) * (yr / s) + (yi / s) * (yi / s));
        P.c = absx / absxy;
        xr = xr / absx;
        xi = xi / absx;
        P.sr = (xr * yr + xi * yi) / absxy;
        P.si = (xi * yr - xr * yi) / absxy;
        P.zr = xr * absxy;
        P.zi = xi * absxy;
    }

    /**
     * Given a Zmat A, genc generates a plane rotation that on premultiplication
     * into rows ii1 and ii2 annihilates A(ii2,jj). The element A(ii2,jj) is
     * overwritten by zero and the element A(ii1,jj) is overwritten by its
     * transformed value.
     * 
     * @param A
     *            The Zmat (altered)
     * @param ii1
     *            The row index of the first element
     * @param ii2
     *            The row index of the second element (the one that is
     *            annihilated
     * @param jj
     *            The column index of the elements
     * @param P
     *            The plane rotation (must be initialized)
     */
    static void genc(Zmat A, int ii1, int ii2, int jj, Rot P) {
        int i1 = ii1 - 1;
        int i2 = ii2 - 1;
        int j = jj - 1;

        Rot.genc(A.re[i1][j], A.im[i1][j], A.re[i2][j], A.im[i2][j], P);
        A.re[i1][j] = P.zr;
        A.im[i1][j] = P.zi;
        A.re[i2][j] = 0;
        A.im[i2][j] = 0;
    }

    /**
     * Multiplies rows (ii1,jj1:jj2) and (ii2,jj1:jj2) of a Zmat (altered) by a
     * plane rotation.
     * 
     * @param P
     *            The plane rotation
     * @param A
     *            The Zmat (altered)
     * @param ii1
     *            The row index of the first row.
     * @param ii2
     *            The row index of the second row.
     * @param jj1
     *            The first index of the range of the rows
     * @param jj2
     *            The second index of the range of the rows
     */
    static void pa(Rot P, Zmat A, int ii1, int ii2, int jj1, int jj2) {
        double t1r, t1i, t2r, t2i;

        int i1 = ii1 - 1;
        int i2 = ii2 - 1;
        int j1 = jj1 - 1;
        int j2 = jj2 - 1;

        for (int j = j1; j <= j2; j++) {
            t1r = P.c * A.re[i1][j] + P.sr * A.re[i2][j] - P.si * A.im[i2][j];
            t1i = P.c * A.im[i1][j] + P.sr * A.im[i2][j] + P.si * A.re[i2][j];
            t2r = P.c * A.re[i2][j] - P.sr * A.re[i1][j] - P.si * A.im[i1][j];
            t2i = P.c * A.im[i2][j] - P.sr * A.im[i1][j] + P.si * A.re[i1][j];
            A.re[i1][j] = t1r;
            A.im[i1][j] = t1i;
            A.re[i2][j] = t2r;
            A.im[i2][j] = t2i;
        }
    }
}
