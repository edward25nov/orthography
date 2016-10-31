package com.example.edward.orthography;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    sessionManager manager;
    ImageView imgavatar;
    TextView txtnameUser;
    TextView txtcorreoUser;

    //variables
    String correo;
    int nivel;
    int idUsuario;
    int puntos;
    double estrellas;
    String nombre;
    int idimagen;

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

        // el menu explandigle el nav_view es donde aparece el nombre la imagen y correo
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //obtengo la vista o contexto del menu expandible
        View hView =  navigationView.getHeaderView(0);
        imgavatar = (ImageView) hView.findViewById(R.id.imgAvatar);
        txtnameUser = (TextView) hView.findViewById(R.id.txtNameUser);
        txtcorreoUser = (TextView) hView.findViewById(R.id.txtCorreoUsuario);

        //agregando los datos al menu hamburguesa y recuperando datos del intent o  variables de sesión
        correo = getIntent().getStringExtra("correo");
        if(correo==null){//si no lo recupero por variables de sesión
            correo = manager.getPreferences(this, "correo");
        }

        nivel = getIntent().getIntExtra("nivel",-1);
        if(nivel==-1){ //si no lo recupero por variables de sesión
            nivel = Integer.valueOf(manager.getPreferences(this,"nivel"));
        }

        idUsuario = getIntent().getIntExtra("idUsuario",-1);
        if(idUsuario==-1){
            idUsuario = Integer.valueOf(manager.getPreferences(this,"idUsuario"));
        }

        puntos = getIntent().getIntExtra("puntos",-1);
        if(puntos==-1){
            puntos = Integer.valueOf(manager.getPreferences(this,"puntos"));
        }

        estrellas = getIntent().getDoubleExtra("Estrellas",-1);
        if(estrellas==-1){
            estrellas = Double.valueOf(manager.getPreferences(this,"Estrellas"));
        }

        nombre = getIntent().getStringExtra("Nombre");
        if(nombre==null){
            nombre = manager.getPreferences(this, "Nombre");
        }

        idimagen = getIntent().getIntExtra("Imagen",-1);
        if(idimagen==-1){
            idimagen = Integer.valueOf(manager.getPreferences(this,"Imagen"));
        }

        //agregamos las pestañas al activity principal
        agregarPestañas();
        //aqui agregamos el fragmento lecciones al tab1
        lecciones m = new lecciones();
        Bundle args = new Bundle(); //para pasarle datos al fragmento
        args.putString("correo",correo);
        args.putInt("nivel",nivel);
        args.putInt("idUsuario",idUsuario);
        args.putInt("puntos",puntos);
        args.putDouble("estrellas",estrellas);
        args.putString("nombre",nombre);
        args.putInt("idimagen",idimagen);
        m.setArguments(args);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.tab1,m,m.getTag()).commit();
        //aqui agregamos el fragmento amigos al tab2
        AmigosFragment nuevoFragment = new AmigosFragment();
        FragmentManager mg = getSupportFragmentManager();
        mg.beginTransaction().replace(R.id.tab2,nuevoFragment,nuevoFragment.getTag()).commit();



        txtnameUser.setText(nombre);
        txtcorreoUser.setText(correo);
        imgavatar.setImageResource(idimagen);
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

           /*** this.finish();
            Intent intent = new Intent(getApplicationContext(),Splash.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            *esto funcionaba bien
            **/
            moveTaskToBack(true);
         /*   finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
*/

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
            lecciones m = new lecciones();
            Bundle args = new Bundle(); //para pasarle datos al fragmento
            args.putString("correo",correo);
            args.putInt("nivel",nivel);
            args.putInt("idUsuario",idUsuario);
            args.putInt("puntos",puntos);
            args.putDouble("estrellas",estrellas);
            args.putString("nombre",nombre);
            args.putInt("idimagen",idimagen);
            m.setArguments(args);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.tab1,m,m.getTag()).commit();

            AmigosFragment nuevoFragment = new AmigosFragment();
            FragmentManager mg = getSupportFragmentManager();
            mg.beginTransaction().replace(R.id.tab2,nuevoFragment,nuevoFragment.getTag()).commit();
        } else if (id == R.id.nav_perfil) {

        } else if (id == R.id.nav_progreso) {

        } else if (id == R.id.nav_ajustes) {

        } else if (id == R.id.nav_acercade) {

        } else if (id == R.id.nav_salir) {

            manager.setPreferences(this, "status", "0");
            this.finish();
            Intent intent = new Intent(MainActivity.this, Splash.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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
