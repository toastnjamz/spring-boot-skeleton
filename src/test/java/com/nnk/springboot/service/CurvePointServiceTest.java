package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.CurvePointService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CurvePointServiceTest {

    @Mock
    CurvePointRepository curvePointRepositoryMock;

    @InjectMocks
    CurvePointService curvePointService;

    private CurvePoint testCurvePoint;

    @Before
    public void setup() {
        testCurvePoint = new CurvePoint(1, 1.0, 1.0);
        testCurvePoint.setId(1);
    }

    @Test
    public void findAllCurvePoints_curvePointExists_curvePointsReturned() {
        // arrange
        List<CurvePoint> curves = new ArrayList<>();
        curves.add(testCurvePoint);

        when(curvePointRepositoryMock.findAll()).thenReturn(curves);

        // act
        List<CurvePoint> listResult = curvePointService.findAllCurvePoints();

        // assert
        assertTrue(listResult.size() > 0);
    }

    @Test
    public void findAllCurvePoints_curvePointDoesNotExist_noCurvePointsReturned() {
        // arrange

        // act
        List<CurvePoint> listResult = curvePointService.findAllCurvePoints();

        // assert
        assertTrue(listResult.size() == 0);
    }

    @Test
    public void findById_curvePointExists_curvePointReturned() {
        // arrange
        when(curvePointRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(testCurvePoint));

        // act
        CurvePoint curveResult = curvePointService.findById(1);

        // assert
        assertEquals(testCurvePoint.getId(), curveResult.getId(), 0);
    }

    @Test
    public void findById_curvePointDoesNotExist_nullReturned() {
        // arrange

        // act
        CurvePoint curveResult = curvePointService.findById(2);

        // assert
        assertNull(curveResult);
    }

    @Test
    public void createCurvePoint_curvePointValid_curvePointReturned() {
        // arrange
        CurvePoint testCurvePoint2 = new CurvePoint(2, 2.0, 2.0);
        testCurvePoint2.setId(2);

        when(curvePointRepositoryMock.save(testCurvePoint2)).thenReturn(testCurvePoint2);

        // act
        CurvePoint curveResult = curvePointService.createCurvePoint(testCurvePoint2);

        // assert
        verify(curvePointRepositoryMock, times(1)).save(any(CurvePoint.class));
        assertEquals(testCurvePoint2.getId(), curveResult.getId(), 0);
    }

    @Test
    public void updateCurvePoint_curvePointValid_curvePointSaved() {
        // arrange
        CurvePoint testCurvePoint2 = new CurvePoint(2, 2.0, 2.0);
        testCurvePoint2.setId(2);
        curvePointService.createCurvePoint(testCurvePoint2);
        testCurvePoint2.setCurveId(3);

        // act
        curvePointService.updateCurvePoint(testCurvePoint2);

        // assert
        verify(curvePointRepositoryMock, times(2)).save(any(CurvePoint.class));
        assertEquals(testCurvePoint2.getCurveId(), 3, 3);
    }

    @Test
    public void deleteCurvePoint_curvePointValid_curvePointDeleted() {
        // arrange

        // act
        curvePointService.deleteCurvePoint(1);

        // assert
        Optional<CurvePoint> curvePoint = curvePointRepositoryMock.findById(1);
        assertFalse(curvePoint.isPresent());
    }
}
