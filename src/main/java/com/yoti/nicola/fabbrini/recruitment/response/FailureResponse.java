package com.yoti.nicola.fabbrini.recruitment.response;

import com.yoti.nicola.fabbrini.recruitment.enumerator.Error;
import org.json.JSONObject;

/**
 * Format of an error response is
 *
 * {
 *  "error_codename": "codename_of_the_error",
 *  "error_message": "A user friendly explanation of the error code"
 * }
 */
public class FailureResponse {

    private static final String ERROR_CODENAME = "error_codename";
    private static final String ERROR_MESSAGE = "error_message";

    private final Error error;

    public FailureResponse(final Error error) {
        if(error == null)
            throw new NullPointerException();

        this.error = error;
    }

    @Override
    public String toString() {
        final JSONObject jsonResponse = new JSONObject();

        jsonResponse.put(ERROR_CODENAME, error.toString());
        jsonResponse.put(ERROR_MESSAGE, error.getMessage());

        return jsonResponse.toString();
    }
}
