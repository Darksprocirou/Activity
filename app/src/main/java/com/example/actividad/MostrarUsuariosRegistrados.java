package com.example.actividad;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MostrarUsuariosRegistrados extends AppCompatActivity {

    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_usuarios_registrados);
        lista = findViewById(R.id.listaUsuarios);

        CargaUsuarios();
    }

    public void CargaUsuarios(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "LeagueofLegends", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        Cursor fila = BaseDatos.rawQuery("Select * from Usuarios", null);
        ArrayList<String> ListaUsuarios = new ArrayList<>();
        if (fila.moveToFirst()){
            do {
                String IDUser = fila.getString(0);
                String NicknameUser = fila.getString(1);
                String CampeonUser = fila.getString(2);
                int RolUser = fila.getInt(3);
                int DivisionUser = fila.getInt(4);

                // Rol del usuario
                String RolUsuario = "";
                if (RolUser == 1){
                    RolUsuario = "Top";
                }else if (RolUser == 2){
                    RolUsuario = "Jg";
                }else if (RolUser == 3) {
                    RolUsuario = "Mid";
                }else if (RolUser == 4) {
                    RolUsuario = "Adc";
                }else if (RolUser == 5) {
                    RolUsuario = "Support";
                }

                // División del usuario
                String DivUsuario = "";
                if(DivisionUser == 1){
                    DivUsuario = "Hierro";
                }else if(DivisionUser == 2){
                    DivUsuario = "Bronce";
                }else if(DivisionUser == 3){
                    DivUsuario = "Plata";
                }else if(DivisionUser == 4){
                    DivUsuario = "Oro";
                }else if(DivisionUser == 5){
                    DivUsuario = "Platino";
                }else if(DivisionUser == 6){
                    DivUsuario = "Esmeralda";
                }else if(DivisionUser == 7){
                    DivUsuario = "Diamante";
                }else if(DivisionUser == 8){
                    DivUsuario = "Master";
                }else if(DivisionUser == 9){
                    DivUsuario = "Grand Master";
                }else if(DivisionUser == 10){
                    DivUsuario = "Challenger";
                }
                String userInfo = "ID: "+IDUser + ", Apodo usuario: " +
                        NicknameUser + ", Campeón: " + CampeonUser + ", Rol: " + RolUsuario + ", División: " + DivUsuario;
                ListaUsuarios.add(userInfo);
            } while (fila.moveToNext());
        }else{
            Toast.makeText(this, "No hay usuarios registrados todavía.", Toast.LENGTH_LONG).show();
        }
        BaseDatos.close();
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, ListaUsuarios);
        lista.setAdapter(adapter);
    }

    public void ActualizarTabla(View view){
        CargaUsuarios();
    }
}