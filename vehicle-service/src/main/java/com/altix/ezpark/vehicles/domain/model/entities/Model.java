package com.altix.ezpark.vehicles.domain.model.entities;


import com.altix.ezpark.shared.domain.model.entities.AuditableModel;
import com.altix.ezpark.vehicles.domain.model.commands.CreateModelCommand;
import com.altix.ezpark.vehicles.domain.model.commands.UpdateModelCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "models")
public class Model extends AuditableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    public Model(CreateModelCommand command){
        this.name = command.name();
        this.description = command.description();
    }

    public Model update(UpdateModelCommand command, Brand brand) {
        this.name = command.name();
        this.description = command.description();
        this.brand = brand;
        return this;
    }
}
