package ru.breffi.androidstoryclmsdk;

import com.google.gson.annotations.JsonAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import ru.breffi.storyclmsdk.TypeAdapters.CustomDateTypeAdapter;

//import com.google.gson.internal.bind.DateTypeAdapter;


public class Profile {
    /// <summary>
    /// Идендификатор записи
    /// Зависит от провайдера таблиц
    /// </summary>
    public String _id ;

    /// <summary>
    /// Имя пользователя
    /// </summary>
    public String  Name ;

    /// <summary>
    /// Возраст
    /// </summary>
    public long Age;

    /// <summary>
    /// Пол
    /// </summary>
    public Boolean Gender ;

    /// <summary>
    /// Рейтинг
    /// </summary>
    public double Rating ;
    /// <summary>
    /// Дата регистрации
    /// </summary>
    @JsonAdapter(CustomDateTypeAdapter.class)
    public Date Created ;

    public Profile(){
      //  format = new SimpleDateFormat("yyyy-MM-dd");
    }


    public void ParseArray(String[] data){
        this._id = data[0];
        this.Name = data[1];
        this.Age = Integer.parseInt(data[2]);
        try {
            this.Created = format.parse(data[3]);
        } catch (ParseException e) {
        }
        this.Gender = Boolean.parseBoolean(data[4]);
        this.Rating = Double.parseDouble(data[5]);
    }
   public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

}

