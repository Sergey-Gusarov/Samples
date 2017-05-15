package ru.breffi.androidstoryclmsdk;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by tselo on 5/12/2017.
 */

public class DatePicker extends DialogFragment {
    Date date;
    DatePickerDialog.OnDateSetListener OnDateSetListener;
    public DatePicker(){super();}

    @SuppressLint("ValidFragment")
    public DatePicker(Date initDate, DatePickerDialog.OnDateSetListener  onDateSetListener){
        super();
        date = initDate;
        OnDateSetListener = onDateSetListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // определяем текущую дату
        final Calendar c = Calendar.getInstance();
        if (date!=null) c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // создаем DatePickerDialog и возвращаем его
        Dialog picker = new DatePickerDialog(getActivity(), OnDateSetListener, year, month, day);
      //  picker.setTitle(getResources().getString(R.string.choose_date));

        return picker;
    }
    @Override
    public void onStart() {
        super.onStart();
        // добавляем кастомный текст для кнопки
        Button nButton =  ((AlertDialog) getDialog())
                .getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText("Готово");

    }
}
