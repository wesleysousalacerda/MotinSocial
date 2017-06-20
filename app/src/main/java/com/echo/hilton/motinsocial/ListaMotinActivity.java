package com.echo.hilton.motinsocial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hilton on 20/06/17.
 */

public class ListaMotinActivity extends AppCompatActivity{
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_motins);
        final ListView listView = (ListView) findViewById(R.id.listaMotin);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        // get reference to 'motins' node
        mFirebaseDatabase = mFirebaseInstance.getReference("motins");
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dsCliente = dataSnapshot.getChildren();
                List<Motin> motins = new ArrayList<Motin>();
                while(dsCliente.iterator().hasNext()){
                    DataSnapshot dsObjetoCliente =
                            dsCliente.iterator().next();
                    Motin motin =
                            dsObjetoCliente.getValue(Motin.class);
                    motins.add(motin);
                }
                ArrayAdapter<Motin> adapter =
                        new ArrayAdapter<Motin>(ListaMotinActivity.this,
                                android.R.layout.simple_list_item_1,
                                motins);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}