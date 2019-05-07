package com.example.bimvendpro;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    public static final int DASHBOARD=1, MACHINE=2,LOCATIONS=3,INVENTORY=4,INGREDIENTS=5;
    private String neededCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Dashboard()).commit();
        toolbar.setTitle("Dashboard");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    private int currFrag=DASHBOARD;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.action_add){
            if(currFrag==DASHBOARD){

            }else if(currFrag==INVENTORY){
                new InventoryItemAddDialogue(this).show();
            }else if(currFrag==MACHINE){
                new MachineAddDialogue(this).show();
            }else if(currFrag==INGREDIENTS) {
                new MachineIngredientsAddDialogue(this,neededCode).show();
                return true;
            }
        } else if(id==R.id.action_search){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Dashboard()).commit();
            currFrag=DASHBOARD;

            toolbar.setTitle("Dashboard");
        } else if (id == R.id.nav_inventory) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new InventoryFragment()).commit();
            currFrag=INVENTORY;
            toolbar.setTitle("Inventory");

        } else if (id == R.id.nav_machines) {
            MachineFragment machineFragment=new MachineFragment();
            Bundle args = new Bundle();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MachineFragment()).commit();
            currFrag=MACHINE;
            toolbar.setTitle("Machine");
        } else if (id == R.id.nav_location) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new LocationFragment()).commit();
            toolbar.setTitle("Locations");
            currFrag=LOCATIONS;
        } else if (id == R.id.nav_routes) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Routes()).commit();
            toolbar.setTitle("Routes");

        } else if (id == R.id.nav_driver) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DriversFragment()).commit();
            toolbar.setTitle("Drivers");
        }
        else if (id == R.id.nav_expenses) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ExpenseFragment()).commit();
            toolbar.setTitle("Expenses");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    public void changeFragment(Fragment newfragment, String title, String neededCode, int type){
        Bundle bundle = new Bundle();
        bundle.putString("code", neededCode);
        newfragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,newfragment).commit();
        toolbar.setTitle(title);
        currFrag=type;
        this.neededCode=neededCode;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
