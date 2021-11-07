package vn.binhduong.eventmgrdemo.services;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import vn.binhduong.eventmgrdemo.domain.EventsEntity;
import vn.binhduong.eventmgrdemo.repositories.EventsEntityRepository;
import vn.binhduong.eventmgrdemo.web.model.EventsDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple service used ONLY for querying data and NOT apply Spring HateOAS.
 */
@Service
public class EventsEntitySimpleService {

    private final EventsEntityRepository eventsEntityRepository;

    public EventsEntitySimpleService(EventsEntityRepository eventsEntityRepository) {
        this.eventsEntityRepository = eventsEntityRepository;
    }

    /**
     * Query all events from DB
     *
     * @return
     */
    public List<EventsDTO> findAll() {
        List<EventsEntity> entities = eventsEntityRepository.findAll();
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Convert entities to DTOs
     *
     * @param original
     * @return
     */
    private EventsDTO toDTO(EventsEntity original) {
        EventsDTO bean = new EventsDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
}
