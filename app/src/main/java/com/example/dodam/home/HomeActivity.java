package com.example.dodam.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.dodam.R;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.home_view_pager);

        tabs = findViewById(R.id.home_tabs);
        tabs.setupWithViewPager(viewPager);

        addTabs(viewPager);
    }

    // 탭 추가
    private void addTabs(ViewPager viewPager) {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        sectionsPagerAdapter.addFrag(new HomeFragment());
        sectionsPagerAdapter.addFrag(new RankFragment());
        sectionsPagerAdapter.addFrag(new RecommendFragment());
        sectionsPagerAdapter.addFrag(new MySkinTypeFragment());

        viewPager.setAdapter(sectionsPagerAdapter);
    }

    // ViewPage 전환 메소드(Fragment를 전환)
    public void replaceFragment(int pos) {
        viewPager.setCurrentItem(pos);
    }
}
