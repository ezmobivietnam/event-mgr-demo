package vn.binhduong.eventmgrdemo.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import vn.binhduong.eventmgrdemo.domain.EventsEntity;

import javax.validation.ConstraintViolationException;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by binhduong on 11/6/2021.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:eventsdb;MODE=MYSQL"
})
class EventsEntityRepositoryTest {

    @Autowired
    TestEntityManager entityManager;
    @Autowired
    private EventsEntityRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    /**
     * Test happy path
     * - Given: A valid entity (this is no data integrity constraint violation, all data set correctly)
     * - Then: That entity is persisted in DB successfully
     */
    @Test
    public void save_Given_ValidEntity_Then_EntityPersisted() {
        //Given
        EventsEntity entity = EventsEntity.builder().name("Test Name")
                .startDate(ZonedDateTime.now().plusDays(1))
                .endDate(ZonedDateTime.now().plusDays(2))
                .build();
        //When
        EventsEntity result = repository.save(entity);
        //Then
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    /**
     * Test constraint on columns (name, start_date, end_date)
     * - Given: An entity has the same name, startDate and endDate with the one existed in DB
     * - Then: DataIntegrityViolationException was thrown
     */
    @Test
    void save_Given_EntityViolatedUniqueConstraint_Then_ThrowsException() {
        //Given
        EventsEntity entity = EventsEntity.builder().name("Test Name")
                .startDate(ZonedDateTime.now().plusDays(1))
                .endDate(ZonedDateTime.now().plusDays(2))
                .build();
        EventsEntity savedEntity = entityManager.persistAndFlush(entity);
        assertNotNull(savedEntity);
        assertNotNull(savedEntity.getId());
        // Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            EventsEntity newEntity = EventsEntity.builder()
                    .name(entity.getName())
                    .startDate(entity.getStartDate())
                    .endDate(entity.getEndDate())
                    .build();
            EventsEntity result = repository.save(newEntity);
        });
    }

    /**
     * Test bean constraint validation:
     * - Given: all fields missing
     * - Then: ConstraintViolationException was thrown
     */
    @Test
    void save_Given_EmptyEntity_Then_ThrowsException() {
        assertThrows(ConstraintViolationException.class, () -> {
            repository.save(EventsEntity.builder().build());
        });
    }

    /**
     * Test bean constraint validation
     * - Given: missing required field "name"
     * - Then: ConstraintViolationException was thrown
     */
    @Test
    void save_Given_EntityMissedRequiredName_Then_ThrowsException() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            // Given
            EventsEntity entity = EventsEntity.builder()
//                    .name("Test Name")
                    .startDate(ZonedDateTime.now().plusDays(1))
                    .endDate(ZonedDateTime.now().plusDays(2))
                    .build();
            //When
            repository.save(entity);
        });
        //Then
        String expectedMessage = "must not be blank";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test bean constraint validation
     * - Given: missing required field "startDate"
     * - Then: ConstraintViolationException was thrown
     */
    @Test
    void save_Given_EntityMissedRequiredStartDate_Then_ThrowsException() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            //Given
            EventsEntity entity = EventsEntity.builder()
                    .name("Test Name")
//                    .startDate(ZonedDateTime.now().plusDays(1))
                    .endDate(ZonedDateTime.now().plusDays(2))
                    .build();
            //When
            repository.save(entity);
        });
        //Then
        String expectedMessage = "must not be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test bean constraint validation:
     * - Given: missing required field "endDate"
     * - Then: ConstraintViolationException was thrown
     */
    @Test
    void save_Given_EntityMissedRequiredEndDate_Then_ThrowsException() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            //Given
            EventsEntity entity = EventsEntity.builder()
                    .name("Test Name")
                    .startDate(ZonedDateTime.now().plusDays(1))
//                    .endDate(ZonedDateTime.now().plusDays(2))
                    .build();
            //When
            repository.save(entity);
        });
        //Then
        String expectedMessage = "must not be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test bean constraint validation
     * - Given:  "startDate" is a date in the past
     * - Then: ConstraintViolationException was thrown
     */
    @Test
    void save_Given_EntityHadPastStartDate_Then_ThrowsException() {
        //Given
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            EventsEntity entity = EventsEntity.builder()
                    .name("Test Name")
                    .startDate(ZonedDateTime.now().minusDays(1)) // date in the past - yesterday
                    .endDate(ZonedDateTime.now().plusDays(2))
                    .build();
            // When
            repository.save(entity);
        });
        // Then
        String expectedMessage = "must be a date in the present or in the future";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test bean constraint validation
     * - Given:  "endDate" is a date in the past
     * - Then: ConstraintViolationException was thrown
     */
    @Test
    void save_Given_EntityHadPastEndDate_Then_ThrowsException() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            //Given
            EventsEntity entity = EventsEntity.builder()
                    .name("Test Name")
                    .startDate(ZonedDateTime.now().plusDays(1))
                    .endDate(ZonedDateTime.now().minusDays(2)) // two dates ago
                    .build();
            // When
            repository.save(entity);
        });
        // Then
        String expectedMessage = "must be a date in the present or in the future";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test bean constraint validation:
     * - Given: "name" has max 32 chars.
     * - Then: ConstraintViolationException was thrown
     */
    @Test
    void save_Given_EntityNameHadOver32Chars_Then_ThrowsException() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            // Given
            EventsEntity entity = EventsEntity.builder()
                    .name("111112222233333444445555566666778") //33 chars
                    .startDate(ZonedDateTime.now().plusDays(1))
                    .endDate(ZonedDateTime.now().plusDays(2))
                    .build();
            // When
            repository.save(entity);
        });
        // Then
        String expectedMessage = "size must be between 1 and 32";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test bean constraint validation
     * - Given: "name" is blank.
     * - Then: ConstraintViolationException was thrown
     */
    @Test
    void save_Given_EntityNameIsBlank_Then_ThrowsException() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            //Given
            EventsEntity entity = EventsEntity.builder()
                    .name(" ") //1 blank char
                    .startDate(ZonedDateTime.now().plusDays(1))
                    .endDate(ZonedDateTime.now().plusDays(2))
                    .build();
            //When
            repository.save(entity);
        });
        // Then
        String expectedMessage = "must not be blank";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test findAll():
     * - Given: two entities existed
     * - Then: retrieve all those two entities correctly
     */
    @Test
    public void findAll_Given_TwoEntitiesExisted_Then_ReturnListOfTwoEntities() {
        // Given
        EventsEntity entity1 = EventsEntity.builder().name("Test Name 1")
                .startDate(ZonedDateTime.now().plusDays(1))
                .endDate(ZonedDateTime.now().plusDays(2))
                .build();
        entity1 = entityManager.persistAndFlush(entity1);
        EventsEntity entity2 = EventsEntity.builder().name("Test Name 2")
                .startDate(ZonedDateTime.now().plusDays(2))
                .endDate(ZonedDateTime.now().plusDays(3))
                .build();
        entity2 = entityManager.persistAndFlush(entity2);
        // When
        List<EventsEntity> allEntities = repository.findAll();
        // Then
        assertTrue(allEntities.contains(entity1));
        assertTrue(allEntities.contains(entity2));
    }

    /**
     * Test findAll() with given pagination request
     */
    @Test
    public void findAll_PageRequest_Given_TwoEntitiesExisted_Then_ReturnListOfTwoEntities() {
        // Given
        EventsEntity entity1 = EventsEntity.builder().name("Test Name 1")
                .startDate(ZonedDateTime.now().plusDays(1))
                .endDate(ZonedDateTime.now().plusDays(2))
                .build();
        entity1 = entityManager.persistAndFlush(entity1);
        EventsEntity entity2 = EventsEntity.builder().name("Test Name 2")
                .startDate(ZonedDateTime.now().plusDays(2))
                .endDate(ZonedDateTime.now().plusDays(3))
                .build();
        entity2 = entityManager.persistAndFlush(entity2);
        PageRequest pageRequest = PageRequest.of(0, 2); //first page with size = 2
        // When
        Page<EventsEntity> page = repository.findAll(pageRequest);
        List<EventsEntity> pageContent = page.getContent();
        assertTrue(pageContent.contains(entity1));
        assertTrue(pageContent.contains(entity2));
    }
}