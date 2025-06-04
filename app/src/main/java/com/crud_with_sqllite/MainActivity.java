package com.crud_with_sqllite;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crud_with_sqllite.Model.User;
import com.crud_with_sqllite.adapter.UserAdapter;
import com.crud_with_sqllite.database.DatabaseHelper;
import com.crud_with_sqllite.util.MessageUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    RecyclerView recyclerView;
    UserAdapter adapter;
    List<User> userList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadUsers();

        btnAdd.setOnClickListener(v -> showUserDialog(null));
    }

    void loadUsers() {
        userList = db.getAllUsers();
        adapter = new UserAdapter(userList, new UserAdapter.UserListener() {
            public void onEdit(User user) { showUserDialog(user); }

            public void onDelete(User user) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure to delete this user?")
                        .setPositiveButton("Yes", (d, w) -> {
                            db.deleteUser(user.getId());
                            loadUsers();
                            MessageUtil.showMessage("Deleted",MainActivity.this);
                        }).setNegativeButton("No", null).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    void showUserDialog(User userToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_user, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();


        EditText etFirstName = view.findViewById(R.id.etFirstName);
        EditText etLastName = view.findViewById(R.id.etLastName);
        Spinner spinnerGender = view.findViewById(R.id.spinnerGender);
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etAddress = view.findViewById(R.id.etAddress);
        EditText etPhone = view.findViewById(R.id.etPhone);
        Spinner spinnerStatus = view.findViewById(R.id.spinnerStatus);
        Button btnSave = view.findViewById(R.id.btnSave);

        // Spinner setup
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Male", "Female", "Other"});
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Active", "Inactive"});
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);




        if (userToEdit != null) {
            etFirstName.setText(userToEdit.getFirstName());
            etLastName.setText(userToEdit.getLastName());
            spinnerGender.setSelection(genderAdapter.getPosition(userToEdit.getGender()));
            etEmail.setText(userToEdit.getEmail());
            etAddress.setText(userToEdit.getAddress());
            etPhone.setText(userToEdit.getPhone());
            spinnerStatus.setSelection(genderAdapter.getPosition(userToEdit.getStatus()));
        }

        btnSave.setOnClickListener(v -> {
            String first = etFirstName.getText().toString().trim();
            String last = etLastName.getText().toString().trim();
            String gender = spinnerGender.getSelectedItem().toString().trim();
            String email = etEmail.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String status = spinnerStatus.getSelectedItem().toString().trim();

            if (first.isEmpty() || last.isEmpty() || gender.isEmpty() || email.isEmpty() ||
                    address.isEmpty() || phone.isEmpty() || status.isEmpty()) {
               MessageUtil.showMessage("All fields required",this);
                return;
            }

            if (userToEdit == null) {
                db.addUser(new User(0, first, last, gender, email, address, phone, status));
                MessageUtil.showMessage( "User added", this);
            } else {
                userToEdit.setFirstName(first);
                userToEdit.setLastName(last);
                userToEdit.setGender(gender);
                userToEdit.setEmail(email);
                userToEdit.setAddress(address);
                userToEdit.setPhone(phone);
                userToEdit.setStatus(status);
                db.updateUser(userToEdit);
                MessageUtil.showMessage( "User updated", this);
            }

            dialog.dismiss();
            loadUsers();
        });

        dialog.show();
    }
}
