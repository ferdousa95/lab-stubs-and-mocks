package edu.cmu.cs.analysisTest;

import edu.cmu.cs.analysis.TwitterBot;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;


public class TestTwitterBot {

    @Test
    public void testPostTweet() throws TwitterException {
        // Initiation
        String text = "Not Hello World";
        Twitter twittermock = mock(Twitter.class);
        Status statusmock = mock(Status.class);
        Status returnStatusmock;

        //setting replacements
        when(twittermock.updateStatus(text)).thenReturn(statusmock);
        when(statusmock.getText()).thenReturn(text);

        returnStatusmock = TwitterBot.postTweet(twittermock, text);

        assertEquals(returnStatusmock.getText(), statusmock.getText());
    }

    @Test
    public void testPostTweetRobustness() throws TwitterException {
        // Initialiation
        String text = "Not Hello World";
        Twitter twittermock = mock(Twitter.class);
        Status statusmock = mock(Status.class);
        Status returnStatusmock;


        //setting replacements
        when(twittermock.updateStatus(text)).thenReturn(statusmock);
        when(statusmock.getText()).thenReturn(text);

        returnStatusmock = TwitterBot.postTweet(twittermock, null); // the program should break with null pointer exception

        assertNull(returnStatusmock); // Return is null after trying 3 times. so if it returns NUll, means the program
                                      // tried 3 times and still could not post the tweet.
    }

//    @Test
//    public void testSearchTweet() throws TwitterException {
//        // Initiation
//        String text = "Not Hello World";
//        Twitter twittermock = mock(Twitter.class);
//        Status statusmock = mock(Status.class);
//        List<Status> listOfStatus = new ArrayList<>();
//        listOfStatus.add(statusmock);
//
//        Query querymock = mock(Query.class);
//        QueryResult queryResultmock = mock(QueryResult.class);
//
//
//        //setting replacements
//        when(twittermock.search(querymock)).thenReturn(queryResultmock);
//        when(queryResultmock.getTweets()).thenReturn(listOfStatus);
//
//
//        TwitterBot.searchForTweetsWrapper(twittermock, text);
//
//    }
}
