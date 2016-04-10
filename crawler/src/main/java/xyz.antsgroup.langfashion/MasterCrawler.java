package xyz.antsgroup.langfashion;

import java.util.Scanner;

/**
 * The main Crawler.
 *
 * @author ants_ypc
 * @version 1.0 4/8/16
 */
public class MasterCrawler {

    public static void main(String[] args) throws InterruptedException {
        UserThread userThread = new UserThread();
        Thread uThread = new Thread(userThread);
        uThread.start();

        // if there is no user in local database. Here trouble goes.
        Thread.sleep(10000L);
        RepoThread repoThread = new RepoThread();
        Thread rThread = new Thread(repoThread);
        rThread.start();


        // control user crawler to stop
        String command;
        Scanner cin = new Scanner(System.in);
        while ((command = cin.next()) != null) {
            if (command.equals("repostop") && rThread.isAlive()) {
                repoThread.getRepoCrawler().setRunning(false);
                System.out.println("repo thread is stopping...");
            } else if (command.equals("userstop") && uThread.isAlive()) {
                userThread.getUserCrawler().setRunning(false);
                System.out.println("user thread is stopping...");
            }
            if (!rThread.isAlive() && !uThread.isAlive()) break;
        }
    }
}

class UserThread implements Runnable {
    private UserCrawler userCrawler;

    @Override
    public void run() {
        userCrawler = new UserCrawler();
        userCrawler.crawlUsers();
    }

    public UserCrawler getUserCrawler() {
        return userCrawler;
    }
}

class RepoThread implements Runnable {
    private RepoCrawler repoCrawler;

    @Override
    public void run() {

            repoCrawler = new RepoCrawler();
            repoCrawler.crawlRepos();

    }

    public RepoCrawler getRepoCrawler() {
        return repoCrawler;
    }
}