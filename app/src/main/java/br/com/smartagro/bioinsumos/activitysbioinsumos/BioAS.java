package br.com.smartagro.bioinsumos.activitysbioinsumos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;



import br.com.smartagro.R;

public class BioAS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_as);
        configuraTollbar();

    }
    private void configuraTollbar() {
        //configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Controle biológico");
        setSupportActionBar( toolbar );
    }
}