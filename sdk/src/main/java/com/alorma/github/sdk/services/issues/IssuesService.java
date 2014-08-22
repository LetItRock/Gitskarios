package com.alorma.github.sdk.services.issues;

import com.alorma.github.sdk.bean.dto.response.ListIssues;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Bernat on 22/08/2014.
 */
public interface IssuesService {

    @GET("/repos/{owner}/{repo}/issues?state=all&sort=updated")
    void issues(@Path("owner") String owner, @Path("repo") String repo, Callback<ListIssues> callback);
}
