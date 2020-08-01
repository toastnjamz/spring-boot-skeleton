package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RatingServiceTest {

    @Mock
    RatingRepository ratingRepositoryMock;

    @InjectMocks
    RatingService ratingService;

    private Rating testRating;

    @Before
    public void setup() {
        testRating = new Rating("moodysRating", "sandPRating", "fitchRating", 1);
        testRating.setId(1);
    }

    @Test
    public void findAllRatings_ratingExists_ratingReturned() {
        // arrange
        List<Rating> ratings = new ArrayList<>();
        ratings.add(testRating);

        when(ratingRepositoryMock.findAll()).thenReturn(ratings);

        // act
        List<Rating> listResult = ratingService.findAllRatings();

        // assert
        assertTrue(listResult.size() > 0);
    }

    @Test
    public void findAllRatings_ratingDoesNotExist_noRatingsReturned() {
        // arrange

        // act
        List<Rating> listResult = ratingService.findAllRatings();

        // assert
        assertTrue(listResult.size() == 0);
    }

    @Test
    public void findById_ratingExists_ratingReturned() {
        // arrange
        when(ratingRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(testRating));

        // act
        Rating ratingResult = ratingService.findById(1);

        // assert
        assertEquals(testRating.getId(), ratingResult.getId(), 0);
    }

    @Test
    public void findById_ratingDoesNotExist_nullReturned() {
        // arrange

        // act
        Rating ratingResult = ratingService.findById(2);

        // assert
        assertNull(ratingResult);
    }

    @Test
    public void createRating_validRating_ratingReturned() {
        // arrange
        Rating testRating2 = new Rating("moodysRating", "sandPRating", "fitchRating", 2);
        testRating2.setId(2);

        when(ratingRepositoryMock.save(testRating2)).thenReturn(testRating2);

        // act
        Rating ratingResult = ratingService.createRating(testRating2);

        // assert
        verify(ratingRepositoryMock, times(1)).save(any(Rating.class));
        assertEquals(testRating2.getId(), ratingResult.getId(), 0);
    }

    @Test
    public void updateRating_validRating_ratingSaved() {
        // arrange
        Rating testRating2 = new Rating("moodysRating", "sandPRating", "fitchRating", 2);
        testRating2.setId(2);
        ratingService.createRating(testRating2);
        testRating2.setMoodysRating("updatedMoodysRating");

        // act
        ratingService.updateRating(testRating2);

        // assert
        verify(ratingRepositoryMock, times(2)).save(any(Rating.class));
        assertEquals(testRating2.getMoodysRating(), "updatedMoodysRating", "updatedMoodysRating");
    }

    @Test
    public void deleteRating_validRating_ratingDeleted() {
        // arrange

        // act
        ratingService.deleteRating(1);

        // assert
        Optional<Rating> rating = ratingRepositoryMock.findById(1);
        assertFalse(rating.isPresent());
    }
}