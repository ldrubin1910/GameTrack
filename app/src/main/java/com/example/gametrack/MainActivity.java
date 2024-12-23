package com.example.gametrack;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Videogame> gameList;
    private ArrayAdapter<Videogame> adapter;
    private DBManager dbManager;
    private CommentDBManager commentDBManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView listViewGame = findViewById(R.id.listViewGame);
        Button buttonAdd = findViewById(R.id.buttonAdd);

        gameList = new ArrayList<>();
        adapter = new VideogameArrayAdapter(this, R.layout.videogame_list_item, gameList);
        dbManager = new DBManager(this);
        commentDBManager = new CommentDBManager(this);

        listViewGame.setAdapter(adapter);

        loadGamesFromDatabase();

        registerForContextMenu(listViewGame);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddGameActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String title = data.getExtras().getString("title");
            String genre = data.getExtras().getString("genre");
            String platform = data.getExtras().getString("platform");
            String developer = data.getExtras().getString("developer");
            int releaseYear = data.getExtras().getInt("releaseYear", 0);
            boolean owned = data.getExtras().getBoolean("owned", false);

            long newId = dbManager.insertVideogame(title, genre, platform, developer, releaseYear, owned);
            if (newId != -1) {
                gameList.add(new Videogame(newId, title, genre, platform, developer, releaseYear, owned));
                adapter.notifyDataSetChanged();

                String comment = data.getExtras().getString("comment");
                if (comment != null && !comment.isEmpty()) {
                    commentDBManager.insertComment(newId, comment);
                }
            }

        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            int position = data.getExtras().getInt("position", -1);

            if (position != -1) {
                Videogame game = gameList.get(position);

                game.setTitle(data.getExtras().getString("title"));
                game.setGenre(data.getExtras().getString("genre"));
                game.setPlatform(data.getExtras().getString("platform"));
                game.setDeveloper(data.getExtras().getString("developer"));
                game.setReleaseYear(data.getExtras().getInt("releaseYear", 0));
                game.setOwned(data.getExtras().getBoolean("owned", false));

                int rowsUpdated = dbManager.updateVideogame(
                        game.getId(),
                        game.getTitle(),
                        game.getGenre(),
                        game.getPlatform(),
                        game.getDeveloper(),
                        game.getReleaseYear(),
                        game.isOwned()
                );

                if (rowsUpdated > 0) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu( menu );
        this.getMenuInflater().inflate( R.menu.main_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean toret = false;

        int itemId = item.getItemId();
        if (itemId == R.id.filter_owned) {
            filterGames(true);
            toret = true;

        } else if (itemId == R.id.filter_not_owned) {
            filterGames(false);
            toret = true;


        } else if (itemId == R.id.show_all) {
            adapter = new VideogameArrayAdapter(this, R.layout.videogame_list_item, gameList);
            ((ListView) findViewById(R.id.listViewGame)).setAdapter(adapter);
            toret = true;
        }

        return toret;
    }

    // Filtra la lista de videojuegos según la propiedad "owned"
    private void filterGames(boolean owned) {
        ArrayList<Videogame> filteredList = new ArrayList<>();
        for (Videogame game : gameList) {
            if (game.isOwned() == owned) {
                filteredList.add(game);
            }
        }

        adapter = new VideogameArrayAdapter(this, R.layout.videogame_list_item, filteredList);
        ListView listView = findViewById(R.id.listViewGame);
        listView.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo cmi) {
        this.getMenuInflater().inflate( R.menu.context_menu, contextMenu );
        contextMenu.setHeaderTitle( R.string.app_name );
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        boolean toret = false;

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        int itemId = item.getItemId();
        if (itemId == R.id.context_delete) {
            showDeleteConfirmationDialog(position);
            toret = true;
        } else if (itemId == R.id.context_edit) {
            Videogame game = gameList.get(position);
            Intent intent = new Intent(MainActivity.this, EditGameActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("title", game.getTitle());
            intent.putExtra("genre", game.getGenre());
            intent.putExtra("platform", game.getPlatform());
            intent.putExtra("developer", game.getDeveloper());
            intent.putExtra("releaseYear", game.getReleaseYear());
            intent.putExtra("owned", game.isOwned());
            startActivityForResult(intent, 2);
            toret = true;
        } else if (itemId == R.id.context_details) {
            long gameId = gameList.get(position).getId();
            Intent intent = new Intent(this, GameDetailsActivity.class);

            Cursor cursor = dbManager.getVideogameById(gameId);
            if (cursor.moveToFirst()) {
                String title = cursor.getString(1);
                String genre = cursor.getString(2);
                String platform = cursor.getString(3);
                String developer = cursor.getString(4);
                int releaseYear = cursor.getInt(5);
                boolean owned = cursor.getInt(6) == 1;

                intent.putExtra("game_id", gameId);
                intent.putExtra("title", title);
                intent.putExtra("genre", genre);
                intent.putExtra("platform", platform);
                intent.putExtra("developer", developer);
                intent.putExtra("release_year", releaseYear);
                intent.putExtra("owned", owned);

                String comment = commentDBManager.getCommentByGameId(gameId);
                intent.putExtra("comment", comment);

                startActivity(intent);
                cursor.close();
            }

            toret = true;
        }

        return toret;
    }

    // Muestra un diálogo de confirmación antes de borrar un videojuego
    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar borrado");
        builder.setMessage("¿Estás seguro de que quieres borrar este videojuego?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Videogame game = gameList.get(position);

                int rowsDeleted = dbManager.deleteVideogame(game.getId());
                if (rowsDeleted > 0) {
                    commentDBManager.deleteCommentByGameId(game.getId());

                    gameList.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }

    // Carga la lista de videojuegos desde la base de datos
    private void loadGamesFromDatabase() {
        gameList.clear();
        Cursor cursor = dbManager.getAllVideogames();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DBManager.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DBManager.COLUMN_TITLE));
                String genre = cursor.getString(cursor.getColumnIndexOrThrow(DBManager.COLUMN_GENRE));
                String platform = cursor.getString(cursor.getColumnIndexOrThrow(DBManager.COLUMN_PLATFORM));
                String developer = cursor.getString(cursor.getColumnIndexOrThrow(DBManager.COLUMN_DEVELOPER));
                int releaseYear = cursor.getInt(cursor.getColumnIndexOrThrow(DBManager.COLUMN_RELEASE_YEAR));
                boolean owned = cursor.getInt(cursor.getColumnIndexOrThrow(DBManager.COLUMN_OWNED)) == 1;

                Videogame game = new Videogame(id, title, genre, platform, developer, releaseYear, owned);
                gameList.add(game);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        adapter.notifyDataSetChanged();
    }


}