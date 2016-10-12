package com.example.edward.orthography;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    sessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = new sessionManager();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //agregamos las pestañas al activity principal
        agregarPestañas();
        //aqui agregamos el fragmento amigos al tab2
        AmigosFragment nuevoFragment = new AmigosFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.tab2,nuevoFragment,nuevoFragment.getTag()).commit();

    }

    public void agregarPestañas(){
        TabHost TbH = (TabHost) findViewById(R.id.tabHost); //llamamos al Tabhost
        TbH.setup();                                       //lo activamos

        TabHost.TabSpec tab1 = TbH.newTabSpec("tab1");  //aspectos de cada Tab (pestaña)
        TabHost.TabSpec tab2 = TbH.newTabSpec("tab2");
       // TabHost.TabSpec tab3 = TbH.newTabSpec("tab3");

        tab1.setIndicator("LECCIONES");//qué queremos que aparezca en las pestañas
        tab1.setContent(R.id.tab1); //definimos el id de cada Tab (pestaña)

        tab2.setIndicator("AMIGOS");
        tab2.setContent(R.id.tab2);

       // tab3.setIndicator("TRES");
        // tab3.setContent(R.id.tab3);

        TbH.addTab(tab1); //añadimos los tabs ya programados
        TbH.addTab(tab2);
       // TbH.addTab(tab3);
        TbH.setCurrentTab(0);


    }


   @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            this.finish();
            Intent intent = new Intent(getApplicationContext(),Splash.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

          /*  manager.setPreferences(MainActivity.this, "status", "1");
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);*/
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action

            /***Reemplanzado el layout principal por un fragmento***/
          /*  TabsFragment nuevoFragment = new TabsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.Layout_a_ser_reemplazado,nuevoFragment,nuevoFragment.getTag()).commit();
            */
        } else if (id == R.id.nav_perfil) {

        } else if (id == R.id.nav_progreso) {

        } else if (id == R.id.nav_ajustes) {

        } else if (id == R.id.nav_acercade) {

        } else if (id == R.id.nav_salir) {

            manager.setPreferences(MainActivity.this, "status", "0");
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


/**
 *
 * Puede cerrar todas las actividades de fondo y cuando se vuelva a abrir la aplicación Se parte de la primera actividad

 this.finish();
 Intent intent = new Intent(getApplicationContext(), CloseApp.class);
 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 startActivity(intent);

 Puede cerrar todas las actividades de fondo y cuando se vuelva a abrir la aplicación Se parte de la actividad en pausa [ donde cerró ] actividad

 this.finish();
 Intent intent = new Intent(Intent.ACTION_MAIN);
 intent.addCategory(Intent.CATEGORY_HOME);
 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 startActivity(intent);

 *
 */
