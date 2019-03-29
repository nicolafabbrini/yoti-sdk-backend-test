package com.yoti.nicola.fabbrini.recruitment.utils;

import com.yoti.nicola.fabbrini.recruitment.domain.Coordinate;
import com.yoti.nicola.fabbrini.recruitment.domain.Room;
import com.yoti.nicola.fabbrini.recruitment.domain.Roomba;
import com.yoti.nicola.fabbrini.recruitment.enumerator.Error;
import com.yoti.nicola.fabbrini.recruitment.exception.RoombaArgumentException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoomParser {

    private static final String ROOM_SIZE = "roomSize";
    private static final String START_POSITION = "coords";
    private static final String INSTRUCTIONS = "instructions";
    private static final String PATCHES = "patches";

    private static final int X_INDEX = 0;
    private static final int Y_INDEX = 1;

    private static final int MINIMUM_ROOM_AXIS_SIZE = 1;
    private static final int LOWER_AXIAL_POSITION = 0;

    private static final int EXPECTED_NUMBER_OF_AXIS = 2;

    private RoomParser() {}

    /**
     * Converts the JSON request to a fully instantiated Room object.
     * Validation of every value is performed here.
     *
     * @param jsonObject The JSONObject from the user's request.
     * @return The fully instantiated Room object.
     * @throws RoombaArgumentException Thrown if validation of the request fails.
     * @throws JSONException Thrown if the json object contains a field with the invalid type e.g. instructions must be a string
     */
    public static Room getModelFromJSON(final JSONObject jsonObject) throws RoombaArgumentException, JSONException {
        if(jsonObject == null)
            throw new NullPointerException();

        // High level validation of the json object
        if(!jsonObject.has(START_POSITION) ||
                !jsonObject.has(INSTRUCTIONS) ||
                !jsonObject.has(PATCHES) ||
                !jsonObject.has(ROOM_SIZE))
            throw new RoombaArgumentException(Error.FORMAT_NOT_VALID);

        final JSONArray roomSizeArray = jsonObject.getJSONArray(ROOM_SIZE);
        if(roomSizeArray.length() != EXPECTED_NUMBER_OF_AXIS)
            throw new RoombaArgumentException(Error.FORMAT_NOT_VALID);

        final int width = roomSizeArray.getInt(X_INDEX);
        final int height = roomSizeArray.getInt(Y_INDEX);

        // Validating the size of the room
        if((width < MINIMUM_ROOM_AXIS_SIZE) || (height < MINIMUM_ROOM_AXIS_SIZE))
            throw new RoombaArgumentException(Error.INVALID_ROOM_DIMENSION);

        // Building the Roomba object
        final JSONArray startPositionJson = jsonObject.getJSONArray(START_POSITION);
        if(startPositionJson.length() != EXPECTED_NUMBER_OF_AXIS)
            throw new RoombaArgumentException(Error.FORMAT_NOT_VALID);

        final Coordinate startPosition = new Coordinate(startPositionJson.getInt(X_INDEX),
                startPositionJson.getInt(Y_INDEX),
                false);

        // Validating the initial position of the roomba
        if(((startPosition.getX() < LOWER_AXIAL_POSITION) || (startPosition.getX() >= width)) ||
                ((startPosition.getY() < LOWER_AXIAL_POSITION) || (startPosition.getY() >= height)))
            throw new RoombaArgumentException(Error.INVALID_START_POSITION);

        final String instructions = jsonObject.getString(INSTRUCTIONS);
        validateInstructions(instructions);

        final Roomba roomba = new Roomba(startPosition, instructions);

        // Building the patches
        final List<Coordinate> patches = new ArrayList<>();
        final JSONArray patchesArray = jsonObject.getJSONArray(PATCHES);
        for(int i = 0; i < patchesArray.length(); i++) {
            final JSONArray patchCoordinateArray = patchesArray.getJSONArray(i);
            if(patchCoordinateArray.length() != EXPECTED_NUMBER_OF_AXIS)
                throw new RoombaArgumentException(Error.FORMAT_NOT_VALID);

            final Coordinate patchCoordinate = new Coordinate(patchCoordinateArray.getInt(X_INDEX),
                    patchCoordinateArray.getInt(Y_INDEX),
                    true);

            // Validating the position of the patch
            if(((patchCoordinate.getX() < LOWER_AXIAL_POSITION) || (patchCoordinate.getX() >= width)) ||
                    ((patchCoordinate.getY() < LOWER_AXIAL_POSITION) || (patchCoordinate.getY() >= height)))
                throw new RoombaArgumentException(Error.INVALID_PATCH_POSITION);

            patches.add(patchCoordinate);
        }

        return new Room(width, height, patches, roomba);
    }

    /**
     * Converts a @{@link Room} object to its json representation.
     * @param room Room object to convert into json.
     * @return The json representation of the given room instance.
     */
    public static JSONObject getJSONObjectFromModel(final Room room) {
        if(room == null)
            throw new NullPointerException();

        final JSONObject jsonRoom = new JSONObject();

        final JSONArray roomSizeArray = new JSONArray();
        roomSizeArray.put(room.getWidth());
        roomSizeArray.put(room.getHeight());

        final JSONArray roombaStartPositionArray = new JSONArray();
        roombaStartPositionArray.put(room.getRoomba().getStartPosition().getX());
        roombaStartPositionArray.put(room.getRoomba().getStartPosition().getY());

        final JSONArray patchesArray = new JSONArray();
        for(final Coordinate c : room.getPatches()) {
            final JSONArray patchArray = new JSONArray();
            patchArray.put(c.getX());
            patchArray.put(c.getY());

            patchesArray.put(patchArray);
        }

        jsonRoom.put(ROOM_SIZE, roomSizeArray);
        jsonRoom.put(START_POSITION, roombaStartPositionArray);
        jsonRoom.put(INSTRUCTIONS, room.getRoomba().getInstructions());
        jsonRoom.put(PATCHES, patchesArray);

        return jsonRoom;
    }

    private static void validateInstructions(final String instructions) throws RoombaArgumentException {
        String processedInstructions = instructions.toUpperCase();

        processedInstructions = processedInstructions.replace(Roomba.NORTH, "");
        processedInstructions = processedInstructions.replace(Roomba.SOUTH, "");
        processedInstructions = processedInstructions.replace(Roomba.WEST, "");
        processedInstructions = processedInstructions.replace(Roomba.EAST, "");

        // If the instruction string only contains valid instructions, the final process string is going to be empty
        if(!processedInstructions.isEmpty())
            throw new RoombaArgumentException(Error.INVALID_INSTRUCTION);
    }
}
