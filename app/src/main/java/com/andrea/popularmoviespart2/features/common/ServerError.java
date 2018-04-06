package com.andrea.popularmoviespart2.features.common;

public class ServerError {

    /**
        This is not a sustainable way to check for error conditions.
        This would be better if converted to Server Status Codes, or by building an interactor within Retrofit.
     */
    public static String getReasonForError(Throwable error) {
        if (error.getCause() == null) {
            return "defaultError";
        }

        if (error.getMessage() == null) {
            return "defaultError";
        }

        // check status
        switch (error.getMessage()) {
            case "HTTP 401 Unauthorized":
                return "unauthorizedError";
            case "timeout":
                return "timeoutError";
            case "Unable to resolve host \"api.themoviedb.org\": No address associated with hostname":
                return "noHostError";
            default:
                return "defaultError";
        }
    }
}
