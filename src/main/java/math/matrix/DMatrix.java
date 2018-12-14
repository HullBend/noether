package math.matrix;

import java.util.Arrays;

/**
 * A {@code DMatrix} is a dense matrix of primitive doubles which can have
 * either column-major storage layout or row-major storage layout (column-major
 * is the default when a new {@code DMatrix} is created). All operations throw a
 * {@code NullPointerException} if any of the method arguments is {@code null}.
 * <p>
 * <b>Note: this is experimental, unfinished and untested code!</b>
 */
public final class DMatrix {

    private /* final */ double[] a;
    private /* final */ int rows;
    private /* final */ int cols;
    private Layout layout = Layout.COL_MAJOR;

    public DMatrix(int rows, int cols) {
        this(rows, cols, new double[rows * cols], Layout.COL_MAJOR, DoCopy.NO);
    }

    public DMatrix(int rows, int cols, double scalar) {
        this(rows, cols, new double[rows * cols], Layout.COL_MAJOR, DoCopy.NO);
        Arrays.fill(a, scalar);
    }

    public DMatrix(double[][] A) {
        rows = A.length;
        cols = A[0].length;
        a = copyJaggedArray(A, Layout.COL_MAJOR);
    }

    public DMatrix(int rows, int cols, double[] array, Layout arrayLayout, DoCopy doCopy) {
        checkRows(rows);
        checkCols(cols);
        checkArrayLength(array, rows, cols);
        checkLayoutArg(arrayLayout);
        checkDoCopyArg(doCopy);
        if (doCopy == DoCopy.YES) {
            double[] copy = new double[array.length];
            System.arraycopy(array, 0, copy, 0, copy.length);
            a = copy;
        } else {
            a = array;
        }
        layout = arrayLayout;
        this.rows = rows;
        this.cols = cols;
    }

    public int getRowDim() {
        return rows;
    }

    public int getColDim() {
        return cols;
    }

    public double get(int row, int col) {
        checkIJ(row, col);
        return a[idx(row, col)];
    }

    public DMatrix set(int row, int col, double val) {
        checkIJ(row, col);
        a[idx(row, col)] = val;
        return this;
    }

    public Layout getLayout() {
        checkLayout();
        return layout;
    }

    public boolean isColumnPacked() {
        checkLayout();
        return layout == Layout.COL_MAJOR;
    }

    public boolean isRowPacked() {
        checkLayout();
        return layout == Layout.ROW_MAJOR;
    }

    public boolean isSquare() {
        return rows == cols;
    }

    public DMatrix transformToColumnPackedInplace() {
        if (isColumnPacked()) {
            return this;
        }
        // do it the simple way in the first prototype
        a = createCopy(Layout.COL_MAJOR);
        layout = Layout.COL_MAJOR;
        return this;
    }

    public DMatrix transformToRowPackedInplace() {
        if (isRowPacked()) {
            return this;
        }
        // do it the simple way in the first prototype
        a = createCopy(Layout.ROW_MAJOR);
        layout = Layout.ROW_MAJOR;
        return this;
    }

    public DMatrix getColumnPackedCopy() {
        return new DMatrix(rows, cols, createCopy(Layout.COL_MAJOR), Layout.COL_MAJOR, DoCopy.NO);
    }

    public DMatrix getRowPackedCopy() {
        return new DMatrix(rows, cols, createCopy(Layout.ROW_MAJOR), Layout.ROW_MAJOR, DoCopy.NO);
    }

    public DMatrix deepCopy() {
        if (isRowPacked()) {
            return getRowPackedCopy();
        }
        return getColumnPackedCopy();
    }

    public DMatrix shallowCopy() {
        return new DMatrix(rows, cols, a, layout, DoCopy.NO);
    }

    public ArrayDesc getArrayUnsafe() {
        return new ArrayDesc(a, layout);
    }

    public ArrayDesc getArrayCopy() {
        return new ArrayDesc(createCopy(layout), layout);
    }

    public double[] getArrayColumnPackedCopy() {
        return createCopy(Layout.COL_MAJOR);
    }

    public double[] getArrayRowPackedCopy() {
        return createCopy(Layout.ROW_MAJOR);
    }

    public double[][] getJaggedArrayCopy() {
        checkLayout();
        int _rows = rows;
        int _cols = cols;
        double[] _a = a;
        double[][] copy = new double[_rows][_cols];
        if (layout == Layout.COL_MAJOR) {
            for (int row = 0; row < _rows; ++row) {
                double[] row_i = copy[row];
                for (int col = 0; col < row_i.length; ++col) {
                    row_i[col] = _a[col * _rows + row];
                }
            }
        } else {
            for (int row = 0; row < _rows; ++row) {
                double[] row_i = copy[row];
                for (int col = 0; col < row_i.length; ++col) {
                    row_i[col] = _a[row * _cols + col];
                }
            }
        }
        return copy;
    }

    /**
     * Get a submatrix copy in {@linkplain Layout#COL_MAJOR} order.
     * 
     * @param r0
     *            initial row index (left upper corner) in this matrix
     * @param c0
     *            initial col index (left upper corner) in this matrix
     * @param r1
     *            last row index (right lower corner) in this matrix
     * @param c1
     *            last col index (right lower corner) in this matrix
     * @return the submatrix
     */
    public DMatrix getSubmatrixCopy(int r0, int c0, int r1, int c1) {
        checkSubmatrixIndices(r0, c0, r1, c1);
        checkLayout();
        int _rows = r1 - r0 + 1;
        int _cols = c1 - c0 + 1;
        double[] sub = new double[_rows * _cols];
        // Note: Submatrices are always created in COL_MAJOR order!
        double[] myA = a;
        if (layout == Layout.COL_MAJOR) {
            int myRows = rows;
            for (int col = 0; col < _cols; ++col) {
                for (int row = 0; row < _rows; ++row) {
                    // COL_MAJOR <- COL_MAJOR
                    sub[col * _rows + row] = myA[(col + c0) * myRows + row + r0];
                }
            }
        } else {
            int myCols = cols;
            for (int col = 0; col < _cols; ++col) {
                for (int row = 0; row < _rows; ++row) {
                    // COL_MAJOR <- ROW_MAJOR
                    sub[col * _rows + row] = myA[(row + r0) * myCols + col + c0];
                }
            }
        }
        return new DMatrix(_rows, _cols, sub, Layout.COL_MAJOR, DoCopy.NO);
    }

    /**
     * Set a submatrix from the values of matrix {@code X}. The values of matrix
     * {@code X} are always copied beginning in the upper left corner (position
     * {@code (0, 0)}) of matrix {@code X}.
     * 
     * @param r0
     *            initial row index (left upper corner) in this matrix
     * @param c0
     *            initial col index (left upper corner) in this matrix
     * @param r1
     *            last row index (right lower corner) in this matrix
     * @param c1
     *            last col index (right lower corner) in this matrix
     * @param X
     *            the matrix that holds the values to set in this matrix
     * @return this
     */
    public DMatrix setSubmatrix(int r0, int c0, int r1, int c1, DMatrix X) {
        checkSubmatrixIndices(r0, c0, r1, c1);
        checkLayout();
        int _rows = r1 - r0 + 1;
        int _cols = c1 - c0 + 1;
        if (X.getRowDim() < _rows || X.getColDim() < _cols) {
            throw new IllegalArgumentException("Matrix X is only (" + X.getRowDim() + " x " + X.getColDim() + ") but ("
                    + _rows + " x " + _cols + ") values are necessary");
        }
        for (int col = c0; col <= c1; ++col) {
            for (int row = r0; row <= r1; ++row) {
                set(row, col, X.get(row - r0, col - c0));
            }
        }
        return this;
    }

    private void checkSubmatrixIndices(int r0, int c0, int r1, int c1) {
        checkIJ(r0, c0);
        checkIJ(r1, c1);
        int _rows = r1 - r0 + 1;
        int _cols = c1 - c0 + 1;
        if (_rows <= 0 || _cols <= 0) {
            throw new IllegalArgumentException(
                    "illegal submatrix indices : [" + r0 + ", " + c0 + ", " + r1 + ", " + c1 + "]");
        }
    }

    private static double[] copyJaggedArray(double[][] data, Layout layoutOfCopy) {
        int _rows = data.length;
        int _cols = data[0].length;
        if (_rows < 1 || _cols < 1) {
            throw new IllegalArgumentException(
                    "number of rows and columns must be strictly positive : (" + _rows + " x " + _cols + ")");
        }
        double[] copy = new double[_rows * _cols];
        if (layoutOfCopy == Layout.COL_MAJOR) {
            for (int row = 0; row < _rows; ++row) {
                double[] row_i = data[row];
                if (row_i.length != _cols) {
                    throwInconsistentRowLengths(_cols, row, row_i.length);
                }
                for (int col = 0; col < row_i.length; ++col) {
                    copy[col * _rows + row] = row_i[col];
                }
            }
            return copy;
        } else {
            for (int row = 0; row < _rows; ++row) {
                double[] row_i = data[row];
                if (row_i.length != _cols) {
                    throwInconsistentRowLengths(_cols, row, row_i.length);
                }
                for (int col = 0; col < row_i.length; ++col) {
                    copy[row * _cols + col] = row_i[col];
                }
            }
            return copy;
        }
    }

    public DMatrix transposeInplace() {
        switchRowsAndCols();
        layout = getSwitchedLayout();
        return this;
    }

    public DMatrix transposeCopy() {
        // the layout of the copy will be the same as this instance's layout
        // but for transposition to take effect during copying we have to
        // pretend it's the opposite
        return new DMatrix(cols, rows, createCopy(getSwitchedLayout()), layout, DoCopy.NO);
    }

    private int idx(int row, int col) {
        if (layout == Layout.COL_MAJOR) {
            return col * rows + row;
        } else if (layout == Layout.ROW_MAJOR) {
            return row * cols + col;
        }
        throw new IllegalStateException();
    }

    private int colMajorIdx(int row, int col) {
        return col * rows + row;
    }

    private int rowMajorIdx(int row, int col) {
        return row * cols + col;
    }

    private double[] createCopy(Layout layoutOfCopy) {
        if (layout == null || layoutOfCopy == null) {
            throw new IllegalStateException();
        }
        double[] copy = new double[a.length];
        if (layout == layoutOfCopy) {
            System.arraycopy(a, 0, copy, 0, copy.length);
            return copy;
        } else {
            int _rows = rows;
            int _cols = cols;
            double[] _a = a;
            if (layout == Layout.COL_MAJOR) {
                // layoutOfCopy must be ROW_MAJOR
                for (int col = 0; col < _cols; ++col) {
                    for (int row = 0; row < _rows; ++row) {
                        // ROW_MAJOR <-- COL_MAJOR
                        copy[row * _cols + col] = _a[col * _rows + row];
                    }
                }
                return copy;
            } else {
                // layout is ROW_MAJOR and layoutOfCopy
                // is COL_MAJOR
                for (int row = 0; row < _rows; ++row) {
                    for (int col = 0; col < _cols; ++col) {
                        // COL_MAJOR <-- ROW_MAJOR
                        copy[col * _rows + row] = _a[row * _cols + col];
                    }
                }
                return copy;
            }
        }
    }

    private void switchRowsAndCols() {
        int tmp = rows;
        rows = cols;
        cols = tmp;
    }

    private Layout getSwitchedLayout() {
        switch (layout) {
        case COL_MAJOR:
            return Layout.ROW_MAJOR;
        case ROW_MAJOR:
            return Layout.COL_MAJOR;
        default:
            throw new IllegalStateException();
        }
    }

    private void checkLayout() {
        if (layout == null) {
            throw new IllegalStateException();
        }
    }

    private void checkIJ(int i, int j) {
        if (i < 0 || i >= rows) {
            throw new IllegalArgumentException("Illegal row index " + i + " in " + rows + " x " + cols + " matrix");
        }
        if (j < 0 || j >= cols) {
            throw new IllegalArgumentException("Illegal colum index " + j + " in " + rows + " x " + cols + " matrix");
        }
    }

    private static void checkRows(int rows) {
        if (rows <= 0) {
            throw new IllegalArgumentException("number of rows must be strictly positive : " + rows);
        }
    }

    private static void checkCols(int cols) {
        if (cols <= 0) {
            throw new IllegalArgumentException("number of columns must be strictly positive : " + cols);
        }
    }

    private static void checkArrayLength(double[] array, int rows, int cols) {
        if (array.length != rows * cols) {
            throw new IllegalArgumentException(
                    "data array has wrong length. Needed : " + rows * cols + " , Is : " + array.length);
        }
    }

    private static void checkDoCopyArg(DoCopy doCopy) {
        if (doCopy == null) {
            throw new IllegalArgumentException("DoCopy argument must not be null");
        }
    }

    private static void checkLayoutArg(Layout newLayout) {
        if (newLayout == null) {
            throw new IllegalArgumentException("Layout must not be null");
        }
    }

    private static void throwInconsistentRowLengths(int cols, int rowIdx, int rowLength) {
        throw new IllegalArgumentException(
                "All rows must have same length: " + cols + " (row " + rowIdx + " has length " + rowLength + ")");
    }
}
