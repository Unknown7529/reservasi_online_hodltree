/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hodltree;
import com.itextpdf.text.BaseColor;
import java.sql.*;
import java.awt.Frame;
import javax.swing.table.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 *
 * @author ASUS
 */
public class HOME extends javax.swing.JFrame {
    int rMouse,sMouse;
    String Path = "C:\\Users\\ASUS\\Desktop\\HODLTREE_App\\src\\image\\";

    // Menyimpan nama lengkap file PDF dengan timestamp agar unik
    String fileName = "C:\\Users\\ASUS\\Desktop\\laporan_pelanggan_" + getCurrentTimestamp() + ".pdf";
    
    
    public HOME() {
        initComponents();
        tampilkanTabelBookingsHome();
        setLocationRelativeTo(null);
        
    }

  private String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.format(formatter);
    }
    
 private String generateID() {
    String uuid = UUID.randomUUID().toString(); // menggunakan UUID sebagai basis ID
    String id = uuid.replaceAll("-", ""); // menghilangkan tanda "-" dari UUID
    return id.substring(0, Math.min(id.length(), 10)); // memotong ID menjadi 10 karakter
}
private void calculateSumAndKembalian() {
    try {
        String[] lines = txtarea_listpesanan.getText().split("\n");
        double sum = 0;

        for (String line : lines) {
            String[] parts = line.trim().split("\\s+"); // Memisahkan berdasarkan spasi
            String hargaStr = parts[parts.length - 1].replace("Rp.", "").replace(",", "").trim();
            double harga = Double.parseDouble(hargaStr); // Mengonversi harga menjadi double
            sum += harga; // Menambahkan harga ke total
        }

        // Format jumlah total
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedSum = decimalFormat.format(sum);
        totalpembayaran.setText(formattedSum); // Menampilkan total pembayaran yang diformat

    } catch (NumberFormatException ex) {
        totalpembayaran.setText("Error"); // Menampilkan error jika terjadi kesalahan konversi
    }
}

private void calculateKembalian() {
    try {
        // Menghapus koma dari total pembayaran dan uang yang dibayar
        String value3 = totalpembayaran.getText().replace(",", "").trim(); // Menghapus koma dan spasi
        String value4 = totalpembayaran2.getText().replace(",", "").trim(); // Menghapus koma dan spasi

        // Menghitung selisih jika value4 (uang yang dibayar) ada
        int difference = 0;
        if (!value4.isEmpty()) {
            int number4 = Integer.parseInt(value4); // Mengonversi uang bayar menjadi int
            int number3 = Integer.parseInt(value3); // Mengonversi total pembayaran menjadi int
            difference = number4 - number3; // Menghitung selisih (kembalian)
        }

        // Format kembalian agar sesuai dengan format angka yang terpisah koma
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedDifference = decimalFormat.format(difference);
        totalpembayaran3.setText(formattedDifference); // Menampilkan uang kembali yang diformat

    } catch (NumberFormatException ex) {
        totalpembayaran3.setText(""); // Menampilkan kosong jika terjadi kesalahan konversi
    }
}


private void generatePDFLaporan_Transaksi() {
    try {
        // Membuat koneksi ke database
        Connection conn = koneksi.koneksiDB();
        String query = "SELECT nama, tanggal, total_harga FROM transaksi";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // Membuat dokumen PDF
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        // Menambahkan logo
        Image logo = Image.getInstance(Path + "logo2.png");
        logo.scaleToFit(70, 70);
        logo.setAbsolutePosition(40, 750);
        document.add(logo);

        // Menambahkan header
        Paragraph header = new Paragraph("Cafe Hodltree Coffee & Chill", new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        // Menambahkan alamat
        Paragraph address = new Paragraph("Jl. Alternatif GOR Pemda, Nanggewer Mekar, Kec. Cibinong, Kabupaten Bogor, Jawa Barat 16912", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL));
        address.setAlignment(Element.ALIGN_CENTER);
        document.add(address);

        // Garis pemisah
        document.add(new Paragraph("\n"));
        LineSeparator line = new LineSeparator();
        line.setLineColor(BaseColor.BLACK);
        line.setLineWidth(1);
        document.add(line);
        document.add(new Paragraph("\n"));

        // Judul laporan
        Paragraph judul = new Paragraph("LAPORAN TRANSAKSI", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        judul.setAlignment(Element.ALIGN_CENTER);
        document.add(judul);
        document.add(new Paragraph("\n"));

        // Tabel transaksi
        PdfPTable table = new PdfPTable(3);
        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
        String[] kolom = {"Nama Pelanggan", "Tanggal", "Total Harga"};
        for (String column : kolom) {
            PdfPCell cell = new PdfPCell(new Phrase(column));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        while (rs.next()) {
            table.addCell(new PdfPCell(new Phrase(rs.getString("nama"))));
            table.addCell(new PdfPCell(new Phrase(rs.getString("tanggal"))));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(rs.getDouble("total_harga")))));
        }

        float[] columnWidths = {2, 2, 2};
        table.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
        table.setLockedWidth(true);
        table.setWidths(columnWidths);
        document.add(table);

        // Menambahkan tanggal dan lokasi
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
        String currentDate = LocalDate.now().format(dateFormatter);
        String location = "Cibinong";
        String formattedDate = location + ", " + currentDate;

        Paragraph dateTimeParagraph = new Paragraph(formattedDate, new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.ITALIC));
        dateTimeParagraph.setAlignment(Element.ALIGN_RIGHT);
        dateTimeParagraph.setSpacingBefore(20);
        document.add(dateTimeParagraph);

        document.add(new Paragraph("\n\n\n\n"));
        Paragraph admin = new Paragraph("ADMIN", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        admin.setAlignment(Element.ALIGN_RIGHT);
        document.add(admin);

        // Menutup dokumen dan koneksi
        document.close();
        conn.close();

        // Menampilkan notifikasi
        SwingUtilities.invokeLater(() -> {
            JPanel loadingPanel = new JPanel();
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            loadingPanel.add(progressBar);

            JOptionPane optionPane = new JOptionPane(loadingPanel, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            JDialog dialog = optionPane.createDialog("Generate Laporan Transaksi");
            Timer timer = new Timer(2000, e -> {
                dialog.dispose();
                JOptionPane.showMessageDialog(null, "Data berhasil digenerate. Tekan OK untuk membuka file.");
                try {
                    File file = new File(fileName);
                    Desktop.getDesktop().open(file);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            timer.setRepeats(false);
            timer.start();
            dialog.setVisible(true);
        });

    } catch (SQLException | DocumentException | IOException ex) {
        ex.printStackTrace();
    }
}





private void generatePDFLaporan_All_Pelanggan() {
    try {
         Connection conn = koneksi.koneksiDB();
        String query = "SELECT Nama, Alamat, Jenis_Kelamin, No_WA FROM pelanggan";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        // Gunakan Path untuk mengambil logo
        Image logo = Image.getInstance(Path + "logo2.png");
        logo.scaleToFit(70, 70);
        logo.setAbsolutePosition(40, 750);
        document.add(logo);

        Paragraph header = new Paragraph("Cafe Hodltree Coffee & Chill", new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        Paragraph address = new Paragraph("Jl. Alternatif GOR Pemda, Nanggewer Mekar, Kec. Cibinong, Kabupaten Bogor, Jawa Barat 16912", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL));
        address.setAlignment(Element.ALIGN_CENTER);
        document.add(address);

        document.add(new Paragraph("\n"));
        LineSeparator line = new LineSeparator();
        line.setLineColor(BaseColor.BLACK);
        line.setLineWidth(1);
        document.add(line);
        document.add(new Paragraph("\n"));

        Paragraph judul = new Paragraph("DATA PELANGGAN HODL TREE CAFE", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        judul.setAlignment(Element.ALIGN_CENTER);
        document.add(judul);
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(4);
        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
        String[] kolom = {"Nama", "Alamat", "Jenis Kelamin", "No HP"};
        for (String column : kolom) {
            PdfPCell cell = new PdfPCell(new Phrase(column));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        while (rs.next()) {
            String jenisKelamin = rs.getString("Jenis_Kelamin").equalsIgnoreCase("perempuan") ? "P" : "L";
            table.addCell(new PdfPCell(new Phrase(rs.getString("Nama"))));
            table.addCell(new PdfPCell(new Phrase(rs.getString("Alamat"))));
            table.addCell(new PdfPCell(new Phrase(jenisKelamin)));
            table.addCell(new PdfPCell(new Phrase(rs.getString("No_WA"))));
        }

        float[] columnWidths = {2, 3, 1, 2};
        table.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
        table.setLockedWidth(true);
        table.setWidths(columnWidths);
        document.add(table);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
        String currentDate = LocalDate.now().format(dateFormatter);
        String location = "Cibinong";
        String formattedDate = location + ", " + currentDate;

        Paragraph dateTimeParagraph = new Paragraph(formattedDate, new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.ITALIC));
        dateTimeParagraph.setAlignment(Element.ALIGN_RIGHT);
        dateTimeParagraph.setSpacingBefore(20);
        document.add(dateTimeParagraph);

        document.add(new Paragraph("\n\n\n\n"));
        Paragraph admin = new Paragraph("ADMIN", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        admin.setAlignment(Element.ALIGN_RIGHT);
        document.add(admin);

        document.close();
        conn.close();

        SwingUtilities.invokeLater(() -> {
            JPanel loadingPanel = new JPanel();
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            loadingPanel.add(progressBar);

            JOptionPane optionPane = new JOptionPane(loadingPanel, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            JDialog dialog = optionPane.createDialog("Generate Laporan Pelanggan");
            Timer timer = new Timer(2000, e -> {
                dialog.dispose();
                JOptionPane.showMessageDialog(null, "Data berhasil digenerate. Tekan OK untuk membuka file.");
                try {
                    File file = new File(fileName);
                    Desktop.getDesktop().open(file);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            timer.setRepeats(false);
            timer.start();
            dialog.setVisible(true);
        });

    } catch (SQLException | DocumentException | IOException ex) {
        ex.printStackTrace();
    }
}


private void generatePDFLaporan_All_Supplier() {
    try {
        // Membuat koneksi ke database MySQL
         Connection conn = koneksi.koneksiDB();

        // Query untuk mengambil data dari tabel supplier
        String query = "SELECT Nama, Alamat, No_Telepon, Keterangan FROM supplier";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // Membuat objek Document dari iText
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));  // Menggunakan fileName yang sudah didefinisikan
        document.open();

        // Menambahkan logo dan header
        Image logo = Image.getInstance(Path + "logo2.png");  // Menggunakan Path dan nama logo
        logo.scaleToFit(70, 70);
        logo.setAbsolutePosition(40, 750);
        document.add(logo);

        // Header dan alamat
        Paragraph header = new Paragraph("Cafe Hodltree Coffee & Chill", new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        Paragraph address = new Paragraph("Jl. Alternatif GOR Pemda, Nanggewer Mekar, Kec. Cibinong, Kabupaten Bogor, Jawa Barat 16912", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL));
        address.setAlignment(Element.ALIGN_CENTER);
        document.add(address);

        // Menambahkan garis pemisah
        document.add(new Paragraph("\n"));
        LineSeparator line = new LineSeparator();
        line.setLineColor(BaseColor.BLACK);
        line.setLineWidth(1);
        document.add(line);
        document.add(new Paragraph("\n"));

        // Judul laporan
        Paragraph judul = new Paragraph("DATA SUPPLIER HODLTREE CAFE", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        judul.setAlignment(Element.ALIGN_CENTER);
        document.add(judul);
        document.add(new Paragraph("\n"));

        // Membuat tabel untuk data supplier
        PdfPTable table = new PdfPTable(4);
        table.getDefaultCell().setBorder(Rectangle.BOTTOM);

        // Menambahkan header kolom tabel
        String[] kolom = {"Nama", "Alamat", "No Telepon", "Keterangan"};
        for (String column : kolom) {
            PdfPCell cell = new PdfPCell(new Phrase(column));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        // Menambahkan data dari ResultSet ke tabel
        while (rs.next()) {
            table.addCell(new PdfPCell(new Phrase(rs.getString("Nama"))));
            table.addCell(new PdfPCell(new Phrase(rs.getString("Alamat"))));
            table.addCell(new PdfPCell(new Phrase(rs.getString("No_Telepon"))));
            table.addCell(new PdfPCell(new Phrase(rs.getString("Keterangan"))));
        }

        // Menetapkan lebar kolom tabel
        float[] columnWidths = {2, 3, 2, 3};
        table.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
        table.setLockedWidth(true);
        table.setWidths(columnWidths);
        document.add(table);

        // Menambahkan footer dengan tanggal
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
        String currentDate = LocalDate.now().format(dateFormatter);
        String location = "Cibinong";
        String formattedDate = location + ", " + currentDate;

        Paragraph dateTimeParagraph = new Paragraph(formattedDate, new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.ITALIC));
        dateTimeParagraph.setAlignment(Element.ALIGN_RIGHT);
        dateTimeParagraph.setSpacingBefore(20);
        document.add(dateTimeParagraph);

        document.add(new Paragraph("\n\n\n\n"));
        Paragraph admin = new Paragraph("ADMIN", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        admin.setAlignment(Element.ALIGN_RIGHT);
        document.add(admin);

        // Menutup dokumen dan koneksi
        document.close();
        conn.close();

        // Menampilkan progress dan membuka file setelah selesai
        SwingUtilities.invokeLater(() -> {
            JPanel loadingPanel = new JPanel();
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            loadingPanel.add(progressBar);

            JOptionPane optionPane = new JOptionPane(loadingPanel, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            JDialog dialog = optionPane.createDialog("Generate Laporan Supplier");
            Timer timer = new Timer(2000, e -> {
                dialog.dispose();
                JOptionPane.showMessageDialog(null, "Data berhasil digenerate. Tekan OK untuk membuka file.");
                try {
                    File file = new File(fileName);
                    Desktop.getDesktop().open(file);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            timer.setRepeats(false);
            timer.start();
            dialog.setVisible(true);
        });

    } catch (SQLException | DocumentException | IOException ex) {
        ex.printStackTrace();
    }
}


private void generatePDFLaporan_All_Produk() {
    try {
        // Membuat koneksi ke database MySQL
         Connection conn = koneksi.koneksiDB();

        // Query untuk mengambil data dari tabel produk
        String query = "SELECT Kategori, Satuan, Nama_Produk, Harga, Stok FROM produk";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // Membuat objek Document dari iText
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));  // Menggunakan fileName yang sudah didefinisikan
        document.open();

        // Menambahkan logo dan header
        Image logo = Image.getInstance(Path + "logo2.png");  // Menggunakan Path dan nama logo
        logo.scaleToFit(70, 70);
        logo.setAbsolutePosition(40, 750);
        document.add(logo);

        // Header dan alamat
        Paragraph header = new Paragraph("Cafe Hodltree Coffee & Chill", new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        Paragraph address = new Paragraph("Jl. Alternatif GOR Pemda, Nanggewer Mekar, Kec. Cibinong, Kabupaten Bogor, Jawa Barat 16912", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL));
        address.setAlignment(Element.ALIGN_CENTER);
        document.add(address);

        // Menambahkan garis pemisah
        document.add(new Paragraph("\n"));
        LineSeparator line = new LineSeparator();
        line.setLineColor(BaseColor.BLACK);
        line.setLineWidth(1);
        document.add(line);
        document.add(new Paragraph("\n"));

        // Judul laporan
        Paragraph judul = new Paragraph("DATA PRODUK HODLTREE CAFE", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        judul.setAlignment(Element.ALIGN_CENTER);
        document.add(judul);
        document.add(new Paragraph("\n"));

        // Membuat tabel untuk data produk
        PdfPTable table = new PdfPTable(5);
        table.getDefaultCell().setBorder(Rectangle.BOTTOM);

        // Menambahkan header kolom tabel
        String[] kolom = {"Kategori", "Satuan", "Nama Produk", "Harga", "Stok"};
        for (String column : kolom) {
            PdfPCell cell = new PdfPCell(new Phrase(column));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        // Menambahkan data dari ResultSet ke tabel
        while (rs.next()) {
            table.addCell(new PdfPCell(new Phrase(rs.getString("Kategori"))));
            table.addCell(new PdfPCell(new Phrase(rs.getString("Satuan"))));
            table.addCell(new PdfPCell(new Phrase(rs.getString("Nama_Produk"))));
            table.addCell(new PdfPCell(new Phrase(String.format("Rp %.2f", rs.getDouble("Harga")))));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(rs.getInt("Stok")))));
        }

        // Menetapkan lebar kolom tabel
        float[] columnWidths = {2, 2, 3, 2, 2};
        table.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
        table.setLockedWidth(true);
        table.setWidths(columnWidths);
        document.add(table);

        // Menambahkan footer dengan tanggal
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
        String currentDate = LocalDate.now().format(dateFormatter);
        String location = "Cibinong";
        String formattedDate = location + ", " + currentDate;

        Paragraph dateTimeParagraph = new Paragraph(formattedDate, new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.ITALIC));
        dateTimeParagraph.setAlignment(Element.ALIGN_RIGHT);
        dateTimeParagraph.setSpacingBefore(20);
        document.add(dateTimeParagraph);

        document.add(new Paragraph("\n\n\n\n"));
        Paragraph admin = new Paragraph("ADMIN", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        admin.setAlignment(Element.ALIGN_RIGHT);
        document.add(admin);

        // Menutup dokumen dan koneksi
        document.close();
        conn.close();

        // Menampilkan progress dan membuka file setelah selesai
        SwingUtilities.invokeLater(() -> {
            JPanel loadingPanel = new JPanel();
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            loadingPanel.add(progressBar);

            JOptionPane optionPane = new JOptionPane(loadingPanel, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            JDialog dialog = optionPane.createDialog("Generate Laporan Produk");
            Timer timer = new Timer(2000, e -> {
                dialog.dispose();
                JOptionPane.showMessageDialog(null, "Data berhasil digenerate. Tekan OK untuk membuka file.");
                try {
                    File file = new File(fileName);
                    Desktop.getDesktop().open(file);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            timer.setRepeats(false);
            timer.start();
            dialog.setVisible(true);
        });

    } catch (SQLException | DocumentException | IOException ex) {
        ex.printStackTrace();
    }
}


private void generatePDFLaporan_All_Bookings() {
    try {
        // Membuat koneksi ke database MySQL
         Connection conn = koneksi.koneksiDB();

        // Query untuk mengambil data dari tabel bookings
        String query = "SELECT * FROM bookings";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // Membuat objek Document dari iText
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));  // Menggunakan fileName yang sudah didefinisikan
        document.open();

        // Menambahkan logo dan header
        Image logo = Image.getInstance(Path + "logo2.png");  // Menggunakan Path dan nama logo
        logo.scaleToFit(70, 70);
        logo.setAbsolutePosition(40, 750);
        document.add(logo);

        // Header dan alamat
        Paragraph header = new Paragraph("Cafe Hodltree Coffee & Chill", new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        Paragraph address = new Paragraph("Jl. Alternatif GOR Pemda, Nanggewer Mekar, Kec. Cibinong, Kabupaten Bogor, Jawa Barat 16912", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL));
        address.setAlignment(Element.ALIGN_CENTER);
        document.add(address);

        // Menambahkan garis pemisah
        document.add(new Paragraph("\n"));
        LineSeparator line = new LineSeparator();
        line.setLineColor(BaseColor.BLACK);
        line.setLineWidth(1);
        document.add(line);
        document.add(new Paragraph("\n"));

        // Judul laporan
        Paragraph judul = new Paragraph("DATA BOOKINGS HODLTREE CAFE", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        judul.setAlignment(Element.ALIGN_CENTER);
        document.add(judul);
        document.add(new Paragraph("\n"));

        // Membuat tabel untuk data bookings
        PdfPTable table = new PdfPTable(8);
        table.getDefaultCell().setBorder(Rectangle.BOTTOM);

        // Menambahkan header kolom tabel
        String[] kolom = {"ID Booking", "Nama", "Tanggal", "Waktu", "Jumlah People", "No WA", "Email", "Member"};
        for (String column : kolom) {
            PdfPCell cell = new PdfPCell(new Phrase(column));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        // Menambahkan data dari ResultSet ke tabel
        while (rs.next()) {
            table.addCell(new PdfPCell(new Phrase(String.valueOf(rs.getInt("booking_id")))));
            table.addCell(new PdfPCell(new Phrase(rs.getString("name"))));
            table.addCell(new PdfPCell(new Phrase(rs.getString("date"))));
            table.addCell(new PdfPCell(new Phrase(rs.getString("time"))));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(rs.getInt("people")))));
            table.addCell(new PdfPCell(new Phrase(rs.getString("whatsapp_number"))));
            table.addCell(new PdfPCell(new Phrase(rs.getString("email"))));
            table.addCell(new PdfPCell(new Phrase(rs.getString("is_member"))));
        }

        // Menetapkan lebar kolom tabel
        float[] columnWidths = {1, 3, 2, 2, 2, 2, 3, 2};
        table.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
        table.setLockedWidth(true);
        table.setWidths(columnWidths);
        document.add(table);

        // Menambahkan footer dengan tanggal
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
        String currentDate = LocalDate.now().format(dateFormatter);
        String location = "Cibinong";
        String formattedDate = location + ", " + currentDate;

        Paragraph dateTimeParagraph = new Paragraph(formattedDate, new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.ITALIC));
        dateTimeParagraph.setAlignment(Element.ALIGN_RIGHT);
        dateTimeParagraph.setSpacingBefore(20);
        document.add(dateTimeParagraph);

        document.add(new Paragraph("\n\n\n\n"));
        Paragraph admin = new Paragraph("ADMIN", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        admin.setAlignment(Element.ALIGN_RIGHT);
        document.add(admin);

        // Menutup dokumen dan koneksi
        document.close();
        conn.close();

        // Menampilkan progress dan membuka file setelah selesai
        SwingUtilities.invokeLater(() -> {
            JPanel loadingPanel = new JPanel();
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            loadingPanel.add(progressBar);

            JOptionPane optionPane = new JOptionPane(loadingPanel, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            JDialog dialog = optionPane.createDialog("Generate Laporan Bookings");
            Timer timer = new Timer(2000, e -> {
                dialog.dispose();
                JOptionPane.showMessageDialog(null, "Data berhasil digenerate. Tekan OK untuk membuka file.");
                try {
                    File file = new File(fileName);
                    Desktop.getDesktop().open(file);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            timer.setRepeats(false);
            timer.start();
            dialog.setVisible(true);
        });

    } catch (SQLException | DocumentException | IOException ex) {
        ex.printStackTrace();
    }
}


private double parseToDouble(String value) {
    // Menghapus koma dan mengonversi string menjadi double
    return Double.parseDouble(value.replace(",", ""));
}

private void generateKwitansiPembayaran() {
    try {
        // Membuat koneksi ke database
         Connection conn = koneksi.koneksiDB();

        // Insert data transaksi
        insertDataTransaksi();

        // Query untuk mengambil data transaksi terakhir
        String query = "SELECT * FROM transaksi ORDER BY id_transaksi DESC LIMIT 1";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        if (rs.next()) {
            // Menyiapkan data transaksi untuk kwitansi
            String namaPelanggan = txtNamaPelangganBayar.getText();
            String noWA = txtNoWABayar.getText();
            String listPesanan = txtarea_listpesanan.getText();
            
            // Mengonversi total pembayaran, uang bayar, dan uang kembali
            double totalPembayaran = parseToDouble(totalpembayaran.getText());
            double uangBayar = parseToDouble(totalpembayaran2.getText());
            double uangKembali = parseToDouble(totalpembayaran3.getText());

              // Membuat objek Document dari iText
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));  // Menggunakan fileName yang sudah didefinisikan
        document.open();

        // Menambahkan logo dan header
        Image logo = Image.getInstance(Path + "logo2.png");  // Menggunakan Path dan nama logo
        logo.scaleToFit(70, 70);
        logo.setAbsolutePosition(40, 750);
        document.add(logo);

        // Header dan alamat
        Paragraph header = new Paragraph("Cafe Hodltree Coffee & Chill", new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        Paragraph address = new Paragraph("Jl. Alternatif GOR Pemda, Nanggewer Mekar, Kec. Cibinong, Kabupaten Bogor, Jawa Barat 16912", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL));
        address.setAlignment(Element.ALIGN_CENTER);
        document.add(address);

        // Menambahkan garis pemisah
        document.add(new Paragraph("\n"));
        LineSeparator line = new LineSeparator();
        line.setLineColor(BaseColor.BLACK);
        line.setLineWidth(1);
        document.add(line);
        document.add(new Paragraph("\n"));

// Membuat tabel dengan 2 kolom, tapi kita akan mengisi kolom satu per satu.
PdfPTable table = new PdfPTable(1);  // Satu kolom untuk membagi data
table.setWidthPercentage(100);

// Bagian Kiri: Nama, List Pesanan
table.addCell(createLeftAlignedCell("Nama: " + namaPelanggan + " (" + (noWA.isEmpty() ? "N/A" : noWA) + ")"));
// Menambahkan header untuk LIST PESANAN
table.addCell(createLeftAlignedCell("LIST PESANAN:"));

// Membagi listPesanan berdasarkan baris baru
String[] pesananArray = listPesanan.split("\n");

// Menambahkan setiap pesanan ke dalam tabel tanpa perubahan apa pun
for (String pesanan : pesananArray) {
    table.addCell(createLeftAlignedCell(pesanan)); // Menambahkan pesanan apa adanya
}


// Menambahkan Baris Kosong antara Kiri dan Kanan
table.addCell(createLeftAlignedCell("")); // Baris kosong
// Menambahkan Baris Kosong antara Kiri dan Kanan
table.addCell(createLeftAlignedCell("")); // Baris kosong
// Menambahkan Baris Kosong antara Kiri dan Kanan
table.addCell(createLeftAlignedCell("")); // Baris kosong

// Bagian Kanan: Total, Bayar, Kembali
table.addCell(createRightAlignedCell("Total: Rp" + totalPembayaran));
table.addCell(createRightAlignedCell("Bayar: Rp" + uangBayar));
table.addCell(createRightAlignedCell("----------------------------------------"));
table.addCell(createRightAlignedCell("Kembali: Rp" + uangKembali));

// Menambahkan tabel ke dokumen
document.add(table);
        
           // Menambahkan footer dengan tanggal
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
        String currentDate = LocalDate.now().format(dateFormatter);
        String location = "Cibinong";
        String formattedDate = location + ", " + currentDate;

        Paragraph dateTimeParagraph = new Paragraph(formattedDate, new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.ITALIC));
        dateTimeParagraph.setAlignment(Element.ALIGN_RIGHT);
        dateTimeParagraph.setSpacingBefore(20);
        document.add(dateTimeParagraph);

        document.add(new Paragraph("\n\n\n\n"));
        Paragraph admin = new Paragraph("ADMIN", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        admin.setAlignment(Element.ALIGN_RIGHT);
        document.add(admin);

            // Jika checkbox booking tidak terpilih, hapus data booking
            if (!cb_boking.isSelected()) {
                String deleteQuery = "DELETE FROM bookings WHERE name = ? AND whatsapp_number = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                deleteStmt.setString(1, namaPelanggan);
                deleteStmt.setString(2, noWA);
                deleteStmt.executeUpdate();
            }

            // Menutup dokumen
            document.close();

            // Menutup koneksi
            conn.close();

            // Menampilkan progress dan membuka file setelah selesai
            SwingUtilities.invokeLater(() -> {
                JPanel loadingPanel = new JPanel();
                JProgressBar progressBar = new JProgressBar();
                progressBar.setIndeterminate(true);
                loadingPanel.add(progressBar);

                JOptionPane optionPane = new JOptionPane(loadingPanel, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
                JDialog dialog = optionPane.createDialog("Generate Kwitansi Pembayaran");

                Timer timer = new Timer(2000, e -> {
                    dialog.dispose();
                    JOptionPane.showMessageDialog(null, "Kwitansi pembayaran berhasil dibuat. Tekan OK untuk membuka file.");
                    try {
                        File file = new File(fileName);
                        Desktop.getDesktop().open(file);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                timer.setRepeats(false);
                timer.start();
                dialog.setVisible(true);
            });

        }
        
    } catch (SQLException | DocumentException | IOException ex) {
        ex.printStackTrace();
    }
}


// Fungsi untuk membuat cell yang teralign kiri
private PdfPCell createLeftAlignedCell(String text) {
    PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL)));
    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    cell.setBorder(Rectangle.NO_BORDER);
    return cell;
}

// Fungsi untuk membuat cell yang teralign kanan
private PdfPCell createRightAlignedCell(String text) {
    PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL)));
    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    cell.setBorder(Rectangle.NO_BORDER);
    return cell;
}



private void clearTextFieldDaftar() {
    txtNamaPelanggan2.setText("");
    txtAlamatPelanggan2.setText("");
    txtNoWA2.setText("");
    comboBoxJenisKelamin2.setSelectedIndex(0); // Mengatur combo box ke indeks pertama (misalnya 'Pilih Jenis Kelamin')
}
private void clearTextFieldAfterUbah() {
    Id_Pelanggan1.setText("");
    txtNamaPelanggan1.setText("");
    txtAlamatPelanggan1.setText("");
    txtNoWA1.setText("");
    comboBoxJenisKelamin1.setSelectedIndex(0); // Mengatur combo box ke indeks pertama (misalnya 'Pilih Jenis Kelamin')
}

private void refreshSupplier() {
    ID_Supplier.setText("");
    txtAlamatSup.setText("");
    txtNamaSup.setText("");
    txtKeteranganSup.setText("");
    txtNoTelpSup.setText("");
}


private void insertDataPelanggan() {
    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/hodltree", "root", "");
        PreparedStatement ps = conn.prepareStatement("INSERT INTO pelanggan (id_pelanggan, nama, jenis_kelamin, no_wa, alamat) VALUES (?, ?, ?, ?, ?)");

        if (txtNamaPelanggan2.getText().isEmpty() || txtAlamatPelanggan2.getText().isEmpty() || comboBoxJenisKelamin2.getSelectedItem() == null || txtNoWA2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon lengkapi semua data pelanggan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ps.setString(1, generateID());
        ps.setString(2, txtNamaPelanggan2.getText());
        ps.setString(3, comboBoxJenisKelamin2.getSelectedItem().toString());
        ps.setString(4, txtNoWA2.getText());
        ps.setString(5, txtAlamatPelanggan2.getText());
        ps.executeUpdate();

        JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
        clearTextFieldDaftar();
        conn.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan data: " + ex.getMessage());
    }
}



private void insertDataSupplier() {
    try {
         Connection conn = koneksi.koneksiDB();

        if (txtNamaSup.getText().isEmpty() || txtAlamatSup.getText().isEmpty() || txtNoTelpSup.getText().isEmpty() || txtKeteranganSup.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon lengkapi semua data supplier!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PreparedStatement ps = conn.prepareStatement("INSERT INTO supplier (id_supplier, nama, alamat, no_telepon, keterangan) VALUES (?,?,?,?,?)");
        ps.setString(1, generateID());
        ps.setString(2, txtNamaSup.getText());
        ps.setString(3, txtAlamatSup.getText());
        ps.setString(4, txtNoTelpSup.getText());
        ps.setString(5, txtKeteranganSup.getText());
        ps.executeUpdate();

        JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
        clearTextFieldDaftar();
        conn.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan data: " + ex.getMessage());
    }
}

private void deleteDataSupplier() {
    int selectedRow = Tabel_Supplier.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Mohon pilih baris untuk dihapus.");
        return;
    }

    String idSupplier = (String) Tabel_Supplier.getValueAt(selectedRow, 0);

    int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
             Connection conn = koneksi.koneksiDB();

            String sql = "DELETE FROM supplier WHERE id_supplier = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idSupplier);
            stmt.executeUpdate();

            stmt.close();
            conn.close();

            DefaultTableModel model = (DefaultTableModel) Tabel_Supplier.getModel();
            model.removeRow(selectedRow);

            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus data: " + ex.getMessage());
        }
    }
}

private void tampilkanTabelSupplier() {
    try {
         Connection conn = koneksi.koneksiDB();

        // buat query untuk mengambil data dari tabel supplier
        String query = "SELECT * FROM supplier";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // buat model tabel dengan header kolom
        String[] kolom = {"ID_Supplier", "Nama", "Alamat", "No_Telepon", "Keterangan"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        // tambahkan data dari ResultSet ke model tabel
        while (rs.next()) {
            Object[] data = {rs.getString("id_supplier"), rs.getString("nama"), rs.getString("alamat"), rs.getString("no_telepon"), rs.getString("keterangan")};
            model.addRow(data);
        }

        // set model tabel pada JTable
        Tabel_Supplier.setModel(model);

        // tutup koneksi ke database
        conn.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

private void updateDataSupplier() {
    try {
         Connection conn = koneksi.koneksiDB();
        PreparedStatement stmt = conn.prepareStatement("UPDATE supplier SET nama=?, alamat=?, no_telepon=?, keterangan=? WHERE id_supplier=?");

        if (ID_Supplier.getText().isEmpty() || txtNamaSup.getText().isEmpty() || txtAlamatSup.getText().isEmpty() || txtNoTelpSup.getText().isEmpty() || txtKeteranganSup.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon pilih salah satu data supplier pada tabel", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        stmt.setString(5, ID_Supplier.getText());
        stmt.setString(1, txtNamaSup.getText());
        stmt.setString(2, txtAlamatSup.getText());
        stmt.setString(3, txtNoTelpSup.getText());
        stmt.setString(4, txtKeteranganSup.getText());

        stmt.executeUpdate();
        JOptionPane.showMessageDialog(this, "Data berhasil diubah!");

        tampilkanTabelSupplier();
        conn.close();
    } catch (SQLException ex) {
        System.out.println("Error: " + ex.getMessage());
    }
}

private void editSelectedRowSupplier() {
    int selectedRow = Tabel_Supplier.getSelectedRow();

    // Jika tidak ada baris yang dipilih, keluar dari method
    if (selectedRow == -1) {
        return;
    }

    // Mendapatkan nilai dari kolom-kolom di baris yang dipilih
    String idSupplier = Tabel_Supplier.getValueAt(selectedRow, 0).toString();
    String nama = Tabel_Supplier.getValueAt(selectedRow, 1).toString();
    String alamat = Tabel_Supplier.getValueAt(selectedRow, 2).toString();
    String noTelepon = Tabel_Supplier.getValueAt(selectedRow, 3).toString();
    String keterangan = Tabel_Supplier.getValueAt(selectedRow, 4).toString();

    // Mengatur nilai pada komponen input berdasarkan data yang dipilih
    ID_Supplier.setText(idSupplier);
    txtNamaSup.setText(nama);
    txtAlamatSup.setText(alamat);
    txtNoTelpSup.setText(noTelepon);
    txtKeteranganSup.setText(keterangan);
}


private void editSelectedRowPelanggan() {
    int selectedRow = Tabel_Pelanggan.getSelectedRow();

    // Jika tidak ada baris yang dipilih, keluar dari method
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Pilih baris yang ingin diedit terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Mendapatkan nilai dari kolom-kolom di baris yang dipilih
    String idPelanggan = Tabel_Pelanggan.getValueAt(selectedRow, 0).toString();
    String nama = Tabel_Pelanggan.getValueAt(selectedRow, 1).toString();
    String jenisKelamin = Tabel_Pelanggan.getValueAt(selectedRow, 2).toString();
    String noWa = Tabel_Pelanggan.getValueAt(selectedRow, 3).toString();
    String alamat = Tabel_Pelanggan.getValueAt(selectedRow, 4).toString();

    // Mengatur nilai pada komponen input berdasarkan data yang dipilih
    txtNamaPelanggan2.setText(nama);
    comboBoxJenisKelamin2.setSelectedItem(jenisKelamin);
    txtNoWA2.setText(noWa);
    txtAlamatPelanggan2.setText(alamat);
    Id_Pelanggan2.setText(idPelanggan);

    
}



private class WatermarkEvent extends PdfPageEventHelper {
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            PdfContentByte canvas = writer.getDirectContentUnder();

            // Buat objek BaseFont dengan font yang diinginkan
            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
            canvas.beginText();

            // Atur font dan ukuran teks
            canvas.setFontAndSize(baseFont, 80);

            // Buat objek PdfGState untuk mengatur opasitas watermark
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.3f); // Ubah nilai opacity sesuai kebutuhan
            canvas.setGState(gs);

            // Tulis teks watermark pada posisi tengah halaman
            canvas.showTextAligned(Element.ALIGN_CENTER, "DIBAYAR LUNAS", document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 45);

            canvas.endText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





private void updateData() {
    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/hodltree", "root", "");
        PreparedStatement stmt = conn.prepareStatement("UPDATE pelanggan SET nama=?, jenis_kelamin=?, no_wa=?, alamat=? WHERE id_pelanggan=?");
        
        // Validasi data sebelum update
        if (Id_Pelanggan2.getText().isEmpty() || txtNamaPelanggan2.getText().isEmpty() || comboBoxJenisKelamin2.getSelectedItem() == null || txtNoWA2.getText().isEmpty() || txtAlamatPelanggan2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon lengkapi semua data pelanggan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        stmt.setString(5, Id_Pelanggan2.getText());
        stmt.setString(1, txtNamaPelanggan2.getText());
        stmt.setString(2, comboBoxJenisKelamin2.getSelectedItem().toString());
        stmt.setString(3, txtNoWA2.getText());
        stmt.setString(4, txtAlamatPelanggan2.getText());
        
        stmt.executeUpdate();
        JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
        
        // Menampilkan data terbaru di tabel
        tampilkanTabelPelanggan();
        conn.close();
    } catch (SQLException ex) {
        System.out.println("Error: " + ex.getMessage());
    }
    clearTextFieldAfterUbah();
    
    
        TabPane.setSelectedIndex(3);
}

public void tampilkanTabelPelanggan() {
    try {
         Connection conn = koneksi.koneksiDB();

        // Buat query untuk mengambil data dari tabel pelanggan
        String query = "SELECT * FROM pelanggan";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // Buat model tabel dengan header kolom
        String[] kolom = {"ID_PELANGGAN", "Nama", "Jenis_Kelamin", "No_WA", "Alamat"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        // Tambahkan data dari ResultSet ke model tabel
        while (rs.next()) {
            Object[] data = {rs.getString("id_pelanggan"), rs.getString("nama"), rs.getString("jenis_kelamin"), rs.getString("no_wa"), rs.getString("alamat")};
            model.addRow(data);
        }

        // Set model tabel pada JTable
        Tabel_Pelanggan.setModel(model);

        // Tutup koneksi ke database
        conn.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

private void deleteSelectedRow() {
    int selectedRow = Tabel_Pelanggan.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Mohon pilih satu data pelanggan dari tabel", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String ID_PELANGGAN = (String) Tabel_Pelanggan.getValueAt(selectedRow, 0);

    int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            // Koneksi ke database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/hodltree", "root", "");

            // Membuat prepared statement untuk menjalankan query delete
            String sql = "DELETE FROM pelanggan WHERE id_pelanggan = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ID_PELANGGAN);
            stmt.executeUpdate();

            // Menutup pernyataan dan koneksi
            stmt.close();
            conn.close();

            // Menghapus baris dari model tabel
            DefaultTableModel model = (DefaultTableModel) Tabel_Pelanggan.getModel();
            model.removeRow(selectedRow);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



 
  private PdfPCell createCell(String label, String value) {
    PdfPCell cell = new PdfPCell();
    cell.setBorder(PdfPCell.NO_BORDER);
    cell.setPadding(5);

    Paragraph paragraph = new Paragraph(label + ":", new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD));
    paragraph.setAlignment(Element.ALIGN_LEFT);
    cell.addElement(paragraph);

    Paragraph valueParagraph = new Paragraph(value, new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
    valueParagraph.setAlignment(Element.ALIGN_LEFT);
    cell.addElement(valueParagraph);

    return cell;
}
  
  
  private void generatePDFLaporan_Selected_Pasien() {
int selectedRow = Tabel_Pelanggan.getSelectedRow();
// Jika tidak ada baris yang dipilih, keluar dari method
if (selectedRow == -1) {
    JOptionPane.showMessageDialog(null, "Mohon pilih satu data pasien dari tabel", "Peringatan", JOptionPane.WARNING_MESSAGE);
    return;
}


// Mendapatkan nilai dari kolom-kolom di baris yang dipilih
String idPasien = Tabel_Pelanggan.getValueAt(selectedRow, 0).toString();
String nik = Tabel_Pelanggan.getValueAt(selectedRow, 1).toString();
String nama = Tabel_Pelanggan.getValueAt(selectedRow, 2).toString();
java.util.Date tanggalLahir = (java.util.Date) Tabel_Pelanggan.getValueAt(selectedRow, 3);
String alamat = Tabel_Pelanggan.getValueAt(selectedRow, 4).toString();
String noHp = Tabel_Pelanggan.getValueAt(selectedRow, 6).toString();
String golDarah = Tabel_Pelanggan.getValueAt(selectedRow, 7).toString();

try {
    // Buat objek Document dari iText
    Document document = new Document();

    // Tentukan lokasi output file PDF
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    LocalDateTime currentDateTime = LocalDateTime.now();
    String currentTimeStamp = currentDateTime.format(formatter);
    String fileName = "C:\\Users\\ASUS\\Desktop\\laporan_pasien_" + currentTimeStamp + ".pdf";

    PdfWriter.getInstance(document, new FileOutputStream(fileName));

    // Buka dokumen
    document.open();

    Image logo = Image.getInstance("C:\\Users\\ASUS\\Desktop\\PERKULIAHAN\\SMT6\\PEMOGRAMAN VISUAL\\APBIKAN\\src\\image\\bidan-delima-logo.png");
        logo.scaleToFit(70, 70);
        logo.setAbsolutePosition(40, 750);
        document.add(logo);

        Paragraph header = new Paragraph("BIDAN DELIMA PIDAHAYATI", new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        Paragraph address = new Paragraph("Jl. H. Misan No.96 RT 003 , RW.011, Jati Luhur., Kec. Jatiasih , Kota Bekasi, Jawa Barat 17425", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL));
        address.setAlignment(Element.ALIGN_CENTER);
        document.add(address);

        document.add(new Paragraph("\n"));
        LineSeparator line = new LineSeparator();
        line.setLineColor(BaseColor.BLACK);
        line.setLineWidth(1);
        document.add(line);
        document.add(new Paragraph("\n"));

        Paragraph judul = new Paragraph("INFORMASI PASIEN", new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD));
        judul.setAlignment(Element.ALIGN_CENTER);
        document.add(judul);
        document.add(new Paragraph("\n"));

PdfPTable table = new PdfPTable(2); // Membuat tabel dengan 2 kolom
table.setWidthPercentage(100); // Set lebar tabel menjadi 100% dari halaman

// Baris pertama (ID Pasien)
table.addCell(createCell("ID Pasien", idPasien));

// Baris kedua (NIK)
table.addCell(createCell("NIK", nik));

// Baris ketiga (Nama)
table.addCell(createCell("Nama", nama));

// Baris keempat (Tanggal Lahir)
table.addCell(createCell("Tanggal Lahir", tanggalLahir.toString()));

// Baris kelima (Alamat)
table.addCell(createCell("Alamat", alamat));

// Baris keenam (No. HP)
table.addCell(createCell("No. HP", noHp));

// Baris ketujuh (Golongan Darah)
table.addCell(createCell("Golongan Darah", golDarah));

document.add(table);

    
        document.add(new Paragraph("\n"));
        LineSeparator linee = new LineSeparator();
        linee.setLineColor(BaseColor.BLACK);
        linee.setLineWidth(1);
        document.add(linee);
        document.add(new Paragraph("\n"));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
String currentDate = LocalDate.now().format(dateFormatter);

String location = "Bekasi";
String formattedDate = location + ", " + currentDate;

Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.ITALIC);
Paragraph dateTimeParagraph = new Paragraph(formattedDate, font);
dateTimeParagraph.setAlignment(Element.ALIGN_RIGHT);
dateTimeParagraph.setSpacingBefore(20);
document.add(dateTimeParagraph);
document.add(new Paragraph("\n"));
document.add(new Paragraph("\n"));
document.add(new Paragraph("\n"));
document.add(new Paragraph("\n"));
    Paragraph admin = new Paragraph("ADMIN", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
    admin.setAlignment(Element.ALIGN_RIGHT);
    document.add(admin);
        
    // Tutup dokumen
    document.close();

    SwingUtilities.invokeLater(() -> {
        // Membuat panel loading
        JPanel loadingPanel = new JPanel();
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        loadingPanel.add(progressBar);

        // Menampilkan JOptionPane dengan panel loading sebagai pesan
        JOptionPane optionPane = new JOptionPane(loadingPanel, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        JDialog dialog = optionPane.createDialog("Generate Laporan Pasien");

        // Menjadwalkan penutupan dialog setelah 3 detik menggunakan javax.swing.Timer
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                // Menampilkan JOptionPane setelah tugas selesai
                JOptionPane.showMessageDialog(null, "Data berhasil digenerate. Tekan OK untuk membuka file.");
                try {
                    File file = new File(fileName);
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(file);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        timer.setRepeats(false); // Hanya dijalankan sekali
        timer.start();

        dialog.setVisible(true);
    });

} catch (DocumentException | IOException ex) {
    ex.printStackTrace();
}

  }
        
   private void editselectedRow(){
        
    int selectedRow = Tabel_Pelanggan.getSelectedRow();

    // Jika tidak ada baris yang dipilih, keluar dari method
    if (selectedRow == -1) {
        return;
    }

    // Mendapatkan nilai dari kolom-kolom di baris yang dipilih
    String idPelanggan = Tabel_Pelanggan.getValueAt(selectedRow, 0).toString();
    String nama = Tabel_Pelanggan.getValueAt(selectedRow, 1).toString();
    String jenisKelamin = Tabel_Pelanggan.getValueAt(selectedRow, 2).toString();
    String noWA = Tabel_Pelanggan.getValueAt(selectedRow, 3).toString();
    String alamat = Tabel_Pelanggan.getValueAt(selectedRow, 4).toString();

    // Mengisi nilai ke dalam form untuk edit
    Id_Pelanggan1.setText(idPelanggan);
    txtNamaPelanggan1.setText(nama);
    comboBoxJenisKelamin1.setSelectedItem(jenisKelamin);
    txtNoWA1.setText(noWA);
    txtAlamatPelanggan1.setText(alamat);
    
    //        DisplayDataCount.displayCount(countpasien);
        TabPane.setSelectedIndex(5);
    
  }

private void clearTextFieldKategori() {
    ID_Kategori.setText("");
    txtNamaKategori.setText("");  // Mengosongkan text field nama kategori
}


   
private void insertDataKategori() {
    try {
         Connection conn = koneksi.koneksiDB();

        if (txtNamaKategori.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon lengkapi data kategori!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Menggunakan UUID untuk menghasilkan id_kategori otomatis
        String idKategori = generateID();  // Fungsi generateID() harus menghasilkan ID unik, bisa menggunakan UUID.randomUUID().toString()

        PreparedStatement ps = conn.prepareStatement("INSERT INTO kategori (id_kategori, nama_kategori) VALUES (?,?)");
        ps.setString(1, idKategori);
        ps.setString(2, txtNamaKategori.getText());
        ps.executeUpdate();

        JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
        clearTextFieldKategori(); // Asumsikan ada metode untuk membersihkan text field
        conn.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan data: " + ex.getMessage());
    }
}

private void tampilkanTabelKategori() {
    try {
         Connection conn = koneksi.koneksiDB();

        String query = "SELECT * FROM kategori";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        String[] kolom = {"ID_Kategori", "Nama_Kategori"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        while (rs.next()) {
            Object[] data = {rs.getString("id_kategori"), rs.getString("nama_kategori")};
            model.addRow(data);
        }

        Tabel_Kategori.setModel(model);
        conn.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

private void updateDataKategori() {
    try {
         Connection conn = koneksi.koneksiDB();
        PreparedStatement stmt = conn.prepareStatement("UPDATE kategori SET nama_kategori=? WHERE id_kategori=?");

        if (ID_Kategori.getText().isEmpty() || txtNamaKategori.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon pilih salah satu data kategori pada tabel", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        stmt.setString(2, ID_Kategori.getText());
        stmt.setString(1, txtNamaKategori.getText());

        stmt.executeUpdate();
        JOptionPane.showMessageDialog(this, "Data berhasil diubah!");

        tampilkanTabelKategori();
        conn.close();
    } catch (SQLException ex) {
        System.out.println("Error: " + ex.getMessage());
    }
}

private void deleteDataKategori() {
    int selectedRow = Tabel_Kategori.getSelectedRow();
    
    // Jika tidak ada baris yang dipilih, tampilkan pesan dan keluar dari method
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Pilih data kategori yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Mendapatkan ID kategori dari baris yang dipilih
    String idKategori = Tabel_Kategori.getValueAt(selectedRow, 0).toString();

    try {
         Connection conn = koneksi.koneksiDB();
        
        // Membuat query untuk menghapus data berdasarkan id_kategori
        PreparedStatement ps = conn.prepareStatement("DELETE FROM kategori WHERE id_kategori=?");
        ps.setString(1, idKategori);
        
        int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus kategori ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
            tampilkanTabelKategori();  // Menampilkan kembali tabel kategori setelah dihapus
        }
        
        conn.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus data: " + ex.getMessage());
    }
}

private void editSelectedRowKategori() {
    int selectedRow = Tabel_Kategori.getSelectedRow();

    if (selectedRow == -1) {
        return;
    }

    String idKategori = Tabel_Kategori.getValueAt(selectedRow, 0).toString();
    String namaKategori = Tabel_Kategori.getValueAt(selectedRow, 1).toString();

    ID_Kategori.setText(idKategori);  // ID_Kategori tetap ada sebagai teks yang ditampilkan, tapi tidak untuk input
    txtNamaKategori.setText(namaKategori);
}

private void clearTextFieldSatuan() {
    ID_Satuan.setText("");  // Jika ada text field untuk ID
    txtNamaSatuan.setText("");  // Mengosongkan text field nama satuan
}


private void insertDataSatuan() {
    try {
         Connection conn = koneksi.koneksiDB();

        if (txtNamaSatuan.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon lengkapi data satuan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Menggunakan auto-increment untuk id_satuan
        PreparedStatement ps = conn.prepareStatement("INSERT INTO satuan (nama_satuan) VALUES (?)");
        ps.setString(1, txtNamaSatuan.getText());
        ps.executeUpdate();

        JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
        clearTextFieldSatuan();  // Mengosongkan form input
        conn.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan data: " + ex.getMessage());
    }
}

private void tampilkanTabelSatuan() {
    try {
         Connection conn = koneksi.koneksiDB();

        String query = "SELECT * FROM satuan";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        String[] kolom = {"ID_Satuan", "Nama_Satuan"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        while (rs.next()) {
            Object[] data = {rs.getString("id_satuan"), rs.getString("nama_satuan")};
            model.addRow(data);
        }

        Tabel_Satuan.setModel(model);
        conn.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

private void updateDataSatuan() {
    try {
         Connection conn = koneksi.koneksiDB();
        PreparedStatement stmt = conn.prepareStatement("UPDATE satuan SET nama_satuan=? WHERE id_satuan=?");

        if (ID_Satuan.getText().isEmpty() || txtNamaSatuan.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon pilih salah satu data satuan pada tabel", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        stmt.setString(2, ID_Satuan.getText());
        stmt.setString(1, txtNamaSatuan.getText());

        stmt.executeUpdate();
        JOptionPane.showMessageDialog(this, "Data berhasil diubah!");

        tampilkanTabelSatuan();
        conn.close();
    } catch (SQLException ex) {
        System.out.println("Error: " + ex.getMessage());
    }
}

private void deleteDataSatuan() {
    int selectedRow = Tabel_Satuan.getSelectedRow();
    
    // Jika tidak ada baris yang dipilih, tampilkan pesan dan keluar dari method
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Pilih data satuan yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Mendapatkan ID satuan dari baris yang dipilih
    String idSatuan = Tabel_Satuan.getValueAt(selectedRow, 0).toString();

    try {
         Connection conn = koneksi.koneksiDB();
        
        // Membuat query untuk menghapus data berdasarkan id_satuan
        PreparedStatement ps = conn.prepareStatement("DELETE FROM satuan WHERE id_satuan=?");
        ps.setString(1, idSatuan);
        
        int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus satuan ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
            tampilkanTabelSatuan();  // Menampilkan kembali tabel satuan setelah dihapus
        }
        
        conn.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus data: " + ex.getMessage());
    }
}

private void editSelectedRowSatuan() {
    int selectedRow = Tabel_Satuan.getSelectedRow();

    if (selectedRow == -1) {
        return;
    }

    String idSatuan = Tabel_Satuan.getValueAt(selectedRow, 0).toString();
    String namaSatuan = Tabel_Satuan.getValueAt(selectedRow, 1).toString();

    ID_Satuan.setText(idSatuan);  // ID_Satuan tetap ada sebagai teks yang ditampilkan, tapi tidak untuk input
    txtNamaSatuan.setText(namaSatuan);
}


private void clearTextFieldProduk() {
    // Membersihkan semua komponen input terkait produk
    ID_Produk.setText("");               // Menghapus ID Produk
    comboBoxKategori.setSelectedIndex(-1); // Mengosongkan pilihan kategori
    comboBoxSatuan.setSelectedIndex(-1);  // Mengosongkan pilihan satuan
    txtNamaProduk.setText("");           // Menghapus nama produk
    txtHarga.setText("");                // Menghapus harga produk
    txtStok.setText("");                 // Menghapus stok produk
     loadCategories();
  loadSatuan();
}


private void insertDataProduk() {
    try {
         Connection conn = koneksi.koneksiDB();

        if (comboBoxKategori.getSelectedItem() == null || comboBoxSatuan.getSelectedItem() == null || txtNamaProduk.getText().isEmpty() || txtHarga.getText().isEmpty() || txtStok.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon lengkapi semua data produk!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PreparedStatement ps = conn.prepareStatement("INSERT INTO produk (kategori, satuan, nama_produk, harga, stok) VALUES (?,?,?,?,?)");
        ps.setString(1, comboBoxKategori.getSelectedItem().toString());
        ps.setString(2, comboBoxSatuan.getSelectedItem().toString());
        ps.setString(3, txtNamaProduk.getText());
        ps.setDouble(4, Double.parseDouble(txtHarga.getText()));
        ps.setInt(5, Integer.parseInt(txtStok.getText()));
        ps.executeUpdate();

        JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
        clearTextFieldProduk();
        conn.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan data: " + ex.getMessage());
    }
}

private void deleteDataProduk() {
    int selectedRow = Tabel_Produk.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Mohon pilih baris untuk dihapus.");
        return;
    }

    String idProduk = (String) Tabel_Produk.getValueAt(selectedRow, 0);

    int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
             Connection conn = koneksi.koneksiDB();

            String sql = "DELETE FROM produk WHERE id_produk = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idProduk);
            stmt.executeUpdate();

            stmt.close();
            conn.close();

            DefaultTableModel model = (DefaultTableModel) Tabel_Produk.getModel();
            model.removeRow(selectedRow);

            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus data: " + ex.getMessage());
        }
    }
}

private void tampilkanTabelProduk() {
    try {
         Connection conn = koneksi.koneksiDB();

        // buat query untuk mengambil data dari tabel produk
        String query = "SELECT * FROM produk";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // buat model tabel dengan header kolom
        String[] kolom = {"ID_Produk", "Kategori", "Satuan", "Nama Produk", "Harga", "Stok"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        // tambahkan data dari ResultSet ke model tabel
        while (rs.next()) {
            Object[] data = {
                rs.getString("id_produk"),
                rs.getString("kategori"),
                rs.getString("satuan"),
                rs.getString("nama_produk"),
                rs.getDouble("harga"),
                rs.getInt("stok")
            };
            model.addRow(data);
        }

        // set model tabel pada JTable
        Tabel_Produk.setModel(model);

        // tutup koneksi ke database
        conn.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

private void updateDataProduk() {
    try {
         Connection conn = koneksi.koneksiDB();
        PreparedStatement stmt = conn.prepareStatement("UPDATE produk SET kategori=?, satuan=?, nama_produk=?, harga=?, stok=? WHERE id_produk=?");

        if (ID_Produk.getText().isEmpty() || comboBoxKategori.getSelectedItem() == null || comboBoxSatuan.getSelectedItem() == null || txtNamaProduk.getText().isEmpty() || txtHarga.getText().isEmpty() || txtStok.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon pilih salah satu data produk pada tabel", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        stmt.setString(1, comboBoxKategori.getSelectedItem().toString());
        stmt.setString(2, comboBoxSatuan.getSelectedItem().toString());
        stmt.setString(3, txtNamaProduk.getText());
        stmt.setDouble(4, Double.parseDouble(txtHarga.getText()));
        stmt.setInt(5, Integer.parseInt(txtStok.getText()));
        stmt.setString(6, ID_Produk.getText());

        stmt.executeUpdate();
        JOptionPane.showMessageDialog(this, "Data berhasil diubah!");

        tampilkanTabelProduk();
        conn.close();
    } catch (SQLException ex) {
        System.out.println("Error: " + ex.getMessage());
    }
}

private void editSelectedRowProduk() {
    int selectedRow = Tabel_Produk.getSelectedRow();

    // Jika tidak ada baris yang dipilih, keluar dari method
    if (selectedRow == -1) {
        return;
    }

    // Mendapatkan nilai dari kolom-kolom di baris yang dipilih
    String idProduk = Tabel_Produk.getValueAt(selectedRow, 0).toString();
    String kategori = Tabel_Produk.getValueAt(selectedRow, 1).toString();
    String satuan = Tabel_Produk.getValueAt(selectedRow, 2).toString();
    String namaProduk = Tabel_Produk.getValueAt(selectedRow, 3).toString();
    String harga = Tabel_Produk.getValueAt(selectedRow, 4).toString();
    String stok = Tabel_Produk.getValueAt(selectedRow, 5).toString();

    // Mengatur nilai pada komponen input berdasarkan data yang dipilih
    ID_Produk.setText(idProduk);
    comboBoxKategori.setSelectedItem(kategori);
    comboBoxSatuan.setSelectedItem(satuan);
    txtNamaProduk.setText(namaProduk);
    txtHarga.setText(harga);
    txtStok.setText(stok);
}


    public void tampilkanTabelBookings() {
        try {
            // Membuat koneksi ke database
             Connection conn = koneksi.koneksiDB();

            // Query untuk mengambil data dari tabel bookings
            String query = "SELECT * FROM bookings";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Membuat model tabel dengan header kolom
            String[] kolom = {"ID Booking", "Nama", "Tanggal", "Waktu", "Jumlah_People", "No_WA", "Email", "Member"};
            DefaultTableModel model = new DefaultTableModel(kolom, 0);

            // Menambahkan data dari ResultSet ke model tabel
            while (rs.next()) {
                Object[] data = {
                    rs.getInt("booking_id"),
                    rs.getString("name"),
                    rs.getString("date"),
                    rs.getString("time"),
                    rs.getInt("people"),
                    rs.getString("whatsapp_number"),
                    rs.getString("email"),
                    rs.getString("is_member")
                };
                model.addRow(data);
            }

            // Set model tabel pada JTable
            Tabel_Booking.setModel(model);

            // Menutup koneksi ke database
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

public void tampilkanTabelBookingsHome() {
    try {
        // Mendapatkan tanggal hari ini dalam format yang sesuai dengan format pada database
        Calendar cal = Calendar.getInstance();
        java.util.Date utilDate = cal.getTime();  // Tanggal hari ini
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());  // Konversi ke java.sql.Date

        // Membuat koneksi ke database
         Connection conn = koneksi.koneksiDB();

        // Query untuk mengambil data hanya untuk tanggal hari ini dan mengurutkan berdasarkan tanggal
        String query = "SELECT booking_id, name, time FROM bookings WHERE date = ? ORDER BY date";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setDate(1, sqlDate);  // Set parameter tanggal dengan tanggal hari ini (java.sql.Date)
        ResultSet rs = ps.executeQuery();

        // Membuat model tabel dengan header kolom yang diinginkan
        String[] kolom = {"ID Booking", "Nama", "Jam"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        // Menambahkan data dari ResultSet ke model tabel
        while (rs.next()) {
            Object[] data = {
                rs.getInt("booking_id"),  // ID Booking
                rs.getString("name"),      // Nama
                rs.getString("time")       // Jam
            };
            model.addRow(data);
        }

        // Set model tabel pada JTable
        Tabel_Antrian.setModel(model);

        // Menutup koneksi ke database
        conn.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}


private void BookingToTransaksi() {
    int selectedRow = Tabel_Booking.getSelectedRow();

     if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Mohon pilih salah satu pelanggan yg booking.");
        return;
    }
    // Mendapatkan nilai dari kolom-kolom di baris yang dipilih
    String namapelbok = Tabel_Booking.getValueAt(selectedRow, 1).toString();
    String wapelbok = Tabel_Booking.getValueAt(selectedRow, 5).toString();

    txtNamaPelangganBayar.setText(namapelbok);
    txtNoWABayar.setText(wapelbok);
        TabPane.setSelectedIndex(7);
}

   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        kButton4 = new com.k33ptoo.components.KButton();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        Side_Bar = new com.k33ptoo.components.KGradientPanel();
        jLabel2 = new javax.swing.JLabel();
        kButton5 = new com.k33ptoo.components.KButton();
        kButton7 = new com.k33ptoo.components.KButton();
        jLabel9 = new javax.swing.JLabel();
        kButton9 = new com.k33ptoo.components.KButton();
        jLabel10 = new javax.swing.JLabel();
        kButton10 = new com.k33ptoo.components.KButton();
        jLabel11 = new javax.swing.JLabel();
        kButton11 = new com.k33ptoo.components.KButton();
        jLabel12 = new javax.swing.JLabel();
        labelwaktu = new LabelTime();
        labelwaktu1 = new LabelDay();
        kButton2 = new com.k33ptoo.components.KButton();
        jLabel38 = new javax.swing.JLabel();
        kButton13 = new com.k33ptoo.components.KButton();
        jLabel24 = new javax.swing.JLabel();
        kButton14 = new com.k33ptoo.components.KButton();
        jLabel13 = new javax.swing.JLabel();
        kButton12 = new com.k33ptoo.components.KButton();
        TOP = new com.k33ptoo.components.KGradientPanel();
        kButton1 = new com.k33ptoo.components.KButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        Exit = new com.k33ptoo.components.KButton();
        Minimize = new com.k33ptoo.components.KButton();
        TabPane = new javax.swing.JTabbedPane();
        beranda = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        Tabel_Antrian = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        SIMPAN_BTN2 = new com.k33ptoo.components.KButton();
        SIMPAN_BTN4 = new com.k33ptoo.components.KButton();
        SIMPAN_BTN5 = new com.k33ptoo.components.KButton();
        SIMPAN_BTN6 = new com.k33ptoo.components.KButton();
        SIMPAN_BTN7 = new com.k33ptoo.components.KButton();
        pendaftaran = new javax.swing.JPanel();
        txtNamaPelanggan = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAlamatPelanggan = new javax.swing.JTextArea();
        txtNoWA = new javax.swing.JTextField();
        comboBoxJenisKelamin = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        Id_Pelanggan = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        SIMPAN_BTN = new com.k33ptoo.components.KButton();
        bidan = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Tabel_Supplier = new javax.swing.JTable();
        reset_tabel2 = new com.k33ptoo.components.KButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        txtNamaSup = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtNoTelpSup = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        txtAlamatSup = new javax.swing.JTextField();
        txtKeteranganSup = new javax.swing.JTextField();
        delete_table2 = new com.k33ptoo.components.KButton();
        ID_Supplier = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        SIMPAN_BTN1 = new com.k33ptoo.components.KButton();
        txtSearchSup = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        pasien = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabel_Pelanggan = new javax.swing.JTable();
        delete_table = new com.k33ptoo.components.KButton();
        reset_tabel1 = new com.k33ptoo.components.KButton();
        edit_table = new com.k33ptoo.components.KButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        txtsearch = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        txtNamaPelanggan2 = new javax.swing.JTextField();
        jScrollPane11 = new javax.swing.JScrollPane();
        txtAlamatPelanggan2 = new javax.swing.JTextArea();
        txtNoWA2 = new javax.swing.JTextField();
        comboBoxJenisKelamin2 = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        Id_Pelanggan2 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        SIMPAN_BTN10 = new com.k33ptoo.components.KButton();
        pelayanan = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        Tabel_Kategori = new javax.swing.JTable();
        reset_tabel3 = new com.k33ptoo.components.KButton();
        txtNamaKategori = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        delete_table3 = new com.k33ptoo.components.KButton();
        SIMPAN_BTN3 = new com.k33ptoo.components.KButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        Tabel_Satuan = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        ID_Kategori = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        reset_tabel5 = new com.k33ptoo.components.KButton();
        txtNamaSatuan = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        delete_table5 = new com.k33ptoo.components.KButton();
        SIMPAN_BTN9 = new com.k33ptoo.components.KButton();
        ID_Satuan = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        edit = new javax.swing.JPanel();
        edit_BTN = new com.k33ptoo.components.KButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        edit_BTN1 = new com.k33ptoo.components.KButton();
        txtNamaPelanggan1 = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtAlamatPelanggan1 = new javax.swing.JTextArea();
        txtNoWA1 = new javax.swing.JTextField();
        comboBoxJenisKelamin1 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        Id_Pelanggan1 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        pembayaran1 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jScrollPane16 = new javax.swing.JScrollPane();
        Tabel_Produk = new javax.swing.JTable();
        reset_tabel4 = new com.k33ptoo.components.KButton();
        txtNamaProduk = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        jLabel119 = new javax.swing.JLabel();
        jLabel120 = new javax.swing.JLabel();
        txtHarga = new javax.swing.JTextField();
        txtStok = new javax.swing.JTextField();
        delete_table4 = new com.k33ptoo.components.KButton();
        ID_Produk = new javax.swing.JTextField();
        jLabel121 = new javax.swing.JLabel();
        SIMPAN_BTN8 = new com.k33ptoo.components.KButton();
        comboBoxKategori = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        comboBoxSatuan = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        txtSearchProduk = new javax.swing.JTextField();
        pembayaran = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel56 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        totalpembayaran = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        cb_boking = new javax.swing.JCheckBox();
        totalpembayaran2 = new javax.swing.JTextField();
        jLabel80 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        totalpembayaran3 = new javax.swing.JTextField();
        jLabel85 = new javax.swing.JLabel();
        reset_tabel7 = new com.k33ptoo.components.KButton();
        jLabel28 = new javax.swing.JLabel();
        txtNamaPelangganBayar = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtNoWABayar = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jLabel75 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtarea_listpesanan = new javax.swing.JTextArea();
        jPanel20 = new javax.swing.JPanel();
        jLabel76 = new javax.swing.JLabel();
        comboBoxProduk = new javax.swing.JComboBox<>();
        jLabel29 = new javax.swing.JLabel();
        txtSatuan = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txtBanyak = new javax.swing.JTextField();
        input_menu = new com.k33ptoo.components.KButton();
        delete_table6 = new com.k33ptoo.components.KButton();
        pasien1 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        Tabel_Booking = new javax.swing.JTable();
        edit_table1 = new com.k33ptoo.components.KButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        txtsearch2 = new javax.swing.JTextField();
        delete_table1 = new com.k33ptoo.components.KButton();

        kButton4.setText("kButton2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Side_Bar.setkEndColor(new java.awt.Color(238, 238, 238));
        Side_Bar.setkStartColor(new java.awt.Color(238, 238, 238));
        Side_Bar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-online-store-30.png"))); // NOI18N
        jLabel2.setText("Beranda");
        Side_Bar.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 100, -1));

        kButton5.setAlignmentY(0.0F);
        kButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        kButton5.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        kButton5.setkEndColor(new java.awt.Color(238, 238, 238));
        kButton5.setkHoverEndColor(new java.awt.Color(192, 192, 192));
        kButton5.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton5.setkHoverStartColor(new java.awt.Color(192, 192, 192));
        kButton5.setkSelectedColor(new java.awt.Color(57, 62, 70));
        kButton5.setkStartColor(new java.awt.Color(238, 238, 238));
        kButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton5ActionPerformed(evt);
            }
        });
        Side_Bar.add(kButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 160, 50));

        kButton7.setAlignmentY(0.0F);
        kButton7.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        kButton7.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        kButton7.setkEndColor(new java.awt.Color(238, 238, 238));
        kButton7.setkHoverEndColor(new java.awt.Color(255, 255, 255));
        kButton7.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton7.setkHoverStartColor(new java.awt.Color(255, 255, 255));
        kButton7.setkSelectedColor(new java.awt.Color(57, 62, 70));
        kButton7.setkStartColor(new java.awt.Color(238, 238, 238));
        Side_Bar.add(kButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 120, 50));

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-supplier-30.png"))); // NOI18N
        jLabel9.setText("Supplier");
        Side_Bar.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 100, -1));

        kButton9.setAlignmentY(0.0F);
        kButton9.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        kButton9.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        kButton9.setkEndColor(new java.awt.Color(238, 238, 238));
        kButton9.setkHoverEndColor(new java.awt.Color(192, 192, 192));
        kButton9.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton9.setkHoverStartColor(new java.awt.Color(192, 192, 192));
        kButton9.setkSelectedColor(new java.awt.Color(57, 62, 70));
        kButton9.setkStartColor(new java.awt.Color(238, 238, 238));
        kButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton9ActionPerformed(evt);
            }
        });
        Side_Bar.add(kButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 160, 50));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-racism-30.png"))); // NOI18N
        jLabel10.setText("Member");
        Side_Bar.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 100, -1));

        kButton10.setAlignmentY(0.0F);
        kButton10.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        kButton10.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        kButton10.setkEndColor(new java.awt.Color(238, 238, 238));
        kButton10.setkHoverEndColor(new java.awt.Color(192, 192, 192));
        kButton10.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton10.setkHoverStartColor(new java.awt.Color(192, 192, 192));
        kButton10.setkSelectedColor(new java.awt.Color(57, 62, 70));
        kButton10.setkStartColor(new java.awt.Color(238, 238, 238));
        kButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton10ActionPerformed(evt);
            }
        });
        Side_Bar.add(kButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 160, 50));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_online_support_30px.png"))); // NOI18N
        jLabel11.setText("Kategori & Satuan");
        Side_Bar.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 130, -1));

        kButton11.setAlignmentY(0.0F);
        kButton11.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        kButton11.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        kButton11.setkEndColor(new java.awt.Color(238, 238, 238));
        kButton11.setkHoverEndColor(new java.awt.Color(192, 192, 192));
        kButton11.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton11.setkHoverStartColor(new java.awt.Color(192, 192, 192));
        kButton11.setkSelectedColor(new java.awt.Color(57, 62, 70));
        kButton11.setkStartColor(new java.awt.Color(238, 238, 238));
        kButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton11ActionPerformed(evt);
            }
        });
        Side_Bar.add(kButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 160, 50));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logo2 (kecil).png"))); // NOI18N
        Side_Bar.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 100, 90));

        labelwaktu.setFont(new java.awt.Font("Tw Cen MT", 3, 12)); // NOI18N
        labelwaktu.setForeground(new java.awt.Color(34, 40, 49));
        labelwaktu.setText("JAM");
        Side_Bar.add(labelwaktu, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, 20));

        labelwaktu1.setFont(new java.awt.Font("Tw Cen MT", 3, 12)); // NOI18N
        labelwaktu1.setForeground(new java.awt.Color(34, 40, 49));
        labelwaktu1.setText("Hari");
        Side_Bar.add(labelwaktu1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, 20));

        kButton2.setText("Logout");
        kButton2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        kButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        kButton2.setIconTextGap(1);
        kButton2.setkEndColor(new java.awt.Color(192, 192, 192));
        kButton2.setkForeGround(new java.awt.Color(0, 0, 0));
        kButton2.setkHoverColor(new java.awt.Color(0, 0, 0));
        kButton2.setkHoverEndColor(new java.awt.Color(255, 255, 255));
        kButton2.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        kButton2.setkHoverStartColor(new java.awt.Color(255, 255, 255));
        kButton2.setkStartColor(new java.awt.Color(192, 192, 192));
        kButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton2ActionPerformed(evt);
            }
        });
        Side_Bar.add(kButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 600, 180, 30));

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_cash_in_hand_30px.png"))); // NOI18N
        jLabel38.setText("Transaksi");
        Side_Bar.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 130, 50));

        kButton13.setAlignmentY(0.0F);
        kButton13.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        kButton13.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        kButton13.setkEndColor(new java.awt.Color(238, 238, 238));
        kButton13.setkHoverEndColor(new java.awt.Color(192, 192, 192));
        kButton13.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton13.setkHoverStartColor(new java.awt.Color(192, 192, 192));
        kButton13.setkSelectedColor(new java.awt.Color(57, 62, 70));
        kButton13.setkStartColor(new java.awt.Color(238, 238, 238));
        kButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton13ActionPerformed(evt);
            }
        });
        Side_Bar.add(kButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 460, 160, 50));

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_Plus_30px.png"))); // NOI18N
        jLabel24.setText("Reservasi");
        Side_Bar.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 130, 50));

        kButton14.setAlignmentY(0.0F);
        kButton14.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        kButton14.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        kButton14.setkEndColor(new java.awt.Color(238, 238, 238));
        kButton14.setkHoverEndColor(new java.awt.Color(192, 192, 192));
        kButton14.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton14.setkHoverStartColor(new java.awt.Color(192, 192, 192));
        kButton14.setkSelectedColor(new java.awt.Color(57, 62, 70));
        kButton14.setkStartColor(new java.awt.Color(238, 238, 238));
        kButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton14ActionPerformed(evt);
            }
        });
        Side_Bar.add(kButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 410, 160, 50));

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_online_support_30px.png"))); // NOI18N
        jLabel13.setText("Produk & Stok");
        Side_Bar.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 130, -1));

        kButton12.setAlignmentY(0.0F);
        kButton12.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        kButton12.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        kButton12.setkEndColor(new java.awt.Color(238, 238, 238));
        kButton12.setkHoverEndColor(new java.awt.Color(192, 192, 192));
        kButton12.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton12.setkHoverStartColor(new java.awt.Color(192, 192, 192));
        kButton12.setkSelectedColor(new java.awt.Color(57, 62, 70));
        kButton12.setkStartColor(new java.awt.Color(238, 238, 238));
        kButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton12ActionPerformed(evt);
            }
        });
        Side_Bar.add(kButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 360, 160, 50));

        getContentPane().add(Side_Bar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 160, 630));

        TOP.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        TOP.setkEndColor(new java.awt.Color(242, 242, 242));
        TOP.setkStartColor(new java.awt.Color(242, 242, 242));
        TOP.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        kButton1.setText("kButton1");
        TOP.add(kButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-120, 40, 120, -1));
        TOP.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, -20, -1, -1));

        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("hodl tree app v1.0");
        TOP.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 0, -1, 30));
        TOP.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, -30, -1, -1));

        Exit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Exit.setkBackGroundColor(new java.awt.Color(204, 0, 0));
        Exit.setkEndColor(new java.awt.Color(204, 0, 0));
        Exit.setkStartColor(new java.awt.Color(204, 0, 0));
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });
        TOP.add(Exit, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 10, 10, 10));

        Minimize.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Minimize.setkBackGroundColor(new java.awt.Color(204, 0, 0));
        Minimize.setkEndColor(new java.awt.Color(255, 255, 51));
        Minimize.setkStartColor(new java.awt.Color(255, 255, 0));
        Minimize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MinimizeActionPerformed(evt);
            }
        });
        TOP.add(Minimize, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 10, 10, 10));

        getContentPane().add(TOP, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1130, 30));

        TabPane.setBackground(new java.awt.Color(57, 62, 70));

        beranda.setBackground(new java.awt.Color(57, 62, 70));
        beranda.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextPane2.setEditable(false);
        jTextPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTextPane2.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPane4.setViewportView(jTextPane2);

        beranda.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 120, 1080, 10));

        Tabel_Antrian.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID BOOKING", "Nama", "Jam"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane6.setViewportView(Tabel_Antrian);

        beranda.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 620, 150));

        jLabel3.setFont(new java.awt.Font("Rockwell Condensed", 3, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 204));
        jLabel3.setText("(APLIKASI RESERVASI CAFE)");
        beranda.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 70, 430, 40));

        jLabel26.setFont(new java.awt.Font("Tahoma", 3, 48)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 102, 102));
        jLabel26.setText("HODL TREE APP");
        beranda.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 400, 90));

        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel68.setFont(new java.awt.Font("Source Code Pro Black", 2, 18)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(57, 62, 70));
        jLabel68.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_Plus_30px.png"))); // NOI18N
        jLabel68.setText("Booking List Hari Ini");
        jPanel16.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, -10, 280, 60));

        beranda.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 330, 40));

        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel69.setFont(new java.awt.Font("Source Code Pro Black", 2, 24)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(57, 62, 70));
        jLabel69.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_sign_up_30px.png"))); // NOI18N
        jLabel69.setText("Buat Laporan");
        jPanel17.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, -10, -1, 60));

        beranda.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 170, 300, 40));

        SIMPAN_BTN2.setText("DATA PELANGGAN");
        SIMPAN_BTN2.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        SIMPAN_BTN2.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        SIMPAN_BTN2.setkBorderRadius(70);
        SIMPAN_BTN2.setkEndColor(new java.awt.Color(0, 0, 204));
        SIMPAN_BTN2.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        SIMPAN_BTN2.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        SIMPAN_BTN2.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        SIMPAN_BTN2.setkStartColor(new java.awt.Color(255, 204, 204));
        SIMPAN_BTN2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SIMPAN_BTN2ActionPerformed(evt);
            }
        });
        beranda.add(SIMPAN_BTN2, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 240, 230, 40));

        SIMPAN_BTN4.setText("LAPORAN TRANSAKSI");
        SIMPAN_BTN4.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        SIMPAN_BTN4.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        SIMPAN_BTN4.setkBorderRadius(70);
        SIMPAN_BTN4.setkEndColor(new java.awt.Color(0, 51, 204));
        SIMPAN_BTN4.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        SIMPAN_BTN4.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        SIMPAN_BTN4.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        SIMPAN_BTN4.setkStartColor(new java.awt.Color(255, 255, 51));
        SIMPAN_BTN4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SIMPAN_BTN4ActionPerformed(evt);
            }
        });
        beranda.add(SIMPAN_BTN4, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 420, 330, 40));

        SIMPAN_BTN5.setText("DATA SUPPLIER");
        SIMPAN_BTN5.setToolTipText("");
        SIMPAN_BTN5.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        SIMPAN_BTN5.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        SIMPAN_BTN5.setkBorderRadius(70);
        SIMPAN_BTN5.setkEndColor(new java.awt.Color(0, 255, 255));
        SIMPAN_BTN5.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        SIMPAN_BTN5.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        SIMPAN_BTN5.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        SIMPAN_BTN5.setkStartColor(new java.awt.Color(51, 51, 255));
        SIMPAN_BTN5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SIMPAN_BTN5ActionPerformed(evt);
            }
        });
        beranda.add(SIMPAN_BTN5, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 300, 230, 40));

        SIMPAN_BTN6.setText("CETAK LIST SEMUA RESERVASI YANG SEDANG BERJALAN");
        SIMPAN_BTN6.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        SIMPAN_BTN6.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        SIMPAN_BTN6.setkBorderRadius(70);
        SIMPAN_BTN6.setkEndColor(new java.awt.Color(0, 51, 204));
        SIMPAN_BTN6.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        SIMPAN_BTN6.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        SIMPAN_BTN6.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        SIMPAN_BTN6.setkStartColor(new java.awt.Color(255, 255, 51));
        SIMPAN_BTN6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SIMPAN_BTN6ActionPerformed(evt);
            }
        });
        beranda.add(SIMPAN_BTN6, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 480, 530, 40));

        SIMPAN_BTN7.setText("LAPORAN PRODUK & STOK");
        SIMPAN_BTN7.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        SIMPAN_BTN7.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        SIMPAN_BTN7.setkBorderRadius(70);
        SIMPAN_BTN7.setkEndColor(new java.awt.Color(0, 51, 204));
        SIMPAN_BTN7.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        SIMPAN_BTN7.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        SIMPAN_BTN7.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        SIMPAN_BTN7.setkStartColor(new java.awt.Color(255, 255, 51));
        SIMPAN_BTN7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SIMPAN_BTN7ActionPerformed(evt);
            }
        });
        beranda.add(SIMPAN_BTN7, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 360, 330, 40));

        TabPane.addTab("beranda", beranda);

        pendaftaran.setBackground(new java.awt.Color(57, 62, 70));
        pendaftaran.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtNamaPelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaPelangganActionPerformed(evt);
            }
        });
        pendaftaran.add(txtNamaPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 320, 30));

        txtAlamatPelanggan.setColumns(20);
        txtAlamatPelanggan.setRows(5);
        jScrollPane1.setViewportView(txtAlamatPelanggan);

        pendaftaran.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, 810, -1));

        txtNoWA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoWAActionPerformed(evt);
            }
        });
        pendaftaran.add(txtNoWA, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 240, 260, 30));

        comboBoxJenisKelamin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laki-Laki", "Perempuan" }));
        pendaftaran.add(comboBoxJenisKelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 130, 180, 30));

        jLabel4.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("ID Pelanggan");
        pendaftaran.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 170, -1));

        jLabel6.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Alamat");
        pendaftaran.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 320, 170, -1));

        jLabel17.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("No Whatsapp");
        pendaftaran.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 220, -1, -1));

        jLabel20.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Jenis Kelamin");
        pendaftaran.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 110, -1, -1));

        Id_Pelanggan.setEnabled(false);
        Id_Pelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Id_PelangganActionPerformed(evt);
            }
        });
        pendaftaran.add(Id_Pelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, 270, 30));

        jLabel18.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Nama");
        pendaftaran.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 100, -1));

        SIMPAN_BTN.setText("DAFTAR ");
        SIMPAN_BTN.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        SIMPAN_BTN.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        SIMPAN_BTN.setkBorderRadius(70);
        SIMPAN_BTN.setkEndColor(new java.awt.Color(0, 255, 255));
        SIMPAN_BTN.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        SIMPAN_BTN.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        SIMPAN_BTN.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        SIMPAN_BTN.setkStartColor(new java.awt.Color(0, 204, 51));
        SIMPAN_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SIMPAN_BTNActionPerformed(evt);
            }
        });
        pendaftaran.add(SIMPAN_BTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 540, 180, 40));

        TabPane.addTab("pendaftaran", pendaftaran);

        bidan.setBackground(new java.awt.Color(57, 62, 70));
        bidan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane3MouseClicked(evt);
            }
        });

        Tabel_Supplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Supllier", "Nama", "Alamat", "No Telepon", "Keterangan"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        Tabel_Supplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabel_SupplierMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(Tabel_Supplier);

        bidan.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 880, 160));

        reset_tabel2.setText("Ubah");
        reset_tabel2.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        reset_tabel2.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        reset_tabel2.setkBorderRadius(70);
        reset_tabel2.setkEndColor(new java.awt.Color(0, 255, 255));
        reset_tabel2.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        reset_tabel2.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        reset_tabel2.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        reset_tabel2.setkStartColor(new java.awt.Color(51, 51, 255));
        reset_tabel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reset_tabel2ActionPerformed(evt);
            }
        });
        bidan.add(reset_tabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 390, 100, 50));

        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel42.setFont(new java.awt.Font("Source Code Pro Black", 2, 24)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(57, 62, 70));
        jLabel42.setText("Daftar Supplier");
        jPanel9.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, -10, 220, 60));

        bidan.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 20, 690, 40));

        txtNamaSup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaSupActionPerformed(evt);
            }
        });
        bidan.add(txtNamaSup, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 380, 200, 30));

        jLabel43.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Nama");
        bidan.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 360, 80, -1));

        txtNoTelpSup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoTelpSupActionPerformed(evt);
            }
        });
        bidan.add(txtNoTelpSup, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 550, 200, 30));

        jLabel45.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("No Telepon");
        bidan.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 530, 80, 20));

        jLabel47.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("KETERANGAN");
        bidan.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 440, 100, 30));

        jLabel58.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("ALAMAT");
        bidan.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, 80, -1));

        txtAlamatSup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAlamatSupActionPerformed(evt);
            }
        });
        bidan.add(txtAlamatSup, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 470, 200, 30));

        txtKeteranganSup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKeteranganSupActionPerformed(evt);
            }
        });
        bidan.add(txtKeteranganSup, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 470, 190, 30));

        delete_table2.setText("Hapus");
        delete_table2.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        delete_table2.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        delete_table2.setkBorderRadius(70);
        delete_table2.setkEndColor(new java.awt.Color(0, 255, 255));
        delete_table2.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        delete_table2.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        delete_table2.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        delete_table2.setkStartColor(new java.awt.Color(255, 0, 0));
        delete_table2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_table2ActionPerformed(evt);
            }
        });
        bidan.add(delete_table2, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 390, 100, 50));

        ID_Supplier.setEnabled(false);
        ID_Supplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ID_SupplierActionPerformed(evt);
            }
        });
        bidan.add(ID_Supplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 200, 30));

        jLabel44.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("ID SUPPLIER");
        bidan.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 80, -1));

        SIMPAN_BTN1.setText("SIMPAN");
        SIMPAN_BTN1.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        SIMPAN_BTN1.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        SIMPAN_BTN1.setkBorderRadius(70);
        SIMPAN_BTN1.setkEndColor(new java.awt.Color(0, 255, 255));
        SIMPAN_BTN1.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        SIMPAN_BTN1.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        SIMPAN_BTN1.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        SIMPAN_BTN1.setkStartColor(new java.awt.Color(0, 204, 51));
        SIMPAN_BTN1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SIMPAN_BTN1ActionPerformed(evt);
            }
        });
        bidan.add(SIMPAN_BTN1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 550, 180, 40));

        txtSearchSup.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchSupKeyReleased(evt);
            }
        });
        bidan.add(txtSearchSup, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 90, 280, 40));

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel50.setFont(new java.awt.Font("Source Code Pro Black", 2, 24)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(57, 62, 70));
        jLabel50.setText("Search by Name :");
        jPanel7.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 0, 230, 40));

        bidan.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 390, 40));

        TabPane.addTab("dokter", bidan);

        pasien.setBackground(new java.awt.Color(57, 62, 70));
        pasien.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Tabel_Pelanggan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Pelanggan", "Nama", "Jenis Kelamin", "No Wa", "Alamat"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        Tabel_Pelanggan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabel_PelangganMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(Tabel_Pelanggan);

        pasien.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 950, 120));

        delete_table.setText("HAPUS");
        delete_table.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        delete_table.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        delete_table.setkBorderRadius(70);
        delete_table.setkEndColor(new java.awt.Color(0, 255, 255));
        delete_table.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        delete_table.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        delete_table.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        delete_table.setkStartColor(new java.awt.Color(255, 0, 0));
        delete_table.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_tableActionPerformed(evt);
            }
        });
        pasien.add(delete_table, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 320, 150, 40));

        reset_tabel1.setText("Refresh");
        reset_tabel1.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        reset_tabel1.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        reset_tabel1.setkBorderRadius(70);
        reset_tabel1.setkEndColor(new java.awt.Color(0, 255, 255));
        reset_tabel1.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        reset_tabel1.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        reset_tabel1.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        reset_tabel1.setkStartColor(new java.awt.Color(51, 51, 255));
        reset_tabel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reset_tabel1ActionPerformed(evt);
            }
        });
        pasien.add(reset_tabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 110, 90, 40));

        edit_table.setText("UBAH");
        edit_table.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        edit_table.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        edit_table.setkBorderRadius(70);
        edit_table.setkEndColor(new java.awt.Color(0, 255, 255));
        edit_table.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        edit_table.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        edit_table.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        edit_table.setkStartColor(new java.awt.Color(255, 204, 51));
        edit_table.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_tableActionPerformed(evt);
            }
        });
        pasien.add(edit_table, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 380, 150, 40));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel30.setFont(new java.awt.Font("Source Code Pro Black", 2, 24)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(57, 62, 70));
        jLabel30.setText("Search by Name :");
        jPanel4.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 0, 230, 40));

        pasien.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 390, 40));

        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });
        txtsearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtsearchKeyReleased(evt);
            }
        });
        pasien.add(txtsearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 110, 280, 40));

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel35.setFont(new java.awt.Font("Source Code Pro Black", 2, 24)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(57, 62, 70));
        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-racism-30.png"))); // NOI18N
        jLabel35.setText("Daftar Member");
        jPanel6.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, -10, 260, 60));

        pasien.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 20, 690, 40));

        txtNamaPelanggan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaPelanggan2ActionPerformed(evt);
            }
        });
        pasien.add(txtNamaPelanggan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 470, 330, 30));

        txtAlamatPelanggan2.setColumns(20);
        txtAlamatPelanggan2.setRows(5);
        jScrollPane11.setViewportView(txtAlamatPelanggan2);

        pasien.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 570, 290, 30));

        txtNoWA2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoWA2ActionPerformed(evt);
            }
        });
        pasien.add(txtNoWA2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 470, 270, 30));

        comboBoxJenisKelamin2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laki-Laki", "Perempuan" }));
        pasien.add(comboBoxJenisKelamin2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 360, 190, 30));

        jLabel8.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("ID Pelanggan");
        pasien.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 180, 20));

        jLabel15.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Alamat");
        pasien.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, 180, 20));

        jLabel33.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("No Whatsapp");
        pasien.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 450, 80, 20));

        jLabel37.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Jenis Kelamin");
        pasien.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 340, 90, 20));

        Id_Pelanggan2.setEnabled(false);
        Id_Pelanggan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Id_Pelanggan2ActionPerformed(evt);
            }
        });
        pasien.add(Id_Pelanggan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 280, 30));

        jLabel39.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Nama");
        pasien.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 110, 20));

        SIMPAN_BTN10.setText("DAFTAR ");
        SIMPAN_BTN10.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        SIMPAN_BTN10.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        SIMPAN_BTN10.setkBorderRadius(70);
        SIMPAN_BTN10.setkEndColor(new java.awt.Color(0, 255, 255));
        SIMPAN_BTN10.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        SIMPAN_BTN10.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        SIMPAN_BTN10.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        SIMPAN_BTN10.setkStartColor(new java.awt.Color(0, 204, 51));
        SIMPAN_BTN10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SIMPAN_BTN10ActionPerformed(evt);
            }
        });
        pasien.add(SIMPAN_BTN10, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 560, 180, 40));

        TabPane.addTab("pasien", pasien);

        pelayanan.setBackground(new java.awt.Color(57, 62, 70));
        pelayanan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pelayanan.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 40, 10, 590));

        jScrollPane7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane7MouseClicked(evt);
            }
        });

        Tabel_Kategori.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID Kategori", "Nama Kategori"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        Tabel_Kategori.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabel_KategoriMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(Tabel_Kategori);

        pelayanan.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 390, 230));

        reset_tabel3.setText("Ubah");
        reset_tabel3.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        reset_tabel3.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        reset_tabel3.setkBorderRadius(70);
        reset_tabel3.setkEndColor(new java.awt.Color(0, 255, 255));
        reset_tabel3.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        reset_tabel3.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        reset_tabel3.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        reset_tabel3.setkStartColor(new java.awt.Color(51, 51, 255));
        reset_tabel3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reset_tabel3ActionPerformed(evt);
            }
        });
        pelayanan.add(reset_tabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 500, 100, 50));

        txtNamaKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaKategoriActionPerformed(evt);
            }
        });
        pelayanan.add(txtNamaKategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 440, 380, 30));

        jLabel46.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("ID Kategori");
        pelayanan.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 130, -1));

        delete_table3.setText("HAPUS");
        delete_table3.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        delete_table3.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        delete_table3.setkBorderRadius(70);
        delete_table3.setkEndColor(new java.awt.Color(0, 255, 255));
        delete_table3.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        delete_table3.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        delete_table3.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        delete_table3.setkStartColor(new java.awt.Color(255, 0, 0));
        delete_table3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_table3ActionPerformed(evt);
            }
        });
        pelayanan.add(delete_table3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 500, 100, 50));

        SIMPAN_BTN3.setText("SIMPAN");
        SIMPAN_BTN3.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        SIMPAN_BTN3.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        SIMPAN_BTN3.setkBorderRadius(70);
        SIMPAN_BTN3.setkEndColor(new java.awt.Color(0, 255, 255));
        SIMPAN_BTN3.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        SIMPAN_BTN3.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        SIMPAN_BTN3.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        SIMPAN_BTN3.setkStartColor(new java.awt.Color(0, 204, 51));
        SIMPAN_BTN3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SIMPAN_BTN3ActionPerformed(evt);
            }
        });
        pelayanan.add(SIMPAN_BTN3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 570, 180, 40));

        jScrollPane8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane8MouseClicked(evt);
            }
        });

        Tabel_Satuan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID Satuan", "Nama Satuan"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        Tabel_Satuan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabel_SatuanMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(Tabel_Satuan);

        pelayanan.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 100, 390, 230));

        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel40.setFont(new java.awt.Font("Source Code Pro Black", 2, 24)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(57, 62, 70));
        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_online_support_30px.png"))); // NOI18N
        jLabel40.setText("Form Satuan");
        jPanel10.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 0, 280, 40));

        jLabel41.setFont(new java.awt.Font("Source Code Pro Black", 2, 24)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(57, 62, 70));
        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_online_support_30px.png"))); // NOI18N
        jLabel41.setText("Form Kategori");
        jPanel10.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 280, 40));

        pelayanan.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 970, 40));

        ID_Kategori.setEnabled(false);
        ID_Kategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ID_KategoriActionPerformed(evt);
            }
        });
        pelayanan.add(ID_Kategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, 210, 30));

        jLabel48.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("Nama Kategori");
        pelayanan.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 420, 130, -1));

        reset_tabel5.setText("Ubah");
        reset_tabel5.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        reset_tabel5.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        reset_tabel5.setkBorderRadius(70);
        reset_tabel5.setkEndColor(new java.awt.Color(0, 255, 255));
        reset_tabel5.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        reset_tabel5.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        reset_tabel5.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        reset_tabel5.setkStartColor(new java.awt.Color(51, 51, 255));
        reset_tabel5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reset_tabel5ActionPerformed(evt);
            }
        });
        pelayanan.add(reset_tabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 500, 100, 50));

        txtNamaSatuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaSatuanActionPerformed(evt);
            }
        });
        pelayanan.add(txtNamaSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 440, 380, 30));

        jLabel59.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText("ID Satuan");
        pelayanan.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 360, 130, -1));

        delete_table5.setText("HAPUS");
        delete_table5.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        delete_table5.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        delete_table5.setkBorderRadius(70);
        delete_table5.setkEndColor(new java.awt.Color(0, 255, 255));
        delete_table5.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        delete_table5.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        delete_table5.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        delete_table5.setkStartColor(new java.awt.Color(255, 0, 0));
        delete_table5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_table5ActionPerformed(evt);
            }
        });
        pelayanan.add(delete_table5, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 500, 100, 50));

        SIMPAN_BTN9.setText("SIMPAN");
        SIMPAN_BTN9.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        SIMPAN_BTN9.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        SIMPAN_BTN9.setkBorderRadius(70);
        SIMPAN_BTN9.setkEndColor(new java.awt.Color(0, 255, 255));
        SIMPAN_BTN9.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        SIMPAN_BTN9.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        SIMPAN_BTN9.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        SIMPAN_BTN9.setkStartColor(new java.awt.Color(0, 204, 51));
        SIMPAN_BTN9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SIMPAN_BTN9ActionPerformed(evt);
            }
        });
        pelayanan.add(SIMPAN_BTN9, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 570, 180, 40));

        ID_Satuan.setEnabled(false);
        ID_Satuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ID_SatuanActionPerformed(evt);
            }
        });
        pelayanan.add(ID_Satuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 380, 210, 30));

        jLabel62.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(255, 255, 255));
        jLabel62.setText("Nama Satuan");
        pelayanan.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 420, 130, -1));

        TabPane.addTab("pelayanan", pelayanan);

        edit.setBackground(new java.awt.Color(57, 62, 70));
        edit.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        edit_BTN.setText("Edit data");
        edit_BTN.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        edit_BTN.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        edit_BTN.setkBorderRadius(70);
        edit_BTN.setkEndColor(new java.awt.Color(0, 255, 255));
        edit_BTN.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        edit_BTN.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        edit_BTN.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        edit_BTN.setkStartColor(new java.awt.Color(255, 153, 153));
        edit_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_BTNActionPerformed(evt);
            }
        });
        edit.add(edit_BTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 510, 380, 40));

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel34.setFont(new java.awt.Font("Source Code Pro Black", 2, 24)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(57, 62, 70));
        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_sign_up_30px.png"))); // NOI18N
        jLabel34.setText("Form Ubah Data Pelanggan");
        jPanel5.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, -10, 410, 60));

        edit.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 10, 450, 40));

        edit_BTN1.setText("BATAL");
        edit_BTN1.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        edit_BTN1.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        edit_BTN1.setkBorderRadius(70);
        edit_BTN1.setkEndColor(new java.awt.Color(0, 255, 255));
        edit_BTN1.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        edit_BTN1.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        edit_BTN1.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        edit_BTN1.setkStartColor(new java.awt.Color(255, 204, 51));
        edit_BTN1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_BTN1ActionPerformed(evt);
            }
        });
        edit.add(edit_BTN1, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 570, 100, 40));

        txtNamaPelanggan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaPelanggan1ActionPerformed(evt);
            }
        });
        edit.add(txtNamaPelanggan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 320, 30));

        txtAlamatPelanggan1.setColumns(20);
        txtAlamatPelanggan1.setRows(5);
        jScrollPane5.setViewportView(txtAlamatPelanggan1);

        edit.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, 810, -1));

        txtNoWA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoWA1ActionPerformed(evt);
            }
        });
        edit.add(txtNoWA1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 240, 260, 30));

        comboBoxJenisKelamin1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laki-Laki", "Perempuan" }));
        edit.add(comboBoxJenisKelamin1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 130, 180, 30));

        jLabel5.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("ID Pelanggan");
        edit.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 170, -1));

        jLabel14.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Alamat");
        edit.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 320, 170, -1));

        jLabel19.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("No Whatsapp");
        edit.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 220, -1, -1));

        jLabel25.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Jenis Kelamin");
        edit.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 110, -1, -1));

        Id_Pelanggan1.setEnabled(false);
        Id_Pelanggan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Id_Pelanggan1ActionPerformed(evt);
            }
        });
        edit.add(Id_Pelanggan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, 270, 30));

        jLabel27.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Nama");
        edit.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 100, -1));

        TabPane.addTab("edit", edit);

        pembayaran1.setBackground(new java.awt.Color(57, 62, 70));
        pembayaran1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel49.setFont(new java.awt.Font("Source Code Pro Black", 2, 18)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(57, 62, 70));
        jLabel49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_Plus_30px.png"))); // NOI18N
        jLabel49.setText("Form dan Tabel Produk");
        jPanel11.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, -10, 300, 60));

        pembayaran1.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 20, 690, 40));

        jScrollPane16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane16MouseClicked(evt);
            }
        });

        Tabel_Produk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Produk", "Nama Produk", "Kategori", "Satuan", "Harga", "Stok"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        Tabel_Produk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabel_ProdukMouseClicked(evt);
            }
        });
        jScrollPane16.setViewportView(Tabel_Produk);

        pembayaran1.add(jScrollPane16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 880, 180));

        reset_tabel4.setText("Ubah");
        reset_tabel4.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        reset_tabel4.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        reset_tabel4.setkBorderRadius(70);
        reset_tabel4.setkEndColor(new java.awt.Color(0, 255, 255));
        reset_tabel4.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        reset_tabel4.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        reset_tabel4.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        reset_tabel4.setkStartColor(new java.awt.Color(51, 51, 255));
        reset_tabel4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reset_tabel4ActionPerformed(evt);
            }
        });
        pembayaran1.add(reset_tabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 370, 100, 50));

        txtNamaProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaProdukActionPerformed(evt);
            }
        });
        pembayaran1.add(txtNamaProduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 380, 200, 30));

        jLabel77.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(255, 255, 255));
        jLabel77.setText("Nama Produk");
        pembayaran1.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 360, 80, -1));

        jLabel119.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel119.setForeground(new java.awt.Color(255, 255, 255));
        jLabel119.setText("Stok");
        pembayaran1.add(jLabel119, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 440, 100, 30));

        jLabel120.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel120.setForeground(new java.awt.Color(255, 255, 255));
        jLabel120.setText("Harga");
        pembayaran1.add(jLabel120, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, 80, -1));

        txtHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHargaActionPerformed(evt);
            }
        });
        pembayaran1.add(txtHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 470, 200, 30));

        txtStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStokActionPerformed(evt);
            }
        });
        pembayaran1.add(txtStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 470, 190, 30));

        delete_table4.setText("Hapus");
        delete_table4.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        delete_table4.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        delete_table4.setkBorderRadius(70);
        delete_table4.setkEndColor(new java.awt.Color(0, 255, 255));
        delete_table4.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        delete_table4.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        delete_table4.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        delete_table4.setkStartColor(new java.awt.Color(255, 0, 0));
        delete_table4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_table4ActionPerformed(evt);
            }
        });
        pembayaran1.add(delete_table4, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 370, 100, 50));

        ID_Produk.setEnabled(false);
        ID_Produk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ID_ProdukActionPerformed(evt);
            }
        });
        pembayaran1.add(ID_Produk, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 200, 30));

        jLabel121.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel121.setForeground(new java.awt.Color(255, 255, 255));
        jLabel121.setText("ID Produk");
        pembayaran1.add(jLabel121, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 80, -1));

        SIMPAN_BTN8.setText("SIMPAN");
        SIMPAN_BTN8.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        SIMPAN_BTN8.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        SIMPAN_BTN8.setkBorderRadius(70);
        SIMPAN_BTN8.setkEndColor(new java.awt.Color(0, 255, 255));
        SIMPAN_BTN8.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        SIMPAN_BTN8.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        SIMPAN_BTN8.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        SIMPAN_BTN8.setkStartColor(new java.awt.Color(0, 204, 51));
        SIMPAN_BTN8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SIMPAN_BTN8ActionPerformed(evt);
            }
        });
        pembayaran1.add(SIMPAN_BTN8, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 490, 180, 40));

        comboBoxKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxKategoriActionPerformed(evt);
            }
        });
        comboBoxKategori.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                comboBoxKategoriPropertyChange(evt);
            }
        });
        pembayaran1.add(comboBoxKategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 560, 180, 30));

        jLabel21.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Kategori");
        pembayaran1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 540, -1, -1));

        pembayaran1.add(comboBoxSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 560, 180, 30));

        jLabel22.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Satuan");
        pembayaran1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 540, -1, -1));

        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel51.setFont(new java.awt.Font("Source Code Pro Black", 2, 24)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(57, 62, 70));
        jLabel51.setText("Search by Name :");
        jPanel21.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 0, 230, 40));

        pembayaran1.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 390, 40));

        txtSearchProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchProdukActionPerformed(evt);
            }
        });
        txtSearchProduk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchProdukKeyReleased(evt);
            }
        });
        pembayaran1.add(txtSearchProduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 80, 280, 40));

        TabPane.addTab("penanganan", pembayaran1);

        pembayaran.setBackground(new java.awt.Color(57, 62, 70));
        pembayaran.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel56.setFont(new java.awt.Font("Source Code Pro Black", 2, 14)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(57, 62, 70));
        jLabel56.setText("isikan, uang pembayaran");
        jPanel14.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 280, 20));

        pembayaran.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 340, 370, 20));

        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel63.setFont(new java.awt.Font("Source Code Pro Black", 2, 24)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(57, 62, 70));
        jLabel63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_cash_in_hand_30px.png"))); // NOI18N
        jLabel63.setText("Transaksi");
        jPanel15.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, -10, -1, 60));

        pembayaran.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, 360, 40));

        totalpembayaran.setEditable(false);
        totalpembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalpembayaranActionPerformed(evt);
            }
        });
        pembayaran.add(totalpembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 270, 240, 30));

        jLabel67.setFont(new java.awt.Font("Arial Unicode MS", 1, 18)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(255, 255, 255));
        jLabel67.setText("Grand Total ");
        pembayaran.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 240, 120, -1));

        jLabel74.setFont(new java.awt.Font("Arial Unicode MS", 1, 24)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(255, 255, 255));
        jLabel74.setText("Rp.");
        pembayaran.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 270, 100, 30));

        jLabel79.setFont(new java.awt.Font("Arial Unicode MS", 1, 24)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(255, 255, 255));
        jLabel79.setText("Rp.");
        pembayaran.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 410, 50, 30));

        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cb_boking.setText("Bukan Booking");
        cb_boking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_bokingActionPerformed(evt);
            }
        });
        jPanel18.add(cb_boking, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 30));

        pembayaran.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 100, 110, 30));

        totalpembayaran2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalpembayaran2ActionPerformed(evt);
            }
        });
        totalpembayaran2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                totalpembayaran2KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                totalpembayaran2KeyTyped(evt);
            }
        });
        pembayaran.add(totalpembayaran2, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 410, 230, 30));

        jLabel80.setFont(new java.awt.Font("Arial Unicode MS", 1, 18)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(255, 255, 255));
        jLabel80.setText("Bayar");
        pembayaran.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 380, 110, -1));

        jLabel84.setFont(new java.awt.Font("Arial Unicode MS", 1, 24)); // NOI18N
        jLabel84.setForeground(new java.awt.Color(255, 255, 255));
        jLabel84.setText("Rp.");
        pembayaran.add(jLabel84, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 490, 50, 30));

        totalpembayaran3.setEditable(false);
        totalpembayaran3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalpembayaran3ActionPerformed(evt);
            }
        });
        totalpembayaran3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                totalpembayaran3KeyReleased(evt);
            }
        });
        pembayaran.add(totalpembayaran3, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 490, 240, 30));

        jLabel85.setFont(new java.awt.Font("Arial Unicode MS", 1, 18)); // NOI18N
        jLabel85.setForeground(new java.awt.Color(255, 255, 255));
        jLabel85.setText("kembali");
        pembayaran.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 460, 120, -1));

        reset_tabel7.setText("Kwitansi Pembayaran Tunai");
        reset_tabel7.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        reset_tabel7.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        reset_tabel7.setkBorderRadius(70);
        reset_tabel7.setkEndColor(new java.awt.Color(0, 255, 255));
        reset_tabel7.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        reset_tabel7.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        reset_tabel7.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        reset_tabel7.setkStartColor(new java.awt.Color(51, 255, 51));
        reset_tabel7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reset_tabel7ActionPerformed(evt);
            }
        });
        pembayaran.add(reset_tabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 560, 230, 40));

        jLabel28.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Nama");
        pembayaran.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 100, -1));

        txtNamaPelangganBayar.setEnabled(false);
        txtNamaPelangganBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaPelangganBayarActionPerformed(evt);
            }
        });
        pembayaran.add(txtNamaPelangganBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 320, 30));

        jLabel23.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("No Whatsapp");
        pembayaran.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, -1, -1));

        txtNoWABayar.setEnabled(false);
        txtNoWABayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoWABayarActionPerformed(evt);
            }
        });
        pembayaran.add(txtNoWABayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 100, 260, 30));

        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel75.setFont(new java.awt.Font("Source Code Pro Black", 2, 14)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(57, 62, 70));
        jLabel75.setText("LIST PESANAN :");
        jPanel19.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 190, 20));

        pembayaran.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 170, 20));

        txtarea_listpesanan.setColumns(20);
        txtarea_listpesanan.setRows(5);
        jScrollPane10.setViewportView(txtarea_listpesanan);

        pembayaran.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 500, 270));

        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel76.setFont(new java.awt.Font("Source Code Pro Black", 2, 14)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(57, 62, 70));
        jLabel76.setText("Pilih Menu, Jika sudah booking maka harus lewat page reservasi");
        jPanel20.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 500, 20));

        pembayaran.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 530, 20));

        comboBoxProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxProdukActionPerformed(evt);
            }
        });
        comboBoxProduk.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                comboBoxProdukPropertyChange(evt);
            }
        });
        pembayaran.add(comboBoxProduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 180, 30));

        jLabel29.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Produk Menu");
        pembayaran.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, -1, -1));

        txtSatuan.setEnabled(false);
        txtSatuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSatuanActionPerformed(evt);
            }
        });
        pembayaran.add(txtSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 200, 70, 30));

        jLabel31.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("banyak :");
        pembayaran.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, 90, -1));

        txtBanyak.setFocusCycleRoot(true);
        txtBanyak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBanyakActionPerformed(evt);
            }
        });
        pembayaran.add(txtBanyak, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 200, 80, 30));

        input_menu.setText("Input Menu");
        input_menu.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        input_menu.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        input_menu.setkBorderRadius(70);
        input_menu.setkEndColor(new java.awt.Color(0, 255, 255));
        input_menu.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        input_menu.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        input_menu.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        input_menu.setkStartColor(new java.awt.Color(51, 51, 255));
        input_menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_menuActionPerformed(evt);
            }
        });
        pembayaran.add(input_menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 190, 250, 50));

        delete_table6.setText("ATUR ULANG LIST PESANAN");
        delete_table6.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        delete_table6.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        delete_table6.setkBorderRadius(70);
        delete_table6.setkEndColor(new java.awt.Color(0, 255, 255));
        delete_table6.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        delete_table6.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        delete_table6.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        delete_table6.setkStartColor(new java.awt.Color(255, 0, 0));
        delete_table6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_table6ActionPerformed(evt);
            }
        });
        pembayaran.add(delete_table6, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 290, 250, 30));

        TabPane.addTab("pembayaran", pembayaran);

        pasien1.setBackground(new java.awt.Color(57, 62, 70));
        pasien1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Tabel_Booking.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Booking", "Nama", "Tanggal", "Waktu", "Orang", "No Wa", "Email", "member"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        Tabel_Booking.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabel_BookingMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(Tabel_Booking);

        pasien1.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 950, 240));

        edit_table1.setForeground(new java.awt.Color(0, 0, 0));
        edit_table1.setText("Menuju Tab Transaksi");
        edit_table1.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        edit_table1.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        edit_table1.setkBorderRadius(70);
        edit_table1.setkEndColor(new java.awt.Color(0, 255, 255));
        edit_table1.setkForeGround(new java.awt.Color(0, 0, 0));
        edit_table1.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        edit_table1.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        edit_table1.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        edit_table1.setkStartColor(new java.awt.Color(255, 204, 51));
        edit_table1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_table1ActionPerformed(evt);
            }
        });
        pasien1.add(edit_table1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 510, 240, 40));

        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel36.setFont(new java.awt.Font("Source Code Pro Black", 2, 24)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(57, 62, 70));
        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-shopping-cart-30.png"))); // NOI18N
        jLabel36.setText("  Booking List");
        jPanel12.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, -10, 260, 60));

        pasien1.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 20, 690, 40));

        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel32.setFont(new java.awt.Font("Source Code Pro Black", 2, 24)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(57, 62, 70));
        jLabel32.setText("Search Booking by Name :");
        jPanel13.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 350, 40));

        pasien1.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 350, 40));

        txtsearch2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtsearch2KeyReleased(evt);
            }
        });
        pasien1.add(txtsearch2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 130, 280, 40));

        delete_table1.setText("BATALKAN RESERVASI");
        delete_table1.setFont(new java.awt.Font("Microsoft YaHei", 1, 14)); // NOI18N
        delete_table1.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        delete_table1.setkBorderRadius(70);
        delete_table1.setkEndColor(new java.awt.Color(0, 255, 255));
        delete_table1.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        delete_table1.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        delete_table1.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        delete_table1.setkStartColor(new java.awt.Color(255, 0, 0));
        delete_table1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_table1ActionPerformed(evt);
            }
        });
        pasien1.add(delete_table1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 450, 220, 40));

        TabPane.addTab("pasien", pasien1);

        getContentPane().add(TabPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 0, 970, 660));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        int result = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin Keluar?", "Logout", JOptionPane.YES_NO_OPTION);
if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
    // tindakan jika pengguna memilih "Yes"
} else {
    // tindakan jika pengguna memilih "No"
}
        // TODO add your handling code here:
    }//GEN-LAST:event_ExitActionPerformed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        int r = evt.getXOnScreen();
        int s = evt.getYOnScreen();
        this.setLocation(r -  rMouse, s - sMouse);
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        rMouse = evt.getX();
        sMouse = evt.getY();
        // TODO add your handling code here:
    }//GEN-LAST:event_formMousePressed

    private void kButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton5ActionPerformed

//        DisplayDataCount.displayCount(countpasien);
        TabPane.setSelectedIndex(0);
        tampilkanTabelBookingsHome();
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton5ActionPerformed

    private void kButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton2ActionPerformed
int result = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin Kembali ke Login?", "Logout", JOptionPane.YES_NO_OPTION);
if (result == JOptionPane.YES_OPTION) {
            this.dispose();
        new login().setVisible(true);
    // tindakan jika pengguna memilih "Yes"
} else {
    // tindakan jika pengguna memilih "No"
}       
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton2ActionPerformed

    private void kButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton9ActionPerformed
        TabPane.setSelectedIndex(2);
        tampilkanTabelSupplier();
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton9ActionPerformed

    private void kButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton10ActionPerformed
        TabPane.setSelectedIndex(3);
        tampilkanTabelPelanggan();
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton10ActionPerformed

    private void kButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton11ActionPerformed
        TabPane.setSelectedIndex(4);
      tampilkanTabelKategori();
        tampilkanTabelSatuan();
        clearTextFieldKategori();
        clearTextFieldSatuan();
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton11ActionPerformed

    private void MinimizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MinimizeActionPerformed
this.setExtendedState(Frame.ICONIFIED);
        // TODO add your handling code here:
    }//GEN-LAST:event_MinimizeActionPerformed

    private void txtNamaPelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaPelangganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaPelangganActionPerformed

    private void txtNoWAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoWAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoWAActionPerformed

    private void Id_PelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Id_PelangganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Id_PelangganActionPerformed

    private void kButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton13ActionPerformed
                    TabPane.setSelectedIndex(7);
                  loadProducts();
                // TODO add your handling code here:
    }//GEN-LAST:event_kButton13ActionPerformed


    private void textFieldAnimationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldAnimationActionPerformed
                
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldAnimationActionPerformed

    private void delete_tableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_tableActionPerformed
deleteSelectedRow();
    }//GEN-LAST:event_delete_tableActionPerformed

    private void reset_tabel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reset_tabel1ActionPerformed
    tampilkanTabelPelanggan();
            // TODO add your hasndling code here:
    }//GEN-LAST:event_reset_tabel1ActionPerformed

    private void edit_tableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_tableActionPerformed
              updateData();
                            tampilkanTabelPelanggan();
        // TODO add your handling code here:
    }//GEN-LAST:event_edit_tableActionPerformed

    private void Tabel_PelangganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabel_PelangganMouseClicked
editSelectedRowPelanggan();
            
        // TODO add your handling code here:
    }//GEN-LAST:event_Tabel_PelangganMouseClicked

    private void SIMPAN_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SIMPAN_BTNActionPerformed
        insertDataPelanggan();
        tampilkanTabelPelanggan();
        // TODO add your handling code here:
    }//GEN-LAST:event_SIMPAN_BTNActionPerformed

    private void edit_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_BTNActionPerformed
                    updateData();
                            tampilkanTabelPelanggan();
        // TODO add your handling code here:
    }//GEN-LAST:event_edit_BTNActionPerformed

    private void textFieldAnimationKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldAnimationKeyReleased

    }//GEN-LAST:event_textFieldAnimationKeyReleased

    private void txtsearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsearchKeyReleased
try {
    String searchValue = txtsearch.getText();
    String query = "SELECT * FROM pelanggan WHERE nama LIKE ?";
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/hodltree", "root", "");
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, "%" + searchValue + "%");  // Menambahkan wildcard untuk pencarian seperti
    ResultSet rs = ps.executeQuery();

    DefaultTableModel model = (DefaultTableModel) Tabel_Pelanggan.getModel();
    model.setRowCount(0);

    while(rs.next()){
        Object[] row = new Object[5]; // Ada 5 kolom sesuai dengan insert, sesuaikan dengan struktur tabel pelanggan
        row[0] = rs.getString("id_pelanggan");  // Sesuaikan dengan nama kolom yang benar
        row[1] = rs.getString("nama");
        row[2] = rs.getString("jenis_kelamin");
        row[3] = rs.getString("no_wa");
        row[4] = rs.getString("alamat");

        model.addRow(row);
    }
} catch (SQLException ex) {
    System.out.println("Error: " + ex.getMessage());
}

    }//GEN-LAST:event_txtsearchKeyReleased

    private void kButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton14ActionPerformed
TabPane.setSelectedIndex(8);
tampilkanTabelBookings();
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton14ActionPerformed

    private void Tabel_SupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabel_SupplierMouseClicked
        editSelectedRowSupplier();
        // TODO add your handling code here:
    }//GEN-LAST:event_Tabel_SupplierMouseClicked

    private void txtNamaSupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaSupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaSupActionPerformed

    private void txtNoTelpSupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoTelpSupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoTelpSupActionPerformed

    private void txtAlamatSupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAlamatSupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAlamatSupActionPerformed

    private void txtKeteranganSupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKeteranganSupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKeteranganSupActionPerformed

    private void delete_table2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_table2ActionPerformed
        deleteDataSupplier();
        tampilkanTabelSupplier();
        refreshSupplier();
        // TODO add your handling code here:
    }//GEN-LAST:event_delete_table2ActionPerformed

    private void ID_SupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ID_SupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ID_SupplierActionPerformed

    private void reset_tabel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reset_tabel2ActionPerformed
      updateDataSupplier();
      tampilkanTabelSupplier();
      refreshSupplier();
        // TODO add your handling code here:
    }//GEN-LAST:event_reset_tabel2ActionPerformed

    private void SIMPAN_BTN1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SIMPAN_BTN1ActionPerformed
        insertDataSupplier();
        tampilkanTabelSupplier();
        refreshSupplier();
        // TODO add your handling code here:
    }//GEN-LAST:event_SIMPAN_BTN1ActionPerformed

    private void jScrollPane3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane3MouseClicked
        editSelectedRowSupplier();
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane3MouseClicked

    private void SIMPAN_BTN2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SIMPAN_BTN2ActionPerformed
generatePDFLaporan_All_Pelanggan();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_SIMPAN_BTN2ActionPerformed

    private void SIMPAN_BTN4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SIMPAN_BTN4ActionPerformed
generatePDFLaporan_Transaksi();
    }//GEN-LAST:event_SIMPAN_BTN4ActionPerformed

    private void SIMPAN_BTN5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SIMPAN_BTN5ActionPerformed
        generatePDFLaporan_All_Supplier();
        // TODO add your handling code here:
    }//GEN-LAST:event_SIMPAN_BTN5ActionPerformed

    private void edit_BTN1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_BTN1ActionPerformed
        
       
        TabPane.setSelectedIndex(3);
        
    }//GEN-LAST:event_edit_BTN1ActionPerformed

    private void txtNamaPelanggan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaPelanggan1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaPelanggan1ActionPerformed

    private void txtNoWA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoWA1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoWA1ActionPerformed

    private void Id_Pelanggan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Id_Pelanggan1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Id_Pelanggan1ActionPerformed

    private void SIMPAN_BTN6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SIMPAN_BTN6ActionPerformed
generatePDFLaporan_All_Bookings();
    }//GEN-LAST:event_SIMPAN_BTN6ActionPerformed

    private void SIMPAN_BTN7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SIMPAN_BTN7ActionPerformed
generatePDFLaporan_All_Produk();      
    }//GEN-LAST:event_SIMPAN_BTN7ActionPerformed

    private void Tabel_KategoriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabel_KategoriMouseClicked
    editSelectedRowKategori();
    }//GEN-LAST:event_Tabel_KategoriMouseClicked

    private void jScrollPane7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane7MouseClicked
        editSelectedRowKategori();
    }//GEN-LAST:event_jScrollPane7MouseClicked

    private void reset_tabel3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reset_tabel3ActionPerformed
        updateDataKategori();
        clearTextFieldKategori();
    }//GEN-LAST:event_reset_tabel3ActionPerformed

    private void txtNamaKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaKategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaKategoriActionPerformed

    private void delete_table3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_table3ActionPerformed
       deleteDataKategori();
       clearTextFieldKategori();
    }//GEN-LAST:event_delete_table3ActionPerformed

    private void SIMPAN_BTN3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SIMPAN_BTN3ActionPerformed
        insertDataKategori();
        tampilkanTabelKategori();
        clearTextFieldKategori();
    }//GEN-LAST:event_SIMPAN_BTN3ActionPerformed
// Fungsi untuk mengisi ComboBox dengan data satuan
private void loadSatuan() {
    // Koneksi ke database MySQL
    String url = "jdbc:mysql://localhost:3306/hodltree"; // ganti dengan URL database
    String username = "root"; // ganti dengan username database
    String password = ""; // ganti dengan password database
    
    try (Connection conn = DriverManager.getConnection(url, username, password)) {
        // Mengosongkan ComboBox sebelum di-load data baru
        comboBoxSatuan.removeAllItems();
        
        // Query untuk mengambil nama satuan
        String query = "SELECT nama_satuan FROM satuan";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        // List untuk menyimpan satuan
        ArrayList<String> units = new ArrayList<>();
        
        while (rs.next()) {
            // Menambahkan nama satuan ke dalam list
            units.add(rs.getString("nama_satuan"));
        }
        
        // Menambahkan satuan ke dalam ComboBox
        for (String unit : units) {
            comboBoxSatuan.addItem(unit);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
// Fungsi untuk mengisi ComboBox dengan data kategori
private void loadCategories() {
    // Koneksi ke database MySQL
    String url = "jdbc:mysql://localhost:3306/hodltree"; // ganti dengan URL database
    String username = "root"; // ganti dengan username database
    String password = ""; // ganti dengan password database
    
    try (Connection conn = DriverManager.getConnection(url, username, password)) {
        // Mengosongkan ComboBox sebelum di-load data baru
        comboBoxKategori.removeAllItems();
        
        // Query untuk mengambil nama kategori
        String query = "SELECT nama_kategori FROM kategori";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        // List untuk menyimpan kategori
        ArrayList<String> categories = new ArrayList<>();
        
        while (rs.next()) {
            // Menambahkan nama kategori ke dalam list
            categories.add(rs.getString("nama_kategori"));
        }
        
        // Menambahkan kategori ke dalam ComboBox
        for (String category : categories) {
            comboBoxKategori.addItem(category);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void loadProducts() {
    // Koneksi ke database MySQL
    String url = "jdbc:mysql://localhost:3306/hodltree"; // Ganti dengan URL database
    String username = "root"; // Ganti dengan username database
    String password = ""; // Ganti dengan password database
    
    try (Connection conn = DriverManager.getConnection(url, username, password)) {
        // Mengosongkan ComboBox sebelum di-load data baru
        comboBoxProduk.removeAllItems();
        
        // Query untuk mengambil nama produk
        String query = "SELECT nama_produk FROM produk"; // Pastikan tabel dan kolom sudah sesuai
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        // List untuk menyimpan nama produk
        ArrayList<String> products = new ArrayList<>();
        
        while (rs.next()) {
            // Menambahkan nama produk ke dalam list
            products.add(rs.getString("nama_produk"));
        }
        
        // Menambahkan produk ke dalam ComboBox
        for (String product : products) {
            comboBoxProduk.addItem(product);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



    private void kButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton12ActionPerformed
  TabPane.setSelectedIndex(6);
  
clearTextFieldProduk();
  tampilkanTabelProduk();
    }//GEN-LAST:event_kButton12ActionPerformed

    private void Tabel_SatuanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabel_SatuanMouseClicked
       editSelectedRowSatuan();
    }//GEN-LAST:event_Tabel_SatuanMouseClicked

    private void jScrollPane8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane8MouseClicked
        editSelectedRowSatuan();
    }//GEN-LAST:event_jScrollPane8MouseClicked

    private void ID_KategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ID_KategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ID_KategoriActionPerformed

    private void reset_tabel5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reset_tabel5ActionPerformed
        updateDataSatuan();
        clearTextFieldSatuan();
    }//GEN-LAST:event_reset_tabel5ActionPerformed

    private void txtNamaSatuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaSatuanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaSatuanActionPerformed

    private void delete_table5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_table5ActionPerformed
          deleteDataSatuan();
       clearTextFieldSatuan();
    }//GEN-LAST:event_delete_table5ActionPerformed

    private void SIMPAN_BTN9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SIMPAN_BTN9ActionPerformed
           insertDataSatuan();
        tampilkanTabelSatuan();
        clearTextFieldSatuan();
    }//GEN-LAST:event_SIMPAN_BTN9ActionPerformed

    private void ID_SatuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ID_SatuanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ID_SatuanActionPerformed

    private void Tabel_ProdukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabel_ProdukMouseClicked
       editSelectedRowProduk();
    }//GEN-LAST:event_Tabel_ProdukMouseClicked

    private void jScrollPane16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane16MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane16MouseClicked

    private void reset_tabel4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reset_tabel4ActionPerformed
       updateDataProduk();
      tampilkanTabelProduk();
       clearTextFieldProduk() ;
    }//GEN-LAST:event_reset_tabel4ActionPerformed

    private void txtNamaProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaProdukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaProdukActionPerformed

    private void txtHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHargaActionPerformed

    private void txtStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStokActionPerformed

    private void delete_table4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_table4ActionPerformed
          deleteDataProduk();
        tampilkanTabelProduk();
         clearTextFieldProduk();
    }//GEN-LAST:event_delete_table4ActionPerformed

    private void ID_ProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ID_ProdukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ID_ProdukActionPerformed

    private void SIMPAN_BTN8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SIMPAN_BTN8ActionPerformed
     insertDataProduk();
        tampilkanTabelProduk();
         clearTextFieldProduk();
        // TODO add your handling code here:        
    }//GEN-LAST:event_SIMPAN_BTN8ActionPerformed

    private void comboBoxKategoriPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_comboBoxKategoriPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxKategoriPropertyChange

    private void comboBoxKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxKategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxKategoriActionPerformed

    private void Tabel_BookingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabel_BookingMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Tabel_BookingMouseClicked

    private void edit_table1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_table1ActionPerformed
BookingToTransaksi();
 loadProducts();
    }//GEN-LAST:event_edit_table1ActionPerformed

    private void txtsearch2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsearch2KeyReleased
       try {
    String searchValue = txtsearch2.getText();  // Mendapatkan nilai pencarian dari text field
    String query = "SELECT * FROM bookings WHERE name LIKE ?";  // Pencarian berdasarkan nama
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/hodltree", "root", "");
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, "%" + searchValue + "%");  // Menambahkan wildcard untuk pencarian yang lebih fleksibel
    ResultSet rs = ps.executeQuery();

    // Mengambil model tabel dan mengatur ulang barisnya
    DefaultTableModel model = (DefaultTableModel) Tabel_Booking.getModel();
    model.setRowCount(0);

    // Menambahkan data dari ResultSet ke tabel
    while (rs.next()) {
        Object[] row = new Object[8];  // Menyesuaikan dengan jumlah kolom pada tabel bookings
        row[0] = rs.getInt("booking_id");  // ID Booking
        row[1] = rs.getString("name");  // Nama
        row[2] = rs.getString("date");  // Tanggal
        row[3] = rs.getString("time");  // Waktu
        row[4] = rs.getInt("people");  // Jumlah orang
        row[5] = rs.getString("whatsapp_number");  // Nomor WhatsApp
        row[6] = rs.getString("email");  // Email
        row[7] = rs.getString("is_member");  // Status Member

        model.addRow(row);  // Menambahkan baris baru ke model tabel
    }
} catch (SQLException ex) {
    System.out.println("Error: " + ex.getMessage());  // Menampilkan pesan kesalahan jika terjadi exception
}

    }//GEN-LAST:event_txtsearch2KeyReleased
private void insertDataTransaksi() {
    try {
        // Membuat koneksi ke database
         Connection conn = koneksi.koneksiDB();

        // Validasi data input
        if (txtNamaPelangganBayar.getText().isEmpty() || txtarea_listpesanan.getText().isEmpty() || totalpembayaran.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon lengkapi semua data pembayaran!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Menghapus koma dari totalpembayaran dan mengonversinya ke double
        String totalHarga = totalpembayaran.getText().replace(",", "").trim(); // Menghapus koma dan spasi
        double totalHargaDouble = Double.parseDouble(totalHarga); // Mengonversi ke double

        // Menyimpan data transaksi ke tabel transaksi
        String query = "INSERT INTO transaksi (nama, tanggal, total_harga) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, txtNamaPelangganBayar.getText());
        ps.setString(2, LocalDate.now().toString()); // Menggunakan tanggal sekarang
        ps.setDouble(3, totalHargaDouble); // Menggunakan nilai yang sudah dikonversi
        ps.executeUpdate();

        // Menutup koneksi
        conn.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan data transaksi: " + ex.getMessage());
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Format total harga tidak valid: " + ex.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
    }
}

    private void reset_tabel7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reset_tabel7ActionPerformed
generateKwitansiPembayaran();
  txtarea_listpesanan.setText("");
     totalpembayaran.setText("");
     totalpembayaran2.setText("");
     totalpembayaran3.setText("");

    }//GEN-LAST:event_reset_tabel7ActionPerformed

    private void totalpembayaran3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalpembayaran3KeyReleased

        // TODO add your handling code here:
    }//GEN-LAST:event_totalpembayaran3KeyReleased

    private void totalpembayaran3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalpembayaran3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalpembayaran3ActionPerformed

    private void totalpembayaran2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalpembayaran2KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_totalpembayaran2KeyTyped

    private void totalpembayaran2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalpembayaran2KeyReleased

        calculateKembalian();
        // TODO add your handling code here:
    }//GEN-LAST:event_totalpembayaran2KeyReleased

    private void totalpembayaran2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalpembayaran2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalpembayaran2ActionPerformed

    private void totalpembayaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalpembayaranActionPerformed


    }//GEN-LAST:event_totalpembayaranActionPerformed

    private void txtNamaPelangganBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaPelangganBayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaPelangganBayarActionPerformed

    private void txtNoWABayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoWABayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoWABayarActionPerformed

    private void cb_bokingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_bokingActionPerformed
        // Cek apakah checkbox dicentang
    if (cb_boking.isSelected()) {
        // Jika dicentang, enable kedua TextField
        txtNamaPelangganBayar.setEnabled(true);
        txtNoWABayar.setEnabled(true);
    } else {
        // Jika tidak dicentang, disable kedua TextField
        txtNamaPelangganBayar.setEnabled(false);
        txtNoWABayar.setEnabled(false);
    }
    }//GEN-LAST:event_cb_bokingActionPerformed
private void setSatuanText(String productName) {
    String url = "jdbc:mysql://localhost:3306/hodltree";
    String username = "root";
    String password = "";
    
    try (Connection conn = DriverManager.getConnection(url, username, password)) {
        String query = "SELECT satuan FROM produk WHERE nama_produk = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, productName);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            String satuan = rs.getString("satuan");
            txtSatuan.setText(satuan);
        } else {
            txtSatuan.setText("Satuan tidak ditemukan");
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
        txtSatuan.setText("Error mengambil satuan");
    }
}

    private void comboBoxProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxProdukActionPerformed
      String selectedProduct = (String) comboBoxProduk.getSelectedItem();
    
    if (selectedProduct != null && !selectedProduct.isEmpty()) {
        setSatuanText(selectedProduct);
    }
    }//GEN-LAST:event_comboBoxProdukActionPerformed

    private void comboBoxProdukPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_comboBoxProdukPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxProdukPropertyChange

    private void txtSatuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSatuanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSatuanActionPerformed

    private void txtBanyakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBanyakActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBanyakActionPerformed
private double getHargaProduk(String productName) {
    double harga = 0;
    String url = "jdbc:mysql://localhost:3306/hodltree";
    String username = "root";
    String password = "";

    try (Connection conn = DriverManager.getConnection(url, username, password)) {
        String query = "SELECT harga FROM produk WHERE nama_produk = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, productName);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            harga = rs.getDouble("harga");
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return harga;
}
    private void input_menuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_menuActionPerformed
 try {
    String selectedProduct = (String) comboBoxProduk.getSelectedItem();
    String banyakText = txtBanyak.getText().trim();
    String satuan = txtSatuan.getText().trim();

    if (selectedProduct != null && !selectedProduct.isEmpty() && !banyakText.isEmpty() && !satuan.isEmpty()) {
        int banyak = Integer.parseInt(banyakText);

        if (banyak > 0) {
            double harga = getHargaProduk(selectedProduct); // Mendapatkan harga dari tabel produk
            
            // Format string dalam bentuk tabel
            String listItem = String.format("%-20s %-10d %-10s Rp.%-10.2f\n", selectedProduct, banyak, satuan, harga * banyak);
            
            // Tambahkan ke txtarea_listpesanan
            txtarea_listpesanan.append(listItem);
            txtBanyak.setText("");
            txtSatuan.setText("");
            comboBoxProduk.setSelectedIndex(-1);
            calculateSumAndKembalian();
        } else {
            JOptionPane.showMessageDialog(this, "Jumlah produk harus lebih dari 0.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Harap isi semua field dengan benar.", "Peringatan", JOptionPane.WARNING_MESSAGE);
    }
} catch (NumberFormatException ex) {
    JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
}


    }//GEN-LAST:event_input_menuActionPerformed

    private void delete_table6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_table6ActionPerformed
     txtarea_listpesanan.setText("");
     totalpembayaran.setText("");
     totalpembayaran2.setText("");
     totalpembayaran3.setText("");
    }//GEN-LAST:event_delete_table6ActionPerformed

    private void txtNamaPelanggan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaPelanggan2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaPelanggan2ActionPerformed

    private void txtNoWA2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoWA2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoWA2ActionPerformed

    private void Id_Pelanggan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Id_Pelanggan2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Id_Pelanggan2ActionPerformed

    private void txtSearchSupKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchSupKeyReleased
try {
        String searchValue = txtSearchSup.getText(); // txtSearchSup adalah JTextField untuk pencarian
        Connection conn = koneksi.koneksiDB(); // Menggunakan koneksi yang telah dibuat
        String query = "SELECT * FROM supplier WHERE nama LIKE ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, "%" + searchValue + "%"); // Menggunakan wildcard untuk pencarian
        
        ResultSet rs = ps.executeQuery();

        // Mengakses model tabel dari JTable
        DefaultTableModel model = (DefaultTableModel) Tabel_Supplier.getModel();
        model.setRowCount(0); // Mengosongkan tabel sebelum menampilkan data baru

        // Menambahkan data hasil pencarian ke tabel
        while (rs.next()) {
            Object[] row = new Object[5]; // Sesuaikan jumlah kolom dengan tabel supplier
            row[0] = rs.getString("id_supplier"); // Sesuaikan dengan nama kolom di database
            row[1] = rs.getString("nama");
            row[2] = rs.getString("alamat");
            row[3] = rs.getString("no_telepon");
            row[4] = rs.getString("keterangan");
            model.addRow(row);
        }
        
        conn.close(); // Menutup koneksi
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mencari data: " + ex.getMessage());
    }    
    }//GEN-LAST:event_txtSearchSupKeyReleased

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtsearchActionPerformed

    private void SIMPAN_BTN10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SIMPAN_BTN10ActionPerformed
        insertDataPelanggan();
        tampilkanTabelPelanggan();
    }//GEN-LAST:event_SIMPAN_BTN10ActionPerformed

    private void txtSearchProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchProdukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchProdukActionPerformed

    private void txtSearchProdukKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchProdukKeyReleased
 try {
        String searchValue = txtSearchProduk.getText(); // txtSearchProduk adalah JTextField untuk pencarian
        Connection conn = koneksi.koneksiDB(); // Menggunakan koneksi yang telah dibuat
        String query = "SELECT * FROM produk WHERE nama_produk LIKE ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, "%" + searchValue + "%"); // Menggunakan wildcard untuk pencarian

        ResultSet rs = ps.executeQuery();

        // Mengakses model tabel dari JTable
        DefaultTableModel model = (DefaultTableModel) Tabel_Produk.getModel();
        model.setRowCount(0); // Mengosongkan tabel sebelum menampilkan data baru

        // Menambahkan data hasil pencarian ke tabel
        while (rs.next()) {
            Object[] row = new Object[6]; // Sesuaikan jumlah kolom dengan tabel produk
            row[0] = rs.getInt("id_produk"); // Sesuaikan dengan nama kolom di database
            row[1] = rs.getString("kategori");
            row[2] = rs.getString("satuan");
            row[3] = rs.getString("nama_produk");
            row[4] = rs.getDouble("harga");
            row[5] = rs.getInt("stok");
            model.addRow(row);
        }

        conn.close(); // Menutup koneksi
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mencari data: " + ex.getMessage());
    }

    }//GEN-LAST:event_txtSearchProdukKeyReleased
private void deleteDataBooking() {
    int selectedRow = Tabel_Booking.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Mohon pilih baris untuk dihapus.");
        return;
    }

    String idBooking = Tabel_Booking.getValueAt(selectedRow, 0).toString(); // Mengambil ID Booking

    int confirm = JOptionPane.showConfirmDialog(this, 
        "Apakah Anda yakin ingin menghapus data ini?", 
        "Konfirmasi Hapus", 
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            // Membuat koneksi ke database
            Connection conn = koneksi.koneksiDB();

            // Query untuk menghapus data berdasarkan ID Booking
            String sql = "DELETE FROM bookings WHERE booking_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idBooking);

            // Eksekusi query
            stmt.executeUpdate();

            // Menutup statement dan koneksi
            stmt.close();
            conn.close();

            // Menghapus baris dari tabel GUI
            DefaultTableModel model = (DefaultTableModel) Tabel_Booking.getModel();
            model.removeRow(selectedRow);

            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus data: " + ex.getMessage());
        }
    }
}

    private void delete_table1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_table1ActionPerformed
       deleteDataBooking();
       tampilkanTabelBookings();
    }//GEN-LAST:event_delete_table1ActionPerformed
     
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HOME().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.k33ptoo.components.KButton Exit;
    private javax.swing.JTextField ID_Kategori;
    private javax.swing.JTextField ID_Produk;
    private javax.swing.JTextField ID_Satuan;
    private javax.swing.JTextField ID_Supplier;
    private javax.swing.JTextField Id_Pelanggan;
    private javax.swing.JTextField Id_Pelanggan1;
    private javax.swing.JTextField Id_Pelanggan2;
    private com.k33ptoo.components.KButton Minimize;
    private com.k33ptoo.components.KButton SIMPAN_BTN;
    private com.k33ptoo.components.KButton SIMPAN_BTN1;
    private com.k33ptoo.components.KButton SIMPAN_BTN10;
    private com.k33ptoo.components.KButton SIMPAN_BTN2;
    private com.k33ptoo.components.KButton SIMPAN_BTN3;
    private com.k33ptoo.components.KButton SIMPAN_BTN4;
    private com.k33ptoo.components.KButton SIMPAN_BTN5;
    private com.k33ptoo.components.KButton SIMPAN_BTN6;
    private com.k33ptoo.components.KButton SIMPAN_BTN7;
    private com.k33ptoo.components.KButton SIMPAN_BTN8;
    private com.k33ptoo.components.KButton SIMPAN_BTN9;
    private com.k33ptoo.components.KGradientPanel Side_Bar;
    private com.k33ptoo.components.KGradientPanel TOP;
    private javax.swing.JTabbedPane TabPane;
    private javax.swing.JTable Tabel_Antrian;
    private javax.swing.JTable Tabel_Booking;
    private javax.swing.JTable Tabel_Kategori;
    private javax.swing.JTable Tabel_Pelanggan;
    private javax.swing.JTable Tabel_Produk;
    private javax.swing.JTable Tabel_Satuan;
    private javax.swing.JTable Tabel_Supplier;
    private javax.swing.JPanel beranda;
    private javax.swing.JPanel bidan;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.JCheckBox cb_boking;
    private javax.swing.JComboBox<String> comboBoxJenisKelamin;
    private javax.swing.JComboBox<String> comboBoxJenisKelamin1;
    private javax.swing.JComboBox<String> comboBoxJenisKelamin2;
    private javax.swing.JComboBox<String> comboBoxKategori;
    private javax.swing.JComboBox<String> comboBoxProduk;
    private javax.swing.JComboBox<String> comboBoxSatuan;
    private com.k33ptoo.components.KButton delete_table;
    private com.k33ptoo.components.KButton delete_table1;
    private com.k33ptoo.components.KButton delete_table2;
    private com.k33ptoo.components.KButton delete_table3;
    private com.k33ptoo.components.KButton delete_table4;
    private com.k33ptoo.components.KButton delete_table5;
    private com.k33ptoo.components.KButton delete_table6;
    private javax.swing.JPanel edit;
    private com.k33ptoo.components.KButton edit_BTN;
    private com.k33ptoo.components.KButton edit_BTN1;
    private com.k33ptoo.components.KButton edit_table;
    private com.k33ptoo.components.KButton edit_table1;
    private com.k33ptoo.components.KButton input_menu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextPane jTextPane2;
    private com.k33ptoo.components.KButton kButton1;
    private com.k33ptoo.components.KButton kButton10;
    private com.k33ptoo.components.KButton kButton11;
    private com.k33ptoo.components.KButton kButton12;
    private com.k33ptoo.components.KButton kButton13;
    private com.k33ptoo.components.KButton kButton14;
    private com.k33ptoo.components.KButton kButton2;
    private com.k33ptoo.components.KButton kButton4;
    private com.k33ptoo.components.KButton kButton5;
    private com.k33ptoo.components.KButton kButton7;
    private com.k33ptoo.components.KButton kButton9;
    private javax.swing.JLabel labelwaktu;
    private javax.swing.JLabel labelwaktu1;
    private javax.swing.JPanel pasien;
    private javax.swing.JPanel pasien1;
    private javax.swing.JPanel pelayanan;
    private javax.swing.JPanel pembayaran;
    private javax.swing.JPanel pembayaran1;
    private javax.swing.JPanel pendaftaran;
    private com.k33ptoo.components.KButton reset_tabel1;
    private com.k33ptoo.components.KButton reset_tabel2;
    private com.k33ptoo.components.KButton reset_tabel3;
    private com.k33ptoo.components.KButton reset_tabel4;
    private com.k33ptoo.components.KButton reset_tabel5;
    private com.k33ptoo.components.KButton reset_tabel7;
    private javax.swing.JTextField totalpembayaran;
    private javax.swing.JTextField totalpembayaran2;
    private javax.swing.JTextField totalpembayaran3;
    private javax.swing.JTextArea txtAlamatPelanggan;
    private javax.swing.JTextArea txtAlamatPelanggan1;
    private javax.swing.JTextArea txtAlamatPelanggan2;
    private javax.swing.JTextField txtAlamatSup;
    private javax.swing.JTextField txtBanyak;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtKeteranganSup;
    private javax.swing.JTextField txtNamaKategori;
    private javax.swing.JTextField txtNamaPelanggan;
    private javax.swing.JTextField txtNamaPelanggan1;
    private javax.swing.JTextField txtNamaPelanggan2;
    private javax.swing.JTextField txtNamaPelangganBayar;
    private javax.swing.JTextField txtNamaProduk;
    private javax.swing.JTextField txtNamaSatuan;
    private javax.swing.JTextField txtNamaSup;
    private javax.swing.JTextField txtNoTelpSup;
    private javax.swing.JTextField txtNoWA;
    private javax.swing.JTextField txtNoWA1;
    private javax.swing.JTextField txtNoWA2;
    private javax.swing.JTextField txtNoWABayar;
    private javax.swing.JTextField txtSatuan;
    private javax.swing.JTextField txtSearchProduk;
    private javax.swing.JTextField txtSearchSup;
    private javax.swing.JTextField txtStok;
    private javax.swing.JTextArea txtarea_listpesanan;
    private javax.swing.JTextField txtsearch;
    private javax.swing.JTextField txtsearch2;
    // End of variables declaration//GEN-END:variables
}
