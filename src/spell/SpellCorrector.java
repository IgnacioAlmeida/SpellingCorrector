package spell;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

        //System.out.println(trie.toString());
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        //use set or tree set(java has it)
        inputWord = inputWord.toLowerCase(Locale.ROOT);
        TreeSet <String> possibleWords = new TreeSet<>();
        String similarWord = null;
        int similarWordCount = 0;
        int editDistance = 0;

        //If word is in the tree
        if(trie.find(inputWord) != null){
            return inputWord;
        }
        possibleWords.add(inputWord);
        do {
            editDistance++;
            TreeSet<String> wordVariations = new TreeSet<>();
            //If word is not found suggest similar
            for (String it : possibleWords) {
                //Deletion Distance - Ex: Bird - bir, bid, bdr, ird
                for (int i = 0; i < it.length(); i++) {
                    StringBuilder string = new StringBuilder(it);
                    string.deleteCharAt(i);
                    wordVariations.add(string.toString());
                }

                //Transposition Distance - Ex: House - ohuse, huose, hosue, houes
                for (int i = 0; i < it.length() - 1; i++) {
                    StringBuilder string = new StringBuilder(it);
                    char temp = string.charAt(i);
                    char nextChar;
                    nextChar = string.charAt(i + 1);
                    string.setCharAt(i, nextChar);
                    string.setCharAt(i + 1, temp);
                    wordVariations.add(string.toString());
                }

                //Alteration Distance Ex: “top” are “aop”, “bop”, …, “zop”, “tap”, “tbp”, …, “tzp”, “toa”, “tob”, …, and “toz”
                int ABC_LENGTH = 26;
                for (int i = 0; i < it.length(); i++) {
                    StringBuilder string = new StringBuilder(it);
                    for (int j = 0; j < ABC_LENGTH; j++) {
                            string.setCharAt(i, (char) ('a' + j));
                            wordVariations.add(string.toString());
                    }
                }

                //Insertion Distance Ex: “ask” are “aask”, “bask”, “cask”, … “zask”, “aask”, “absk”, “acsk”, … “azsk”, “asak”, “asbk”, “asck”, … “aszk”, “aska”, “askb”, “askc”, … “askz”
                for (int i = 0; i < it.length(); i++) {
                    for (int j = 0; j < ABC_LENGTH; j++) {
                        StringBuilder string = new StringBuilder(it);
                        string.insert(i, (char) ('a' + j));
                        wordVariations.add(string.toString());
                    }
                }
                for(int i = 0; i < ABC_LENGTH; i++){
                    wordVariations.add(it + (char)('a' + i));
                }
            }

            for (String word : wordVariations) {
                INode toReturn = trie.find(word);
                if (toReturn != null) {
                    if(similarWord == null) {
                        similarWord = word;
                        similarWordCount = toReturn.getValue();
                    }
                    else {
                        if (similarWordCount != toReturn.getValue()) {
                            similarWord = word;
                            similarWordCount = toReturn.getValue();
                        }
                    }
                }
            }
           possibleWords = wordVariations;
        }while(similarWord == null && editDistance < 2);

        return similarWord;
    }
}
