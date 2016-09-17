package com.alorma.github.ui.fragment.events;

import android.os.Bundle;
import com.alorma.github.R;
import com.alorma.github.sdk.services.user.events.GetUserCreatedEventsClient;
import com.alorma.github.ui.listeners.TitleProvider;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.octicons_typeface_library.Octicons;

public class CreatedEventsListFragment extends EventsListFragment implements TitleProvider {

  public static CreatedEventsListFragment newInstance(String username) {
    Bundle bundle = new Bundle();
    bundle.putString(USERNAME, username);

    CreatedEventsListFragment f = new CreatedEventsListFragment();
    f.setArguments(bundle);

    return f;
  }

  @Override
  public void onResume() {
    super.onResume();

    getActivity().setTitle("");
  }

  @Override
  protected void executeRequest() {
    GetUserCreatedEventsClient client = new GetUserCreatedEventsClient(username);
    executeClient(client);
  }

  @Override
  protected void executePaginatedRequest(int page) {
    GetUserCreatedEventsClient client = new GetUserCreatedEventsClient(username, page);
    executeClient(client);
  }

  @Override
  public int getTitle() {
    return R.string.events;
  }

  @Override
  public IIcon getTitleIcon() {
    return Octicons.Icon.oct_calendar;
  }

  @Override
  protected int getLightTheme() {
    return R.style.AppTheme_Profile;
  }

  @Override
  protected int getDarkTheme() {
    return R.style.AppTheme_Dark_Profile;
  }
}
