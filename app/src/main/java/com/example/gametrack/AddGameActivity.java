package com.example.gametrack;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddGameActivity extends AppCompatActivity {
    private Intent resultIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        EditText editTextTitle = findViewById(R.id.editTextTitle);
        EditText editTextGenre = findViewById(R.id.editTextGenre);
        EditText editTextPlatform = findViewById(R.id.editTextPlatform);
        EditText editTextDeveloper = findViewById(R.id.editTextDeveloper);
        EditText editTextReleaseYear = findViewById(R.id.editTextReleaseYear);
        CheckBox checkBoxOwned = findViewById(R.id.checkBoxOwned);
        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonCancel = findViewById(R.id.buttonCancel);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString().trim();
                String genre = editTextGenre.getText().toString().trim();
                String platform = editTextPlatform.getText().toString().trim();
                String developer = editTextDeveloper.getText().toString().trim();
                String releaseYearStr = editTextReleaseYear.getText().toString().trim();

                if (title.isEmpty() || genre.isEmpty() || platform.isEmpty() || developer.isEmpty() || releaseYearStr.isEmpty()) {
                    Toast.makeText(AddGameActivity.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        int releaseYear = Integer.parseInt(releaseYearStr);
                        boolean owned = checkBoxOwned.isChecked();

                        resultIntent = new Intent();
                        resultIntent.putExtra("title", title);
                        resultIntent.putExtra("genre", genre);
                        resultIntent.putExtra("platform", platform);
                        resultIntent.putExtra("developer", developer);
                        resultIntent.putExtra("releaseYear", releaseYear);
                        resultIntent.putExtra("owned", owned);

                        showAddCommentDialog(resultIntent);
                    } catch (NumberFormatException e) {
                        Toast.makeText(AddGameActivity.this, "El año de lanzamiento debe ser un número válido.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    private void showAddCommentDialog(Intent gameIntent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Añadir comentario");
        builder.setMessage("¿Deseas añadir un comentario a este videojuego?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent commentIntent = new Intent(AddGameActivity.this, AddCommentActivity.class);
                startActivityForResult(commentIntent, 2);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setResult(Activity.RESULT_OK, gameIntent);
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            String comment = data.getExtras().getString("comment");
            resultIntent.putExtra("comment", comment);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}
