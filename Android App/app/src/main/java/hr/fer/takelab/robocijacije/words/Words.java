package hr.fer.takelab.robocijacije.words;

/**
 * Created by Fran on 12/14/2017.
 */

public class Words {
    String[] words = new String[4];
    String answer;
    boolean[] isRevealed = {false, false, false, false};
    boolean solved;

    /**
     * Expects String array size 4
     * @param words words stored
     */
    public Words(String answer, String[] words){
        if(words.length != 4){
            throw new IllegalArgumentException("Expected String array of size 4");
        }

        setWords(words);
        setAnswer(answer);
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    /**
     * Checks whether i-th word is revealed.
     *
     * indexing starts with 1
     *
     * @param i
     * @return true or false
     */
    public boolean checkIfRevealed(int i){
        return isRevealed[i-1];
    }

    /**
     * Set value for <code>isRevealed</code>
     *
     * indexing starts with 1
     *
     * @param i
     * @param value
     */
    public void setRevealed(int i, boolean value){
        if(i<1 || i>4){
            throw new IllegalArgumentException("i should be between 1 and 4 (including)");
        }
        isRevealed[i-1] = value;
    }

    public String[] getWords() {
        return words;
    }

    /**
     * gets i-th word. Indexing starts with 1
     *
     * @param i
     * @return
     */
    public String getWord(int i ){
        return words[i-1];
    }

    public String getAnswer() {
        return answer;
    }

    public void setWords(String[] words) {
        this.words = words;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
