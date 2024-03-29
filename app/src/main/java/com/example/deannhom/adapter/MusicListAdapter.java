package com.example.deannhom.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.deannhom.MusicPlayerActivity;
import com.example.deannhom.MyMediaPlayer;
import com.example.deannhom.R;
import com.example.deannhom.model.AudioModel;

import java.io.File;
import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicViewHolder> {
    ArrayList<AudioModel> songsList;
    Context context;
    SongUserCallback songUserCallback;

    public MusicListAdapter(ArrayList<AudioModel> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
    }

    public void setSongCallback(SongUserCallback songUserCallback){
        this.songUserCallback=songUserCallback;
    }

    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MusicViewHolder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        AudioModel songData = songsList.get(position);
        holder.titleTextView.setText(songData.getTitle());
        String path = "android.resource://" + context.getPackageName() + "/drawable/" + songData.getAvatar();

        holder.iconImageView.setImageURI(Uri.parse(path));
        holder.creatorTextView.setText(songData.getActors());

        if (MyMediaPlayer.currentIndex == position) {
            holder.titleTextView.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.titleTextView.setTextColor(Color.parseColor("#000000"));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to another activity
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = position;
                Intent intent = new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("LIST", songsList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView iconImageView;
        TextView creatorTextView;
        LinearLayout ivWrapper;

        public MusicViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.music_title_text);
            iconImageView = itemView.findViewById(R.id.icon_view);
            creatorTextView=itemView.findViewById(R.id.music_creator);
        }
    }

    public interface SongUserCallback{
        void onItemClickPlay(String songName);
        void onItemClickStop();
    }
}
