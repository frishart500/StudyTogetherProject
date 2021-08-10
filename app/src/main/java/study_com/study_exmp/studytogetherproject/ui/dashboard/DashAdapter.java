package study_com.study_exmp.studytogetherproject.ui.dashboard;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DashAdapter extends FragmentPagerAdapter {
    private Context context;
    private int totalTabs;
    public DashAdapter(FragmentManager fm, Context context, int toolTabs){
        super(fm);
        this.context = context;
        this.totalTabs = toolTabs;
    }

    public Fragment getItem(int position){
        switch(position){
            case 0:
                AllTasksFragment allTasksFragment = new AllTasksFragment();
                return allTasksFragment;
            case 1:
                MyTasksFragment myTasksFragment = new MyTasksFragment();
                return myTasksFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
