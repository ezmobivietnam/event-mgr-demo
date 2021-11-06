package vn.binhduong.eventmgrdemo.web.model;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import vn.binhduong.eventmgrdemo.web.constraints.CompareDate;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@CompareDate(before = "startDate", after = "endDate", message = "The start date must be before the end date")
public class EventsDTO extends RepresentationModel<EventsDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Null
    private Long id;

    @NotBlank
    @Size(min = 1, max = 32)
    private String name;

    @Size(max = 255)
    private String description;

    @NotNull
    @FutureOrPresent
    private ZonedDateTime startDate;

    @NotNull
    @FutureOrPresent
    private ZonedDateTime endDate;

    @Null
    private ZonedDateTime lastUpdateDate;

    @Builder
    public EventsDTO(Long id,
                     String name,
                     String description,
                     ZonedDateTime startDate,
                     ZonedDateTime endDate,
                     ZonedDateTime lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.lastUpdateDate = lastUpdateDate;
    }
}
