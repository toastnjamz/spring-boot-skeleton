package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class BidListServiceTest {

    @Mock
    BidListRepository bidListRepositoryMock;

    @InjectMocks
    BidListService bidListService;

    private BidList testBid;

    @Before
    public void setup() {
        testBid = new BidList("testAccount", "testType", 1.0);
        testBid.setBidListId(1);
    }

    @Test
    public void findAllBids_bidExists_bidsReturned() {
        // arrange
        List<BidList> bids = new ArrayList<>();
        bids.add(testBid);

        when(bidListRepositoryMock.findAll()).thenReturn(bids);

        // act
        List<BidList> listResult = bidListService.findAllBids();

        // assert
        assertTrue(listResult.size() > 0);
    }

    @Test
    public void findAllBids_bidDoesNotExist_noBidsReturned() {
        // arrange

        // act
        List<BidList> listResult = bidListService.findAllBids();

        // assert
        assertTrue(listResult.size() == 0);
    }

    @Test
    public void findById_bidExists_bidReturned() {
        // arrange
        when(bidListRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(testBid));

        // act
        BidList bidResult = bidListService.findById(1);

        // assert
        assertEquals(testBid.getBidListId(), bidResult.getBidListId(), 0);
    }

    @Test
    public void findById_bidDoesNotExist_nullReturned() {
        // arrange

        // act
        BidList bidResult = bidListService.findById(2);

        // assert
        assertNull(bidResult);
    }

    @Test
    public void createBidList_validBid_bidReturned() {
        // arrange
        BidList testBid2 = new BidList("testAccount", "testType", 1.0);
        testBid2.setBidListId(2);

        when(bidListRepositoryMock.save(testBid2)).thenReturn(testBid2);

        // act
        BidList bidResult = bidListService.createBidList(testBid2);

        // assert
        verify(bidListRepositoryMock, times(1)).save(any(BidList.class));
        assertEquals(testBid2.getBidListId(), bidResult.getBidListId(), 0);
    }

    @Test
    public void updateBidList_validBid_bidSaved() {
        // arrange
        BidList testBid2 = new BidList("testAccount", "testType", 1.0);
        testBid2.setBidListId(2);
        bidListService.createBidList(testBid2);
        testBid2.setAccount("updatedAccount");

        // act
        bidListService.updateBidList(testBid2);

        // assert
        verify(bidListRepositoryMock, times(2)).save(any(BidList.class));
        assertEquals(testBid2.getAccount(), "updatedAccount", "updatedAccount");
    }

    @Test
    public void deleteBidList_validBid_bidDeleted() {
        // arrange

        // act
        bidListService.deleteBidList(1);

        // assert
        Optional<BidList> bidList = bidListRepositoryMock.findById(1);
        assertFalse(bidList.isPresent());
    }
}
