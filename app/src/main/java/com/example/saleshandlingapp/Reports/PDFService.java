package com.example.saleshandlingapp.Reports;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.saleshandlingapp.Formatters.DateFormatter;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


/**
 * Klasa służąca do obsługi generowania raportów sprzedażowych w formacie dokumentu tekstowego PDF.
 */
public class PDFService {

    /**
     * Metoda sterująca generowaniem raportu i wyświetlaniem go na ekran urządzenia mobilnego.
     * @param reportSummary Mapa zawierająca informacje o sprzedanych produktach.
     *                      Klucz to nazwa produktu, a wartość to para z liczbą sprzedanych (szt/kg)
     *                      i całkowitym zyskiem.
     * @param startDate data rozpoczęcia okresu raportu.
     * @param endDate data zakończenia okresu raporu.
     * @param context kontekst obecnego stanu aplikacji Android.
     */
    public static void createPdfReport(Map<String, Map.Entry<Integer, Double>> reportSummary, LocalDateTime startDate, LocalDateTime endDate,Context context){
        File file = prepareFile(prepareFileName());
        if(!createAndWriteDocument(reportSummary,file,prepareDatesHeader(startDate,endDate))){
            Toast.makeText(context, "Nie udało się utworzyć raportu!", Toast.LENGTH_LONG).show();
            return;
        }
        if(!displayPdf(file,context)){
            Toast.makeText(context, "Nie udało się wyświetlić pliku!", Toast.LENGTH_LONG).show();
            return;
        }
    }

    /**
     * Metoda tworząca dokument tekstowy oraz zapisująca jego zawartość.
     * @param productSummary Mapa zawierająca informacje o sprzedanych produktach.
     *                       Klucz to nazwa produktu, a wartość to para z liczbą sprzedanych (szt/kg)
     *                       i całkowitym zyskiem.
     * @param file dokument tekstowy do którego zapisywany jest raport.
     * @param dateTime przedział datowy generowanego raportu w formie ciągu znaków.
     * @return true, jeśli operacja zapisu zakończyła się powodzeniem; false, jeśli wystąpił błąd.
     */
    private static boolean createAndWriteDocument(Map<String, Map.Entry<Integer, Double>> productSummary,File file, String dateTime){

        Map.Entry<Integer, Double> orderKey = productSummary.get("Zamówienia");
        productSummary.remove("Zamówienia");

        try {

            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(file));
            Document document = new Document(pdfDocument);

            PdfFont header = PdfFontFactory.createFont(StandardFonts.COURIER_BOLD, PdfEncodings.CP1250);
            PdfFont font = PdfFontFactory.createFont(StandardFonts.COURIER, PdfEncodings.CP1250);
            Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();

            document.add(new Paragraph("Raport sprzedanych produktów z dni:\n " + dateTime + "\n\n").setFontSize(20).setTextAlignment(TextAlignment.CENTER).setFont(font));

            //Tabla z produktami
            table.addCell(createCell("Nazwa produktu", header));
            table.addCell(createCell("Liczba sprzedanych (szt/kg)", header));
            table.addCell(createCell("Całkowity zysk (zł)", header));

            for (Map.Entry<String, Map.Entry<Integer, Double>> entry : productSummary.entrySet()) {
                table.addCell(createCell(entry.getKey(), font));
                table.addCell(createCell(entry.getValue().getKey().toString(), font));
                table.addCell(createCell(entry.getValue().getValue().toString(), font));
            }
            document.add(table);
            document.add(new Paragraph("\n\n"));

            //Nagłówek tabeli podsumowującej
            Table summaryTableHeader = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
            summaryTableHeader.addCell(createCell("Podsumowanie", header));

            document.add(summaryTableHeader);

            //Tabela z podsumowaniem
            Table summaryTable = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
            summaryTable.addCell(createCell("Zamówienia", font));
            summaryTable.addCell(createCell(orderKey.getKey().toString(), font));
            summaryTable.addCell(createCell(orderKey.getValue().toString(), font));

            document.add(summaryTable);

            document.add(new Paragraph("\n\nWygenerowano: " + DateFormatter.formatDate(LocalDateTime.now())).setFont(font));
            document.close();

        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Metoda wyświetlająca dokument PDF korzystając z aplikacji obsługującej ten format.
     * @param file plik PDF do wyświetlenia.
     * @param context kontekst obecnego stanu aplikacji Android.
     * @return true, jeśli udało się uruchomić aktywność do wyświetlenia pliku PDF; false w przeciwnym razie.
     */
    private static boolean displayPdf(File file, Context context) {
        if (file.exists()) {
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(FileProvider.getUriForFile(context, "com.example.saleshandlingapp.provider", file), "application/pdf");
            target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                context.startActivity(target);
            } catch (ActivityNotFoundException e) {
                return false;
            }
        } else
            return false;
        return true;
    }

    /**
     * Metoda tworząca komórkę w tabeli z określoną czcionką i treścią.
     * @param cellHeader treść do umieszczenia w komórce.
     * @param font czcionka do użycia dla treści w komórce.
     * @return komórka z zawartością.
     */
    private static Cell createCell(String cellHeader, PdfFont font) {
        Cell cell = new Cell().add(new Paragraph(cellHeader))
                .setFont(font).setFontColor(ColorConstants.BLACK);
        cell.setTextAlignment(TextAlignment.CENTER);
        return cell;
    }

    /**
     * Metoda przygotowująca obiekt pliku w katalogu Downloads o określonej nazwie.
     * @param fileName nazwa pliku do utworzenia.
     * @return obiekt File reprezentujący utworzony plik.
     */
    private static File prepareFile(String fileName){
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadDir, fileName);
        return file;
    }

    /**
     * Metoda przygotowująca unikalną nazwę dla pliku PDF na podstawie bieżącej daty.
     * @return unikalna nazwa pliku PDF z uwzględnieniem daty i czasu utworzenia.
     */
    private static String prepareFileName() {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm"));
        String fileName = "Raport_sprzedażowy_" + date + ".pdf";
        return fileName;
    }

    /**
     * Metoda przygotowująca nagłówek z datami w formie czytelnej dla użytkownika.
     * @param startDate data rozpoczęcia okresu.
     * @param endDate data zakończenia okresu.
     * @return nagłówek z datami w formie czytelnej dla użytkownika (np. "01 stycznia 2024 - 31 grudnia 2024").
     */
    private static String prepareDatesHeader(LocalDateTime startDate, LocalDateTime endDate){
        return DateFormatter.dateForHeader(startDate) + " - "+ DateFormatter.dateForHeader(endDate);
    }
}
