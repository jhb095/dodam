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

    private void addTabs(ViewPager viewPager) {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        sectionsPagerAdapter.addFrag(new HomeFragment());
        sectionsPagerAdapter.addFrag(new RankFragment());
        sectionsPagerAdapter.addFrag(new RecommendFragment());
        sectionsPagerAdapter.addFrag(new MySkinTypeFragment());

        viewPager.setAdapter(sectionsPagerAdapter);
    }
}
