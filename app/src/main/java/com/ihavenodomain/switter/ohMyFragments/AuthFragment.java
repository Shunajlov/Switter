package com.ihavenodomain.switter.ohMyFragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.ihavenodomain.switter.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuthFragment extends Fragment implements View.OnClickListener {
    private Button btnAuth; // Кнопка "Авторизоваться"
    private WebView webView; // какбэ браузер

    public AuthFragment() {
        // Required empty public constructor
    }
    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);
        btnAuth = (Button) view.findViewById(R.id.btnAuth);
        btnAuth.setOnClickListener(this);
        webView = (WebView) view.findViewById(R.id.webViewAuth);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAuth:
                if(isOnline()) {
                    btnAuth.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                } else
                    Toast.makeText(getContext(), getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(cs);
        return cm.getActiveNetworkInfo() != null;
    }
}
