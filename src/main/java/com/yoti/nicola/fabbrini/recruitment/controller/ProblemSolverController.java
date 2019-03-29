package com.yoti.nicola.fabbrini.recruitment.controller;

import com.yoti.nicola.fabbrini.recruitment.domain.Coordinate;
import com.yoti.nicola.fabbrini.recruitment.domain.Room;
import com.yoti.nicola.fabbrini.recruitment.domain.Roomba;
import com.yoti.nicola.fabbrini.recruitment.enumerator.Error;
import com.yoti.nicola.fabbrini.recruitment.exception.RoombaArgumentException;
import com.yoti.nicola.fabbrini.recruitment.response.FailureResponse;
import com.yoti.nicola.fabbrini.recruitment.response.SuccessfulResponse;
import com.yoti.nicola.fabbrini.recruitment.service.RoomService;
import com.yoti.nicola.fabbrini.recruitment.utils.RoomParser;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProblemSolverController {

    private static final int LOWER_BOUND = 0;

    private final RoomService roomService;

    @Autowired
    public ProblemSolverController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping(value = "/roomba", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> solver(@RequestBody String problemConstraints) {
        Room room = null;

        try {
            final JSONObject problemConstraintsJSON = new JSONObject(problemConstraints);
            room = RoomParser.getModelFromJSON(problemConstraintsJSON);
        } catch (final RoombaArgumentException e) {
            return new ResponseEntity<>(new FailureResponse(e.getError()).toString(), HttpStatus.BAD_REQUEST);
        } catch (final JSONException e2) {
            return new ResponseEntity<>(new FailureResponse(Error.FORMAT_NOT_VALID).toString(), HttpStatus.BAD_REQUEST);
        }

        /* At this point the room object is fully instantiated with all parameters validated.
         *  This means that we can run the algorithm that moves the roomba safely and persist
         *  the informations to the database afterwards.
         */

        final List<Coordinate> patches = room.getPatches();
        final Roomba roomba = room.getRoomba();

        // Check if the roomba has been positioned on top of a patch as its starting position
        if(patches.contains(roomba.getStartPosition())) {
            roomba.increasePatchCount();
            patches.remove(roomba.getStartPosition());
        }

        final Coordinate currentPosition = new Coordinate(roomba.getStartPosition());
        final String instructions = roomba.getInstructions().toUpperCase();
        // Cycle across all the characters of the instruction-set and perform the cleaning
        for(final char command : roomba.getInstructions().toCharArray()) {
            final String sCommand = String.valueOf(command);
            int x = currentPosition.getX();
            int y = currentPosition.getY();

            if(sCommand.equals(Roomba.NORTH)) {
                if(y < (room.getHeight() - 1))
                    y++;
            } else if(sCommand.equals(Roomba.SOUTH)) {
                if(y > LOWER_BOUND)
                    y--;
            } else if(sCommand.equals(Roomba.WEST)) {
                if(x > LOWER_BOUND)
                    x--;
            } else {
                if(x < (room.getWidth() - 1))
                    x++;
            }

            currentPosition.setX(x);
            currentPosition.setY(y);

            // Check if the new position is above a patch
            if(patches.contains(currentPosition)) {
                roomba.increasePatchCount();
                patches.remove(currentPosition);
            }
        }

        // Updating final position of the roomba and saving data in the db.
        roomba.setEndPosition(currentPosition);

        roomService.save(room);

        return ResponseEntity.ok(new SuccessfulResponse(room.getRoomba()).toString());
    }
}
