package mydiary.com.mydiary.hometab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class DataProvider extends FragmentStatePagerAdapter {

    public DataProvider(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        Fragment fragment = null;

        switch (i) {
            case 0:
                fragment = new StoryFragment();
                break;
            case 1:
                fragment = new UpcomingFragment();
                break;
            case 2 :
                fragment = new FavouriteFragment();
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
