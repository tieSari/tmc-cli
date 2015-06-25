package wad.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wad.service.ext.BaseService;

@Service
public class QuoteService {

    // i guess you didn't know that you can do this as well :)
    @Autowired
    private List<BaseService> services;

    public BaseService getLowestPriceService(final String item) {

BaseService lowestPriceService = null;
Double lowest = null;

for (BaseService service : services) {
double price = service.getLowestPrice(item);
if (lowest == null || lowest > price) {
lowest = price;
lowestPriceService = service;
}
}

return lowestPriceService;

    }
}
