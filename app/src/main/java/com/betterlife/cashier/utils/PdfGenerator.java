package com.betterlife.cashier.utils;

import android.os.Environment;
import android.util.Log;

import com.betterlife.cashier.entity.Order;
import com.betterlife.cashier.entity.OrderDetails;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DoubleBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PdfGenerator {
    public static void generate(String filename, ArrayList<Order> orders) {
        try {
            String path = "/storage/emulated/0/Download/report.pdf";
            FileOutputStream outputStream = new FileOutputStream(path);
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            paintTitle(document, filename);
            paintEmptyLines(document, 3);
            paintTable(document, orders);

            document.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void paintTable(Document document, ArrayList<Order> orders) {
        float[] values = {
                50f,
                200f,
                100f,
                200f,
                200f,
        };

        Table table = new Table(UnitValue.createPointArray(values));
        table.setBorder(new DoubleBorder(3f));
        paintHeaderCell(table, "No");
        paintHeaderCell(table, "Tanggal");
        paintHeaderCell(table, "Total Penjualan");
        paintHeaderCell(table, "Total Harga");
        paintHeaderCell(table, "Total Dikon");

        for (int i = 0; i < orders.size(); i++) {
            String date = Shared.formatDate(orders.get(i).getCreatedOn());

            table.addCell(new Paragraph(String.valueOf(i + 1)).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Paragraph(date));
            table.addCell(new Paragraph(String.valueOf(orders.get(i).getOrderDetails().size())));
            table.addCell(new Paragraph("Rp. " + orders.get(i).getAmount()));
            table.addCell(new Paragraph("Rp. " + orders.get(i).getDiscount()));

            // order details
            paintHeaderCell(table, "");
            paintHeaderCell(table, "Nama Produk");
            paintHeaderCell(table, "Kuantitas");
            paintHeaderCell(table, "Harga");
            paintHeaderCell(table, "Diskon");
            for (int j = 0; j < orders.get(i).getOrderDetails().size(); j++) {
                OrderDetails details = orders.get(i).getOrderDetails().get(j);

                table.addCell(new Paragraph(""));
                table.addCell(new Paragraph(details.getProductName()));
                table.addCell(new Paragraph(String.valueOf(details.getQty())));
                table.addCell(new Paragraph("Rp. " + (int)(details.getPrice())));
                table.addCell(new Paragraph("Rp. " + (int)(details.getDiscount())));
            }
        }

        document.add(table);
    }

    private static void paintEmptyLines(Document document, int lines) {
        for (int i = 0; i < lines; i++) {
            document.add(new Paragraph(" "));
        }
    }

    private static void paintTitle(Document document, String text) {
        document.add(new Paragraph(text).setBold().setTextAlignment(TextAlignment.CENTER));
    }

    private static void paintHeaderCell(Table table, String text) {
        table.addCell(new Paragraph(text).setBold().setTextAlignment(TextAlignment.CENTER));
    }
}
