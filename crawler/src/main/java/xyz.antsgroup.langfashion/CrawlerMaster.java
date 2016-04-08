package xyz.antsgroup.langfashion;

import java.util.Scanner;

/**
 * The main Crawler.
 *
 * @author ants_ypc
 * @version 1.0 4/8/16
 */
public class CrawlerMaster {
    public static void main(String[] args) {
        UserThread userThread = new UserThread();
        Thread thread = new Thread(userThread);
        thread.start();

        // control user crawler to stop
        String command;
        Scanner cin = new Scanner(System.in);
        while ((command = cin.next()) != null) {
            if (command.equals("stop")) {
                userThread.getUserCrawler().setRunning(false);
                break;
            }
        }

    }
}

class UserThread implements Runnable {
    public UserCrawler userCrawler;

    @Override
    public void run() {
        userCrawler = new UserCrawler();
        userCrawler.crawlUsers();
    }

    public UserCrawler getUserCrawler() {
        return userCrawler;
    }
}