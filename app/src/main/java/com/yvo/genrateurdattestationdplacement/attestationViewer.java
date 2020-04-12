package com.yvo.genrateurdattestationdplacement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;

public class attestationViewer extends AppCompatActivity {

    Context context = attestationViewer.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attestation_viewer);

        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView.fromFile(new File(context.getFileStreamPath("attestationpdf.pdf").getPath()))
                .defaultPage(0)
                .pageFitPolicy(FitPolicy.BOTH)
                .load();
    }
}
