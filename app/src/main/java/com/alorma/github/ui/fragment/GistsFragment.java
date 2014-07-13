package com.alorma.github.ui.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.alorma.github.R;
import com.alorma.github.sdk.bean.Link;
import com.alorma.github.sdk.bean.RelType;
import com.alorma.github.sdk.bean.dto.response.Gist;
import com.alorma.github.sdk.bean.dto.response.ListGists;
import com.alorma.github.sdk.services.client.BaseClient;
import com.alorma.github.sdk.services.gists.UserGistsClient;
import com.alorma.github.ui.adapter.GistsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

public class GistsFragment extends ListFragment implements BaseClient.OnResultCallback<ListGists>, AbsListView.OnScrollListener {

    private static final String USERNAME = "USERNAME";
    private GistsAdapter gistsAdapter;
    private Link upLink;
    private Link bottomLink;
    private String username;

    public static GistsFragment newInstance() {
        return new GistsFragment();
    }

    public static GistsFragment newInstance(String username) {
        GistsFragment gistsFragment = new GistsFragment();
        if (username != null) {
            Bundle bundle = new Bundle();
            bundle.putString(USERNAME, username);

            gistsFragment.setArguments(bundle);
        }
        return gistsFragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserGistsClient client;

        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
        }

        client = new UserGistsClient(getActivity(), username);

        client.setOnResultCallback(this);
        client.execute();

        getListView().setOnScrollListener(this);
    }

    private void setUpList() {
        gistsAdapter = new GistsAdapter(getActivity(), new ArrayList<Gist>());
        setListAdapter(gistsAdapter);
    }

    @Override
    public void onResponseOk(ListGists gists, Response r) {
        List<Header> headers = r.getHeaders();
        Map<String, String> headersMap = new HashMap<String, String>(headers.size());
        for (Header header : headers) {
            headersMap.put(header.getName(), header.getValue());
        }

        String link = headersMap.get("Link");

        if (link != null) {
            getLinkData(link);
        }

        if (getActivity() != null) {

            if (gistsAdapter == null) {
                setUpList();
            }

            gistsAdapter.addAll(gists);
        }
    }

    private void getLinkData(String link) {
        String[] parts = link.split(",");
        try {
            bottomLink = new Link(parts[0]);
            upLink = new Link(parts[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFail(RetrofitError error) {
        Toast.makeText(getActivity(), "failed: " + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int first, int last, int total) {
        if (total > 0 && first + last == total) {
            if (bottomLink != null && bottomLink.rel == RelType.next) {
                UserGistsClient client = new UserGistsClient(getActivity(), username, bottomLink.page);
                client.setOnResultCallback(this);
                client.execute();
                bottomLink = null;
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        restoreActionBar();
    }

    private void restoreActionBar() {
        int rgb = getResources().getColor(R.color.accent);
        ColorDrawable cd = new ColorDrawable(rgb);
        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setBackgroundDrawable(cd);
            getActivity().getActionBar().setTitle(R.string.title_gists);
            getActivity().getActionBar().setSubtitle("");
        }
    }
}