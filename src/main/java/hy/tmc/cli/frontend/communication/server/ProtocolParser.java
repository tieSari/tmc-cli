package hy.tmc.cli.frontend.communication.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProtocolParser {

    public ProtocolParser() {

    }

    public String[] getElements(String userInput) throws ProtocolException {
        List<String> items = new ArrayList<>();
        boolean parsingLongValue = false;
        String multiWordItem = "";
        for (String word : userInput.split(" ")) {
            handleSingleWord(parsingLongValue, word, items, multiWordItem);
        }
        String[] array = new String[items.size()];
        array = items.toArray(array);
        return array;
    }

    private void handleSingleWord(boolean parsingLongValue, String word, List<String> items,
        String multiWordItem) {
        if (parsingLongValue) {
            if (word.contains("}")) {
                items.add(multiWordItem.trim());
            }
        } else if (!word.contains("{")) {
            items.add(word);
        }
    }

    public HashMap<String, String> giveData(String[] userInput, HashMap<String, String> params) {
        int index = 1;
        while (index < userInput.length) {
            String key = userInput[index];
            if (userInput[index].charAt(0) == '-') {
                params.put(key, "");
                index++;
            } else {
                String value = userInput[index + 1].replace("<newline>", "\n");
                params.put(key, value);
                index += 2;
            }
        }
        return params;
    }
}
