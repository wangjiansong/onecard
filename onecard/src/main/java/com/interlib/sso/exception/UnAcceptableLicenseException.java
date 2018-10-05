package com.interlib.sso.exception;

import org.apache.shiro.authc.AuthenticationException;

/** 
 * @author home
 */
public class UnAcceptableLicenseException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 484069115476669391L;

	public UnAcceptableLicenseException() {
		
	}
	
	public UnAcceptableLicenseException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnAcceptableLicenseException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public UnAcceptableLicenseException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new UnAcceptableLicenseException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public UnAcceptableLicenseException(String message, Throwable cause) {
        super(message, cause);
    }

	
}
