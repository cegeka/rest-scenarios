package be.cegeka.restscenario.steps;


import com.google.common.base.Objects;

public class StepErrorException extends RuntimeException {
	private static final long serialVersionUID = -1348608787487247581L;

	private int statuscode;
	private String errorMessage;

	public StepErrorException(int code, String message) {
		super(String.format("Erronous return status: %s. Message: %s", code, message));
		this.statuscode = code;
		this.errorMessage = message;
	}

	public int getStatuscode() {
		return statuscode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(statuscode, errorMessage);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StepErrorException other = (StepErrorException) obj;
		return Objects.equal(this.statuscode, other.statuscode)
				&& Objects.equal(this.errorMessage, other.errorMessage);
	}
}
