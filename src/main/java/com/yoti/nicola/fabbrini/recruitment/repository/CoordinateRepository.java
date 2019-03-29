package com.yoti.nicola.fabbrini.recruitment.repository;

import com.yoti.nicola.fabbrini.recruitment.domain.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {

    Coordinate findByXAndYAndIsPatch(final int x, final int y, final boolean isPatch);
}
