package ru.breffi.androidstoryclmsdk;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import ru.breffi.storyclmsdk.*;

import ru.breffi.storyclmsdk.AsyncResults.IAsyncResult;


public class MainActivity extends AppCompatActivity  {



    static StoryCLMServiceGeneric<Profile> StoryCLMProfileService;
    public static StoryCLMServiceGeneric<Profile> getService(){
        if (StoryCLMProfileService==null){
            StoryCLMServiceConnector clientConnector =  StoryCLMConnectorsGenerator.GetStoryCLMServiceConnector(App.getContext().getResources().getString(R.string.client_id),App.getContext().getResources().getString(R.string.client_secret),null);
            StoryCLMProfileService = clientConnector.GetService(Profile.class, 23);
        }
        return StoryCLMProfileService;
    }
    TextView tvDisplay;
    MainActivity _this=this;
    Profile[] profiles;
    int size = 0;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // получаем экземпляр элемента ListView
        listView = (ListView)findViewById(R.id.listview);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView)view).getText().toString();
                sendMessage(profiles[position],false);
            }
        });



    }

    void initByProfiles(){
        String[] profileNames = new String[profiles.length];

        for(int i=0;i<profiles.length;i++){
            profileNames[i]=profiles[i].Name;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(_this,android.R.layout.simple_list_item_1, profileNames);

        listView.setAdapter(adapter);

    }

    public void sendMessage(Profile profile, boolean insert) {
        Intent intent = new Intent(this, DetailsActivity.class);
        try{
            intent.putExtra("Name",profile.Name);
            intent.putExtra("Age",profile.Age);
            intent.putExtra("Created",profile.Created);

            intent.putExtra("Gender",profile.Gender);
            intent.putExtra("Rating",profile.Rating);

            intent.putExtra("_id",profile._id);
            intent.putExtra("insert", insert);


            startActivity(intent);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        //*/
    }
    @Override
    public void onResume() {
        super.onResume();
       Refresh();
    }
    void RefreshButtonClick(View view){
        Refresh();
    }
    public void Refresh(){

        ((Button)findViewById(R.id.RefreshButton)).setEnabled(false);
        try {


            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

            final IAsyncResult sizeresult = getService().Find(0, 100);
            sizeresult.OnResult(new OnResultCallback() {
                @Override
                public void OnSuccess(Object o) {
                    profiles = ((List<Profile>) o).toArray(new Profile[size]);
                    initByProfiles();
                    ((Button) findViewById(R.id.RefreshButton)).setEnabled(true);
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                }

                @Override
                public void OnFail(Throwable throwable) {
                    Toast.makeText(getBaseContext(), "Ошибка сети. Повторите операцию. ", Toast.LENGTH_LONG).show();
                    ((Button) findViewById(R.id.RefreshButton)).setEnabled(true);
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    void AddButtonOnClick(View view){
        Profile p = new Profile();
        sendMessage(p, true);
    }
}
