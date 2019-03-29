package com.yoti.nicola.fabbrini.recruitment.enumerator;

public enum Error {

    INVALID_ROOM_DIMENSION("Both width (X) and height (Y) of the room must be greater or equals to 1"),
    INVALID_START_POSITION("The initial position of the roomba is outside the room perimeter. 0 <= X < width and 0 <= Y < height."),
    INVALID_PATCH_POSITION("The position of one or more patches is outside the room perimeter. 0 <= X < width and 0 <= Y < height."),
    INVALID_INSTRUCTION("The concatenated instruction string can only contain the following characters: N,S,W,E."),
    FORMAT_NOT_VALID("The format of the request is not valid. Please read the documentation for an example.");

    private String message;

    Error(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
