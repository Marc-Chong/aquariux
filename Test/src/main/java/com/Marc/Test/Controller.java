package com.Marc.Test;

import com.Marc.Test.repository.PricingRepository;
import com.Marc.Test.repository.entity.Binance;
import com.Marc.Test.repository.entity.Huobi;
import com.Marc.Test.repository.entity.Pricing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    PricingRepository pricingRepository;
    @Autowired
    Price price;
    @GetMapping("/refresh")
    void refreshPrice()
    {
        pricingRepository.deleteAll();
        List<Binance> binanceList = price.Binance();
        List<Huobi> huobiList = price.Huobi();

        //check for overlaps to compare and take the better price
        for (Binance binance : binanceList)
        {
            for (Huobi huobi : huobiList)
            {
                if (binance.getSymbol().equalsIgnoreCase(huobi.getSymbol()))
                {
                    Pricing pricing = new Pricing(binance.getSymbol().toUpperCase(),
                            binance.getAskPrice() < huobi.getAsk() ? binance.getAskPrice() : huobi.getAsk(),
                            binance.getBidPrice() > huobi.getBid() ? binance.getBidPrice() : huobi.getBid());
                    pricingRepository.save(pricing);
                    break;
                }
            }
        }

        //add the rest of the lists from both skipping the already matched symbols since they are primary keys
        for (Binance binance : binanceList)
        {
            pricingRepository.save(new Pricing(binance.getSymbol().toUpperCase(), binance.getAskPrice(), binance.getBidPrice()));
        }

        for (Huobi huobi : huobiList)
        {
            pricingRepository.save(new Pricing(huobi.getSymbol().toUpperCase(), huobi.getAsk(), huobi.getBid()));
        }
    }

    @GetMapping("/price")
    List<Pricing> bestPrice()
    {
        return pricingRepository.findAll();
    }

    @GetMapping("/trade")
    List<Object> bestTrade()
    {
        System.out.println("Best price.");
        return null;
    }

    @GetMapping("/balance")
    List<Object> cryptoBalance()
    {
        System.out.println("Best price.");
        return null;
    }

    @GetMapping("/history")
    List<Object> tradingHistory()
    {
        System.out.println("Best price.");
        return null;
    }
}
