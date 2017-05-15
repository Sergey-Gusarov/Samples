package ru.breffi.androidstoryclmsdk;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import ru.breffi.storyclmsdk.AsyncResults.IAsyncResult;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;
import ru.breffi.storyclmsdk.OnResultCallback;

public class DetailsActivity extends AppCompatActivity {
    String _id;
    boolean insert;
    Date created;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        insert =  getIntent().getBooleanExtra("insert",false);
         ((Button)findViewById(R.id.deleteButton)).setVisibility(insert?View.INVISIBLE:View.VISIBLE);


        SimpleDateFormat format = Profile.format;

        ((EditText)findViewById(R.id.editName)).setText(getIntent().getStringExtra("Name"));
        ((EditText)findViewById(R.id.editAge)).setText(Objects.toString(getIntent().getLongExtra("Age",0)));

        created = (Date)getIntent().getSerializableExtra("Created");
        if (created!=null) ((TextView)findViewById(R.id.dateView)).setText(format.format(created));
        ((RadioButton)findViewById(R.id.radioFemale)).setChecked(!getIntent().getBooleanExtra("Gender",true));
        ((RadioButton)findViewById(R.id.radioMale)).setChecked(getIntent().getBooleanExtra("Gender",true));

        ((EditText)findViewById(R.id.editRating)).setText(Objects.toString(getIntent().getDoubleExtra("Rating",0),""));
        _id =  getIntent().getStringExtra("_id");


    }

    Profile constructProfile(){
        Profile p = new Profile();
        String[] par = new String[]{
                _id,
                ((EditText)findViewById(R.id.editName)).getText().toString(),
                ((EditText)findViewById(R.id.editAge)).getText().toString(),
                ((TextView)findViewById(R.id.dateView)).getText().toString(),
                Objects.toString(((RadioButton)findViewById(R.id.radioMale)).isChecked()),
                ((EditText)findViewById(R.id.editRating)).getText().toString(),
        };
        p.ParseArray(par);
        return p;
    }

    public void saveButtonClick(View target) throws AsyncResultException, AuthFaliException {
        ((Button)findViewById(R.id.saveButton)).setEnabled(false);
        IAsyncResult r;
        if (insert){
             r =  MainActivity.getService().Insert(constructProfile());

        }
        else{
             r =  MainActivity.getService().Update(constructProfile());

         }
       //finish();
         r.OnResult(new OnResultCallback() {
            @Override

            public void OnSuccess(Object o) {
              finish();
            }

            @Override
            public void OnFail(Throwable throwable) {
                ((Button)findViewById(R.id.saveButton)).setEnabled(true);
                Toast.makeText(getBaseContext(), "Ошибка сети. Повторите операцию. ", Toast.LENGTH_LONG).show();
            }
        });//*/



    }

    void dateSelect(View view){
        ru.breffi.androidstoryclmsdk.DatePicker datePicker = new ru.breffi.androidstoryclmsdk.DatePicker(created, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                created = new GregorianCalendar(year, month, dayOfMonth).getTime();
                if (created!=null) ((TextView)findViewById(R.id.dateView)).setText( Profile.format.format(created));
            }
        });
        datePicker.show(getSupportFragmentManager(), "datePicker");

    }

    void CancelClick(View view){
        finish();
    }

    void DeleteClick(View view){
        ((Button)findViewById(R.id.deleteButton)).setEnabled(false);
        IAsyncResult r =  MainActivity.getService().Delete(_id);
        r.OnResult(new OnResultCallback() {
            @Override

            public void OnSuccess(Object o) {
                finish();
            }

            @Override
            public void OnFail(Throwable throwable) {
                ((Button)findViewById(R.id.deleteButton)).setEnabled(true);
                Toast.makeText(getBaseContext(), "Ошибка сети. Повторите операцию. ", Toast.LENGTH_LONG).show();
            }
        });//*/
    }

}
