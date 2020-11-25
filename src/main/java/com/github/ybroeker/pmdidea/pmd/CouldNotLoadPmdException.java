package com.github.ybroeker.pmdidea.pmd;


public class CouldNotLoadPmdException extends RuntimeException {

    public CouldNotLoadPmdException(final String message) {
        super(message);
    }

    public CouldNotLoadPmdException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CouldNotLoadPmdException(final Throwable cause) {
        super(cause);
    }

}
