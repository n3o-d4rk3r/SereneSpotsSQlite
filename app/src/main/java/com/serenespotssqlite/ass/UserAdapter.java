package com.serenespotssqlite.ass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> userList;
    private Context context;
    private DatabaseHelper databaseHelper;

    public UserAdapter(Context context) {
        this.context = context;
        this.userList = new ArrayList<>();
        this.databaseHelper = new DatabaseHelper(context);
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImageView;
        private TextView userNameTextView;
        private TextView admissionNoTextView;
        private TextView textTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.userImageView);
            userNameTextView = itemView.findViewById(R.id.nameTextView);
            admissionNoTextView = itemView.findViewById(R.id.admissionTextView);
            textTextView = itemView.findViewById(R.id.textTextView);
        }

        public void bind(User user) {
            // Bind data to views
            userNameTextView.setText(user.getName());
            admissionNoTextView.setText(user.getAdmissionNo());
            textTextView.setText(user.getText());

            // Load image using AsyncTask
            new LoadImageAsyncTask(userImageView).execute(user);
        }
    }

    private class LoadImageAsyncTask extends AsyncTask<User, Void, String> {

        private final ImageView userImageView;
        private User user;

        LoadImageAsyncTask(ImageView userImageView) {
            this.userImageView = userImageView;
        }

        @Override
        protected String doInBackground(User... users) {
            this.user = users[0];
            return loadImagePathFromDatabase(user.getId());
        }

        @Override
        protected void onPostExecute(String imagePath) {
            if (imagePath != null && !imagePath.isEmpty() && new File(imagePath).exists()) {
                // Load image using Glide
                Glide.with(context)
                        .load(imagePath)
                        .placeholder(R.drawable.ic_profile)
                        .into(userImageView);
            } else {
                userImageView.setImageResource(R.drawable.ic_profile); // Set a default image if the file is not found
                Log.e("LoadImageAsyncTask", "File not found at path: " + imagePath);
            }
        }

        private String loadImagePathFromDatabase(int userId) {
            String imagePath = null;
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            Cursor cursor = null;

            try {
                cursor = db.rawQuery("SELECT " + DatabaseHelper.COLUMN_IMAGE_PATH +
                        " FROM " + DatabaseHelper.TABLE_NAME +
                        " WHERE " + DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});

                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_PATH);
                    if (columnIndex != -1) {
                        imagePath = cursor.getString(columnIndex);
                    } else {
                        Log.e("UserAdapter", "Image column index not found");
                    }
                }
            } catch (Exception e) {
                Log.e("UserAdapter", "Error loading image: " + e.getMessage());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                // try-with-resources automatically closes the SQLiteDatabase
            }

            return imagePath;
        }
    }
}
