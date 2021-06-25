package spell;

public class Node implements INode{
    private int wordCount;//How many times the word shows up
    Node[] children;

    public Node(){
        this.children = new Node[26];
        this.wordCount = 0;
    }

    //It is not the character because the node doesn't know what character it represents. It's its frequency count
    //It will be 0 unless that node is the end node for a word
    @Override
    public int getValue() {
        return wordCount;
    }

    @Override
    public void incrementValue() {
        wordCount++;
    }

    @Override
    public INode[] getChildren() {
        return children;
    }
}
