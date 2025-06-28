package com.altix.ezpark.reviews.interfaces.rest;

import com.altix.ezpark.reviews.domain.model.commands.DeleteReviewCommand;
import com.altix.ezpark.reviews.domain.model.queries.GetAllReviewsByParkingIdQuery;
import com.altix.ezpark.reviews.domain.model.queries.GetAllReviewsByProfileIdQuery;
import com.altix.ezpark.reviews.domain.model.queries.GetAllReviewsQuery;
import com.altix.ezpark.reviews.domain.services.ReviewCommandService;
import com.altix.ezpark.reviews.domain.services.ReviewQueryService;
import com.altix.ezpark.reviews.interfaces.rest.resources.CreateReviewResource;
import com.altix.ezpark.reviews.interfaces.rest.resources.ReviewResource;
import com.altix.ezpark.reviews.interfaces.rest.resources.UpdateReviewResource;
import com.altix.ezpark.reviews.interfaces.rest.transformers.CreateReviewCommandFromResourceAssembler;
import com.altix.ezpark.reviews.interfaces.rest.transformers.ReviewResourceFromEntityAssembler;
import com.altix.ezpark.reviews.interfaces.rest.transformers.UpdateReviewCommandFromResourceAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "api/v1/reviews")
public class ReviewController {
    private final ReviewCommandService reviewCommandService;
    private final ReviewQueryService reviewQueryService;

    public ReviewController(ReviewCommandService reviewCommandService, ReviewQueryService reviewQueryService) {
        this.reviewCommandService = reviewCommandService;
        this.reviewQueryService = reviewQueryService;
    }

    @PostMapping
    public ResponseEntity<ReviewResource> createReview(@Valid @RequestBody CreateReviewResource resource){
        var createReviewCommand = CreateReviewCommandFromResourceAssembler.toCommandFromResource(resource);
        var review = reviewCommandService.handle(createReviewCommand)
                .map(ReviewResourceFromEntityAssembler::toResourceFromEntity);

        return review.map(r -> new ResponseEntity<>(r, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResource> updateReview(@PathVariable("id") Long id, @Valid @RequestBody UpdateReviewResource resource) {
        var updateReviewCommand = UpdateReviewCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updatedReview = reviewCommandService.handle(updateReviewCommand)
                .map(ReviewResourceFromEntityAssembler::toResourceFromEntity);

        return updatedReview.map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable("id") Long id) {
        var deleteReviewCommand = new DeleteReviewCommand(id);
        reviewCommandService.handle(deleteReviewCommand);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResource>> getAllReviews() {
        var getAllReviewsQuery = new GetAllReviewsQuery();
        var reservationList = reviewQueryService.handle(getAllReviewsQuery)
                .stream()
                .map(ReviewResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return new ResponseEntity<>(reservationList, HttpStatus.OK);
    }

    @GetMapping("/parking/{parkingId}")
    public ResponseEntity<List<ReviewResource>> getReviewsByParkingId(@PathVariable Long parkingId) {
        var getReviewsByParkingIdQuery = new GetAllReviewsByParkingIdQuery(parkingId);
        var reviews = reviewQueryService.handle(getReviewsByParkingIdQuery)
                .stream()
                .map(ReviewResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<ReviewResource>> getReviewsByProfileId(@PathVariable Long profileId) {
        var getReviewsByProfileIdQuery = new GetAllReviewsByProfileIdQuery(profileId);
        var reviews = reviewQueryService.handle(getReviewsByProfileIdQuery)
                .stream()
                .map(ReviewResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }


}
