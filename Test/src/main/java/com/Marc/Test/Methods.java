package com.Marc.Test;

import com.Marc.Test.repository.BalanceRepository;
import com.Marc.Test.repository.HistoryRepository;
import com.Marc.Test.repository.PricingRepository;
import com.Marc.Test.repository.entity.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class Methods {

    void refreshPrice(PricingRepository pricingRepository, Price price)
    {
        pricingRepository.deleteAll();
        List<Binance> binanceList = price.Binance();
        List<Huobi> huobiList = price.Huobi();
        List<String> matchedSymbols = new ArrayList<>();

        //check for overlaps to compare and take the better price
        for (Binance binance : binanceList)
        {
            String binanceSymbol = binance.getSymbol().toUpperCase();

            for (Huobi huobi : huobiList)
            {
                if (binance.getSymbol().equalsIgnoreCase(huobi.getSymbol()))
                {
                    Pricing pricing = new Pricing(binanceSymbol,
                            binance.getAskPrice() < huobi.getAsk() ? binance.getAskPrice() : huobi.getAsk(),
                            binance.getBidPrice() > huobi.getBid() ? binance.getBidPrice() : huobi.getBid());
                    pricingRepository.save(pricing);
                    matchedSymbols.add(binanceSymbol);
                    break;
                }
            }
        }

        //go through the rest of the list that have no overlaps
        for (Binance binance : binanceList)
        {
            String binanceSymbol = binance.getSymbol().toUpperCase();

            if (!matchedSymbols.contains(binanceSymbol)) {
                pricingRepository.save(new Pricing(binanceSymbol, binance.getAskPrice(), binance.getBidPrice()));
            }
        }

        for (Huobi huobi : huobiList)
        {
            String huobiSymbol = huobi.getSymbol().toUpperCase();

            if (!matchedSymbols.contains(huobiSymbol)) {
                pricingRepository.save(new Pricing(huobiSymbol, huobi.getAsk(), huobi.getBid()));
            }
        }
    }

    String validateParameters(String mySymbol, String otherSymbol, String buySell, String quantity,
                              PricingRepository pricingRepository)
    {
        String ret = "";

        if (pricingRepository.findById(mySymbol + otherSymbol).isEmpty() &&
                pricingRepository.findById(otherSymbol + mySymbol).isEmpty())
        {
            ret += "Cannot find information on this pairing. ";
        }

        if (!buySell.equalsIgnoreCase("buy") && !buySell.equalsIgnoreCase("sell"))
        {
            ret += "Please specify buy or sell. ";
        }

        try {
            Double.parseDouble(quantity);
        }

        catch (Exception e)
        {
            ret += "Please input a valid number for quantity. ";
        }

        return ret;
    }

    Pricing findPricing(String mySymbol, String otherSymbol, PricingRepository pricingRepository)
    {
        if (pricingRepository.findById(mySymbol + otherSymbol).isEmpty())
        {
            Pricing price = pricingRepository.findById(otherSymbol + mySymbol).get();
            Pricing ret = new Pricing(price);
            ret.setAskPrice(1.0 / ret.getAskPrice());
            ret.setBidPrice(1.0 / ret.getBidPrice());
            return ret;
        }

        return pricingRepository.findById(mySymbol + otherSymbol).get();
    }

    String tradeOperation(String mySymbol, String otherSymbol, String buySell, Double value, Pricing pricing,
                          BalanceRepository balanceRepository, HistoryRepository historyRepository)
    {
        return buySell.equalsIgnoreCase("BUY") ?
        buy(mySymbol, otherSymbol, value, pricing, balanceRepository, historyRepository)
    : sell(mySymbol, otherSymbol, value, pricing, balanceRepository, historyRepository);
    }

    String buy(String mySymbol, String otherSymbol, Double value, Pricing pricing,
               BalanceRepository balanceRepository, HistoryRepository historyRepository)
    {
        String ret = "";

        if (balanceRepository.findById(otherSymbol).isPresent()) {
            Balance balance = balanceRepository.findById(otherSymbol).get();

            Double totalPrice = value * pricing.getAskPrice();

            if (balance.getBalance() >= totalPrice)
            {
                ret += "Bought " + value + " of " + mySymbol + " for " + totalPrice + " of " + otherSymbol;
                balance.setBalance(balance.getBalance() - totalPrice);
                balanceRepository.save(balance);
                balanceRepository.save(findOtherBalanceIfExists(balanceRepository, mySymbol, value));
                historyRepository.save(new History(LocalDateTime.now(), "BUY", totalPrice, otherSymbol, value, mySymbol));
            }

            else
            {
                ret += "Not enough remaining balance in " + otherSymbol;
            }
        }

        else
        {
            ret += "Unable to find symbol " + otherSymbol + " in user balance. ";
        }

        return ret;
    }

    String sell(String mySymbol, String otherSymbol, Double value, Pricing pricing,
                BalanceRepository balanceRepository, HistoryRepository historyRepository)
    {
        String ret = "";

        if (balanceRepository.findById(mySymbol).isPresent())
        {
            Balance balance = balanceRepository.findById(mySymbol).get();

            if (balance.getBalance() >= value)
            {
                Double obtainedValue = value * pricing.getBidPrice();
                ret += "Sold " + value + " of " + mySymbol + " for " + obtainedValue + " of " + otherSymbol;

                balance.setBalance(balance.getBalance() - value);
                balanceRepository.save(balance);
                balanceRepository.save(findOtherBalanceIfExists(balanceRepository, otherSymbol, obtainedValue));
                historyRepository.save(new History(LocalDateTime.now(), "SELL", value, mySymbol, obtainedValue, otherSymbol));
            }

            else
            {
                ret += "Not enough remaining balance in " + mySymbol;
            }
        }

        else
        {
            ret += "Unable to find symbol " + mySymbol + " in user balance. ";
        }

        return ret;
    }

    //if the currency exists in balance then we add to the existing currency
    Balance findOtherBalanceIfExists(BalanceRepository balanceRepository, String otherSymbol, Double otherValue)
    {
        if (balanceRepository.findById(otherSymbol).isPresent())
        {
            Balance ret = balanceRepository.findById(otherSymbol).get();
            ret.setBalance(ret.getBalance() + otherValue);
            return ret;
        }

        return new Balance(otherSymbol, otherValue);
    }
}
