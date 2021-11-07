package vn.binhduong.eventmgrdemo.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import vn.binhduong.eventmgrdemo.domain.EventsEntity;
import vn.binhduong.eventmgrdemo.repositories.EventsEntityRepository;
import vn.binhduong.eventmgrdemo.web.model.EventsDTO;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

/**
 * Created by binhduong on 11/7/2021.
 */
@SpringBootTest
class EventsEntitySimpleServiceTest {
    @MockBean
    private EventsEntityRepository mockEventsEntityRepository;

    @Autowired
    private EventsEntitySimpleService eventsEntityService;

    /**
     * Test method findAll()
     * Given:
     * - There are 03 entities existed in DB
     * Then:
     * - Got all 03 entities from DB
     * - The "self" links is appended to the response
     */
    @Test
    void findAll_Given_03EventsExisted_Then_GotAll03Events() {
        //given
        EventsEntity entity1 = EventsEntity.builder().name("Test Name 1")
                .startDate(ZonedDateTime.now().plusDays(1))
                .endDate(ZonedDateTime.now().plusDays(2))
                .build();
        EventsEntity entity2 = EventsEntity.builder().name("Test Name 2")
                .startDate(ZonedDateTime.now().plusDays(2))
                .endDate(ZonedDateTime.now().plusDays(3))
                .build();
        EventsEntity entity3 = EventsEntity.builder().name("Test Name 3")
                .startDate(ZonedDateTime.now().plusDays(3))
                .endDate(ZonedDateTime.now().plusDays(4))
                .build();
        List<EventsEntity> records = List.of(entity1, entity2, entity3);
        given(mockEventsEntityRepository.findAll()).willReturn(records);
        //when
        List<EventsDTO> result = eventsEntityService.findAll();
        //then
        assertNotNull(result);
        assertEquals(records.size(), result.size());
    }
}