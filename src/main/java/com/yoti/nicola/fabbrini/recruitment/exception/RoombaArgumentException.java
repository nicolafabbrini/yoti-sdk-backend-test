package com.yoti.nicola.fabbrini.recruitment.exception;

import com.yoti.nicola.fabbrini.recruitment.enumerator.Error;

public class RoombaArgumentException extends Exception {

    final Error error;

    public RoombaArgumentException(final Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }

    @Override
    public String getMessage() {
        return error.getMessage();
    }
}
