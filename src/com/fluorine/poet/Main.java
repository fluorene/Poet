package com.fluorine.poet;

import java.io.IOException;

public class Main {
    /*
    Version 1.0.

    ABBA Example:

    Yeah the Dancing Queen, young and me
    Yeah Yeah Yeah Yeah Ma ma be
    Yeah defeated, you and I can do
    Yeah digging the pretty birds have to
    Yeah to be around
    Yeah easy, I don't worry, I found
    Yeah me tell you see what you
    Yeah Yeah Yeah Yeah Yeah Try to
    Yeah They'll be around
    Yeah Yeah It's simple and I found
    Yeah this time of your mother know?
    Yeah Yeah Yeah Yeah Yeah There's no
    Yeah But you honey
    Yeah and it's funny
    Yeah can I feel the Rio Grande?
    Yeah Yeah If you go this land

    Comments:
    First of all, the syllable number is incorrect.
    Second of all, nuzhno dobavit' udareniya!
    Third of all, way too many "Yeah"s.
     */

    public static int requests = 0;

    //Text sample of songs
    public static String sampleFilePath = "C:\\Users\\Irochka\\workspace\\and\\Poet\\files\\abba.txt";

    //Poe variables
    public static int lines = 16;
    public static int syllables = 7; //Syllables per line. 7 is really common for ABBA =)

    public static String poem = "";

    public static void main(String[] args) throws IOException {
        new Poet().run();
        //new RequestOutput().run();
        System.out.println(poem);
    }
}