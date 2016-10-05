package com.github.shareme.greenandroid.materialdrawer.model;

import com.github.shareme.greenandroid.fastadapter.IItem;
import com.github.shareme.greenandroid.materialize.holder.StringHolder;

/**
 * Created by mikepenz on 03.02.15.
 */
public class PrimaryDrawerItem extends AbstractBadgeableDrawerItem<PrimaryDrawerItem> {

  @Override
  public void unbindView(ViewHolder holder) {

  }

  @Override
  public PrimaryDrawerItem withBadge(StringHolder badgeRes) {
    return null;
  }

  @Override
  public IItem getParent() {
    return null;
  }

  @Override
  public Object withParent(IItem parent) {
    return null;
  }
}
