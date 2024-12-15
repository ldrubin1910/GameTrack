package com.example.gametrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        EditText editTextComment = findViewById(R.id.editTextComment);

        Button button = findViewById(R.id.buttonSaveComment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = editTextComment.getText().toString().trim();
                if (!comment.isEmpty()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("comment", comment);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(AddCommentActivity.this, "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
