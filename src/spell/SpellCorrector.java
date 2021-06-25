package spell;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class SpellCorrector implements ISpellCorrector{
    private Trie trie = new Trie();
    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File file = new File(dictionaryFileName);
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("((#[^\\n]*\\n)|(\\s+))+");


        while(scanner.hasNext()) {
            String str = scanner.next();
            trie.add(str);
        }

        System.out.println(trie.toString());
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        //use set or tree set(java has it)
        return null;
    }
}
