package com.altix.ezpark.profiles.domain.model.aggregates;

import com.altix.ezpark.profiles.domain.model.commands.CreateProfileCommand;
import com.altix.ezpark.profiles.domain.model.commands.UpdateProfileCommand;
import com.altix.ezpark.profiles.domain.model.valueobject.UserId;
import com.altix.ezpark.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@NoArgsConstructor
@Table(name = "profiles")
public class Profile extends AuditableAbstractAggregateRoot<Profile> {

    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    @Embedded
    private UserId userId;

    public Profile(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public Profile(CreateProfileCommand command) {
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.birthDate = command.birthDate();
        this.userId = new UserId(command.userId());
    }
    public Profile updatedProfile(UpdateProfileCommand command) {
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.birthDate = command.birthDate();
        return this;
    }
}
