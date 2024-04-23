package com.example.app;

import com.example.app.Activity.HomeActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.app.Adapter.MyViewPagerAdapter;
import com.example.app.util.util.PrefManager;

public class SliderActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip;
    private Button btnNext;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);


        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layout_dots);
        btnSkip = findViewById(R.id.btn_skip);
        btnNext = findViewById(R.id.btn_next);

        // Layouts of all welcome sliders
        layouts = new int[]{
                R.layout.welcome_slider1,
                R.layout.welcome_slider2,
                R.layout.welcome_slider3,
                R.layout.welcome_slider4
        };

        // Adding bottom dots
        addBottomDots(0);

        // Making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter(this, layouts);
        viewPager.setAdapter(myViewPagerAdapter);

        viewPager.addOnPageChangeListener(viewPagerChangeListener);

        btnSkip.setOnClickListener(v -> launchHomeScreen());
        btnNext.setOnClickListener(v -> {
            // Checking for the last page
            // If the last page, launch the home screen
            int current = getItem(+1);

            if (current < layouts.length) {
                // Move to the next screen
                viewPager.setCurrentItem(current);
            } else {
                launchHomeScreen();
            }
        });
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private final ViewPager.OnPageChangeListener viewPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            addBottomDots(position);

            // Changing the button text to 'NEXT' or 'GOT IT'
            if (position == layouts.length - 1) {
                // Last page, change text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorInactive = getResources().getIntArray(R.array.array_dot_inactive);
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35f);
            dots[i].setTextColor(colorInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(colorActive[currentPage]);
        }
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}