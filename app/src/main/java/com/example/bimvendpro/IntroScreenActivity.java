package com.example.bimvendpro;

import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntroScreenActivity extends AppCompatActivity {

    ViewPager viewPager;
    IntroAdapter mAdapter;
    LinearLayout linearLayout;

    Button mBackBtn,mNextBtn;

    int mCurrentPage;

    private TextView[] mdots;

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            Intent i = new Intent(IntroScreenActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }


        viewPager = findViewById(R.id.intro_holder);
        linearLayout = findViewById(R.id.dot_layout);

        mBackBtn = findViewById(R.id.intro_back);
        mNextBtn = findViewById(R.id.intro_next);

        mBackBtn.setEnabled(false);
        mBackBtn.setVisibility(View.INVISIBLE);
        mBackBtn.setText("");



        mAdapter = new IntroAdapter(this);

        viewPager.setAdapter(mAdapter);

        addDotsIndecatior(0);

        viewPager.addOnPageChangeListener(pageChangeListener);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(mCurrentPage -1);
            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mNextBtn.getText().equals("Finish")){
                    prefManager.setFirstTimeLaunch(false);
                    Intent i = new Intent(IntroScreenActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    viewPager.setCurrentItem(mCurrentPage +1);
                }

            }
        });
    }

    private void addDotsIndecatior(int pos) {

        mdots = new TextView[4];
        linearLayout.removeAllViews();

        for(int i = 0;i<mdots.length;i++){
            mdots[i] = new TextView(this);
            mdots[i].setText(Html.fromHtml("&#8226;"));
            mdots[i].setTextSize(35);
            mdots[i].setTextColor(getResources().getColor(R.color.colorWhite));

            linearLayout.addView(mdots[i]);

        }

        if(mdots.length>0){
            mdots[pos].setTextColor(getResources().getColor(R.color.colorPink));
        }
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndecatior(i);

            mCurrentPage = i;

            if(i == 0){
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(false);
                mBackBtn.setVisibility(View.INVISIBLE);

                mNextBtn.setText("Next");
                mBackBtn.setText("");
            }
            else if(i == mdots.length-1){
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("Finish");
                mBackBtn.setText("Back");
            }
            else{
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("Next");
                mBackBtn.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
