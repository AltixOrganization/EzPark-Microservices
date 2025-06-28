package com.altix.ezpark.reviews.domain.model.aggregate;

import com.altix.ezpark.reviews.domain.model.commands.CreateReviewCommand;
import com.altix.ezpark.reviews.domain.model.commands.UpdateReviewCommand;
import com.altix.ezpark.reviews.domain.model.valueobject.ParkingId;
import com.altix.ezpark.reviews.domain.model.valueobject.ProfileId;
import com.altix.ezpark.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
@Entity
@NoArgsConstructor
@Table(name = "reservations")
public class Review extends AuditableAbstractAggregateRoot<Review> {
    private Integer rating;
    private String comment;

    @Embedded
    private ParkingId parkingId;
    @Embedded
    private ProfileId profileId;

    public Review(CreateReviewCommand command) {
        this.rating = command.rating();
        this.comment = command.comment();
        this.parkingId = new ParkingId(command.parkingId());
        this.profileId = new ProfileId(command.profileId());
    }

    public Review update(UpdateReviewCommand command) {
        this.rating = command.rating();
        this.comment = command.comment();
        return this;
    }
}
