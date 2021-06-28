package spell;

import java.util.Locale;

public class Trie implements ITrie{
    private int nodeCount;
    private int wordCount;
    private Node root;
    int A = 97; //Decimal value of the character 'a'

    public Trie(){
        root = new Node();
        nodeCount = 1;
        wordCount = 0;
    }

    @Override
    public void add(String word) {
        word.toLowerCase();
        INode current = root;
        //Iterate through each character of the word
        for(int i = 0; i < word.length(); i++){
            int index = word.charAt(i) - A;
            INode[] children = current.getChildren();
            //We create the node if it is empty at the Index
            if(children[index] == null) {
                children[index] = new Node();
                nodeCount++;
            }
            //If node already exists or after it got created, set current so it goes to the next one
            current = current.getChildren()[index];

            //If we are at the last character of the word
            if(i == word.length() - 1){
                //Increments the times that the word shows up
                current.incrementValue();
                //But only increment the count of unique words if it is the first time that it shows up
                if(current.getValue() == 1) {
                    wordCount++;
                }
            }
        }
        //System.out.println("nodeCount: " + nodeCount + "\nwordCount " + wordCount + "\n");
    }

    @Override
    public INode find(String word) {
        INode current = root;

        //Iterate through the trie checking children by children from the beginning
        for(int i = 0; i < word.length(); i++){
            int index = word.charAt(i) - A;
            INode[] children = current.getChildren();;
            //Assign the current node to its children if it has a character
            if(children[index] != null){
                current = children[index];
            }
            //If it doesn't have a char, leave the loop
            else{
                break;
            }
            //If it was a char and that happens to be the last of the word, return that word
            if(i == word.length() - 1 && current.getValue() > 0) {
                return current;
            }
        }
        //The word isn't in the trie
        return null;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    // Traverse trie in alphabetical order - Recursive traversal
    @Override
    public String toString() {
        //deliver the word to the node as we are traversing
        StringBuilder word = new StringBuilder();
        //It has a list of all the words found
        StringBuilder words = new StringBuilder();
        toStringTraversal(root, word, words);
        return words.toString();
    }

    private void toStringTraversal(INode node, StringBuilder word, StringBuilder words){
        INode[] children = node.getChildren();

        for(int i = 0; i < children.length; i++){
            if(children[i] != null){
                //The value of 'a' will be 97 so if we add i and convert that to a char it will give us the node letter
                word.append((char)(i + A));
                //check to see if we find the node that has a count greater than 0 meaning it's the end of the word
                if(children[i].getValue() > 0){
                    words.append(word.toString());
                    words.append('\n');
                }
                //Call recursively for each word in the trie
                toStringTraversal(children[i], word, words);
                //todo delete one char
                word.deleteCharAt(word.length() - 1);
            }
        }
    }

    @Override
    public int hashCode() {
        int hashValue = 31 * wordCount * nodeCount;

        for(int i = 0; i < root.getChildren().length; i++){
            if(root.getChildren()[i] != null) {
                hashValue *= i;
            }
        }

        return hashValue;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }

        if(!(obj instanceof Trie)){
            return false;
        }

        Trie param = (Trie) obj;

        if(this.nodeCount != param.nodeCount){
            return false;
        }

        if(this.wordCount != param.wordCount){
            return false;
        }

        return equalsTraversal(root, param.root);

    }

    public boolean equalsTraversal(INode current, INode param) {
        if (current.getValue() != param.getValue()) {
            return false;
        }

        for (int i = 0; i < current.getChildren().length; i++) {
            if (current.getChildren()[i] != null && param.getChildren()[i] != null) {
                if (!(equalsTraversal(current.getChildren()[i], param.getChildren()[i]))) {
                    return false;
                }
                else{
                    return true;
                    }
            }
        }
        return true;
    }
}
