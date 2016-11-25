package com.ihavenodomain.switter.ohMyFragments.mainReadableFragment;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ihavenodomain.switter.ActivityMain;
import com.ihavenodomain.switter.R;
import com.ihavenodomain.switter.Tweets.Tweet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;


public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.TweetViewHolder> {

//    private List<City> cities;
    private List<Tweet> tweets;

    public TweetAdapter(List<Tweet> suchTweets) {
        this.tweets = suchTweets;
    }


    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_view, parent, false);
        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        holder.tvContent.setText(tweets.get(position).getText(), TextView.BufferType.SPANNABLE);
        holder.tvDate.setText(tweets.get(position).getCreatedAt());
        // Найдём ссылку на пользователя, подсветим, и навесим нажатие
        Spannable spannable = (Spannable) holder.tvContent.getText();
        Pattern p = Pattern.compile("@[\\w.]+");
        String spnString = holder.tvContent.getText().toString();
        Matcher m = p.matcher(spnString);
        while(m.find()) {
            spannable.setSpan(new myClickableSpan(spnString.substring(m.start()+1, m.end())), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private class myClickableSpan extends ClickableSpan {
        String user;
        myClickableSpan(String user) {
            this.user = user;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(ActivityMain.getAppContext(), "Вы перешли к: " + user, Toast.LENGTH_SHORT).show();
            ActivityMain.openMainFragment(user);
        }
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    static class TweetViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvContent;
        TextView tvDate;

        TweetViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            tvContent = (TextView) itemView.findViewById(R.id.tvContentText);
            tvDate = (TextView) itemView.findViewById(R.id.tvDateTime);
        }
    }
}
