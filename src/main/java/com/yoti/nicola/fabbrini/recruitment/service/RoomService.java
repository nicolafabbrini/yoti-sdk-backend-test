package com.yoti.nicola.fabbrini.recruitment.service;

import com.yoti.nicola.fabbrini.recruitment.domain.Coordinate;
import com.yoti.nicola.fabbrini.recruitment.domain.Room;
import com.yoti.nicola.fabbrini.recruitment.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;

    private final RoombaService roombaService;
    private final CoordinateService coordinateService;

    @Autowired
    public RoomService(final RoomRepository roomRepository,
                       final RoombaService roombaService,
                       final CoordinateService coordinateService) {

        this.roomRepository = roomRepository;
        this.roombaService = roombaService;
        this.coordinateService = coordinateService;
    }

    public Room save(final Room room) {
        if(room == null)
            throw new NullPointerException();
        if(room.getRoomba() == null)
            throw new NullPointerException();
        if(room.getPatches() == null)
            throw new NullPointerException();

        // Makes sure to reuse as many coordinates as possible from the db.s
        final List<Coordinate> cachedPatches = new ArrayList<>();
        for(final Coordinate c : room.getPatches()) {
            cachedPatches.add(coordinateService.getCoordinate(c.getX(), c.getY(), true));
        }

        room.setPatches(cachedPatches);

        return roomRepository.save(room);
    }
}
