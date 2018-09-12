package com.budziaszek.tabmate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG =  "MainProcedure";

    private Fragment newFragment = null;
    private GroupsFragment groupsFragment = null;
    private FirebaseUser user = null;

    public String getCurrentUserEmail(){
        if(user!=null)
            return user.getEmail();
        else
            finish();
        return null;
    }

    public String getCurrentUserId(){
        if(user!=null)
            return user.getUid();
        else
            finish();
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initializeDrawer();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        TextView user_email = headerLayout.findViewById(R.id.user_email);
        user = FirebaseAuth.getInstance().getCurrentUser();

        user_email.setText(getCurrentUserEmail());

        if (savedInstanceState == null) {
           initializeFragments();

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;
        }else if(id == R.id.action_new_member){
            return false;
        }
        return false;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_dashboard){
            //TODO add dashboard fragment
        }else if (id == R.id.nav_tasks) {
            //TODO add tasks gragment
        }else if (id == R.id.nav_group) {
            newFragment = groupsFragment;
        }else if (id == R.id.nav_logOut) {
            alertAndLogOut();
        }

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());

        // Close the navigation drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void initializeDrawer(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                        if(newFragment != null) {
                            try {
                                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, newFragment).commit();
                            } catch (Exception e) {
                                Log.e(TAG, "Error in fragment transaction " + e.getMessage());
                            }
                            newFragment = null;
                        }
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initializeFragments(){
        try {
            this.groupsFragment = GroupsFragment.class.newInstance();
            //getSupportFragmentManager().beginTransaction().replace(R.id.flContent, groupsFragment).commit();
        } catch (Exception e) {
            Log.e(TAG, "Error initializing fragment " + e.getMessage());
        }
    }

    /**
     * Shows alert when user choose log out action. If action is confirmed Firebase signOut is called
     * and activity finishes.
     */
    private void alertAndLogOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);

        builder.setTitle(R.string.log_out)
                .setMessage(R.string.confirm_log_out)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                        MainActivity.this.startActivity(myIntent);
                        Log.d(TAG, "User logged out.");
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
