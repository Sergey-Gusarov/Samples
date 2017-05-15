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


    //ParceablE

    public String[] ToStringArray(){
        return new String[]{
                _id,Name, Objects.toString(Age,""),format.format(Created), Gender.toString(), Objects.toString(Rating,"")
        };
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
/*
    public Profile(Parcel in){
        String[] data = new String[6];
        in.readStringArray(data);
        ParseArray(data);
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String[] ar = this.ToStringArray();
        dest.writeString(ar[0]);
        dest.writeString(ar[1]);
        dest.writeString(ar[2]);
        dest.writeString(ar[3]);
        dest.writeString(ar[4]);
        dest.writeString(ar[5]);
    }//*/
}

