package com.example.bimvendpro;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.annotation.Documented;

public class GeneratePDF {

    private Context context;
    private File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fTitle = new Font();
    public GeneratePDF(Context context) {
        this.context = context;
    }

    public void openDocument(){
        createFile();
        try {
            document = new Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
        }catch (Exception e){
            Log.e("openDocument", e.toString() );
        }
    }

    public  void closeDocument(){
        document.close();
    }

    public void addMetaData(String title,String subject,String author){
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);

    }

    public void addTitle(String title,String subTitle){
        paragraph = new Paragraph();
        addChildP(new Paragraph(title,fTitle));
        addChildP(new Paragraph(subTitle,fTitle));
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void createFile(){
        File folder = new File(Environment.getExternalStorageDirectory().toString(),"PDF");

        if(!folder.exists()){
            System.out.println("hoise");
            folder.mkdir();
        }

        pdfFile = new File(folder,"test.pdf");
    }

    private void addChildP(Paragraph childParagraph){
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }




}
