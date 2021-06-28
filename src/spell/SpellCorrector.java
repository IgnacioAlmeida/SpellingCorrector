package spell;


import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpellCorrector implements ISpellCorrector{
    private Trie trie = new Trie();
    private int ABC_LENGTH = 26;
    private char A = 97;

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
        TreeSet <String> distanceOneWords = new TreeSet<>();
        TreeSet <String> distanceTwoWords = new TreeSet<>();
        inputWord = inputWord.toLowerCase(Locale.ROOT);
        String word = null;

        //If word is in the tree
        if(trie.find(inputWord) != null){
            return inputWord;
        }
        //If word is not found suggest similar
        else {
                //Deletion Distance - Ex: Bird - bir, bid, bdr, ird
                deletion(inputWord, distanceOneWords);

                //Transposition Distance - Ex: House - ohuse, huose, hosue, houes
                transposition(inputWord, distanceOneWords);

                //a b c d e f g h i j k l m n o p q r s
                //Alteration Distance Ex: “top” are “aop”, “bop”, …, “zop”, “tap”, “tbp”, …, “tzp”, “toa”, “tob”, …, and “toz”
                //Zupem: i(0): aupem,...,j(18):supem,.., zupem /
                alteration(inputWord, distanceOneWords);

                //Insertion Distance Ex: “ask” are “aask”, “bask”, “cask”, … “zask”, “aask”, “absk”, “acsk”, … “azsk”, “asak”, “asbk”, “asck”, … “aszk”, “aska”, “askb”, “askc”, … “askz”
                insertion(inputWord, distanceOneWords);

                //check to see if we can findWordFrom Distance 1 set
                word = findWordFrom(distanceOneWords);

                //check to see if we can findWordFrom Distance 2 set only if we couldn't find the word in Distance 1 set
            if(word == null){
                for(String it : distanceOneWords){
                    deletion(it, distanceTwoWords);
                    transposition(it, distanceTwoWords);
                    alteration(it, distanceTwoWords);
                    insertion(it, distanceTwoWords);
                }
                word = findWordFrom(distanceTwoWords);
            }
        }
        return word;
    }

    private String findWordFrom(TreeSet<String> possibleWords){
        String word = null;
        int max = 0;

        for(String it : possibleWords) {
            Node node = (Node) trie.find(it);
            if (trie.find(it) != null && node.getValue() > max) {
                max = node.getValue();
                word = it;
            }
        }
        return word;
    }

    private void deletion(String inputWord, TreeSet<String> possibleWords) {
            for (int i = 0; i < inputWord.length(); i++) {
                StringBuilder string = new StringBuilder(inputWord);
                string.deleteCharAt(i);
                possibleWords.add(string.toString());
            }

    }

    private void transposition(String inputWord, TreeSet<String> possibleWords){
            for (int i = 0; i < inputWord.length() - 1; i++) {
                StringBuilder string = new StringBuilder(inputWord);
                char temp = string.charAt(i);
                char nextChar;
                nextChar = string.charAt(i + 1);
                string.setCharAt(i, nextChar);
                string.setCharAt(i + 1, temp);
                possibleWords.add(string.toString());
            }
    }

    private void alteration(String inputWord, TreeSet<String> possibleWords){
            for (int i = 0; i < inputWord.length(); i++) {
                StringBuilder string = new StringBuilder(inputWord);
                for (int j = 0; j < ABC_LENGTH; j++) {
                    string.setCharAt(i, (char) (A + j));
                    possibleWords.add(string.toString());
                }
            }

    }

    private void insertion(String inputWord, TreeSet<String> possibleWords){
            for (int i = 0; i < inputWord.length(); i++) {
                for (int j = 0; j < ABC_LENGTH; j++) {
                    StringBuilder string = new StringBuilder(inputWord);
                    string.insert(i, (char) (A + j));
                    possibleWords.add(string.toString());
                }
            }
            for (int i = 0; i < ABC_LENGTH; i++) {
                possibleWords.add(inputWord + (char) (A + i));
            }
        }

}

