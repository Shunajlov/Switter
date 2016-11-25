package com.ihavenodomain.switter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ihavenodomain.switter.dataManagement.UserData;
import com.ihavenodomain.switter.ohMyFragments.AuthFragment;
import com.ihavenodomain.switter.ohMyFragments.mainReadableFragment.MainReadableFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityMain extends AppCompatActivity {
    /**
     * А вот это будет нетрудно достать при реверс-инжиниринге.
     * Нам это нужно для получения bearer token и использования rest api твиттера
     * без использования OAuth.
     */

    // Прошу этим не злоупотреблять
    public static final String ACCESS_TOKEN = "799319870370447363-GtKOiUtOgr4dA4z0JO9xyUk9xMYUgP3";
    public static final String ACCESS_TOKEN_SECRET = "Y0jOi5DngSTWJW95lPO5ZiIWHp9nJARISjksbnD83KP5i";
    // =============================================================================================
    public static final String API_KEY ="1udbuHD44Zv66MafXNIRxLce9"; // Consumer Key
    public static final String API_SECRET ="soqABY5UZwkEkXvzXEN9d9oA631Cn0oOIDDmFOGeiyLEmIG1r2"; // Consumer Secret
    public static final String DEFAULT_NAME = "KremlinRussia"; // "AlexShunailov";
    public static final String API_ENDPOINT = "https://api.twitter.com";

    public static FragmentManager manager;
    private RelativeLayout rlLoad;

    private UserData ud;

    private static Context ctx;

    public static Context getAppContext() {
        return ctx;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = getApplicationContext();

        manager = getSupportFragmentManager();

        ud = new UserData(this);

        rlLoad = (RelativeLayout) findViewById(R.id.loading);

        viewsHistory = new ArrayList<>();

        if(isOnline()) {
            initialize();
        } else {
            findViewById(R.id.button).setOnClickListener(view -> initialize());
        }
    }

    /**
     * ПОЕХАЛИ
     */
    public void initialize() {
        if(isOnline()) {
            String apiKey = API_KEY;
            String apiSecret = API_SECRET;
            try {
                apiKey = URLEncoder.encode(apiKey, "UTF-8");
                apiSecret = URLEncoder.encode(apiSecret, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String credentials = apiKey + ":" + apiSecret;
            credentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            if (ud.getBearerToken().equals(""))
                ud.setBearerToken(receiveCorrectBearToken(credentials, ud));
            else {
                rlLoad.setVisibility(View.GONE);
                openMainFragment(DEFAULT_NAME);
            }
        } else
            Toast.makeText(this, getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
    }

    /**
     * Получим токен для приложения старым способом через volley
     * @return готовый ответ от твиттера
     */
    public String receiveCorrectBearToken(final String credentials, final UserData ud) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String answer = "";
        final String url = "https://api.twitter.com/oauth2/token";
        final String requestBody = "grant_type=client_credentials";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            Log.i("onResponse", response.toString());
            try {
                ud.setBearerToken(response.getString("access_token")); // Забираем bearer access token
                openMainFragment(DEFAULT_NAME); // Открываем фрагмент и читаем DEFAULT_NAME
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), getString(R.string.badToken), Toast.LENGTH_SHORT).show();
            }
        }, error -> Log.e("onErrorResponse", error.toString()))
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Простая аутентификация
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                String auth = "Basic " + credentials;
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                headers.put("Authorization", auth);
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
            };

        queue.add(jsonObjectRequest);
        return answer;
    }

    /**
     * Проверяет есть ли интернет
     * @return есть или нет
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void openMainFragment(String userName) {
        MainReadableFragment mainFragment = MainReadableFragment.newInstance(userName);
        FragmentTransaction transaction = manager.beginTransaction();
        if(viewsHistory.size() != 0) {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.mainReplaceable, mainFragment);
        transaction.commit();
    }

    public static List<String> viewsHistory;

    @Override
    public void onBackPressed() {
        if(viewsHistory.size() > 0)
            viewsHistory.remove(viewsHistory.size()-1);
        super.onBackPressed();
    }

//    Предполагалось использовать для авторизации, но впоследствии для простоты от этой идеи я отказался
//    public static void openAuthFragment() {
//        AuthFragment authFragment = AuthFragment.newInstance();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.mainReplaceable, authFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
}
