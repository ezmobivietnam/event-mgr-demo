package vn.binhduong.eventmgrdemo.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import vn.binhduong.eventmgrdemo.web.model.EventsDTO;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by binhduong on 11/5/2021.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EventsEntityControllerTest {
    private static final String END_POINT = "http://localhost:8080/api/events";

    @Autowired
    private MockMvc mockMvc;

    /**
     * Given:
     * 1. Target endpoint {http://localhost:8080/api/events} is used.
     * 2. The request body contains all the valid required fields
     * <p>
     * When:
     * 1. The endpoint is called by using the POST method
     * <p>
     * Then: the client will receive the status code 200 (OK)
     *
     * @throws Exception
     */
    @Test
    @Rollback(true)
    void save_Given_ValidEventDTO_ThenGot_StatusCode200() throws Exception {
        //Given
        EventsDTO dto = EventsDTO.builder().name("New Test Event")
                .description("This is new test event")
                .startDate(ZonedDateTime.now().plusDays(1)).
                endDate(ZonedDateTime.now().plusDays(5)).build();
        //when
        mockMvc.perform(post(END_POINT).contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isOk()); //then
    }

    /**
     * Given:
     * 1. Target endpoint {http://localhost:8080/api/events} is used.
     * 2. The request body contains all the valid required fields BUT the unique data constraint was violated.
     * <p>
     * When:
     * 1. The endpoint is called by using the POST method
     * <p>
     * Then: the client will receive the status code 400 (Bad Request)
     *
     * @throws Exception
     */
    @Test
    void save_Given_ValidEventDTOButUniqueConstraintViolated_Then_GotStatusCode400WithMessage() throws Exception {
        //given
        EventsDTO dto = EventsDTO.builder().name("New Test Event")
                .description("This is new test event")
                .startDate(ZonedDateTime.now().plusDays(1)).
                endDate(ZonedDateTime.now().plusDays(5)).build();
        // add the event for the first time should be okay.
        mockMvc.perform(post(END_POINT).contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto))).andExpect(status().isOk());
        //when
        // Add the same event (same name, same start date and same end date) should cause DB constraint error
        mockMvc.perform(post(END_POINT).contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto))).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.DataIntegrityViolation", notNullValue())) //then
        ;
    }

    /**
     * Given:
     * 1. Target endpoint {http://localhost:8080/api/events} is used.
     * 2. The request body contains the UNEXPECTED field id
     * <p>
     * When:
     * 1. The endpoint is called by using the POST method
     * <p>
     * Then expect:
     * 1. Client will receive the status code 400 (Bad request)
     * 2. The error message {"id": "must be null"} is returned
     *
     * @throws Exception
     */
    @Test
    void save_Given_EventsDTOContainsUnexpectID_Then_ReturnStatus400WithMessage() throws Exception {
        EventsDTO dto = EventsDTO.builder().id(1L)
                .name("New Test Event")
                .description("This is new test event")
                .startDate(ZonedDateTime.now().plusDays(1)).
                endDate(ZonedDateTime.now().plusDays(5)).build();
        mockMvc.perform(post(END_POINT).contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id", is("must be null")))
        ;
    }

    /**
     * Given:
     * 1. Target endpoint {http://localhost:8080/api/events} is used.
     * 2. The request body MISSED the "name" info
     * <p>
     * When:
     * 1. The endpoint is called by using the POST method
     * <p>
     * Then expect:
     * 1. Client will receive the status code 400 (Bad request)
     * 2. The error message {"name": "must not be blank"} is returned
     *
     * @throws Exception
     */
    @Test
    void save_Given_EventsDTOMissedRequiredName_Then_ReturnStatus400WithMessage() throws Exception {
        EventsDTO dto = EventsDTO.builder()
                .startDate(ZonedDateTime.now().plusDays(1))
                .endDate(ZonedDateTime.now().plusDays(5)).build();
        mockMvc.perform(post(END_POINT).contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name", is("must not be blank")))
        ;
    }

    /**
     * Given:
     * 1. Target endpoint {http://localhost:8080/api/events} is used.
     * 2. The request body MISSED the "startDate" info
     * <p>
     * When:
     * 1. The endpoint is called by using the POST method
     * <p>
     * Then expect:
     * 1. Client will receive the status code 400 (Bad request)
     * 2. The error message {"startDate": "must not be null"} is returned
     *
     * @throws Exception
     */
    @Test
    void save_Given_EventsDTOMissedRequiredStartDate_Then_ReturnStatus400WithMessage() throws Exception {
        EventsDTO dto = EventsDTO.builder()
                .name("New Test Event")
                .endDate(ZonedDateTime.now().plusDays(5)).build();
        mockMvc.perform(post(END_POINT).contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.startDate", is("must not be null")))
        ;
    }

    /**
     * Given:
     * 1. Target endpoint {http://localhost:8080/api/events} is used.
     * 2. The request body MISSED the "endDate" info
     * <p>
     * When:
     * 1. The endpoint is called by using the POST method
     * <p>
     * Then expect:
     * 1. Client will receive the status code 400 (Bad request)
     * 2. The error message {"endDate": "must not be null"} is returned
     *
     * @throws Exception
     */
    @Test
    void save_Given_EventsDTOMissedRequiredEndDate_Then_ReturnStatus400WithMessage() throws Exception {
        EventsDTO dto = EventsDTO.builder().name("New Test Event")
                .startDate(ZonedDateTime.now().plusDays(1)).build();
        mockMvc.perform(post(END_POINT).contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.endDate", is("must not be null")))
        ;
    }

    /**
     * Given:
     * 1. Target endpoint {http://localhost:8080/api/events} is used.
     * 2. The request body contains the "startDate" is a date in the past
     * <p>
     * When:
     * 1. The endpoint is called by using the POST method
     * <p>
     * Then expect:
     * 1. Client will receive the status code 400 (Bad request)
     * 2. The error message {"startDate": "must be a date in the present or in the future"} is returned
     *
     * @throws Exception
     */
    @Test
    void save_Given_EventsDTOHasPastStartDate_Then_ReturnStatus400WithMessage() throws Exception {
        EventsDTO dto = EventsDTO.builder().name("New Test Event")
                .startDate(ZonedDateTime.now().minusDays(2))
                .endDate(ZonedDateTime.now().plusDays(5)).build();
        mockMvc.perform(post(END_POINT).contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.startDate", is("must be a date in the present or in the future")))
        ;
    }

    /**
     * Given:
     * 1. Target endpoint {http://localhost:8080/api/events} is used.
     * 2. The request body contains the "endDate" is a date in the past
     * <p>
     * When:
     * 1. The endpoint is called by using the POST method
     * <p>
     * Then expect:
     * 1. Client will receive the status code 400 (Bad request)
     * 2. The error message {"endDate": "must be a date in the present or in the future"} is returned
     *
     * @throws Exception
     */
    @Test
    void save_Given_EventsDTOHasPastEndDate_Then_ReturnStatus400WithMessage() throws Exception {
        EventsDTO dto = EventsDTO.builder().name("New Test Event")
                .startDate(ZonedDateTime.now().minusDays(2))
                .endDate(ZonedDateTime.now().minusDays(1)).build();
        mockMvc.perform(post(END_POINT).contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.endDate", is("must be a date in the present or in the future")))
                .andExpect(jsonPath("$.startDate", is("must be a date in the present or in the future")))
        ;
    }

    /**
     * Given:
     * 1. Target endpoint {http://localhost:8080/api/events} is used.
     * 2. The request body contains invalid date constraint, e.g. the "startDate" NOT before "endDate"
     * <p>
     * When:
     * 1. The endpoint is called by using the POST method
     * <p>
     * Then expect:
     * 1. Client will receive the status code 400 (Bad request)
     * 2. The error message {"CompareDate": "The start date must be before the end date"} is returned
     *
     * @throws Exception
     */
    @Test
    void save_Given_EventsDTOHasStartDateNotBeforeEndDate_Then_ReturnStatus400WithMessage() throws Exception {
        EventsDTO dto = EventsDTO.builder().name("New Test Event")
                .startDate(ZonedDateTime.now().plusDays(2))
                .endDate(ZonedDateTime.now().plusDays(1)).build();
        mockMvc.perform(post(END_POINT).contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.CompareDate", is("The start date must be before the end date")))
        ;
    }

    /**
     * Given:
     * 1. Target endpoint {http://localhost:8080/api/events} is used.
     * 2. The request body contains an element which has invalid JSON format
     * <p>
     * When:
     * 1. The endpoint is called by using the POST method
     * <p>
     * Then expect:
     * 1. Client will receive the status code 400 (Bad request)
     * 2. The error message {"JsonParseError": "JSON parse error: ..."} is returned
     *
     * @throws Exception
     */
    @Test
    void save_Given_EventsDTOHasInvalidJsonFormat_Then_ReturnStatus400WithMessage() throws Exception {
        EventsDTO dto = EventsDTO.builder()
                .name("New Test Event").startDate(ZonedDateTime.now().plusDays(2))
                .endDate(ZonedDateTime.now().plusDays(5)).build();
        String validJson = asJsonString(dto);
        String invalidJson = validJson.replaceAll("\"startDate\":", "\"startDate\":YYYY");
        mockMvc.perform(post(END_POINT).contentType(MediaType.APPLICATION_JSON).content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.JsonParseError", containsString("JSON parse error: ")))
        ;
    }

    /**
     * Given:
     * 1. Target endpoint {http://localhost:8080/api/events} is used.
     * 2. There is no parameters are specified
     * <p>
     * When:
     * 1. The endpoint is called by using the GET method
     * <p>
     * Then expect:
     * 1. Client will receive the list of 7 events in json format (the data was initialized when the app boot up)
     * 2. The "self" link is added to the response
     *
     * @throws Exception
     */
    @Test
    void query_Given_NoParamsSpecified_Then_ReturnAllEventsWithSelfLink() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.eventsDTOList", hasSize(7)))
                .andExpect(jsonPath("$._links.self.href", containsStringIgnoringCase("/api/events")));
    }

    /**
     * Given:
     * 1. Target endpoint {http://localhost:8080/api/events} is used.
     * 2. Parameters {page=1&size=3} is used
     * <p>
     * When:
     * 1. The endpoint is called by using the GET method
     * <p>
     * Then expect:
     * 1. Client will receive the list of 3 events of the page 01 in json format
     * 2. The pagination links ("first, "prev", "self", "next, "last") link is added to the response
     *
     * @throws Exception
     */
    @Test
    void query_Given_PageAndSizeParamsUsed_Then_ReturnEventsOfSelectedPageWithPaginationLinks() throws Exception {
        String url = END_POINT + "?page=1&size=3";
        mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.eventsDTOList", hasSize(3)))
                .andExpect(jsonPath("$._links.first.href", containsStringIgnoringCase("/api/events?page=0&size=3")))
                .andExpect(jsonPath("$._links.prev.href", containsStringIgnoringCase("/api/events?page=0&size=3")))
                .andExpect(jsonPath("$._links.self.href", containsStringIgnoringCase("/api/events?page=1&size=3")))
                .andExpect(jsonPath("$._links.next.href", containsStringIgnoringCase("/api/events?page=2&size=3")))
                .andExpect(jsonPath("$._links.last.href", containsStringIgnoringCase("/api/events?page=2&size=3")))
                .andExpect(jsonPath("$.page.number", is(1)))
                .andExpect(jsonPath("$.page.size", is(3)))
                .andExpect(jsonPath("$.page.totalElements", is(7)))
                .andExpect(jsonPath("$.page.totalPages", is(3)))
        ;
    }

    private String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}