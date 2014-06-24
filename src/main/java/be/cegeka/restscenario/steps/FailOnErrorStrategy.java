package be.cegeka.restscenario.steps;

import org.assertj.core.api.Assertions;

import com.sun.jersey.api.client.ClientResponse;

public class FailOnErrorStrategy extends AbstractStepErrorHandlingStrategy {

	@Override
	public void handleError(ClientResponse response) {
		if (isErrorCode(response.getStatus())) {
			String message = response.getEntity(String.class);
			Assertions.fail(String.format("Erronous return status: %s. Message: %s", response.getStatus(), message));
		}
	}

}
