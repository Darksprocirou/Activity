package com.example.actividad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText ID, Nickname, Campeon;
    Spinner Rol, Division;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Lukras
        // El alfredo w
        ID = findViewById(R.id.txtID);
        Nickname = findViewById(R.id.txtNickname);
        Campeon = findViewById(R.id.txtCampeon);
        Rol = findViewById(R.id.spinnerRol);
        Division = findViewById(R.id.spinnerDivision);
        String [] Roles = {"Seleccione su rol:", "Top", "Jg", "Mid", "Adc", "Support"};
        String [] division = {"Seleccione su División:", "Hierro", "Bronce", "Plata", "Oro", "Platino",
                "Esmeralda", "Diamante", "Master", "Grand Master", "Challenger"};

        ArrayAdapter<String> adapterRol = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Roles);
        ArrayAdapter<String> adapterDiv = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, division);

        Rol.setAdapter(adapterRol);
        Division.setAdapter(adapterDiv);
    }

    //Create
    public void RegistrarUser(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "LeagueofLegends", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String IDUser = ID.getText().toString();
        String NicknameUser = Nickname.getText().toString();
        String CampeonUser = Campeon.getText().toString();
        int RolUser = Rol.getSelectedItemPosition();
        int DivisionUser = Division.getSelectedItemPosition();

        if (!NicknameUser.isEmpty() && !CampeonUser.isEmpty() &&
                !(RolUser == 0) && !(DivisionUser == 0)){
            ContentValues DatosUsuario = new ContentValues();
            DatosUsuario.put("idUsuario", IDUser);
            DatosUsuario.put("Nickname", NicknameUser);
            DatosUsuario.put("Campeon", CampeonUser);
            DatosUsuario.put("Rol", RolUser);
            DatosUsuario.put("Division", DivisionUser);
            BaseDatos.insert("Usuarios", null, DatosUsuario);
            BaseDatos.close();
            ID.setText("");
            Nickname.setText("");
            Campeon.setText("");
            Rol.setSelection(0);
            Division.setSelection(0);
            Toast.makeText(this, "Se ha registrado el usuario: '" + NicknameUser+"'.",
                    Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No puede haber campos vacíos.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Read
    public void BuscarUser(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "LeagueofLegends", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();

        String IDUser = ID.getText().toString();

        if(!IDUser.isEmpty()){
            Cursor fila = BaseDatos.rawQuery("Select Nickname, Campeon, Rol, Division from Usuarios where idUsuario=" + IDUser, null);

            if(fila.moveToFirst()){
                Nickname.setText(fila.getString(0));
                Campeon.setText(fila.getString(1));
                Rol.setSelection(fila.getInt(2));
                Division.setSelection(fila.getInt(3));
                BaseDatos.close();
            } else {
                Toast.makeText(this, "No se encontró el usuario con el ID ingresado.",
                        Toast.LENGTH_SHORT).show();
                Nickname.setText("");
                Campeon.setText("");
                Rol.setId(0);
                Division.setId(0);
                BaseDatos.close();
            }

        } else {
            Toast.makeText(this, "El campo ID no puede estar vacío.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Update
    public void ActualizarUsuario(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "LeagueofLegends", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String IDUser = ID.getText().toString();
        String NicknameUser = Nickname.getText().toString();
        String CampeonUser = Campeon.getText().toString();
        int RolUser = Rol.getSelectedItemPosition();
        int DivisionUser = Division.getSelectedItemPosition();
        if (!IDUser.isEmpty() && !NicknameUser.isEmpty() && !CampeonUser.isEmpty() && !(RolUser == 0) && !(DivisionUser == 0)){
            ContentValues DatosUsuario = new ContentValues();
            DatosUsuario.put("Nickname", NicknameUser);
            DatosUsuario.put("Campeon", CampeonUser);
            DatosUsuario.put("Rol", RolUser);
            DatosUsuario.put("Division", DivisionUser);
            int Cantidad = BaseDatos.update("Usuarios", DatosUsuario,
                    "idUsuario="+ IDUser, null);
            BaseDatos.close();
            if (Cantidad == 1){
                Toast.makeText(this, "El usuario con ID "+IDUser+" se actualizó correctamente.",
                        Toast.LENGTH_SHORT).show();
                ID.setText("");
                Nickname.setText("");
                Campeon.setText("");
                Rol.setSelection(0);
                Division.setSelection(0);

            } else {
                Toast.makeText(this, "No se encontró el ID ingresado.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No pueden haber campos vacíos.", Toast.LENGTH_SHORT).show();
        }
    }

    // Delete
    public void BtnEliminarUsuario(View view){
        String idUsuario = ID.getText().toString();

        if(!idUsuario.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("¡¡¡ADVERTENCIA!!!");
            builder.setMessage("¿Estás seguro de que deseas eliminar el usuario con ID n°"+idUsuario+"?");
            builder.setPositiveButton("Confirmar",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EliminarUsuario();
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Se ha cancelado la acción.", Toast.LENGTH_SHORT).show();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            Toast.makeText(this, "Por favor, ingrese un ID a eliminar.", Toast.LENGTH_SHORT).show();
        }

    }

    public void EliminarUsuario(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "LeagueofLegends", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String IDUsuario = ID.getText().toString();
        if (!IDUsuario.isEmpty()){
            int Eliminar = BaseDatos.delete("Usuarios", "idUsuario="+ IDUsuario, null);
            BaseDatos.close();
            if(Eliminar == 1){
                Toast.makeText(this, "Se eliminó el usuario n°" + IDUsuario + ".",
                        Toast.LENGTH_SHORT).show();
                ID.setText("");
                Nickname.setText("");
                Campeon.setText("");
                Rol.setSelection(0);
                Division.setSelection(0);
            } else {
                Toast.makeText(this, "El ID que intenta eliminar no existe. \nVerifique los datos en la tabla.",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "El campo de ID del usuario no puede estar vacío.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Mostrar en un activity nuevo los usuarios registrados
    public void MostrarUsuariosRegistrados(View view){
        Intent UsuariosRegistrados = new Intent(this, MostrarUsuariosRegistrados.class);
        startActivity(UsuariosRegistrados);
    }
}