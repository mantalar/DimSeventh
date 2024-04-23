package id.go.kominfo.roman.dimseventh;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import id.go.kominfo.roman.dimseventh.adapter.FriendAdapter;
import id.go.kominfo.roman.dimseventh.helper.FriendDB;
import id.go.kominfo.roman.dimseventh.model.Friend;

public class MainActivity extends AppCompatActivity {
    private final List<Friend> mList = new ArrayList<>();
    private FriendAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));

        mAdapter = new FriendAdapter(mList, this);

        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(mAdapter);

        listView.setOnItemLongClickListener(this::itemOnLongClick);

        findViewById(R.id.fab).setOnClickListener(v -> createDialog(null).show());
    }

    private boolean itemOnLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        CharSequence[] options = {"Edit", "Delete"};
        int[] checked = {-1};
        new AlertDialog.Builder(this)
                .setTitle("Your Options")
                .setSingleChoiceItems(options, checked[0], (dialog, which) -> checked[0] = which)
                .setPositiveButton("Yes", (dialog, which) -> {
                    switch (checked[0]) {
                        case 0: //TODO Edit
                            Friend friend = mList.get(i);
                            Bundle bundle = new Bundle();
                            bundle.putInt("pos", i);
                            bundle.putInt("id", friend.getId());
                            bundle.putString("name", friend.getName());

                            createDialog(bundle).show();
                            break;
                        case 1: //Delete
                            new AlertDialog.Builder(this)
                                    .setTitle("Confirm")
                                    .setMessage(String.format("Delete %s", mList.get(i)))
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("Yes", (dialog1, which1) -> {
                                        //TODO Delete
                                        try (FriendDB db = new FriendDB(this)) {
                                            db.delete(mList.get(i).getId());
                                            mList.remove(i);
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    })
                                    .show();
                            break;
                    }
                })
                .show();
        return true;
    }

    //tempel menu ke Appbar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mi_about) {
            new AlertDialog.Builder(this)
                    .setTitle("Info")
                    .setMessage("This is Info")
                    .setPositiveButton("OK", null)
                    .show();
        } else if (item.getItemId() == R.id.mi_exit) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm")
                    .setMessage("Close App?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    //bottom dialog
    private BottomSheetDialog createDialog(Bundle bundle) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this)
                .inflate(R.layout.new_item, null, false);

        TextInputEditText tieName = view.findViewById(R.id.tie_country);

        boolean isUpdate = false;
        if (bundle != null) {
            tieName.setText(bundle.getString("name"));
            isUpdate = true;
        }

        final boolean finalIsUpdate = isUpdate;

        Button btAdd = view.findViewById(R.id.bt_add);
        btAdd.setOnClickListener(v -> {
            if (Objects.requireNonNull(tieName.getText()).toString().isEmpty())
                return;

            try (FriendDB db = new FriendDB(this)) {
                if (finalIsUpdate) {
                    //TODO Update
                    Friend friend = new Friend(bundle.getInt("id"), tieName.getText().toString());
                    db.update(friend);
                    mList.set(bundle.getInt("pos"), friend);
                } else {
                    //TODO Insert
                    Friend friend = new Friend(tieName.getText().toString());
                    long id = db.insert(friend);
                    mList.add(new Friend((int) id, tieName.getText().toString()));
                }
            }

            mAdapter.notifyDataSetChanged();
            dialog.dismiss();//tutup dialog
        });


        dialog.setContentView(view);
        return dialog;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try (FriendDB db = new FriendDB(this)) {
            mList.clear();
            mList.addAll(db.getAllFriends());
            mAdapter.notifyDataSetChanged();
        }
    }
}