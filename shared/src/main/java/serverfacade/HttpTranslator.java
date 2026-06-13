// Giant copy paste from petshop.

package serverfacade;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpRequest.BodyPublishers;
import static java.net.http.HttpResponse.BodyHandlers;

public class HttpTranslator {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private final Gson gson = new Gson();

    public HttpTranslator(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public <T> T get(
            String path,
            String authToken,
            Class<T> responseClass) throws Exception {

        var request = buildRequest(
                "GET",
                path,
                null,
                authToken);

        var response = sendRequest(request);

        return handleResponse(
                response,
                responseClass);
    }

    public <T> T post(
            String path,
            Object body,
            String authToken,
            Class<T> responseClass) throws Exception {

        var request = buildRequest(
                "POST",
                path,
                body,
                authToken);

        var response = sendRequest(request);

        return handleResponse(
                response,
                responseClass);
    }

    public <T> T put(
            String path,
            Object body,
            String authToken,
            Class<T> responseClass) throws Exception {

        var request = buildRequest(
                "PUT",
                path,
                body,
                authToken);

        var response = sendRequest(request);

        return handleResponse(
                response,
                responseClass);
    }

    public <T> T delete(
            String path,
            Object body,
            String authToken,
            Class<T> responseClass) throws Exception {

        var request = buildRequest(
                "DELETE",
                path,
                body,
                authToken);

        var response = sendRequest(request);

        return handleResponse(
                response,
                responseClass);
    }

    private HttpRequest buildRequest(
            String method,
            String path,
            Object body,
            String authToken) {

        var builder = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(
                        method,
                        makeRequestBody(body));

        if (body != null) {
            builder.setHeader(
                    "Content-Type",
                    "application/json");
        }

        if (authToken != null) {
            builder.setHeader(
                    "Authorization",
                    authToken);
        }

        return builder.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object body) {
        if (body == null) {
            return BodyPublishers.noBody();
        }

        return BodyPublishers.ofString(
                gson.toJson(body));
    }

    private HttpResponse<String> sendRequest(HttpRequest request)
            throws Exception {

        return client.send(
                request,
                BodyHandlers.ofString());
    }

    private <T> T handleResponse(
            HttpResponse<String> response,
            Class<T> responseClass) throws Exception {

        int status = response.statusCode();

        if (status / 100 != 2) {
            throw new Exception(
                    extractErrorMessage(response.body()));
        }

        if (responseClass == null) {
            return null;
        }

        return gson.fromJson(
                response.body(),
                responseClass);
    }

    private String extractErrorMessage(String body) {
        if (body == null || body.isBlank()) {
            return "Error: request failed";
        }

        try {
            ErrorDataHolder error =
                    gson.fromJson(
                            body,
                            ErrorDataHolder.class);

            if (error.message() != null) {
                return error.message();
            }
        } catch (Exception ignored) {
        }

        return "Error: request failed";
    }

    private record ErrorDataHolder(String message) {
    }
}