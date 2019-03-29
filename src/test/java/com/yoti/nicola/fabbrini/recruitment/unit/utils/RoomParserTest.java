package com.yoti.nicola.fabbrini.recruitment.unit.utils;

import com.yoti.nicola.fabbrini.recruitment.domain.Coordinate;
import com.yoti.nicola.fabbrini.recruitment.domain.Room;
import com.yoti.nicola.fabbrini.recruitment.domain.Roomba;
import com.yoti.nicola.fabbrini.recruitment.enumerator.Error;
import com.yoti.nicola.fabbrini.recruitment.exception.RoombaArgumentException;
import com.yoti.nicola.fabbrini.recruitment.utils.RoomParser;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RoomParserTest {

    @Test(expected = NullPointerException.class)
    public void testGetModelFromJSONWithNullJSON() throws Exception {
        RoomParser.getModelFromJSON(null);
    }

    @Test(expected = JSONException.class)
    public void testGetModelFromJSONWithInvalidTypeRoomSize() throws Exception {
        final String sampleJSON = "{\"roomSize\": \"4, 5\",\"coords\": [3, 3],\"instructions\": \"NSWE\",\"patches\": [[0, 1],[1, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);
        RoomParser.getModelFromJSON(jsonObject);
    }

    @Test(expected = JSONException.class)
    public void testGetModelFromJSONWithInvalidTypeStartPosition() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": \"3, 3\",\"instructions\": \"NSWE\",\"patches\": [[0, 1],[1, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);
        RoomParser.getModelFromJSON(jsonObject);
    }

    @Test(expected = JSONException.class)
    public void testGetModelFromJSONWithInvalidTypeInstructions() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": [3, 3],\"instructions\": 1,\"patches\": [[0, 1],[1, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);
        RoomParser.getModelFromJSON(jsonObject);
    }

    @Test(expected = JSONException.class)
    public void testGetModelFromJSONWithInvalidTypePatches() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": [3, 3],\"instructions\": \"NSWE\",\"patches\": 1";

        final JSONObject jsonObject = new JSONObject(sampleJSON);
        RoomParser.getModelFromJSON(jsonObject);
    }

    @Test(expected = JSONException.class)
    public void testGetModelFromJSONWithInvalidTypePatch() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": [3, 3],\"instructions\": \"NSWE\",\"patches\": [1,2,3]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);
        RoomParser.getModelFromJSON(jsonObject);
    }

    @Test
    public void testGetModelFromJSONWithInvalidRoomSizeWidth() throws Exception {
        final String sampleJSON = "{\"roomSize\": [0, 5],\"coords\": [3, 3],\"instructions\": \"NSWE\",\"patches\": [[0, 1],[1, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);

        try {
            RoomParser.getModelFromJSON(jsonObject);
            Assert.fail();
        } catch(final RoombaArgumentException e) {
            Assert.assertEquals(Error.INVALID_ROOM_DIMENSION, e.getError());
        }
    }

    @Test
    public void testGetModelFromJSONWithInvalidRoomSizeHeight() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 0],\"coords\": [3, 3],\"instructions\": \"NSWE\",\"patches\": [[0, 1],[1, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);

        try {
            RoomParser.getModelFromJSON(jsonObject);
            Assert.fail();
        } catch(final RoombaArgumentException e) {
            Assert.assertEquals(Error.INVALID_ROOM_DIMENSION, e.getError());
        }
    }

    @Test
    public void testGetModelFromJSONWithInvalidStartPositionX() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": [4, 3],\"instructions\": \"NSWE\",\"patches\": [[0, 1],[1, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);

        try {
            RoomParser.getModelFromJSON(jsonObject);
            Assert.fail();
        } catch(final RoombaArgumentException e) {
            Assert.assertEquals(Error.INVALID_START_POSITION, e.getError());
        }
    }

    @Test
    public void testGetModelFromJSONWithInvalidStartPositionY() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": [2, 5],\"instructions\": \"NSWE\",\"patches\": [[0, 1],[1, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);

        try {
            RoomParser.getModelFromJSON(jsonObject);
            Assert.fail();
        } catch(final RoombaArgumentException e) {
            Assert.assertEquals(Error.INVALID_START_POSITION, e.getError());
        }
    }

    @Test
    public void testGetModelFromJSONWithInvalidInstructionSet() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": [2, 2],\"instructions\": \"NSOE\",\"patches\": [[0, 1],[1, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);

        try {
            RoomParser.getModelFromJSON(jsonObject);
            Assert.fail();
        } catch(final RoombaArgumentException e) {
            Assert.assertEquals(Error.INVALID_INSTRUCTION, e.getError());
        }
    }

    @Test
    public void testGetModelFromJSONWithInvalidPatchPositionX() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": [2, 2],\"instructions\": \"NSWE\",\"patches\": [[0, 1],[4, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);

        try {
            RoomParser.getModelFromJSON(jsonObject);
            Assert.fail();
        } catch(final RoombaArgumentException e) {
            Assert.assertEquals(Error.INVALID_PATCH_POSITION, e.getError());
        }
    }

    @Test
    public void testGetModelFromJSONWithInvalidPatchPositionY() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": [2, 2],\"instructions\": \"NSWE\",\"patches\": [[0, 1],[1, 5],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);

        try {
            RoomParser.getModelFromJSON(jsonObject);
            Assert.fail();
        } catch(final RoombaArgumentException e) {
            Assert.assertEquals(Error.INVALID_PATCH_POSITION, e.getError());
        }
    }

    @Test
    public void testGetModelFromJSON() throws Exception {
        final String sampleJSON = "{\"roomSize\": [20, 25],\"coords\": [12, 11],\"instructions\": \"NSWENNWSSS\",\"patches\": [[0, 1],[2, 3],[4, 5]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);
        final Room room = RoomParser.getModelFromJSON(jsonObject);

        Assert.assertEquals(20, room.getWidth());
        Assert.assertEquals(25, room.getHeight());
        Assert.assertEquals(12, room.getRoomba().getStartPosition().getX());
        Assert.assertEquals(11, room.getRoomba().getStartPosition().getY());
        Assert.assertFalse(room.getRoomba().getStartPosition().isPatch());
        Assert.assertEquals("NSWENNWSSS", room.getRoomba().getInstructions());
        Assert.assertEquals(0, room.getPatches().get(0).getX());
        Assert.assertEquals(1, room.getPatches().get(0).getY());
        Assert.assertTrue(room.getPatches().get(0).isPatch());
        Assert.assertEquals(2, room.getPatches().get(1).getX());
        Assert.assertEquals(3, room.getPatches().get(1).getY());
        Assert.assertTrue(room.getPatches().get(1).isPatch());
        Assert.assertEquals(4, room.getPatches().get(2).getX());
        Assert.assertEquals(5, room.getPatches().get(2).getY());
        Assert.assertTrue(room.getPatches().get(2).isPatch());
    }

    @Test(expected = NullPointerException.class)
    public void testGetJSONObjectFromModelWithRoomNull() {
        RoomParser.getJSONObjectFromModel(null);
    }

    @Test
    public void testGetJSONObjectFromModel() {
        final Coordinate startPosition = new Coordinate(1,2);
        final String instructions = "NSWENNSS";
        final Roomba roomba = new Roomba(startPosition, instructions);

        final List<Coordinate> patches = new ArrayList<>();
        patches.add(new Coordinate(2,3, true));
        patches.add(new Coordinate(4,5, true));
        patches.add(new Coordinate(6,7, true));

        final Room room = new Room(18,20, patches, roomba);
        final JSONObject jsonObject = RoomParser.getJSONObjectFromModel(room);

        Assert.assertEquals("{\"instructions\":\"NSWENNSS\",\"roomSize\":[18,20],\"patches\":[[2,3],[4,5],[6,7]],\"coords\":[1,2]}", jsonObject.toString());
    }
}
