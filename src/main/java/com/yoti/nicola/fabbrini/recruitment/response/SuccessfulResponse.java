package com.yoti.nicola.fabbrini.recruitment.response;

import com.yoti.nicola.fabbrini.recruitment.domain.Roomba;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The format of a successful response will look like
 *
 * {
 *   "coords" : [1, 3],
 *   "patches" : 1
 * }
 */
public class SuccessfulResponse {

    private static final String FINAL_ROOMBA_POSITION = "coords";
    private static final String NUMBER_OF_PATCHES_CLEANED = "patches";

    final private Roomba roomba;

    public SuccessfulResponse(final Roomba roomba) {
        if(roomba == null)
            throw new NullPointerException();

        this.roomba = roomba;
    }

    @Override
    public String toString() {
        final JSONObject jsonResponse = new JSONObject();

        final JSONArray finalPositionArray = new JSONArray();
        finalPositionArray.put(roomba.getEndPosition().getX());
        finalPositionArray.put(roomba.getEndPosition().getY());

        jsonResponse.put(FINAL_ROOMBA_POSITION, finalPositionArray);
        jsonResponse.put(NUMBER_OF_PATCHES_CLEANED, roomba.getCleanedPatchesCount());

        return jsonResponse.toString();
    }
}
