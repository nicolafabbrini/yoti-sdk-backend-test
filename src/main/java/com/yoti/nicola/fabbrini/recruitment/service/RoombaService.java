package com.yoti.nicola.fabbrini.recruitment.service;

import com.yoti.nicola.fabbrini.recruitment.domain.Coordinate;
import com.yoti.nicola.fabbrini.recruitment.domain.Roomba;
import com.yoti.nicola.fabbrini.recruitment.repository.RoombaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoombaService {

    private final RoombaRepository roombaRepository;
    private final CoordinateService coordinateService;

    @Autowired
    public RoombaService(final RoombaRepository roombaRepository, final CoordinateService coordinateService) {
        this.roombaRepository = roombaRepository;
        this.coordinateService = coordinateService;
    }

    public Roomba save(final Roomba roomba) {
        if(roomba == null)
            throw new NullPointerException();
        if(roomba.getEndPosition() == null)
            throw new NullPointerException();

        /* CoordinateService is used to reuse all existing coordinates. This prevents the database to grow indefinitely.
         * For example if you run the problem an infinite amount of times with a room dimension of 100*100, the coordinate
         * table will never have more than 100*100*2 elements.
         */

        final Coordinate startPosition = roomba.getStartPosition();
        final Coordinate endPosition = roomba.getEndPosition();
        roomba.setStartPosition(coordinateService.getCoordinate(startPosition.getX(), startPosition.getY(), false));
        roomba.setEndPosition(coordinateService.getCoordinate(endPosition.getX(), endPosition.getY(), false));

        return roombaRepository.save(roomba);
    }
}
