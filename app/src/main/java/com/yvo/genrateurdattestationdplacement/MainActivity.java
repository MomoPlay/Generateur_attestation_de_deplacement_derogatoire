package com.yvo.genrateurdattestationdplacement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    AdapterMotifList adapter;
    Context context = MainActivity.this;

    String prenom = "";
    String nom = "";
    String date = "";
    String lieu = "";
    String adresse = "";
    String ville = "";
    String cp = "";
    String dateNow = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setpersonName();

        // Populate recyclerview
        String[] motifs = getResources().getStringArray(R.array.motifs_sortie);
        String[] motifs_long = getResources().getStringArray(R.array.motifs_sortie_full);
        String[] motifs_court = getResources().getStringArray(R.array.motifs_sortie_short);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterMotifList(this, motifs, motifs_long, motifs_court);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        setpersonName();

        super.onResume();
    }

    public void onChangerPersonneClick(View view)
    {
        Intent intent = new Intent(this, person.class);
        startActivity(intent);
    }

    public void onGenererAttestationClick(View view) {

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy 'a' HH:mm");
        dateNow = dateFormater.format(cal.getTime());

        //Création qr code
        String qrText = String.format("Cree le: %s; Nom: %s; Prenom: %s; Naissance: %s a %s; Adresse: %s %s %s; Sortie: %s; Motifs: %s", dateNow, nom, prenom, date, lieu, adresse, cp, ville, dateNow, adapter.getCheckedAsString());

        BitMatrix bitMatrix = generateMatrix(qrText);

        writeImage(bitMatrix);

        //création pdf
        modifyPdf();

        //Snackbar.make(view, qrText, Snackbar.LENGTH_LONG).show();

        Intent intent = new Intent(this, qrcode.class);
        startActivity(intent);
    }

    private BitMatrix generateMatrix(String data) {
        try {
            return new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, 400, 400);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeImage(BitMatrix bitMatrix)  {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(context.getFilesDir(), "/qrcode.png"));

            Bitmap bmp = Bitmap.createBitmap(400, 400, Bitmap.Config.RGB_565);
            for (int x = 0; x < 400; x++) {
                for (int y = 0; y < 400; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            bmp.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            Log.i("Chemin enregistrement", context.getFilesDir() + "/qrcode.png");

            //MatrixToImageWriter.writeToStream(bitMatrix, "png", fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setpersonName()
    {
        //Set person
        SharedPreferences sharedPref = getSharedPreferences(ApplicationConstants.PREFERENCES, Context.MODE_PRIVATE);

        prenom = sharedPref.getString("Prenom", "Pas de personne configurée");

        nom = sharedPref.getString("Nom", "");

        date = sharedPref.getString("Date", "");

        lieu = sharedPref.getString("Lieu", "");

        adresse = sharedPref.getString("Adresse", "");

        ville = sharedPref.getString("Ville", "");

        cp = sharedPref.getString("CP", "");

        TextView e = findViewById(R.id.selected_person);
        e.setText(String.format("%s %s\r\n%s %s\r\n%s\r\n%s %s", prenom, nom, date, lieu, adresse, cp, ville));
    }

    private void modifyPdf()
    {
        try {
            PdfReader reader = new PdfReader(getResources().openRawResource(R.raw.attestationdeplacementapp));
            PdfStamper stamper = new PdfStamper(reader,
                    new FileOutputStream(new File(context.getFilesDir(), "/attestationpdf.pdf")));
            BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);

            PdfContentByte over = stamper.getOverContent(1);

            //Premier qr code
            Image qrCode = Image.getInstance(context.getFileStreamPath("qrcode.png").toURL());
            qrCode.setAbsolutePosition(350, 150);
            qrCode.scaleAbsolute(150,150);
            over.addImage(qrCode);


            over.beginText();
            over.setFontAndSize(bf_times, 10);
            over.setTextMatrix(120, 670);
            over.showText(nom + " " + prenom);
            over.setTextMatrix(120, 658);
            over.showText(date);
            over.setTextMatrix(85, 646);
            over.showText(lieu);
            over.setTextMatrix(130, 634);
            over.showText(adresse);
            over.setTextMatrix(110, 268);
            over.showText(ville);
            over.setTextMatrix(90, 243);
            over.showText(dateNow);

            for (String motif : adapter.getChecked())
            {
                switch(motif)
                {
                    case "sport":
                        //sport
                        over.setTextMatrix(74, 391);
                        over.showText("x");
                        break;
                    case "travail":
                        //travail
                        over.setTextMatrix(74, 562);
                        over.showText("x");
                        break;
                    case "courses":
                        //courses
                        over.setTextMatrix(74, 513);
                        over.showText("x");
                        break;
                    case "sante":
                        //soins
                        over.setTextMatrix(74, 464);
                        over.showText("x");
                        break;
                    case "famille":
                        //famille
                        over.setTextMatrix(74, 427);
                        over.showText("x");
                        break;
                    case "judiciaire":
                        //judiciaire
                        over.setTextMatrix(74, 330);
                        over.showText("x");
                        break;
                    case "missions":
                        //mission
                        over.setTextMatrix(74, 305);
                        over.showText("x");
                        break;
                }
            }


            over.setTextMatrix(360, 150);
            over.showText("Date de création:");
            over.setTextMatrix(360, 142);
            over.showText(dateNow);
            over.endText();

            //Second qr code
            stamper.insertPage(2, reader.getPageSize(1));
            over = stamper.getOverContent(2);
            qrCode.setAbsolutePosition(0, reader.getPageSize(2).getHeight()-400);
            qrCode.scaleAbsolute(400,400);
            over.addImage(qrCode);

            /*over.setRGBColorStroke(0xFF, 0x00, 0x00);
            over.setLineWidth(5f);
            over.moveTo(72, 303);
            over.lineTo(72, 562);
            over.closePathStroke();*/

            stamper.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public void onLastAttestationClick(View v)
    {
        Intent intent = new Intent(this, qrcode.class);
        startActivity(intent);
    }
}
