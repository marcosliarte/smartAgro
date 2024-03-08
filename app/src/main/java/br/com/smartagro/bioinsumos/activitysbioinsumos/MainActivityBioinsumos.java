package br.com.smartagro.bioinsumos.activitysbioinsumos;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import br.com.smartagro.R;


public class MainActivityBioinsumos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bioinsumos);
        configuraTollbar();
    }


    public void configuraTollbar(){
        //configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar( toolbar );
    }

    public void buttonbiologicos(View view){
        Intent intent = new Intent(this, br.com.smartagro.bioinsumos.activitysbioinsumos.Biologicos.class);
        startActivity(intent);
    }

    public void buttonInoculantes(View view){
        Intent intent = new Intent(this, br.com.smartagro.bioinsumos.activitysbioinsumos.Inoculantes.class);
        startActivity(intent);
    }


}
