package com.example.edward.orthography;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

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
        fm.beginTransaction().replace(R.id.ContenedorPrincipal,m,m.getTag()).commit();



        txtnameUser.setText(nombre);
        txtcorreoUser.setText(correo);
        imgavatar.setImageResource(idimagen);
    }

   @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            moveTaskToBack(true);
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
            MensajeBox("Letter's War 1.0.0\n\n" +
                    "Edward Gómez 201212838\n" +
                    "Fernando González 201222587\n" +
                    "Trabajo de graduación\n" +
                    "Facultad de Ingeniería\n" +
                    "Universidad de San Carlos de Guatemala\n\n" +
                    "Built on November 4,2016\n" +
                    "2016 All rights reserved.","Acerca de");


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
            if(getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Letter's War");
            }
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
            fm.beginTransaction().replace(R.id.ContenedorPrincipal,m,m.getTag()).commit();

        } else if (id == R.id.nav_perfil) {
            if(getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Mi perfil");
            }
            Miperfil m = new Miperfil();
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
            fm.beginTransaction().replace(R.id.ContenedorPrincipal,m,m.getTag()).commit();

        } else if (id == R.id.nav_progreso) {
            if(getSupportActionBar() != null){
                getSupportActionBar().setTitle("Mi progreso");
            }
            Miprogreso m = new Miprogreso();
            Bundle args = new Bundle(); //para pasarle datos al fragmento
            args.putString("correo",correo);
            args.putInt("nivel",nivel);
            args.putInt("idUsuario",idUsuario);
            args.putInt("puntos",puntos);
            args.putDouble("estrellas",estrellas);
            args.putString("nombre",nombre);
            args.putInt("idimagen",idimagen);
            m.setArguments(args);


            FragmentManager mg = getSupportFragmentManager();
            mg.beginTransaction().replace(R.id.ContenedorPrincipal,m,m.getTag()).commit();

        }else if (id == R.id.nav_Ayuda) {
            if(getSupportActionBar() != null){
                getSupportActionBar().setTitle("Ayuda");
            }
            ayuda m = new ayuda();
            FragmentManager mg = getSupportFragmentManager();
            mg.beginTransaction().replace(R.id.ContenedorPrincipal,m,m.getTag()).commit();

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

    public void MensajeBox(String mensaje,String titulo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje)
                .setTitle(titulo)
                .setIcon(R.drawable.info)
                .setCancelable(false)
                .setNeutralButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
