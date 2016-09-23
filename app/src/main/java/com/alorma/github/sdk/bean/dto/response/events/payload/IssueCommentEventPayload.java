package com.alorma.github.sdk.bean.dto.response.events.payload;

import com.alorma.github.sdk.bean.dto.response.GithubComment;
import com.alorma.github.sdk.bean.dto.response.Issue;

public class IssueCommentEventPayload extends ActionEventPayload {
  public Issue issue;
  public GithubComment comment;

  public IssueCommentEventPayload() {
  }
}
