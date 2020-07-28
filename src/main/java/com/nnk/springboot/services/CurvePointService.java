package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurvePointService {

    private CurvePointRepository curvePointRepository;

    @Autowired
    public CurvePointService(CurvePointRepository curvePointRepository) {
        this.curvePointRepository = curvePointRepository;
    }

    public List<CurvePoint> findAllCurvePoints() {
        return curvePointRepository.findAll();
    }

    public CurvePoint findById(Integer id) {
        Optional<CurvePoint> curvePointOptional = curvePointRepository.findById(id);
        if (curvePointOptional.isPresent()) {
            CurvePoint curvePoint = curvePointOptional.get();
            return curvePoint;
        }
        return null;
    }

    public CurvePoint createCurvePoint(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    public void updateCurvePoint(CurvePoint curvePoint) {
        if (curvePointRepository.findById(curvePoint.getId()) != null) {
            curvePointRepository.save(curvePoint);
        }
    }

    public void deleteCurvePoint(Integer id) {
        if (curvePointRepository.findById(id) != null) {
            curvePointRepository.deleteById(id);
        }
    }
}
