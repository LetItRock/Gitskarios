package com.alorma.github.ui.adapter.events.holders;

import android.text.Html;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.alorma.github.R;
import com.alorma.github.sdk.bean.dto.response.GithubEvent;
import com.alorma.github.ui.view.UserAvatarView;
import core.User;

public class IssueCommentEventViewHolder extends EventViewHolder {

  @BindView(R.id.authorAvatar) UserAvatarView authorAvatar;
  @BindView(R.id.authorName) TextView authorName;
  @BindView(R.id.textDate) TextView textDate;

  public IssueCommentEventViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void inflateViews(View itemView) {
    ButterKnife.bind(this, itemView);
  }

  @Override
  protected void populateAvatar(User actor) {
    authorAvatar.setUser(actor);
  }

  @Override
  protected void populateContent(GithubEvent event) {
    String type = event.payload.issue.pullRequest == null ? "issue" : "pull request";
    String text = "<b>"
        + event.actor.getLogin()
        + "</b>"
        + " "
        + "commented on "
        + type
        + " "
        + "<b>"
        + event.repo.name
        + "#"
        + event.payload.issue.number
        + "</b>";
    authorName.setText(Html.fromHtml(text));
  }

  @Override
  protected void populateDate(String date) {
    textDate.setText(date);
  }
}
