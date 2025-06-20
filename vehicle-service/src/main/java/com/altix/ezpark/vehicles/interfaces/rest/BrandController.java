package com.altix.ezpark.vehicles.interfaces.rest;

import com.altix.ezpark.vehicles.domain.model.commands.DeleteBrandCommand;
import com.altix.ezpark.vehicles.domain.model.queries.GetAllBrandsQuery;
import com.altix.ezpark.vehicles.domain.services.BrandCommandService;
import com.altix.ezpark.vehicles.domain.services.BrandQueryService;
import com.altix.ezpark.vehicles.interfaces.rest.resources.BrandWithModelListResource;
import com.altix.ezpark.vehicles.interfaces.rest.resources.CreateBrandResource;
import com.altix.ezpark.vehicles.interfaces.rest.resources.UpdateBrandResource;
import com.altix.ezpark.vehicles.interfaces.rest.transform.BrandResourceFromEntityAssembler;
import com.altix.ezpark.vehicles.interfaces.rest.transform.CreateBrandCommandFromResourceAssembler;
import com.altix.ezpark.vehicles.interfaces.rest.transform.UpdateBrandCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "api/v1/brands")
@Tag(name = "Brand", description = "Brand Management Endpoints")
public class BrandController {
    private BrandCommandService brandCommandService;
    private BrandQueryService brandQueryService;

    @GetMapping
    public ResponseEntity<List<BrandWithModelListResource>> getAllBrands() {
        var getAllBrandsQuery = new GetAllBrandsQuery();
        List<BrandWithModelListResource> brands = brandQueryService.handle(getAllBrandsQuery).stream().map(BrandResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(brands);
    }

    @PostMapping
    public ResponseEntity<BrandWithModelListResource> createBrand(@Valid @RequestBody CreateBrandResource resource) {
        return brandCommandService.handle(CreateBrandCommandFromResourceAssembler.toCommandFromResource(resource))
                .map(BrandResourceFromEntityAssembler::toResourceFromEntity)
                .map(brand -> ResponseEntity.status(HttpStatus.CREATED).body(brand))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandWithModelListResource> updateBrand(@PathVariable("id") Long id, @Valid @RequestBody UpdateBrandResource resource) {
        return brandCommandService.handle(UpdateBrandCommandFromResourceAssembler.toCommandFromResource(id, resource))
                .map(BrandResourceFromEntityAssembler::toResourceFromEntity)
                .map(brand -> ResponseEntity.status(HttpStatus.OK).body(brand))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("id") Long id) {
        var deleteBrandCommand = new DeleteBrandCommand(id);
        brandCommandService.handle(deleteBrandCommand);
        return ResponseEntity.noContent().build();
    }
}
