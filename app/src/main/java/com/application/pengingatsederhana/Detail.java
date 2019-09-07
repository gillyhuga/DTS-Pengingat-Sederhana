package com.application.pengingatsederhana;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.application.pengingatsederhana.Db.ReminderHelper;
import com.application.pengingatsederhana.Entity.Reminder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Detail extends AppCompatActivity implements View.OnClickListener{
    EditText edtTitle, edtDescription,edtWaktu;
    Button btnSubmit, btnSelesai, btnKembali;

    public static String EXTRA_REMINDER = "extra_note";
    public static String EXTRA_POSITION = "extra_position";

    private boolean isEdit = false;
    public static int REQUEST_ADD = 100;
    public static int RESULT_ADD = 101;
    public static int REQUEST_UPDATE = 200;
    public static int RESULT_UPDATE = 201;
    public static int REQUEST_DELETE = 300;
    public static int RESULT_DELETE = 301;

    private Reminder reminder;
    private int position;
    private ReminderHelper reminderHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        edtTitle = (EditText) findViewById(R.id.edt_tittle);
        edtDescription = (EditText) findViewById(R.id.edt_desc);
        edtWaktu = (EditText) findViewById(R.id.edt_waktu);
        btnSubmit = (Button) findViewById(R.id.btn_ubah);
        btnKembali = (Button) findViewById(R.id.btn_kembali);
        btnSelesai = (Button) findViewById(R.id.btn_selesai);


        btnSubmit.setOnClickListener(this);


        reminderHelper = new ReminderHelper(this);
        reminderHelper.open();

        reminder = getIntent().getParcelableExtra(EXTRA_REMINDER);

        if (reminder != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        }

        String actionBarTitle = null;
        String btnTitle = null;

        if (isEdit) {
            actionBarTitle = "Ubah";
            btnTitle = "Update";
            edtTitle.setText(reminder.getTitle());
            edtDescription.setText(reminder.getDesc());
            edtWaktu.setText(reminder.getWaktu());
        } else {
            actionBarTitle = "Tambah";
            btnTitle = "Simpan";
        }

        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSubmit.setText(btnTitle);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reminderHelper != null){
            reminderHelper.close();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_ubah) {
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String waktu = edtWaktu.getText().toString().trim();

            boolean isEmpty = false;

            /*
            Jika fieldnya masih kosong maka tampilkan error
             */
            if (TextUtils.isEmpty(title)) {
                isEmpty = true;
                edtTitle.setError("Field tidak boleh kosong");
            }

            if (!isEmpty) {
                Reminder newReminder = new Reminder();
                newReminder.setTitle(title);
                newReminder.setDesc(description);
                newReminder.setWaktu(waktu);

                Intent intent = new Intent();

                /*
                Jika merupakan edit setresultnya UPDATE, dan jika bukan maka setresultnya ADD
                 */
                if (isEdit) {
                    newReminder.setClock(reminder.getClock());
                    newReminder.setId(reminder.getId());
                    reminderHelper.update(newReminder);

                    intent.putExtra(EXTRA_POSITION, position);
                    setResult(RESULT_UPDATE, intent);
                    finish();
                } else {
                    newReminder.setClock(getCurrentDate());
                    reminderHelper.insert(newReminder);

                    setResult(RESULT_ADD);
                    finish();
                }
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    final int ALERT_DIALOG_CLOSE = 10;
    final int ALERT_DIALOG_DELETE = 20;

    /*
    Konfirmasi dialog sebelum proses batal atau hapus
    close = 10
    delete = 20
     */
    private void showAlertDialog(int type){
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle = null, dialogMessage = null;

        if (isDialogClose){
            dialogTitle = "Batal";
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?";
        }else{
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?";
            dialogTitle = "Hapus Note";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        if (isDialogClose){
                            finish();
                        }else{
                            reminderHelper.delete(reminder.getId());
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_POSITION, position);
                            setResult(RESULT_DELETE, intent);
                            finish();
                        }
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    private String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        return dateFormat.format(date);
    }


}
