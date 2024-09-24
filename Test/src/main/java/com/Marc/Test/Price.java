package com.Marc.Test;

import com.Marc.Test.repository.entity.Binance;
import com.Marc.Test.repository.entity.Huobi;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class Price {
    private final RestClient restClient = RestClient.builder().build();

    public List<Binance> Binance()
    {
        JsonNode getData = restClient.get().uri("https://api.binance.com/api/v3/ticker/bookTicker").retrieve().body(JsonNode.class);
        return convert(getData, Binance.class);
    }

    public List<Huobi> Huobi()
    {
        JsonNode getData = restClient.get().uri("https://api.huobi.pro/market/tickers").retrieve().body(JsonNode.class);
        return convert(getData.get("data"), Huobi.class);
    }

    public List convert(JsonNode node, Class<?> classType)
    {
        List ret = new ArrayList<>();

        if (node.isNull())
        {
            return ret;
        }

        ObjectMapper om = new ObjectMapper();

        for (JsonNode element : node)
        {
            ret.add(om.convertValue(element, classType));
        }

        return ret;
    }
}
