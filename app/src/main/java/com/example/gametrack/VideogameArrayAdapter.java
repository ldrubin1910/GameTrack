package com.example.gametrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class VideogameArrayAdapter extends ArrayAdapter<Videogame> {
    private Context myContext;
    private List<Videogame> videogameList;

    public VideogameArrayAdapter(@NonNull Context context, int resource, @NonNull List<Videogame> objects) {
        super(context, 0, objects);
        this.myContext = context;
        this.videogameList = objects;
    }

    public static class ViewHolder {
        TextView textViewTitle;
        TextView textViewGenre;
        TextView textViewPlatform;
        TextView textViewDeveloper;
        TextView textViewReleaseYear;
        CheckBox checkBoxOwned;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater from = LayoutInflater.from(myContext);
            convertView = from.inflate(R.layout.videogame_list_item, parent, false);

            convertView.setLongClickable(true);

            viewHolder.textViewTitle =convertView.findViewById(R.id.textViewTitle);
            viewHolder.textViewGenre = convertView.findViewById(R.id.textViewGenre);
            viewHolder.textViewPlatform = convertView.findViewById(R.id.textViewPlatform);
            viewHolder.textViewDeveloper = convertView.findViewById(R.id.textViewDeveloper);
            viewHolder.textViewReleaseYear = convertView.findViewById(R.id.textViewReleaseYear);
            viewHolder.checkBoxOwned = convertView.findViewById(R.id.checkBoxOwned);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textViewTitle.setText(getItem(position).getTitle());
        viewHolder.textViewGenre.setText(getItem(position).getGenre());
        viewHolder.textViewPlatform.setText(getItem(position).getPlatform());
        viewHolder.textViewDeveloper.setText(getItem(position).getDeveloper());
        viewHolder.textViewReleaseYear.setText(String.valueOf(getItem(position).getReleaseYear()));

        viewHolder.checkBoxOwned.setChecked(getItem(position).isOwned());

        convertView.setTag(viewHolder);

        return convertView;
    }
}
