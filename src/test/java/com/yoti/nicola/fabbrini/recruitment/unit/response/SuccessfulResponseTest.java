package com.yoti.nicola.fabbrini.recruitment.unit.response;

import com.yoti.nicola.fabbrini.recruitment.domain.Coordinate;
import com.yoti.nicola.fabbrini.recruitment.domain.Roomba;
import com.yoti.nicola.fabbrini.recruitment.response.SuccessfulResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class SuccessfulResponseTest {

    @Test(expected = NullPointerException.class)
    public void testWithNullError() {
        new SuccessfulResponse(null);
    }

    @Test
    public void testNumberOfPatchesCleaned() {
        final Coordinate startPosition = new Coordinate(1,1);
        final Coordinate endPosition = new Coordinate(3,3);
        final String instructions = "NSWE";
        final Roomba roomba = new Roomba(startPosition, instructions);
        roomba.setCleanedPatchesCount(1);
        roomba.setEndPosition(endPosition);

        final SuccessfulResponse response = new SuccessfulResponse(roomba);
        final JSONObject jsonResponse = new JSONObject(response.toString());

        Assert.assertEquals(1, jsonResponse.getInt("patches"));
    }

    @Test
    public void testFinalPosition() {
        final Coordinate startPosition = new Coordinate(1,1);
        final Coordinate endPosition = new Coordinate(3,5);
        final String instructions = "NSWE";
        final Roomba roomba = new Roomba(startPosition, instructions);
        roomba.setCleanedPatchesCount(1);
        roomba.setEndPosition(endPosition);

        final SuccessfulResponse response = new SuccessfulResponse(roomba);
        final JSONObject jsonResponse = new JSONObject(response.toString());
        final JSONArray finalPositionArray = jsonResponse.getJSONArray("coords");

        Assert.assertEquals(3, finalPositionArray.getInt(0));
        Assert.assertEquals(5, finalPositionArray.getInt(1));
    }
}
