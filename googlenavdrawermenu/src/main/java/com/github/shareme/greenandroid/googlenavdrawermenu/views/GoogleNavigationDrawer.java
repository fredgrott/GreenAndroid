
package com.github.shareme.greenandroid.googlenavdrawermenu.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.shareme.greenandroid.googlenavdrawermenu.R;
import com.github.shareme.greenandroid.googlenavdrawermenu.adapters.GoogleNavigationDrawerAdapter;
import com.github.shareme.greenandroid.googlenavdrawermenu.utils.Utils;


/**
 * Created by Arasthel on 14/04/14.
 */
@SuppressWarnings("unused")
public class GoogleNavigationDrawer extends DrawerLayout {

    private ListView mListView;

    private int mListPaddingTop = 0;
    private int mListPaddingBottom = 0;
    private int mListPaddingLeft = 0;
    private int mListPaddingRight = 0;

    private int mDrawerGravity = Gravity.START;

    private OnNavigationSectionSelected mSelectionListener;

    private String[] mMainSections;
    private String[] mSecondarySections;

    private int[] mMainSectionsDrawableIds;
    private int[] mSecondarySectionsDrawableIds;

    private View mHeaderView;
    private View mFooterView;

    public GoogleNavigationDrawer(Context context) {
        super(context);
    }

    public GoogleNavigationDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GoogleNavigationDrawer, 0, 0);

        configureWithTypedArray(a);
    }

    public GoogleNavigationDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GoogleNavigationDrawer, defStyle, 0);

        configureWithTypedArray(a);
    }

    /**
     * We need to override onFinishInflate so that it adds the ListView when it has finished inflating itself
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setListViewSections(mMainSections, mSecondarySections, mMainSectionsDrawableIds, mSecondarySectionsDrawableIds);
    }

    /**
     * Configure View with custom attrs
     * @param typedArray - A TypedArray
     */
    public void configureWithTypedArray(TypedArray typedArray) {
        mListPaddingTop = typedArray.getDimensionPixelSize(R.styleable.GoogleNavigationDrawer_list_paddingTop, mListPaddingTop);
        mListPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.GoogleNavigationDrawer_list_paddingBottom, mListPaddingBottom);
        mListPaddingRight = typedArray.getDimensionPixelSize(R.styleable.GoogleNavigationDrawer_list_paddingRight, mListPaddingRight);
        mListPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.GoogleNavigationDrawer_list_paddingLeft, mListPaddingLeft);

        mDrawerGravity = typedArray.getInt(R.styleable.GoogleNavigationDrawer_drawer_gravity, mDrawerGravity);

        mMainSections = Utils.convertToStringArray(typedArray.getTextArray(R.styleable.GoogleNavigationDrawer_list_mainSectionsEntries));
        mSecondarySections = Utils.convertToStringArray(typedArray.getTextArray(R.styleable.GoogleNavigationDrawer_list_secondarySectionsEntries));

        int mainSectDrawableId = typedArray.getResourceId(R.styleable.GoogleNavigationDrawer_list_mainSectionsDrawables, -1);

        if(mMainSections != null) {
            mMainSectionsDrawableIds = new int[mMainSections.length];

            if (mainSectDrawableId != -1) {
                TypedArray mainSectTypedArray = getResources().obtainTypedArray(mainSectDrawableId);
                for (int i = 0; i < mMainSections.length; i++) {
                    mMainSectionsDrawableIds[i] = mainSectTypedArray.getResourceId(i, 0);
                }
            }
        }

        int secondarySectDrawableId = typedArray.getResourceId(R.styleable.GoogleNavigationDrawer_list_secondarySectionsDrawables, -1);

        if(mSecondarySections != null) {
            mSecondarySectionsDrawableIds = new int[mSecondarySections.length];

            if (secondarySectDrawableId != -1) {
                TypedArray secondarySectTypedArray = getResources().obtainTypedArray(secondarySectDrawableId);
                for (int i = 0; i < mSecondarySections.length; i++) {
                    mSecondarySectionsDrawableIds[i] = secondarySectTypedArray.getResourceId(i, 0);
                }
            }
        }

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int headerViewId = typedArray.getResourceId(R.styleable.GoogleNavigationDrawer_list_headerView, -1);
        if(headerViewId != -1) {
            mHeaderView = inflater.inflate(headerViewId, null);
        }

        int footerViewId = typedArray.getResourceId(R.styleable.GoogleNavigationDrawer_list_footerView, -1);
        if(footerViewId != -1) {
            mFooterView = inflater.inflate(footerViewId, null);
        }

        typedArray.recycle();

    }

    /**
     * The ListView menu is inflated and added to de DrawerLayout
     */
    private void configureList() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = (ListView) inflater.inflate(R.layout.navigation_list, this, false);
        mListView.setPadding(mListPaddingLeft, mListPaddingTop, mListPaddingRight, mListPaddingBottom);
        ((LayoutParams) mListView.getLayoutParams()).gravity = mDrawerGravity;
        if(mHeaderView != null) {
            setMenuHeader(mHeaderView);
        }
        if(mFooterView != null) {
            setMenuFooter(mFooterView);
        }
        addView(mListView);
    }

    /**
     * The way to set the ListView adapter items
     * @param mainSections A String array with the main section titles. Cannot be null.
     * @param secondarySections A String array with the secondary section titles. May be null.
     * @param mainDrawableIds An integer array with the ids of the main sections icons. Must have the same length as mainSections. May be null.
     * @param secondaryDrawableIds An integer array with the ids of the secondary sections icons. Must have the same length as secondarySections. May be null.
     */
    public void setListViewSections(String[] mainSections, String[] secondarySections, int[] mainDrawableIds, int[] secondaryDrawableIds) {
        if(mListView == null) {
            configureList();
        }
        GoogleNavigationDrawerAdapter adapter = new GoogleNavigationDrawerAdapter(getContext(), mainSections, secondarySections, mainDrawableIds, secondaryDrawableIds);
        mListView.setAdapter(adapter);
        if(mHeaderView != null) {
            mListView.setItemChecked(1, true);
        } else {
            mListView.setItemChecked(0, true);
        }
    }

    /**
     * Helper to access the isDrawerOpen method of the ListView menu.
     * @return whether it's opened or not
     */
    public boolean isDrawerMenuOpen() {
        return super.isDrawerOpen(mListView);
    }

    /**
     * Helper to open the ListView menu.
     */
    public void openDrawerMenu() {
        super.openDrawer(mListView);
    }

    /**
     * Helper to close the ListView menu.
     */
    public void closeDrawerMenu() {
        super.closeDrawer(mListView);
    }

    /**
     * Set custom listener so when a ListView item is clicked, it's also checked
     * @param listener The OnNavigationSectionSelected listener. Use null to disable.
     */
    public void setOnNavigationSectionSelected(OnNavigationSectionSelected listener) {
        mSelectionListener = listener;
        if(mSelectionListener != null) {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if(mHeaderView != null && i == 0) {
                        return;
                    }

                    if(mFooterView != null && i == mListView.getCount()-1) {
                        return;
                    }
                    ((CheckableRelativeLayout) view).setChecked(true);

                    mSelectionListener.onSectionSelected(view, i, l);
                    closeDrawerMenu();
                }
            });
        } else {
            mListView.setOnItemClickListener(null);
        }
    }

    /**
     * Set a custom header view
     * @param v The header view
     */
    public void setMenuHeader(View v) {
        if(mListView != null) {
            mHeaderView = v;
            mListView.addHeaderView(v);
        }
    }

    /**
     * Returns the header of the ListView
     * @return The header of the ListView
     */
    public View getMenuHeader() {
        return mHeaderView;
    }

    /**
     * Set a custom footer view
     * @param v The footer view
     */
    public void setMenuFooter(View v) {
        if(mListView != null) {
            mFooterView = v;
            mListView.addFooterView(v);
        }
    }

    /**
     * Returns the footer of the ListView
     * @return The footer of the ListView
     */
    public View getMenuFooter() {
        return mFooterView;
    }

    public interface OnNavigationSectionSelected {

        public void onSectionSelected(View v, int i, long l);

    }
}
