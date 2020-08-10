package net.broscorp.pdf.chart;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class PdfComposer {

  public void createPdf() throws IOException {
    try (PDDocument document = new PDDocument()) {
      PDPage page = new PDPage(PDRectangle.A4);

      float pageWidth = page.getMediaBox().getWidth();
      float pageHeight = page.getMediaBox().getHeight();

      PDPageContentStream contentStream = new PDPageContentStream(document, page);

      PDImageXObject chartImage = JPEGFactory.createFromImage(document,
          createChart((int) pageWidth, (int) pageHeight));

      PDImageXObject chartImage1 = JPEGFactory.createFromImage(document,
          createChart((int) pageWidth / 2, (int) pageHeight / 2));

      PDImageXObject chartImage2 = JPEGFactory.createFromImage(document,
          createChart((int) pageWidth / 3, (int) pageHeight / 3));

      contentStream.transform(new Matrix(0.5f, 0, 0, 0.5f, 0, pageHeight * 0.5f));
      contentStream.drawImage(chartImage, 0, 0);
      contentStream.drawImage(chartImage1, 0.5f, 0.5f);
      contentStream.drawImage(chartImage2, 1, 1);
      contentStream.close();

      document.addPage(page);

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      document.save(byteArrayOutputStream);

      FileUtils.writeByteArrayToFile(new File("file.pdf"), byteArrayOutputStream.toByteArray());
    }
  }


  private BufferedImage createChart(int width, int height) {
    XYChart chart = new XYChartBuilder().xAxisTitle("X").yAxisTitle("Y").width(width).height(height)
        .theme(ChartTheme.Matlab).build();
    XYSeries series = chart.addSeries("Random", null, getRandomNumbers(200));
    series.setMarker(SeriesMarkers.NONE);
    return BitmapEncoder.getBufferedImage(chart);
  }

  private double[] getRandomNumbers(int numPoints) {
    double[] y = new double[numPoints];
    for (int i = 0; i < y.length; i++) {
      y[i] = ThreadLocalRandom.current().nextDouble(0, 1000);
    }
    return y;
  }

}
