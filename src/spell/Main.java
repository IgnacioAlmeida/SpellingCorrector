package spell;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * A simple main class for running the spelling corrector. This class is not
 * used by the passoff program.
 */
public class Main {

	/**
	 * Give the dictionary file name as the first argument and the word to correct
	 * as the second argument.
	 */
	// Prints data in file
	public static void printFile(File file) throws IOException{
		Scanner x = new Scanner(file);
		//hasNext is a built in method that is going to loop through x until end of file
		while(x.hasNext()){
			String word = x.next();
			System.out.printf("%s ", word);
		}
		x.close();
	}

	//puts data into variable
	public static void processFile(File file) throws IOException {
		Scanner scanner = new Scanner(file);
		scanner.useDelimiter("((#[^\\n]*\\n)|(\\s+))+");

		while(scanner.hasNext()) {
			String str = scanner.next();
			Trie trie = new Trie();
			trie.add(str);
			//System.out.println("\nnodeCount: " + trie.getNodeCount() + "\nwordCount: " + trie.getWordCount());
			//System.out.println("\ntrie find: " + trie.find("yeah"));
			trie.toString();
		}

	}
	public static void main(String[] args) throws IOException {
		
		String dictionaryFileName = args[0];
		String inputWord = args[1];

		//Testing getting data from file
		printFile(new File(dictionaryFileName));
		System.out.println("\n");
//		processFile(new File(dictionaryFileName));
//		System.out.println("\n");
		//how to print a character by adding from a
		//System.out.println("\n" + (char)('a' + 2));
		//
        //Create an instance of your corrector here
        //
		ISpellCorrector corrector = new SpellCorrector();
		corrector.useDictionary(dictionaryFileName);
		String suggestion = corrector.suggestSimilarWord(inputWord);
		if (suggestion == null) {
		    suggestion = "No similar word found";
		}

		System.out.println("Suggestion is: " + suggestion);
	}

}
