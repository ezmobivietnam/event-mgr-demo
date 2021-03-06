package vn.binhduong.eventmgrdemo.services;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import vn.binhduong.eventmgrdemo.domain.EventsEntity;
import vn.binhduong.eventmgrdemo.repositories.EventsEntityRepository;
import vn.binhduong.eventmgrdemo.utils.EventsEntityAssembler;
import vn.binhduong.eventmgrdemo.web.controllers.EventsEntityController;
import vn.binhduong.eventmgrdemo.web.model.EventsDTO;

import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EventsEntityService {

    private final EventsEntityRepository eventsEntityRepository;
    private final EventsEntityAssembler eventsEntityAssembler;
    private final PagedResourcesAssembler pagedResourcesAssembler;

    public EventsEntityService(EventsEntityRepository eventsEntityRepository,
                               EventsEntityAssembler eventsEntityAssembler,
                               PagedResourcesAssembler pagedResourcesAssembler) {
        this.eventsEntityRepository = eventsEntityRepository;
        this.eventsEntityAssembler = eventsEntityAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    /**
     * Save the event
     *
     * @param eventsDTO
     * @return
     */
    public Long save(EventsDTO eventsDTO) {
        EventsEntity bean = new EventsEntity();
        BeanUtils.copyProperties(eventsDTO, bean);
        bean = eventsEntityRepository.save(bean);
        return bean.getId();
    }

    /**
     * Delete an event
     *
     * @param id
     */
    public void delete(Long id) {
        eventsEntityRepository.deleteById(id);
    }

    /**
     * Update an existing event.
     *
     * @param id
     * @param eventsDTO
     * @throws NoSuchElementException if event is not exist.
     */
    public void update(Long id, EventsDTO eventsDTO) {
        EventsEntity bean = requireOne(id);
        eventsDTO.setId(id);
        BeanUtils.copyProperties(eventsDTO, bean);
        eventsEntityRepository.save(bean);
    }

    /**
     * Get an event by id
     *
     * @param id
     * @return
     */
    public EventsDTO getById(Long id) {
        EventsEntity original = requireOne(id);
        return toDTO(original);
    }

    /**
     * Find all events
     *
     * @return
     */
    public CollectionModel<EventsDTO> findAll() {
        List<EventsEntity> entities = eventsEntityRepository.findAll();
        CollectionModel<EventsDTO> collectionModel = eventsEntityAssembler.toCollectionModel(entities);
        collectionModel.add(linkTo(methodOn(EventsEntityController.class).query(null, null))
                .withSelfRel().expand());
        return collectionModel;
    }

    /**
     * Find events at a specific page.
     *
     * @param pageRequest
     * @return
     */
    public CollectionModel<EventsDTO> findAllPaginated(PageRequest pageRequest) {
        Assert.notNull(pageRequest, "PageRequest must not be null");
        Page<EventsEntity> page = eventsEntityRepository.findAll(pageRequest);
        return pagedResourcesAssembler.toModel(page, eventsEntityAssembler);
    }

    /**
     * Convert an entity to DTO object.
     *
     * @param original
     * @return
     */
    private EventsDTO toDTO(EventsEntity original) {
        EventsDTO bean = new EventsDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private EventsEntity requireOne(Long id) {
        return eventsEntityRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
