/*
 Copyright 2013 Andras Kindler
 Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */
package com.github.shareme.greenandroid.quickscroll;

/**
 * Interface required for FastTrack.
 * 
 * @author andraskindler
 *
 */
public interface Scrollable {

	/**
	 * This function returns the corresponding String to display at any given position
	 * <p>
	 * 
	 * @param childposition
	 *            equals childposition if used with ExpandableListView, position otherwise.
	 * @param groupposition
	 *            equals groupposition if used with ExpandableListView, zero otherwise.
	 */
	String getIndicatorForPosition(final int childposition, final int groupposition);

	/**
	 * This second function is responsible for is for implementing scroll behaviour. This can be used to perform special tasks, e.g. if you want to snap to the first item starting with a letter in an alphabetically ordered list or jump between groups in an ExpandableListView. If you want the normal approach, simply return childposition.
	 * <p>
	 * 
	 * @param childposition
	 *            equals childposition if used with ExpandableListView, position otherwise.
	 * @param groupposition
	 *            equals groupposition if used with ExpandableListView, zero otherwise.
	 */
	int getScrollPosition(final int childposition, final int groupposition);

}
