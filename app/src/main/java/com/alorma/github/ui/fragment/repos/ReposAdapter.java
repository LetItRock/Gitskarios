package com.alorma.github.ui.fragment.repos;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alorma.github.R;
import com.alorma.github.sdk.bean.info.RepoInfo;
import com.alorma.github.ui.activity.repo.RepoDetailActivity;
import com.alorma.github.ui.adapter.base.RecyclerArrayAdapter;
import com.crashlytics.android.Crashlytics;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.octicons_typeface_library.Octicons;
import core.repositories.Permissions;
import core.repositories.Repo;
import io.fabric.sdk.android.Fabric;
import java.text.DecimalFormat;

public class ReposAdapter extends RecyclerArrayAdapter<Repo, ReposAdapter.ViewHolder> {

  private boolean showOwnerName;
  private boolean showOwnerNameExtra = true;

  public ReposAdapter(LayoutInflater inflater) {
    super(inflater);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(getInflater().inflate(R.layout.row_repo, parent, false));
  }

  @Override
  protected void onBindViewHolder(ViewHolder holder, Repo repo) {
    try {
      if (repo.getOwner() != null) {
        holder.textTitle.setText(showOwnerName ? repo.getOwner().getLogin() : repo.name);
      }
    } catch (NullPointerException e) {
      if (Fabric.isInitialized()) {
        Crashlytics.logException(e);
      }
    }

    if (showOwnerNameExtra && repo.getOwner() != null) {
      holder.textOwnerName.setText(repo.getOwner().getLogin());
    } else {
      holder.textOwnerName.setText("");
    }

    applyIcon(holder.textStarts, Octicons.Icon.oct_star);
    holder.textStarts.setText(new DecimalFormat().format(repo.getStargazersCount()));

    applyIcon(holder.textForks, Octicons.Icon.oct_repo_forked);
    holder.textForks.setText(new DecimalFormat().format(repo.forks_count));

    if (repo.description != null) {
      holder.textDescription.setVisibility(View.VISIBLE);
      holder.textDescription.setText(repo.description);
    } else {
      holder.textDescription.setVisibility(View.GONE);
    }

    if (repo.isPrivateRepo()) {
      holder.repoPrivate.setVisibility(View.VISIBLE);
    } else {
      holder.repoPrivate.setVisibility(View.GONE);
    }
  }

  public void showOwnerName(boolean showOwnerName) {
    this.showOwnerName = showOwnerName;
    notifyDataSetChanged();
  }

  public void showOwnerNameExtra(boolean showOwnerNameExtra) {
    this.showOwnerNameExtra = showOwnerNameExtra;
    notifyDataSetChanged();
  }

  private void applyIcon(TextView textView, Octicons.Icon value) {
    IconicsDrawable drawableForks = new IconicsDrawable(textView.getContext(), value);
    drawableForks.sizeRes(R.dimen.textSizeSmall);
    drawableForks.colorRes(R.color.icons);
    textView.setCompoundDrawables(null, null, drawableForks, null);
    int offset = textView.getResources().getDimensionPixelOffset(R.dimen.textSizeSmall);
    textView.setCompoundDrawablePadding(offset);
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView textTitle;
    public TextView repoPrivate;
    public TextView textOwnerName;
    public TextView textDescription;
    public TextView textForks;
    public TextView textStarts;

    public ViewHolder(View itemView) {
      super(itemView);
      textTitle = (TextView) itemView.findViewById(R.id.repoName);
      repoPrivate = (TextView) itemView.findViewById(R.id.repoPrivate);
      textOwnerName = (TextView) itemView.findViewById(R.id.textOwnerName);
      textDescription = (TextView) itemView.findViewById(R.id.descriptionText);
      textStarts = (TextView) itemView.findViewById(R.id.textStarts);
      textForks = (TextView) itemView.findViewById(R.id.textForks);

      itemView.setOnClickListener(v -> {
        Repo item = getItem(getAdapterPosition());
        if (item != null) {
          RepoInfo repoInfo = new RepoInfo();
          repoInfo.owner = item.owner.getLogin();
          repoInfo.name = item.name;
          Permissions permissions = new Permissions();
          permissions.pull = item.permissions.pull;
          permissions.push = item.permissions.push;
          permissions.admin = item.permissions.admin;
          repoInfo.permissions = permissions;
          Intent intent = RepoDetailActivity.createLauncherIntent(v.getContext(), repoInfo);
          v.getContext().startActivity(intent);
        }
      });
    }
  }
}