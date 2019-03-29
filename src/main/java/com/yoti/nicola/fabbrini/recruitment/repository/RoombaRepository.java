package com.yoti.nicola.fabbrini.recruitment.repository;

import com.yoti.nicola.fabbrini.recruitment.domain.Roomba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoombaRepository extends JpaRepository<Roomba, Long> {

}
