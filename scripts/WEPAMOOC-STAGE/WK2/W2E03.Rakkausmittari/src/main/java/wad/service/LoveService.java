package wad.service;

import org.springframework.stereotype.Service;
import wad.domain.Pair;

public class LoveService {

    private int matched = 0;

    public int countMatch(Pair pair) {
        String first = pair.getNameOne();
        String second = pair.getNameTwo();
        if (first.contains("tieto")
                || second.contains("tieto")) {
            return 99;
        }

        int minLength = Math.min(first.length(), second.length());

        int result = 0;
        for (int i = 0; i < minLength; i++) {
            result += (first.charAt(i) * second.charAt(i));
        }

        result += 42;

        matched++;
        return result % 101;
    }

    public Integer getMatched() {
        return matched;
    }

}
