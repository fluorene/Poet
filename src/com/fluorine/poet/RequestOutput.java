package com.fluorine.poet;

public class RequestOutput implements Runnable {

    public static void main(String args[]) {
        (new Thread(new RequestOutput())).start();
    }

    public void run() {
        while (true) {
            System.out.println("There have been " + Main.requests + " requests so far.");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
