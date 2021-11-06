package vn.binhduong.eventmgrdemo.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "events")
public class EventsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 32)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @FutureOrPresent
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @NotNull
    @FutureOrPresent
    @Column(name = "end_date", nullable = false)
    private ZonedDateTime endDate;

    @UpdateTimestamp
    @Column(name = "last_update_date", nullable = false)
    private ZonedDateTime lastUpdateDate;

    @Builder
    public EventsEntity(Long id,
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
