package be.cegeka.restscenario.steps;

import static com.google.common.collect.FluentIterable.*;
import static com.google.common.collect.Lists.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public abstract class AbstractStep<SELF extends AbstractStep<SELF>> {

	private WebResource baseResource;
	private String version = "v1";
	private MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
	private AbstractStepErrorHandlingStrategy errorHandlingStrategy = new FailOnErrorStrategy();

	public AbstractStep(WebResource baseResource) {
		this.baseResource = baseResource;
	}

	protected abstract Object getEntity();

	protected abstract WebResource path(WebResource baseResource);

	/**
	 * Will throw exceptions instead of failing on error statuses so you can catch thrown exceptions with e.g. an ExpectedException @Rule
	 * Generally used when you want to test that your REST API throws exceptions in some cases.
	 */
	@SuppressWarnings("unchecked")
	public SELF withExceptionThrowingStrategy() {
		this.errorHandlingStrategy = new ExceptionOnErrorStrategy();
		return (SELF) this;
	}

	/**
	 * Adds a QueryParam to your path
	 * 
	 * @param queryParam
	 * @param paramValues
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SELF queryParam(String queryParam, String... paramValues) {
		queryParams.put(queryParam, Lists.newArrayList(paramValues));
		return (SELF) this;
	}

	/**
	 * Helper method typically used after POSTing.<br/>
	 * According to the REST spec, the response of a POST should contain the location of the created resource.
	 * 
	 * @param response
	 * @return
	 */
	public final Optional<String> getLastPartFromPath(ClientResponse response) {
		return from(newArrayList(response.getLocation().getPath().split("/"))).last();
	}

	/**
	 * Perform a GET on this configured Resource.<br/>
	 * By default adds application/json as Content-Type
	 * Errors will result in test failures, or thrown exceptions see {@link AbstractStep.withExceptionThrowingStrategy()}
	 * 
	 * @return
	 */
	public ClientResponse get() {
		WebResource resource = path(getBaseResource());
		if (!queryParams.isEmpty()) {
			resource = resource.queryParams(queryParams);
		}
		return handleErrorStatus(asMediaTypeJSON(resource).get(ClientResponse.class));
	}

	/**
	 * Short-hand method that will straight up convert the {@link ClientResponse} to the Entity you expect
	 * 
	 * @param c
	 * @return
	 */
	public <T> T get(Class<T> c) {
		return get().getEntity(c);
	}

	/**
	 * Perform a POST on this configured Resource.<br/>
	 * By default adds application/json as Content-Type
	 * Errors will result in test failures, or thrown exceptions see {@link AbstractStep.withExceptionThrowingStrategy()}
	 * 
	 * @return
	 */
	public ClientResponse post() {
		WebResource resource = path(getBaseResource());
		return handleErrorStatus(asMediaTypeJSON(resource).post(ClientResponse.class, getEntity()));
	}

	/**
	 * Perform a PUT on this configured Resource.<br/>
	 * By default adds application/json as Content-Type
	 * Errors will result in test failures, or thrown exceptions see {@link AbstractStep.withExceptionThrowingStrategy()}
	 * 
	 * @param businesskey required resource location
	 * @return
	 */
	public ClientResponse put(String businesskey) {
		WebResource resource = path(getBaseResource()).path(businesskey);
		return handleErrorStatus(asMediaTypeJSON(resource).put(ClientResponse.class, getEntity()));
	}

	/**
	 * Perform a DELETE on this configured Resource.<br/>
	 * By default adds application/json as Content-Type
	 * Errors will result in test failures, or thrown exceptions see {@link AbstractStep.withExceptionThrowingStrategy()}
	 * 
	 * @param businesskey required resource location
	 * @return
	 */
	public ClientResponse delete(String businesskey) {
		WebResource resource = path(getBaseResource()).path(businesskey);
		return handleErrorStatus(asMediaTypeJSON(resource).delete(ClientResponse.class));
	}

	private Builder asMediaTypeJSON(WebResource resource) {
		return resource.type(MediaType.APPLICATION_JSON);
	}

	private ClientResponse handleErrorStatus(ClientResponse response) {
		response.bufferEntity();
		errorHandlingStrategy.handleError(response);
		return response;
	}

	private final WebResource getBaseResource() {
		return baseResource.path(version);
	}
}
