package com.github.shareme.greenandroid.materialdrawer.model;

import com.github.shareme.greenandroid.fastadapter.IItem;

/**
 * Created by mikepenz on 03.02.15.
 */
public class SwitchDrawerItem extends AbstractSwitchableDrawerItem<SwitchDrawerItem> {

  @Override
  public void unbindView(ViewHolder holder) {

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
