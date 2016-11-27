package com.ihavenodomain.switter;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.ihavenodomain.switter.dataManagement.UserData;

import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
import twitter4j.conf.ConfigurationBuilder;

public class MyService extends IntentService {
    public MyService() {
        super("MyService");
    }

    private static final String MYLOGGER = "MyServiceLog";

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Делаем всё тут
        if(isOnline()) {
            UserData ud = new UserData(getApplicationContext());
            ud.setServiceRunning(true);
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true);
            cb.setOAuthConsumerKey(ActivityMain.CONSUMER_KEY);
            cb.setOAuthConsumerSecret(ActivityMain.CONSUMER_SECRET);
            cb.setOAuthAccessToken(ActivityMain.ACCESS_TOKEN);
            cb.setOAuthAccessTokenSecret(ActivityMain.ACCESS_TOKEN_SECRET);

            TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
            UserStreamListener userStreamListener = new UserStreamListener() {
                @Override
                public void onStatus(Status status) {
                    Log.d(MYLOGGER, "UserStreamListener.onStatus:" + status.getUser().getName() + ":" + status.getText());
                    // Похоже, статус - это то, что нужно
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("Wow, new post from " + status.getUser().getName())
                            .setContentText(status.getText());

                    Intent resultIntent = new Intent(getApplicationContext(), ActivityMain.class);
                    PendingIntent resultPendingIntent =
                            PendingIntent.getActivity(
                                    getApplicationContext(),
                                    0,
                                    resultIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(0, mBuilder.build());
                }

                @Override
                public void onDeletionNotice(long l, long l2) {
                    Log.d(MYLOGGER, "UserStreamListener.onDeletionNotice");
                }

                @Override
                public void onFriendList(long[] longs) {
                    Log.d(MYLOGGER, "UserStreamListener.onFriendList");
                }

                @Override
                public void onFavorite(User user, User user2, Status status) {
                    Log.d(MYLOGGER, "UserStreamListener.onFavorite");
                }

                @Override
                public void onUnfavorite(User user, User user2, Status status) {
                    Log.d(MYLOGGER, "UserStreamListener.onUnfavorite");
                }

                @Override
                public void onFollow(User user, User user2) {
                    Log.d(MYLOGGER, "UserStreamListener.onFollow");
                }

                @Override
                public void onDirectMessage(DirectMessage directMessage) {
                    Log.d(MYLOGGER, "UserStreamListener.onDirectMessage");
                }

                @Override
                public void onUserListMemberAddition(User user, User user2, UserList userList) {
                    Log.d(MYLOGGER, "UserStreamListener.onUserListMemberAddition");
                }

                @Override
                public void onUserListMemberDeletion(User user, User user2, UserList userList) {
                    Log.d(MYLOGGER, "UserStreamListener.onUserListMemberDeletion");
                }

                @Override
                public void onUserListSubscription(User user, User user2, UserList userList) {
                    Log.d(MYLOGGER, "UserStreamListener.onUserListSubscription");
                }

                @Override
                public void onUserListUnsubscription(User user, User user2, UserList userList) {
                    Log.d(MYLOGGER, "UserStreamListener.onUserListUnsubscription");
                }

                @Override
                public void onUserListCreation(User user, UserList userList) {
                    Log.d(MYLOGGER, "UserStreamListener.onUserListCreation");
                }

                @Override
                public void onUserListUpdate(User user, UserList userList) {
                    Log.d(MYLOGGER, "UserStreamListener.onUserListUpdate");
                }

                @Override
                public void onUserListDeletion(User user, UserList userList) {
                    Log.d(MYLOGGER, "UserStreamListener.onUserListDeletion");
                }

                @Override
                public void onUserProfileUpdate(User user) {
                    Log.d(MYLOGGER, "UserStreamListener.onUserProfileUpdate");
                }

                @Override
                public void onBlock(User user, User user2) {
                    Log.d(MYLOGGER, "UserStreamListener.onUnblock");
                }

                @Override
                public void onUnblock(User user, User user2) {
                    Log.d(MYLOGGER, "UserStreamListener.onUnblock");
                }

                @Override
                public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                    Log.d(MYLOGGER, "UserStreamListener.onDeletionNotice:" + statusDeletionNotice.getStatusId());
                }

                @Override
                public void onTrackLimitationNotice(int i) {
                    Log.d(MYLOGGER, "UserStreamListener.onTrackLimitationNotice:" + i);
                }

                @Override
                public void onScrubGeo(long l, long l2) {
                    Log.d(MYLOGGER, "UserStreamListener.onScrubGeo:" + l + ":" + l2);
                }

                @Override
                public void onStallWarning(StallWarning stallWarning) {
                    Log.d(MYLOGGER, "UserStreamListener.onStallWarning:" + stallWarning.getCode() + ":" + stallWarning.getMessage() + ":" + stallWarning.getPercentFull());
                }

                @Override
                public void onException(Exception e) {
                    Log.d(MYLOGGER, "UserStreamListener.onException", e);
                }
            };
            String userIds[] = {ActivityMain.DEFAULT_ID};
            twitterStream.clearListeners();
            twitterStream.addListener(userStreamListener);
            twitterStream.user(userIds);

        } else {
            MyReceiver.completeWakefulIntent(intent);
        }
    }
}
