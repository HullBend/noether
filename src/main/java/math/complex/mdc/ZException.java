package math.complex.mdc;

/**
 * This is the exception class for Jampack. Since most errors in matrix
 * algorithms are unrecoverable, the standard response is to pass an error
 * message up the line.
 * 
 * @version Pre-alpha
 * @author G. W. Stewart
 */
@SuppressWarnings("serial")
final class ZException extends RuntimeException {
    ZException(String s) {
        super(s);
    }
}
