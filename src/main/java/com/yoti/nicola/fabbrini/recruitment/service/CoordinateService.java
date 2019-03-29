package com.yoti.nicola.fabbrini.recruitment.service;

import com.yoti.nicola.fabbrini.recruitment.domain.Coordinate;
import com.yoti.nicola.fabbrini.recruitment.repository.CoordinateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CoordinateService {

    private final CoordinateRepository coordinateRepository;

    @Autowired
    public CoordinateService(final CoordinateRepository coordinateRepository) {
        this.coordinateRepository = coordinateRepository;
    }

    /** Gets a fresh coordinate instance from the database if it's already present. This prevents the database from
     * growing indefinitely.
     * For example if you run the problem an infinite amount of times with a room dimension of 100*100, the coordinate
     * table will never have more than 100*100*2 elements.
     *
     * If a coordinate exists, it returns the instance from the database, if nothing is found then return a new instance
     */
    public Coordinate getCoordinate(final int x, final int y, final boolean isPatch) {
        final Coordinate coordinate = coordinateRepository.findByXAndYAndIsPatch(x, y, isPatch);
        if(coordinate != null)
            return coordinate;

        return createCoordinate(x, y ,isPatch);
    }

    private Coordinate createCoordinate(final int x, final int y, final boolean isPatch) {
        final Coordinate coordinate = new Coordinate(x, y, isPatch);

        return coordinateRepository.save(coordinate);
    }
}
