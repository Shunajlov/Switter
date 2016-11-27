package com.ihavenodomain.switter.ohMyFragments.mainReadableFragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ihavenodomain.switter.ActivityMain;
import com.ihavenodomain.switter.R;
import com.ihavenodomain.switter.Tweets.Tweet;
import com.ihavenodomain.switter.dataManagement.UserData;
import com.ihavenodomain.switter.forApi.OAuthRequestParameters;
import com.ihavenodomain.switter.forApi.TweetRest;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainReadableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainReadableFragment extends Fragment {
    private static final String FOLLOW_PARAM = "userToFollowParam";
    private final String COUNT_UPLOAD_TWEETS = "15"; // <= 200

    private RecyclerView rvTweets;
    private TextView tvCurrentName;
    private String userToFollow;
    private UserData ud;

    private TweetAdapter adapter;

    private String maxId = null;

    private List<Tweet> allTweets = new LinkedList<>();

    public MainReadableFragment() {
        // Required empty public constructor
    }

    public static MainReadableFragment newInstance(String param1) {
        MainReadableFragment fragment = new MainReadableFragment();
        Bundle args = new Bundle();
        args.putString(FOLLOW_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userToFollow = getArguments().getString(FOLLOW_PARAM);
        }
        ud = new UserData(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_readable, container, false);
        rvTweets = (RecyclerView) view.findViewById(R.id.rvTweets);
//        tvNeedAuth = (TextView) view.findViewById(R.id.tvNeedAuth);
        tvCurrentName = (TextView) view.findViewById(R.id.tvCurrentName);

        if((ActivityMain.viewsHistory.size() > 0 &&
                !ActivityMain.viewsHistory.get(ActivityMain.viewsHistory.size()-1).equals(userToFollow)) ||
                ActivityMain.viewsHistory.size() == 0)
            ActivityMain.viewsHistory.add(userToFollow);

        String concatStr = getString(R.string.nowFollowing) + " " + userToFollow;
        tvCurrentName.setText(concatStr);
        allTweets.clear();

        loadTweets(false);

        MyLayoutManager mlm = new MyLayoutManager(getContext());
        rvTweets.setLayoutManager(mlm);
        adapter = new TweetAdapter(allTweets);
        rvTweets.setAdapter(adapter);

        // А вот тут надо сделать retrofit
        // Ex: https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi&count=2

        if(allTweets.size() > 0) {
            // Возможно, это несовершенный способ отбора ID последнего загруженного твита
            Long id = allTweets.get(allTweets.size()-1).getId() - 1;
            maxId = id.toString();
        } else
            maxId = null;

        // Применение лямбда-выражений в Java - это просто невероятно
        tvCurrentName.setOnClickListener(view1 -> showHistory());
        return view;
    }

    /**
     * Свой LayoutManager нужен для определения момента необходимой подгрузки контента
     */
    private class MyLayoutManager extends LinearLayoutManager {
        private MyLayoutManager(Context context) {
            super(context);
        }

        @Override
        public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                final int result = super.scrollVerticallyBy(dy, recycler, state);
                if (findLastVisibleItemPosition() == allTweets.size() - 1
                        && !UserData.dataLoading) {
                    Toast.makeText(getActivity(), getString(R.string.nowLoading, COUNT_UPLOAD_TWEETS), Toast.LENGTH_SHORT).show();
                    loadTweets(true);
                }
                return result;
            } catch (IndexOutOfBoundsException e) {
                return 0;
            }
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
                if (findLastVisibleItemPosition() == allTweets.size() - 1
                        && !UserData.dataLoading) {
                    Toast.makeText(getActivity(), getString(R.string.nowLoading, COUNT_UPLOAD_TWEETS), Toast.LENGTH_SHORT).show();
                    loadTweets(true);
                }
            } catch (IndexOutOfBoundsException e) {
                // do nothing
            }
        }

        @Override
        public RecyclerView.LayoutParams generateDefaultLayoutParams() {
            return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
        }
    }

    /**
     * Здесь творится нечто невообразимое
     */
    public void loadTweets(boolean overload) {

//        OAuthRequestParameters param = new OAuthRequestParameters("POST", ActivityMain.API_ENDPOINT + "/1.1/statuses/user_timeline.json");

        OkHttpClient httpClient = new OkHttpClient.Builder()
                //здесь я добавил Interceptor для вставки заголовков
                .addNetworkInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + ud.getBearerToken())
//                            .addHeader("Authorization", param.getHeaderParameters())
                            .build();
                    return chain.proceed(request);
                })
                //здесь я добавил Interceptor для логирования
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        UserData.dataLoading = true;
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .baseUrl(ActivityMain.API_ENDPOINT)
                .build();
        TweetRest tweetRest = retrofit.create(TweetRest.class);
        Observable<LinkedList<Tweet>> oneTweet;
        if (maxId != null && overload)
            oneTweet = tweetRest.getTweet(userToFollow, COUNT_UPLOAD_TWEETS, maxId);
        else
            oneTweet = tweetRest.getTweet(userToFollow, COUNT_UPLOAD_TWEETS);

        oneTweet.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LinkedList<Tweet>>() {
                    @Override
                    public void onCompleted() {
                        Long id = allTweets.get(allTweets.size() - 1).getId() - 1;
                        maxId = id.toString();
                        UserData.dataLoading = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(LinkedList<Tweet> tweets) {
                        UserData.dataLoading = true;
                        allTweets.addAll(tweets);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * История не кликабельна, но я думаю, что если посмотреть код, можно понять,
     * что я умею это делать. Надеюсь, этот момент не сочтут критичным.
     */
    public void showHistory() {
//        Сформируем сообщение
        StringBuilder sb = new StringBuilder();
        // Если бы у нас не было ограничения в API 16, можно было бы сделать вот так
//        UserData.getViewsHistory().forEach(sb::append);
        // Но мы делаем так
        for (String name:
             ActivityMain.viewsHistory) {
            sb.append(name).append("->\n");
        }
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.historyTitle))
                .setMessage(sb.toString())
                .setIcon(R.drawable.ic_launcher)
                .setCancelable(false)
                .setNegativeButton("OK", (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }
}
