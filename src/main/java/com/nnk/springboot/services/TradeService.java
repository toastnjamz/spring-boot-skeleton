package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TradeService {

    private TradeRepository tradeRepository;

    @Autowired
    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    public List<Trade> findAllTrades() {
        return tradeRepository.findAll();
    }

    public Trade findById(Integer id) {
        Optional<Trade> tradeOptional = tradeRepository.findById(id);
        if (tradeOptional.isPresent()) {
            Trade trade = tradeOptional.get();
            return trade;
        }
        return null;
    }

    public Trade createTrade(Trade trade) {
        return tradeRepository.save(trade);
    }

    public void updateTrade(Trade trade) {
        if (tradeRepository.findById(trade.getTradeId()) != null) {
            tradeRepository.save(trade);
        }
    }

    public void deleteTrade(Integer id) {
        if (tradeRepository.findById(id) != null) {
            tradeRepository.deleteById(id);
        }
    }
}
