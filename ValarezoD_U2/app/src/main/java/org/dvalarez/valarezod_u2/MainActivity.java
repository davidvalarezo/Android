package org.dvalarez.valarezod_u2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private  static int[] imgIds = {
            R.drawable.ic_home,
            R.drawable.ic_layout,
            R.drawable.ic_button_imagen,
            R.drawable.ic_calcu
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new MiPagerAdapter(getSupportFragmentManager()));
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setIcon(imgIds[0]);
        tabs.getTabAt(1).setIcon(imgIds[1]);
        tabs.getTabAt(2).setIcon(imgIds[2]);
        tabs.getTabAt(3).setIcon(imgIds[3]);

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
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        if (id == R.id.acercaDe){
            lanzarAcercaDe(null);
            return true;
        }
        if (id == R.id.salida){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MiPagerAdapter extends FragmentPagerAdapter {
        public MiPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0: fragment = new Tab1();
                    break;
                case 1: fragment = new Tab2();
                    break;
                case 2: fragment = new Tab3();
                    break;
                case 3: fragment = new Calculadora();
                    break;

            }
            return fragment;
        }

        @Override public int getCount() {
            return 4;
        }
        @Override public CharSequence getPageTitle(int position) {

            String tab1 = getString(R.string.titleApp);
            String tab2 = getString(R.string.tab2_name);
            String tab3 = getString(R.string.tab3_name);
            String tab4 = getString(R.string.tab4_name);
            switch (position) {
                case 0: return tab1;
                case 1: return tab2;
                case 2: return tab3;
                case 3: return tab4;
            }
            return null;
        }
    }


    public void lanzarAcercaDe (View view){
        Intent i = new Intent(this,AcercaDeActivity.class );
        startActivity(i);
    }


}
