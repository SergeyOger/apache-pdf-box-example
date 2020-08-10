package net.broscorp.chat;

import java.io.IOException;
import net.broscorp.pdf.chart.PdfComposer;

public class ChartBuilder {

  public static void main(String[] args) throws IOException {
    PdfComposer pdfComposer = new PdfComposer();
    pdfComposer.createPdf();
  }
}
