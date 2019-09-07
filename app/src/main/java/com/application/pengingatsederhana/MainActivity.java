package com.application.pengingatsederhana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.application.pengingatsederhana.Adapter.ReminderAdaptor;
import com.application.pengingatsederhana.Db.ReminderHelper;
import com.application.pengingatsederhana.Entity.Reminder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.application.pengingatsederhana.Detail.REQUEST_UPDATE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

        RecyclerView rvReminder;
        ProgressBar progressBar;
        Button btn;

    private LinkedList<Reminder> list;
    private ReminderAdaptor adapter;
    private ReminderHelper reminderHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Reminder");

        rvReminder = (RecyclerView)findViewById(R.id.rv_reminder);
        rvReminder.setLayoutManager(new LinearLayoutManager(this));
        rvReminder.setHasFixedSize(true);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        btn = (Button) findViewById(R.id.btn_add);
        btn.setOnClickListener(this);

        reminderHelper = new ReminderHelper(this);
        reminderHelper.open();

        list = new LinkedList<>();

        adapter = new ReminderAdaptor(this);
        adapter.setListReminder(list);
        rvReminder.setAdapter(adapter);

        new LoadNoteAsync().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_add){
            Intent intent = new Intent(MainActivity.this, Detail.class);
            startActivityForResult(intent, Detail.REQUEST_ADD);
        }
    }

    private class LoadNoteAsync extends AsyncTask<Void, Void, ArrayList<Reminder>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

            if (list.size() > 0){
                list.clear();
            }
        }

        @Override
        protected ArrayList<Reminder> doInBackground(Void... voids) {
            return reminderHelper.query();
        }

        @Override
        protected void onPostExecute(ArrayList<Reminder> reminder) {
            super.onPostExecute(reminder);
            progressBar.setVisibility(View.GONE);

            list.addAll(reminder);
            adapter.setListReminder(list);
            adapter.notifyDataSetChanged();

            if (list.size() == 0){
                showSnackbarMessage("Tidak ada data saat ini");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Akan dipanggil jika request codenya ADD
        if (requestCode == Detail.REQUEST_ADD){
            if (resultCode == Detail.RESULT_ADD){
                new LoadNoteAsync().execute();
                showSnackbarMessage("Satu item berhasil ditambahkan");
                rvReminder.getLayoutManager().smoothScrollToPosition(rvReminder, new RecyclerView.State(), 0);
            }
        }
        // Update dan Delete memiliki request code sama akan tetapi result codenya berbeda
        else if (requestCode == REQUEST_UPDATE) {
            /*
            Akan dipanggil jika result codenya UPDATE
            Semua data di load kembali dari awal
            */
            if (resultCode == Detail.RESULT_UPDATE) {
                new LoadNoteAsync().execute();
                showSnackbarMessage("Satu item berhasil diubah");
                int position = data.getIntExtra(Detail.EXTRA_POSITION, 0);
                rvReminder.getLayoutManager().smoothScrollToPosition(rvReminder, new RecyclerView.State(), position);
            }
            /*
            Akan dipanggil jika result codenya DELETE
            Delete akan menghapus data dari list berdasarkan dari position
            */
            else if (resultCode == Detail.RESULT_DELETE) {
                int position = data.getIntExtra(Detail.EXTRA_POSITION, 0);
                list.remove(position);
                adapter.setListReminder(list);
                adapter.notifyDataSetChanged();
                showSnackbarMessage("Satu item berhasil dihapus");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reminderHelper != null){
            reminderHelper.close();
        }
    }

    /**
     * Tampilkan snackbar
     * @param message inputan message
     */
    private void showSnackbarMessage(String message){
        Snackbar.make(rvReminder, message, Snackbar.LENGTH_SHORT).show();
    }
}
