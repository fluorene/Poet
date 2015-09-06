package com.fluorine.poet;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Utils {
    static Random rand = new Random();

    /**
     * Gets a rhyme for a word.
     * @param word - word to find a rhyme for.
     * @return - list of words that rhyme with the parameter. Can be empty.
     * @throws IOException
     */
    public static List<String> getRhyme(String word) throws IOException {
        Main.requests++;

        List<String> rhymes = new ArrayList<String>();

        URL url = new URL("http://rhymebrain.com/talk?function=getRhymes&word=" + word);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            String[] parts = inputLine.split("\"word\":\"");
            if (parts.length > 0) {
                String rest = parts[1];
                String[] moreParts = rest.split("\",\"freq\":");
                String realWord = moreParts[0];
                rhymes.add(realWord);
            } else {
                rhymes.add("Yeah");
            }
        }
        in.close();

        return rhymes;
    }

    /**
     * Builds a reverse Markov chain.
     * @param word - Last word of line
     * @param length - Maximum number of syllables in the line
     * @param lines - The text sample
     * @param filler - A word to use, such as "Hey", "Nah", "Yeah", or "Gvprtskvnis" ;), when a syllable is needed
     * @return the Markov chain
     * @throws IOException
     */
    public static String buildMarkovChain(String word, int length, List<String> lines, String filler) throws IOException {
        List<String> words = new ArrayList<String>();
        String output = "";
        int syllables = getSyllables(word);

        String currentWord = word;
        while (syllables < length) {
//            System.out.println("syllables: " + syllables);
            List<String> previousWords = getPreviousWords(currentWord, lines);
            if (previousWords.size() < 1) {
                for (int i = syllables; i < length; i++) {
                    words.add("Yeah");
                }
                break;
            }
            int which = randInt(0, previousWords.size() - 1);
            String previousWord = previousWords.get(which);
            if (syllables + getSyllables(word) >= length) {
                for (int i = syllables; i < length; i++) {
                    words.add("Yeah");
                }
            } else {
                words.add(previousWord);
            }
            syllables += getSyllables(word);
            currentWord = previousWord;
        }
        ;

        Collections.reverse(words);

        words.add(word);

        for (String cWord : words) {
            output += (cWord + " ");
        }

        output = output.substring(0, output.length() - 1);

        return output;
    }

    /**
     * Gets the words that come before a word.
     * @param word - the word to look before
     * @param lines - the text sample
     * @return - a list of the words that come before the parameter word
     */
    public static List<String> getPreviousWords(String word, List<String> lines) {
        List<String> previousWords = new ArrayList<String>();
        for (String line : lines) {
            if (line.toLowerCase().contains(word.toLowerCase())) {
                int pos = line.indexOf(word);
                if (pos > 0 && (line.length() - word.length() == pos || line.substring(pos + word.length(), pos + word.length() + 1).equals(" ") || line.substring(pos + word.length(), pos + word.length() + 1).equals("."))) {
                    String before = line.substring(0, pos - 1);
                    String[] parts = before.split(" ");
                    String lastPart = parts[parts.length - 1];
                    previousWords.add(lastPart);
                }
            }
        }
        return previousWords;
    }

    /**
     * Gets a last word from a random line in the given text sample
     * @param lines - text sample
     * @return - last word from a random line
     */
    public static String getRandomLastWord(List<String> lines) {
        List<String> lastWords = new ArrayList<String>(); //Ominous!
        for (String line : lines) {
            lastWords.add(getLastWord(line));
        }
        int which = randInt(0, lastWords.size() - 1);
        return lastWords.get(which).replaceAll("\\.", "");
    }

    /**
     * Gets lines from a file.
     * @param file - the file to get lines from.
     * @return - a list of the lines in the file.
     * @throws FileNotFoundException - sucks.
     */
    public static List<String> getLinesFromFile(File file) throws FileNotFoundException {
        String token1 = "";

        Scanner inFile1 = new Scanner(file).useDelimiter("\r\n");

        List<String> temps = new ArrayList<String>();

        // while loop
        while (inFile1.hasNext()) {
            // find next line
            token1 = inFile1.next();
            if (!token1.equals("")) {
                temps.add(token1);
            }
        }
        inFile1.close();

        return temps;
    }

    /**
     * Gets the last word of a line.
     * @param line - the line.
     * @return - the last word.
     */
    public static String getLastWord(String line) {
        return line.substring(line.lastIndexOf(" ") + 1);
    }

    /**
     * Gets the number of syllables in a word.
     * @param word - the word.
     * @return - the number of syllables.
     * @throws IOException
     */
    public static int getSyllables(String word) throws IOException {
        Main.requests++;
        int syllables = 0;
        URL url = new URL("http://rhymebrain.com/talk?function=getWordInfo&word=" + word);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            String[] parts = inputLine.split("\"syllables\":\"");
            String rest = parts[1];
            String[] moreParts = rest.split("\"}");
            String syllableNYeah = moreParts[0];
            syllables = Integer.parseInt(syllableNYeah);
        }
        in.close();

        return syllables;
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Lol #toolazytodocument but yeah it's true if the sample contains the word otherwise 'tis false
     * @param word - the word to look for
     * @param lines - the text sample to look in
     * @return - true if lines contains word (as a separate word); otherwise false
     */
    public static boolean sampleContains(String word, List<String> lines) {
        for (String line : lines) {
            if (line.toLowerCase().contains(word.toLowerCase())) {
                int pos = line.indexOf(word);
//                System.out.println("Yay! The word is " + word + " at line \"" + line + "\" and pos " + pos + ".");
                //If this word is a separate word
                if (pos == 0) {
                    if (line.substring(pos + word.length(), pos + word.length() + 1).equals(" ")) {
                        return true;
                    }
                } else if (pos == line.length() - word.length()) {
//                    System.out.println("END OF LINE");
                    if (line.substring(pos - 1, pos).equals(" ")) {
                        return true;
                    }
                } else {
                    if (line.substring(pos + word.length(), pos + word.length() + 1).equals(" ") && line.substring(pos - 1, pos).equals(" ")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
