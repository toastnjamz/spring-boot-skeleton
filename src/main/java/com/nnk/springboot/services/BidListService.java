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

//    public BidList createBidList(BidList bidList) {
//        for (BidList bidListInList : bidListRepository.findAll()) {
//            if (bidListInList.equals(bidList)) {
//                return null;
//            }
//        }
//        BidList newBid = new BidList();
//        newBid.setAccount(bidList.getAccount());
//        newBid.setType(bidList.getType());
//        newBid.setBidQuantity(bidList.getBidQuantity());
//        return bidListRepository.save(newBid);
//    }

    public BidList createBidList(BidList bidList) {
        return bidListRepository.save(bidList);
    }

    public void updateBidList(BidList bidList) {
        if (bidListRepository.findById(bidList.getBidListId()) != null) {
            BidList updatedBidList = findById(bidList.getBidListId());
            updatedBidList.setAccount(bidList.getAccount());
            updatedBidList.setType(bidList.getType());
            updatedBidList.setBidQuantity(bidList.getAskQuantity());
            bidListRepository.save(updatedBidList);
        }
    }

    public void deleteBidList(Integer id) {
        if (bidListRepository.findById(id) != null) {
            bidListRepository.deleteById(id);
        }
    }
}
