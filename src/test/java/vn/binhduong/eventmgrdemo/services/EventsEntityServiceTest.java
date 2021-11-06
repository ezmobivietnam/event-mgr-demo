package vn.binhduong.eventmgrdemo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import vn.binhduong.eventmgrdemo.domain.EventsEntity;
import vn.binhduong.eventmgrdemo.repositories.EventsEntityRepository;
import vn.binhduong.eventmgrdemo.web.model.EventsDTO;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * Created by binhduong on 11/5/2021.
 */
@SpringBootTest
class EventsEntityServiceTest {

    @MockBean
    private EventsEntityRepository mockEventsEntityRepository;

    @Autowired
    private EventsEntityService eventsEntityService;

    @BeforeEach
    void setUp() {
    }

    /**
     * Test happy path
     * Given:
     * 1. A valid EventDTO instance containing the event info
     * 2. The method EventsEntityRepository.save() is executed successfully and return the saved entity
     * <p>
     * Then expect: the ID of the saved entity was returned
     */
    @Test
    void save_Given_ValidEventDTO_Then_ReturnSavedInstanceID() {
        //given
        EventsEntity savedEvent = EventsEntity.builder().id(1L)
                .name("Event 1").description("Description 1")
                .startDate(ZonedDateTime.now().plusDays(1)).
                endDate(ZonedDateTime.now().plusDays(2))
                .lastUpdateDate(ZonedDateTime.now()).build();
        EventsDTO dto = EventsDTO.builder().name("Event 1").description("Description 1")
                .startDate(ZonedDateTime.now().plusDays(1)).
                endDate(ZonedDateTime.now().plusDays(2))
                .lastUpdateDate(ZonedDateTime.now()).build();

        given(mockEventsEntityRepository.save(any(EventsEntity.class))).willReturn(savedEvent);
        //when
        Long id = eventsEntityService.save(dto);
        //then
        assertNotNull(id);
        assertEquals(id, 1L);
    }

    /**
     * Test method findAll()
     * Given:
     * - PageRequest parameter is null
     * - There are 03 entities existed in DB
     * Then:
     * - Got all 03 entities from DB
     * - The "self" links is appended to the response
     */
    @Test
    void findAll_Given_NoParamsSpecified_Then_GotAllEntities() {
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
        CollectionModel<EventsDTO> eventsDTOsModel = eventsEntityService.findAll();
        //then
        assertNotNull(eventsDTOsModel);
        assertEquals(records.size(), eventsDTOsModel.getContent().size());
        assertNotNull(eventsDTOsModel.getLink(IanaLinkRelations.SELF_VALUE).get());
    }

    /**
     * Test method findAllPaginated()
     * Given: PageRequest parameter is null
     * Then: throws IllegalArgumentException
     */
    @Test
    void findAllPaginated_Given_NullEventDTO_Then_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            //when
            eventsEntityService.findAllPaginated(null);
        });
        //then
        String expectedMessage = "PageRequest must not be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test method findAll()
     * Given:
     * - PageRequest parameter is not null which included page=0 and size=2
     * - There are 03 entities existed in DB
     * Then:
     * - Got first 02 entities of total 03 entities from DB
     * - The link "first" is appended to the response
     * - The link "self" is appended to the response
     * - The link "next" is appended to the response
     * - The link "last" is appended to the response
     */
    @Test
    void findAllPaginated_Given_ValidPageRequestParam_Then_GotTwoEntitiesFromFirstPage() {
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
        PageRequest pageRequest = PageRequest.of(0, 2);
        List<EventsEntity> allEntities = List.of(entity1, entity2, entity3);
        List<EventsEntity> firstPageEntities = List.of(entity1, entity2);
        Page<EventsEntity> stubData = new PageImpl<>(firstPageEntities, pageRequest, allEntities.size());
        given(mockEventsEntityRepository.findAll(any(PageRequest.class))).willReturn(stubData);
        //when
        CollectionModel<EventsDTO> paginationModel = eventsEntityService.findAllPaginated(pageRequest);
        //then
        PagedModel pagedModel = (PagedModel) paginationModel;
        assertNotNull(pagedModel.getMetadata());
        //check if the number total number of entities is correct
        assertEquals(pagedModel.getMetadata().getTotalElements(), allEntities.size());
        //check if the page size in meta data is correct or not
        assertEquals(pagedModel.getMetadata().getSize(), pageRequest.getPageSize());
        //check if the number of entities in the first page (page 0) is correct
        assertEquals(pagedModel.getContent().size(), firstPageEntities.size());
        //check if the link "first" (links to the first page) existed or not
        assertNotNull(pagedModel.getLink(IanaLinkRelations.FIRST_VALUE).get());
        //check if the link "self" (links to the current page) existed or not
        assertNotNull(pagedModel.getLink(IanaLinkRelations.SELF_VALUE).get());
        //check if the link "next" (links to the next page) existed or not
        assertNotNull(pagedModel.getLink(IanaLinkRelations.NEXT_VALUE).get());
        //check if the link "last" (links to the last page) existed or not
        assertNotNull(pagedModel.getLink(IanaLinkRelations.LAST_VALUE).get());
    }
}