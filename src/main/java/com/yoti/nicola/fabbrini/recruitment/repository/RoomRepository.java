package com.yoti.nicola.fabbrini.recruitment.repository;

import com.yoti.nicola.fabbrini.recruitment.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

}
