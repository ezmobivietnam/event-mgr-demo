package vn.binhduong.eventmgrdemo.web.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.binhduong.eventmgrdemo.services.EventsEntityService;
import vn.binhduong.eventmgrdemo.web.model.EventsDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Validated
@RestController
@RequestMapping(EventsEntityController.BASE_URL)
public class EventsEntityController {

    public static final String BASE_URL = "/api/events";
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 20;

    private final EventsEntityService eventsEntityService;

    public EventsEntityController(EventsEntityService eventsEntityService) {
        this.eventsEntityService = eventsEntityService;
    }

    @PostMapping
    public String save(@Valid @RequestBody EventsDTO eventsDTO) {
        return eventsEntityService.save(eventsDTO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        eventsEntityService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody EventsDTO eventsDTO) {
        eventsEntityService.update(id, eventsDTO);
    }

    @GetMapping("/{id}")
    public EventsDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return eventsEntityService.getById(id);
    }

//    @GetMapping
//    public Page<EventsDTO> query(@Valid EventsDTO eventsDTO) {
//        return eventsEntityService.query(eventsDTO);
//    }

    @GetMapping
    public ResponseEntity<CollectionModel<EventsDTO>> query(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {

        PageRequest pageRequest = createPageRequest(page, size);
        CollectionModel<EventsDTO> collectionModel = null;
        if (Objects.nonNull(pageRequest)) {
            // query data supporting pagination
            collectionModel = getService().findAllPaginated(pageRequest);
        } else {
            // query all data WITHOUT pagination
            collectionModel = getService().findAll();
        }
        return ResponseEntity.ok(collectionModel);
    }

    /**
     * Create a paging request object.
     *
     * @param page the requested page number, starting from page zero (the first page)
     * @param size the expected number of records per page.
     * @return
     */
    protected PageRequest createPageRequest(Integer page, Integer size) {
        if (Objects.nonNull(page) || Objects.nonNull(size)) {
            // Create the PageRequest obj if at least one of two parameters is non-null
            int actualPageNumber = Objects.isNull(page) ? DEFAULT_PAGE_NUMBER : page;
            int actualPageSize = Objects.isNull(size) ? DEFAULT_PAGE_SIZE : size;
            return PageRequest.of(actualPageNumber, actualPageSize);
        } else {
            return null;
        }
    }

    /**
     * Get the EventsEntityService injected by Spring framework.
     *
     * @return
     */
    private EventsEntityService getService() {
        return eventsEntityService;
    }
}
