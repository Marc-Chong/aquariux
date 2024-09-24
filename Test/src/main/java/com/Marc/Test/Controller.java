package com.Marc.Test;

import com.Marc.Test.repository.BalanceRepository;
import com.Marc.Test.repository.HistoryRepository;
import com.Marc.Test.repository.PricingRepository;
import com.Marc.Test.repository.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    PricingRepository pricingRepository;

    @Autowired
    BalanceRepository balanceRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    Methods methods;

    @Autowired
    Price price;

    public void refreshPrice()
    {
        methods.refreshPrice(pricingRepository, price);
    }

    @GetMapping("/price")
    List<Pricing> bestPrice()
    {
        return pricingRepository.findAll();
    }

    @GetMapping("/price/{symbol}")
    Object bestPriceBySymbol(@PathVariable String symbol)
    {
        String caps = symbol.toUpperCase();
        return pricingRepository.findById(caps).isPresent() ? pricingRepository.findById(caps) : "Cannot find information on this symbol.";
    }

    @GetMapping("/trade/{mySymbol}/{otherSymbol}/{buySell}/{quantity}")
    String bestTrade(@PathVariable String mySymbol, @PathVariable String otherSymbol,
                     @PathVariable String buySell, @PathVariable String quantity)
    {
        mySymbol = mySymbol.toUpperCase();
        otherSymbol = otherSymbol.toUpperCase();
        buySell = buySell.toUpperCase();

        String ret = methods.validateParameters(mySymbol, otherSymbol, buySell, quantity, pricingRepository);

        if (!ret.isEmpty())
        {
            return ret;
        }

        Pricing pricing = methods.findPricing(mySymbol, otherSymbol, pricingRepository);
        Double value = Double.parseDouble(quantity);
        ret = methods.tradeOperation(mySymbol, otherSymbol, buySell,
                value, pricing, balanceRepository, historyRepository);

        return ret;
    }

    @GetMapping("/balance")
    List<Balance> cryptoBalance()
    {
        return balanceRepository.findAll();
    }

    @GetMapping("/history")
    List<History> tradingHistory()
    {
        return historyRepository.findAll();
    }
}
