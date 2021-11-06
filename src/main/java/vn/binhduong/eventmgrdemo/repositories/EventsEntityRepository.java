package vn.binhduong.eventmgrdemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.binhduong.eventmgrdemo.domain.EventsEntity;

public interface EventsEntityRepository extends JpaRepository<EventsEntity, Long>, JpaSpecificationExecutor<EventsEntity> {

}