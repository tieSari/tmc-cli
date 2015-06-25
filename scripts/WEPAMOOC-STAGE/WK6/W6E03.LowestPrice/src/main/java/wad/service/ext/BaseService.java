package wad.service.ext;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseService {

    private final Map<String, Double> quotes;

    public BaseService() {
        this.quotes = new TreeMap<>();
    }

    public abstract String getName();

    public double getLowestPrice(String item) {
        if (this.quotes.containsKey(item)) {
            return this.quotes.get(item);
        }

        try {
            Thread.sleep(500 + Math.abs(new Random().nextLong()) % 500);
        } catch (InterruptedException ex) {
            Logger.getLogger(BaseService.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.quotes.put(item, 9 * new Random().nextDouble() + 1);
        return this.quotes.get(item);
    }

    public Map<String, Double> getQuotes() {
        return quotes;
    }
}
