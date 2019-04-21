package com.example.bimvendpro;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Window;


public class InventoryItemHistoryDialogue extends Dialog {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String code;


    public InventoryItemHistoryDialogue(Context context, String code) {
        super(context);
        this.code=code;
    }

    public InventoryItemHistoryDialogue(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected InventoryItemHistoryDialogue( Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.item_history_dlg);

        init();


    }

    private void init(){

        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Product"));
        tabLayout.addTab(tabLayout.newTab().setText("Machine"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }
}
