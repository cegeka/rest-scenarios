package be.cegeka.restscenario.steps;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.sun.jersey.api.client.ClientResponse;

public class ExceptionOnErrorStrategy extends AbstractStepErrorHandlingStrategy {

	@Override
	public void handleError(ClientResponse response) {
		if (isErrorCode(response.getStatus())) {
			String message = response.getEntity(String.class);
			throw new StepErrorException(response.getStatus(), message);
		}
	}

	public static StepErrorMatcher matchesStatus(int statuscode) {
		return new StepErrorMatcher(statuscode);
	}

	public static class StepErrorMatcher extends TypeSafeMatcher<StepErrorException> {

		private int statuscode;

		public StepErrorMatcher(int statuscode) {
			this.statuscode = statuscode;
		}

		public void describeTo(Description description) {
			description.appendText("expected statuscode: " + this.statuscode);
		}

		@Override
		protected boolean matchesSafely(StepErrorException item) {
			return statuscode == item.getStatuscode();
		}

	}
}
