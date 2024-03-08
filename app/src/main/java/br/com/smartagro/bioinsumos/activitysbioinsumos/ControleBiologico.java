package br.com.smartagro.bioinsumos.activitysbioinsumos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.smartagro.R;

public class ControleBiologico extends AppCompatActivity {

    private TextView textViewC;
    private TextView textViewD;

    private ImageView imageViewC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle_biologico);
        configuraTollbar();

        textViewC = findViewById(R.id.textViewC);
        imageViewC = findViewById(R.id.imageViewC);
        textViewD = findViewById(R.id.textViewD);

    }

    private void configuraTollbar() {
        //configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Controle biológico");
        setSupportActionBar( toolbar );
    }
}