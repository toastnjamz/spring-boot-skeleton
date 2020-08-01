package com.nnk.springboot.service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.TradeService;
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
public class TradeServiceTest {

    @Mock
    TradeRepository tradeRepositoryMock;

    @InjectMocks
    TradeService tradeService;

    private Trade testTrade;

    @Before
    public void setup() {
        testTrade = new Trade("account", "type");
        testTrade.setTradeId(1);
    }

    @Test
    public void findAllTrades_tradeExists_tradesReturned() {
        // arrange
        List<Trade> trades = new ArrayList<>();
        trades.add(testTrade);

        when(tradeRepositoryMock.findAll()).thenReturn(trades);

        // act
        List<Trade> listResult = tradeService.findAllTrades();

        // assert
        assertTrue(listResult.size() > 0);
    }

    @Test
    public void findAllTrades_tradeDoesNotExist_noTradesReturned() {
        // arrange

        // act
        List<Trade> listResult = tradeService.findAllTrades();

        // assert
        assertTrue(listResult.size() == 0);
    }

    @Test
    public void findById_tradeExists_tradeReturned() {
        // arrange
        when(tradeRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(testTrade));

        // act
        Trade tradeResult = tradeService.findById(1);

        // assert
        assertEquals(testTrade.getTradeId(), tradeResult.getTradeId(), 0);
    }

    @Test
    public void findById_tradeDoesNotExist_nullReturned() {
        // arrange

        // act
        Trade tradeResult = tradeService.findById(2);

        // assert
        assertNull(tradeResult);
    }

    @Test
    public void createTrade_validTrade_tradeReturned() {
        // arrange
        Trade testTrade2 = new Trade("testAccount", "testType");
        testTrade2.setTradeId(2);

        when(tradeRepositoryMock.save(testTrade2)).thenReturn(testTrade2);

        // act
        Trade tradeResult = tradeService.createTrade(testTrade2);

        // assert
        verify(tradeRepositoryMock, times(1)).save(any(Trade.class));
        assertEquals(testTrade2.getTradeId(), tradeResult.getTradeId(), 0);
    }

    @Test
    public void updateTrade_validTrade_tradeSaved() {
        // arrange
        Trade testTrade2 = new Trade("testAccount", "testType");
        testTrade2.setTradeId(2);
        tradeService.createTrade(testTrade2);
        testTrade2.setAccount("updatedAccount");

        // act
        tradeService.updateTrade(testTrade2);

        // assert
        verify(tradeRepositoryMock, times(2)).save(any(Trade.class));
        assertEquals(testTrade2.getAccount(), "updatedAccount", "updatedAccount");
    }

    @Test
    public void deleteTrade_validTrade_tradeDeleted() {
        // arrange

        // act
        tradeService.deleteTrade(1);

        // assert
        Optional<Trade> trade = tradeRepositoryMock.findById(1);
        assertFalse(trade.isPresent());
    }
}
