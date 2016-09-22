package com.alorma.github.injector.module;

import com.alorma.github.injector.named.IOScheduler;
import com.alorma.github.injector.named.MainScheduler;
import com.alorma.github.injector.named.Token;
import com.alorma.github.injector.scope.PerActivity;
import com.alorma.github.sdk.core.ApiClient;
import com.alorma.github.sdk.core.Github;
import com.alorma.github.sdk.core.GithubEnterprise;
import com.alorma.gitskarios.core.client.TokenProvider;
import com.alorma.gitskarios.core.client.UrlProvider;
import com.alorma.gitskarios.core.client.UrlProviderInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module public class ApiModule {

  @Provides
  @PerActivity
  ApiClient providesGithubApi() {
    UrlProviderInterface instance = UrlProvider.getInstance();
    if (instance == null || instance.getUrl() == null) {
      return new Github();
    } else {
      return new GithubEnterprise(instance.getUrl());
    }
  }

  @Provides @PerActivity @MainScheduler
  Scheduler provideMainScheduler(){
    return AndroidSchedulers.mainThread();
  }

  @Provides @PerActivity @IOScheduler
  Scheduler provideIOScheduler(){
    return Schedulers.io();
  }

  @Provides @PerActivity @Token
  String getToken() {
    return TokenProvider.getInstance().getToken();
  }
}
