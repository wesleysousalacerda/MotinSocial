package com.echo.hilton.motinsocial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView txtDetails;
    private EditText inputName,inputDemanda,inputServico, inputEmail;
    private Button btnSave, btnRemove;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private String motinId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Displaying toolbar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        txtDetails = (TextView) findViewById(R.id.txt_user);
        inputName = (EditText) findViewById(R.id.name);
        inputDemanda = (EditText) findViewById(R.id.demanda);
        inputServico = (EditText) findViewById(R.id.servico);
        inputEmail = (EditText) findViewById(R.id.email);

        btnSave = (Button) findViewById(R.id.btn_save);
        btnRemove = (Button) findViewById(R.id.btn_remove);


        mFirebaseInstance = FirebaseDatabase.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        // get reference to 'motins' node
        mFirebaseDatabase = mFirebaseInstance.getReference("motins");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("Motin Social");

        // app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");

                String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                getSupportActionBar().setTitle(appTitle);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Falha ao ler o titulo do app.", error.toException());
            }
        });

        // Save / update the motin
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = inputName.getText().toString();
                String demanda= inputDemanda.getText().toString();
                String servico = inputServico.getText().toString();
                String email = inputEmail.getText().toString();



                // Check for already existed motinId
                if (TextUtils.isEmpty(motinId)) {
                    createMotin(nome,demanda,servico, email);
                    Toast.makeText(getApplicationContext(), "Motin Criado com Sucesso", Toast.LENGTH_LONG).show();

                } else {
                    updateMotin(nome,demanda,servico, email);
                    Toast.makeText(getApplicationContext(), "Motin Atualizado com Sucesso", Toast.LENGTH_LONG).show();

                }
            }
        });
        // Delete motin
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check for already existed motinId
                if (TextUtils.isEmpty(motinId)) {
                    Toast.makeText(getApplicationContext(), "Nao ha nada a ser deletado", Toast.LENGTH_LONG).show();


                } else {
                    mFirebaseDatabase.child(motinId).removeValue();
                    Log.e(TAG, "Motin deletado com sucesso");
                    Toast.makeText(getApplicationContext(), "Motin Deletado com Sucesso", Toast.LENGTH_LONG).show();


                }
            }
        });

        toggleButton();
    }

    // Changing button text
    private void toggleButton() {
        if (TextUtils.isEmpty(motinId)) {
            btnSave.setText("Save");
        } else {
            btnSave.setText("Update");
        }
    }

    /**
     * Creating new motin node under 'motins'
     */
    private void createMotin(String nome, String demanda, String servico, String email) {
        // TODO
        // Fazer autenticacao do firebase.
        // In real apps this motinId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(motinId)) {
            motinId = mFirebaseDatabase.push().getKey(); //Push creates a unique code for users.
        }

        Motin user = new Motin(nome,demanda,servico,email);

        mFirebaseDatabase.child(motinId).setValue(user);

        addMotinChangeListener();
    }

    /**
     * Motin data change listener
     */
    private void addMotinChangeListener() {
        // Motin data change listener
        mFirebaseDatabase.child(motinId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Motin motin = dataSnapshot.getValue(Motin.class);

                // Checar se o objeto esta nulo
                if (motin == null) {
                    Log.e(TAG, "Dados do motin esta nulo!");
                    return;
                }

                Log.e(TAG, "Dados do motin alterado" + motin.nome + ", " + motin.demanda);

                // Mostrar dados atualizados
                txtDetails.setText(motin.nome + ", " + motin.demanda);

                // Limpar editText
                inputEmail.setText("");
                inputName.setText("");
                inputDemanda.setText("");
                inputServico.setText("");

                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Falha ao ler Motin", error.toException());
            }
        });
    }

    private void updateMotin(String nome,String demanda, String servico, String email) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(nome))
            mFirebaseDatabase.child(motinId).child("nome").setValue(nome);
        if (!TextUtils.isEmpty(demanda))
            mFirebaseDatabase.child(motinId).child("demanda").setValue(demanda);
        if (!TextUtils.isEmpty(servico))
            mFirebaseDatabase.child(motinId).child("servico").setValue(servico);
        if (!TextUtils.isEmpty(email))
            mFirebaseDatabase.child(motinId).child("email").setValue(email);
    }

    public void VerLista(View view) {
        Intent i = new Intent(MainActivity.this, ListaMotinActivity.class);
        startActivity(i);
    }
}