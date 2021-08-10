package study_com.study_exmp.studytogetherproject.MainClasses;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LoginAdapter extends FragmentPagerAdapter {
    private Context context;
    private int totalTabs;
    public LoginAdapter(FragmentManager fm, Context context, int toolTabs){
        super(fm);
        this.context = context;
        this.totalTabs = toolTabs;
    }

    public Fragment getItem(int position){
        switch(position){
            case 0:
                LoginFragment loginFragment = new LoginFragment();
                return loginFragment;
            case 1:
                RegisterFragment registerFragment = new RegisterFragment();
                return registerFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
