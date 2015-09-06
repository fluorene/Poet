package com.fluorine.poet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Poet implements Runnable {

    public static void main(String args[]) {
        (new Thread(new RequestOutput())).start();
    }

    /**
     * Runs the Poet.
     */
    public void run() {
        //The sample file
        File sample = new File(Main.sampleFilePath);

        //Lines go in doubles. 14 lines = 7 loops
        for (int i = 0; i < Math.ceil(Main.lines / 2); i++) {

            //Get lines from text sample, put them into a list
            List<String> lines = null;
            try {
                lines = Utils.getLinesFromFile(sample);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //Get the first word to rhyme with
            String firstWordForRhyme = Utils.getRandomLastWord(lines);

            //Build a reverse Markov chain for line 1 based on the text sample
            String chain = null;
            try {
                chain = Utils.buildMarkovChain(firstWordForRhyme, Main.syllables, lines, "Yeah");
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Get words that rhyme with the first word
            List<String> rhymeWords = null;
            try {
                rhymeWords = Utils.getRhyme(firstWordForRhyme);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Find a word that rhymes with the first word that is also in the sample
            String secondWordForRhyme = "be"; //Default
            assert rhymeWords != null;
            for (String word : rhymeWords) { //TODO: Randomize
                if (Utils.sampleContains(word, lines)) {
                    secondWordForRhyme = word;
                    break;
                }
            }

            //Build a reverse Markov chain for line 2 based on the text sample
            String nextChain = null;
            try {
                nextChain = Utils.buildMarkovChain(secondWordForRhyme, Main.syllables, lines, "Yeah");
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Add the two lines to the main poem
            Main.poem += chain + "\n";
            Main.poem += nextChain + "\n";
        }
    }

}
