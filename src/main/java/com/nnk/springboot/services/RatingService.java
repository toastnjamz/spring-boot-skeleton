package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Basic CRUD operations for Rating
 * Business logic layer, separates repository from controller
 */
@Service
public class RatingService {

    private RatingRepository ratingRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public List<Rating> findAllRatings() {
        return ratingRepository.findAll();
    }

    public Rating findById(Integer id) {
        Optional<Rating> ratingOptional = ratingRepository.findById(id);
        if (ratingOptional.isPresent()) {
            Rating rating = ratingOptional.get();
            return rating;
        }
        return null;
    }

    public Rating createRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public void updateRating(Rating rating) {
        if (ratingRepository.findById(rating.getId()) != null) {
            ratingRepository.save(rating);
        }
    }

    public void deleteRating(Integer id) {
        if (ratingRepository.findById(id) != null) {
            ratingRepository.deleteById(id);
        }
    }
}
