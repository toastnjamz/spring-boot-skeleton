package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BidListService {

    BidListRepository bidListRepository;

    @Autowired
    public BidListService(BidListRepository bidListRepository) {
        this.bidListRepository = bidListRepository;
    }

    public List<BidList> findAllBids() {
        return bidListRepository.findAll();
    }

    public BidList findById(Integer id) {
        Optional<BidList> bidListOptional = bidListRepository.findById(id);
        if (bidListOptional.isPresent()) {
            BidList bidList = bidListOptional.get();
            return bidList;
        }
        return null;
    }

    public BidList createBidList(BidList bidList) {
        return bidListRepository.save(bidList);
    }

    public void updateBidList(BidList bidList) {
        if (bidListRepository.findById(bidList.getBidListId()) != null) {
            bidListRepository.save(bidList);
        }
    }

    public void deleteBidList(Integer id) {
        if (bidListRepository.findById(id) != null) {
            bidListRepository.deleteById(id);
        }
    }
}
