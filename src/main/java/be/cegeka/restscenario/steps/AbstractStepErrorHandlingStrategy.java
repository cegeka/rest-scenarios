package be.cegeka.restscenario.steps;


import com.sun.jersey.api.client.ClientResponse;

public abstract class AbstractStepErrorHandlingStrategy {

	public abstract void handleError(ClientResponse response);

	protected boolean isErrorCode(int status) {
		return status > 300;
	}
}
