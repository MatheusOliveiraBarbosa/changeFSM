package br.com.changefsm.exceptions;

public class ChangeFSMException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String message;
	
	public ChangeFSMException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
