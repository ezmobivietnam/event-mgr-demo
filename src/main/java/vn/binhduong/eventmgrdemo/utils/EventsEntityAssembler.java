package vn.binhduong.eventmgrdemo.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import vn.binhduong.eventmgrdemo.domain.EventsEntity;
import vn.binhduong.eventmgrdemo.web.controllers.EventsEntityController;
import vn.binhduong.eventmgrdemo.web.model.EventsDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Created by binhduong on 11/4/2021.
 */
@Component
public class EventsEntityAssembler implements RepresentationModelAssembler<EventsEntity, EventsDTO> {

    @Override
    public EventsDTO toModel(EventsEntity original) {
        EventsDTO bean = new EventsDTO();
        BeanUtils.copyProperties(original, bean);
        bean.add(linkTo(methodOn(EventsEntityController.class).getById(original.getId())).withSelfRel());
        return bean;
    }
}
