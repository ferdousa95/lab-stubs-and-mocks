package edu.cmu.cs.analysis;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TwitterBot {

  static boolean debug = false;

  // if something goes wrong, we might see a TwitterException
  public static void main(String... args) {
    // send a tweet
    if (!debug) {
      try {
        // Send Tweet
        Twitter twitter;
        twitter = TwitterFactory.getSingleton();
        // postTweet(twitter, "Hello World!");
        // sendTweet(twitter, "Hello World!");

        // Get tweets from Home Timeline
        // getHomeTimeLine();

        // Seach for tweets
        // searchForTweets("@gdg_nd");

        // Reply to a tweet
        String query_text = "\"your welcome\"";
        String reply = "I believe you meant \"you're\" here?";
        replyToTweet(query_text, reply);

        // Reply with variety
        List<String> searches = new ArrayList<>();
        searches.add("\"your welcome\"");
        searches.add("\"your the\"");
        searches.add("\"your a \"");

        List<String> replies = new ArrayList<>();
        replies.add("I believe you meant \"you're\" here?");
        replies.add(" I've detected the wrong \"you're\". Destroy!");
        replies.add(" No, you are! Seriously. You are. \"You're\".");

        replyToTweetWithVariety(searches, replies);

      } catch (TwitterException e) {
        e.printStackTrace();
      }
    } else {
      // print a message so we know when it finishes
      System.out.println("Debug Mode Enabled!");
    }
  }

  public static Status postTweet(Twitter twitter, String text) throws TwitterException {
    return sendTweet(twitter, text);
  }

  private static Status sendTweet(Twitter twitter, String text) throws TwitterException {
    // access the twitter API using your twitter4j.properties file
    // The factory instance is re-useable and thread safe.

    Logger logger = Logger.getLogger(TwitterBot.class.getName());

    int count = 0;
    int maxtries = 3;
    Status status = null;

    while(count < maxtries) {
      try {
        status = twitter.updateStatus(text);
        System.out.println("Successfully updated the status to [" + status.getText() + "].");
        return status;
      }
      catch (TwitterException e) {
        logger.log(Level.WARNING, "Failed in posting a Tweet, TwitterRelated issue");
        count++;
      }
      catch(Exception e) {
        logger.log(Level.WARNING, "Failed in posting a Tweet");
        count++;
      }
    }

    logger.log(Level.SEVERE, "Error in posting a new Tweet");
    return null;
  }



  private static void getHomeTimeLine() throws TwitterException {
    Twitter twitter = TwitterFactory.getSingleton();
    List<Status> statuses = null;
    statuses = twitter.getHomeTimeline();

    System.out.println("Showing home timeline.");
    if (statuses != null) {
      for (Status status : statuses) {
        System.out.println(status.getUser().getName() + ":" + status.getText());
      }
    }
  }

  public static void searchForTweetsWrapper(Twitter twitter, String query_text) throws TwitterException {
    searchForTweets(twitter, query_text);
  }
  private static void searchForTweets(Twitter twitter, String query_text) throws TwitterException {
    Query query = new Query(query_text);
    QueryResult result;
    result = twitter.search(query);
    for (Status status : result.getTweets()) {
      System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
    }
  }

  private static void replyToTweet(String query_text, String reply) throws TwitterException {
    // access the twitter API using your twitter4j.properties file
    Twitter twitter = TwitterFactory.getSingleton();

    // create a new search
    Query query = new Query(query_text);

    // get the results from that search
    QueryResult result = twitter.search(query);

    // get the first tweet from those results
    Status tweetResult = result.getTweets().get(0);

    // reply to that tweet
    StatusUpdate statusUpdate =
        new StatusUpdate(".@" + tweetResult.getUser().getScreenName() + " " + reply);
    statusUpdate.inReplyToStatusId(tweetResult.getId());
    Status status = twitter.updateStatus(statusUpdate);
  }

  private static void replyToTweetWithVariety(List<String> searches, List<String> replies)
      throws TwitterException {
    // access the twitter API using your twitter4j.properties file
    Twitter twitter = TwitterFactory.getSingleton();

    // keep tweeting forever
    while (true) {
      // create a new search, chosoe from random searches
      Query query = new Query(searches.get((int) (searches.size() * Math.random())));

      // get the results from that search
      QueryResult result = twitter.search(query);

      // get the first tweet from those results
      Status tweetResult = result.getTweets().get(0);

      // reply to that tweet, choose from random replies
      StatusUpdate statusUpdate =
          new StatusUpdate(
              ".@"
                  + tweetResult.getUser().getScreenName()
                  + replies.get((int) (replies.size() * Math.random())));
      statusUpdate.inReplyToStatusId(tweetResult.getId());
      Status status = twitter.updateStatus(statusUpdate);
      System.out.println("Sleeping.");

      // go to sleep for an hour
      try {
        Thread.sleep(60 * 60 * 1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
