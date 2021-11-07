package vn.binhduong.eventmgrdemo.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.binhduong.eventmgrdemo.services.EventsEntitySimpleService;
import vn.binhduong.eventmgrdemo.web.model.EventsDTO;

import java.util.List;

@Validated
@RestController
@RequestMapping(EventsEntitySimpleController.BASE_URL)
/**
 * Simple controller used ONLY for querying data and NOT apply Spring HateOAS.
 */
public class EventsEntitySimpleController {

    public static final String BASE_URL = "/api/simple/events";

    private final EventsEntitySimpleService simpleService;

    public EventsEntitySimpleController(EventsEntitySimpleService simpleService) {
        this.simpleService = simpleService;
    }

    @GetMapping
    public ResponseEntity<List<EventsDTO>> query() {

        List<EventsDTO> data = getService().findAll();
        return ResponseEntity.ok(data);
    }

    /**
     * Get the EventsEntityService injected by Spring framework.
     *
     * @return
     */
    private EventsEntitySimpleService getService() {
        return simpleService;
    }
}
