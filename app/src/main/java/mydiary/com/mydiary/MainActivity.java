package mydiary.com.mydiary;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mydiary.com.mydiary.helper.SQLiteHandler;
import mydiary.com.mydiary.helper.SessionManager;
import mydiary.com.mydiary.hometab.DataProvider;
import mydiary.com.mydiary.hometab.StoryDetailFragment;
import mydiary.com.mydiary.hometab.StoryFragment;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, ActionBar.TabListener {

//    private SQLiteHandler db;
//    private SessionManager session;

    private ViewPager viewPager;
    private ActionBar actionBar;
    private DataProvider adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Call Pager View
        viewPager = (ViewPager)findViewById(R.id.homePager);
        viewPager.setOnPageChangeListener(this);

        //Call Fragment Manager and setup with pager;
        FragmentManager fmg = getSupportFragmentManager();
        adapter = new DataProvider(fmg);
        viewPager.setAdapter(adapter);

        /* Setup ActionBar ****/
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //MyStory Tab
        ActionBar.Tab StoryTab = actionBar.newTab();
        StoryTab.setText("Story");
        StoryTab.setTabListener(this);

        //Upcoming Tab
        ActionBar.Tab UpcomingTab = actionBar.newTab();
        UpcomingTab.setText("Upcoming");
        UpcomingTab.setTabListener(this);

        //Favourite Tab
        ActionBar.Tab FavouriteTab = actionBar.newTab();
        FavouriteTab.setText("Favourite");
        FavouriteTab.setTabListener(this);

        actionBar.addTab(StoryTab);
        actionBar.addTab(UpcomingTab);
        actionBar.addTab(FavouriteTab);

        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#4d4d4d")));

    }

    ///////////////// On Page Changed /////////////////

    @Override
    public void onPageScrolled(int i, float v, int i1) {}

    @Override
    public void onPageSelected(int i) {
        actionBar.setSelectedNavigationItem(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {}


    ////////////////// On Tab Changed /////////////////
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {}

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {}


    public void backToList(View v) {

        Toast.makeText(getApplicationContext(), "To Story", Toast.LENGTH_SHORT).show();

        FragmentManager fmg = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fgT = fmg.beginTransaction();

        StoryFragment storyFragment = new mydiary.com.mydiary.hometab.StoryFragment();
        fgT.remove(storyFragment);

        fgT.detach(storyFragment);
        fgT.attach(storyFragment);

        fgT.replace(R.id.story, storyFragment);

        fgT.addToBackStack(null);

        fgT.commit();

    }

}
