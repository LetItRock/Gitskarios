package com.alorma.github.ui.activity.login;

import core.User;

public interface AlternateLoginPresenterViewInterface {

  void willLogin();

  void onGenericError();

  void finishAccess(User user);

  void didLogin();

  class NullView implements AlternateLoginPresenterViewInterface {

    @Override
    public void willLogin() {

    }

    @Override
    public void onGenericError() {

    }

    @Override
    public void finishAccess(User user) {

    }

    @Override
    public void didLogin() {

    }
  }
}