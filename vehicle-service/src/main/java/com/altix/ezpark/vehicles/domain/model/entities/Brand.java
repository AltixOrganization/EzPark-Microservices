package com.altix.ezpark.vehicles.domain.model.entities;


import com.altix.ezpark.shared.domain.model.entities.AuditableModel;
import com.altix.ezpark.vehicles.domain.model.commands.CreateBrandCommand;
import com.altix.ezpark.vehicles.domain.model.commands.UpdateBrandCommand;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "brands")
public class Brand extends AuditableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Model> models = List.of();

    public Brand(CreateBrandCommand command) {
        this.name = command.name();
        this.description = command.description();
    }

    public Brand update(UpdateBrandCommand command) {
        this.name = command.name();
        this.description = command.description();
        return this;
    }
}
