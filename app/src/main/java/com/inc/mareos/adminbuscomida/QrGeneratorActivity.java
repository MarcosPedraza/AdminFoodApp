package com.inc.mareos.adminbuscomida;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.WriterException;
import com.inc.mareos.adminbuscomida.models.Tarjeta;

import java.io.File;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class QrGeneratorActivity extends AppCompatActivity {

    private static final String TAG = "QrGeneratorActivity";

    //witgets

    ImageView img_show;
    ImageView btn_save;
    AppCompatSpinner spinner_activo;
    SwitchCompat switch_vi;
    Button btn_generar;

    //var
    static String dir = "qr_folder";
    static int dimension = 512;

    private DatabaseReference myRef;

    Bitmap img_qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        img_show = findViewById(R.id.img_qr_show);
        spinner_activo = findViewById(R.id.spinner_valor);
        switch_vi = findViewById(R.id.switch_activo);
        btn_generar = findViewById(R.id.btn_generar);
        btn_save = findViewById(R.id.btn_save);

        crearCarpeta();


        myRef = FirebaseDatabase.getInstance().getReference("prepagos_tarjeta");

        final String rutaCarpeta = getFilesDir()+"/"+dir;

        File dirImgQR = new File(rutaCarpeta);

        if(dirImgQR.exists() && dirImgQR.isDirectory())
        {
            Log.d(TAG, "onCreate: la carpeta ha sido creada");
            Log.d(TAG, "onCreate: ruta:  " + dirImgQR.getPath());
        }
        else
        {
            Log.d(TAG, "onCreate: la carpeta no a podido crearse");
        }



        btn_generar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //generar QR

                String key = myRef.push().getKey();
                myRef.child(key).setValue(createTarjeta());

                QRGEncoder  qrgEncoder = new QRGEncoder(key,null, QRGContents.Type.TEXT,dimension);


                try
                {
                    img_qr = qrgEncoder.encodeAsBitmap();
                    img_show.setImageBitmap(img_qr);

                }catch (WriterException ex)
                {

                    Log.d(TAG, "error de escritura: " + ex.toString());

                }


            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    QRGSaver.save(rutaCarpeta,"qrImag",img_qr,QRGContents.ImageType.IMAGE_JPEG);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });


    }


    private Tarjeta createTarjeta()
    {
        Tarjeta tarjeta = new Tarjeta();

        double saldo = Double.valueOf(spinner_activo.getSelectedItem().toString());
        if(switch_vi.isChecked())
        {
            tarjeta.setActiva(true);
        }
        else
        {
            tarjeta.setActiva(false);
        }

        tarjeta.setSaldo(saldo);
        tarjeta.setCobrada(false);

        return tarjeta;

    }

    public File crearCarpeta()
    {
        File directorio = new File(getFilesDir(),dir);

        if(!directorio.mkdir())
        {
            Log.d(TAG, "crearCarpeta: no se creo el directorio");
        }

        return directorio;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
