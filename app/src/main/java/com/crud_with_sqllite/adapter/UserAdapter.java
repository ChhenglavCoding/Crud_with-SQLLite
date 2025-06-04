package com.crud_with_sqllite.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crud_with_sqllite.Model.User;
import com.crud_with_sqllite.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    public interface UserListener {
        void onEdit(User user);
        void onDelete(User user);
    }

    private List<User> userList;
    private UserListener listener;

    public UserAdapter(List<User> list, UserListener listener) {
        this.userList = list;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvGender;
        ImageView btnEdit, btnDelete;

        public ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvEmail = v.findViewById(R.id.tvEmail);
            tvGender = v.findViewById(R.id.tvGender);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(ViewHolder h, int pos) {
        User u = userList.get(pos);
        h.tvName.setText(u.getFirstName().toUpperCase() + " " + u.getLastName().toUpperCase());
        h.tvEmail.setText(u.getEmail());
        h.tvGender.setText(u.getGender());

        h.btnEdit.setOnClickListener(v -> listener.onEdit(u));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(u));
    }

    public int getItemCount() { return userList.size(); }
}
