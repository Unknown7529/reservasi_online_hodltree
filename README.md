# ☕ Hodltree App — Sistem Reservasi & Manajemen Cafe

> Aplikasi desktop berbasis **Java Swing** untuk manajemen cafe **Hodltree Coffee & Chill**, mencakup sistem reservasi meja, manajemen pelanggan, produk, supplier, transaksi kasir, dan ekspor laporan PDF. Dilengkapi juga dengan **aplikasi web reservasi online** berbasis PHP untuk pemesanan meja oleh pelanggan.

---

## 📋 Daftar Isi

- [Tentang Aplikasi](#tentang-aplikasi)
- [Fitur](#fitur)
- [Teknologi yang Digunakan](#teknologi-yang-digunakan)
- [Struktur Aplikasi](#struktur-aplikasi)
- [Struktur Database](#struktur-database)
- [Prasyarat](#prasyarat)
- [Instalasi & Konfigurasi](#instalasi--konfigurasi)
- [Cara Menjalankan](#cara-menjalankan)
- [Struktur Proyek](#struktur-proyek)
- [Informasi Cafe](#informasi-cafe)

---

## 📖 Tentang Aplikasi

**Hodltree App** terdiri dari dua komponen:

1. **Aplikasi Desktop (Java Swing)** — digunakan oleh admin/kasir cafe untuk mengelola data pelanggan, produk, supplier, pemrosesan transaksi kasir, manajemen booking, serta mencetak laporan dan kwitansi dalam format PDF.

2. **Aplikasi Web Reservasi (PHP)** — digunakan oleh pelanggan untuk melakukan reservasi meja secara online, lengkap dengan pengecekan ketersediaan tanggal secara real-time (maksimal 10 booking per hari).

---

## ✨ Fitur

### Aplikasi Desktop (Admin)

**Manajemen Pelanggan**
- Tambah, edit, hapus data pelanggan (nama, jenis kelamin, No. WhatsApp, alamat)
- Pencarian pelanggan secara real-time
- Ekspor laporan data seluruh pelanggan ke PDF

**Manajemen Supplier**
- Tambah, edit, hapus data supplier (nama, alamat, no. telepon, keterangan)
- Pencarian supplier secara real-time
- Ekspor laporan data supplier ke PDF

**Manajemen Produk**
- Tambah, edit, hapus data produk (nama, kategori, satuan, harga, stok)
- Manajemen kategori produk dan satuan
- Pencarian produk secara real-time
- Ekspor laporan data produk ke PDF

**Manajemen Booking**
- Tampilkan daftar reservasi yang masuk dari pelanggan web
- Hapus data booking
- Konversi booking langsung ke transaksi kasir
- Ekspor laporan seluruh data booking ke PDF

**Kasir / Transaksi**
- Pemilihan produk dari daftar menu
- Kalkulasi total pembayaran dan kembalian otomatis
- Pencatatan transaksi ke database
- Cetak kwitansi pembayaran (PDF) dengan timestamp unik

**Laporan PDF (iTextPDF)**
- Laporan transaksi (nama, tanggal, total harga)
- Laporan data pelanggan
- Laporan data supplier
- Laporan data produk
- Laporan data booking
- Kwitansi pembayaran per transaksi
- Semua laporan menggunakan kop surat resmi cafe (logo, nama, alamat)

### Aplikasi Web (Pelanggan)

- Form reservasi meja online (nama, tanggal, waktu, jumlah orang, No. WhatsApp, email)
- Pengecekan ketersediaan tanggal secara real-time via AJAX
- Pembatasan otomatis: maksimal **10 booking per hari**
- Notifikasi booking berhasil beserta **Booking ID** unik

---

## 🛠️ Teknologi yang Digunakan

### Aplikasi Desktop

| Teknologi | Versi | Keterangan |
|-----------|-------|------------|
| Java | 17 | Bahasa pemrograman utama |
| Java Swing | — | Framework GUI desktop |
| NetBeans IDE | — | IDE yang digunakan |
| MySQL Connector/J | 5.1.49 | Driver koneksi Java ke MySQL |
| iTextPDF | 5.3.5 | Pembuatan laporan & kwitansi PDF |
| KControls | 2.0 | Komponen UI modern (KButton, KGradientPanel) |
| JCalendar | 1.4 | Komponen date picker/kalender |
| AbsoluteLayout | — | Layout manager NetBeans |
| TimingFramework | 5.5.0 | Animasi UI |

### Aplikasi Web

| Teknologi | Keterangan |
|-----------|------------|
| PHP | Backend form reservasi |
| MySQL (MySQLi) | Database booking |
| HTML / CSS / JavaScript | Antarmuka web pelanggan |
| AJAX | Pengecekan ketersediaan tanggal real-time |

---

## 🗂️ Struktur Aplikasi

```
Hodltree App
├── Aplikasi Desktop (Java)
│   ├── Login Admin
│   └── Dashboard (HOME)
│       ├── Menu Pelanggan     → CRUD + PDF
│       ├── Menu Supplier      → CRUD + PDF
│       ├── Menu Produk        → CRUD + Kategori + Satuan + PDF
│       ├── Menu Booking       → Lihat, Hapus, Konversi ke Transaksi + PDF
│       ├── Menu Kasir         → Input pesanan, hitung total & kembalian, kwitansi
│       └── Menu Laporan       → Generate semua laporan PDF
│
└── Aplikasi Web (PHP)
    ├── index.php              → Form reservasi pelanggan
    ├── process_booking.php    → Simpan booking ke database
    └── check_date.php         → Cek ketersediaan tanggal (AJAX)
```

---

## 🗄️ Struktur Database

**Nama Database:** `hodltree`

**Koneksi default:**
```
Host     : localhost
Database : hodltree
Username : root
Password : (kosong)
```

**Tabel-tabel yang digunakan:**

| Tabel | Kolom Utama | Keterangan |
|-------|-------------|------------|
| `admin` | username, password | Akun login admin desktop |
| `pelanggan` | id_pelanggan, nama, jenis_kelamin, no_wa, alamat | Data pelanggan cafe |
| `supplier` | id_supplier, nama, alamat, no_telepon, keterangan | Data supplier/vendor |
| `kategori` | id_kategori, nama_kategori | Kategori produk menu |
| `satuan` | nama_satuan | Satuan produk (porsi, gelas, dll.) |
| `produk` | Kategori, Satuan, Nama_Produk, Harga, Stok | Data menu/produk cafe |
| `bookings` | booking_id, name, date, time, people, whatsapp_number, email | Data reservasi meja |
| `transaksi` | id_transaksi, nama, tanggal, total_harga | Riwayat transaksi kasir |

---

## ⚙️ Prasyarat

Pastikan perangkat Anda sudah memiliki:

- **JDK 17** atau lebih baru → [Download JDK](https://www.oracle.com/java/technologies/downloads/)
- **MySQL Server** (5.x atau 8.x) → [Download MySQL](https://dev.mysql.com/downloads/)
- **XAMPP / WAMP** (untuk menjalankan aplikasi web PHP) → [Download XAMPP](https://www.apachefriends.org/)
- **NetBeans IDE** (untuk development) → [Download NetBeans](https://netbeans.apache.org/)

Cek versi Java:
```bash
java -version
```

---

## 🚀 Instalasi & Konfigurasi

### 1. Clone Repositori

```bash
git clone https://github.com/Unknown7529/reservasi_online_hodltree.git
cd reservasi_online_hodltree
```

### 2. Buat Database MySQL

```sql
CREATE DATABASE hodltree;
USE hodltree;
-- Import file SQL jika tersedia:
-- SOURCE hodltree.sql;
```

### 3. Konfigurasi Koneksi Database — Aplikasi Desktop

Edit file `src/hodltree/koneksi.java`:

```java
String DB   = "jdbc:mysql://localhost/hodltree";
String user = "root";
String pass = "";  // sesuaikan jika ada password MySQL
```

### 4. Konfigurasi Path Laporan PDF

Edit variabel `Path` dan `fileName` di `src/hodltree/HOME.java` sesuai lokasi instalasi:

```java
String Path = "C:\\path\\ke\\src\\image\\";
String fileName = "C:\\path\\output\\laporan_" + getCurrentTimestamp() + ".pdf";
```

### 5. Setup Aplikasi Web (PHP)

Salin folder `App_Web_reservasionly_hodltree/App_Web_reservasionly_hodltree/` ke direktori web server:

```
XAMPP  → C:\xampp\htdocs\hodltree\
WAMP   → C:\wamp64\www\hodltree\
Linux  → /var/www/html/hodltree/
```

Akses aplikasi web di browser:
```
http://localhost/hodltree/
```

---

## ▶️ Cara Menjalankan

### Aplikasi Desktop — via NetBeans

1. Buka **NetBeans IDE**
2. **File → Open Project** → pilih folder proyek
3. Klik kanan proyek → **Run** atau tekan `F6`
4. Aplikasi membuka halaman **Login Admin** (`hodltree.login`)

### Aplikasi Desktop — via Command Line

```bash
cd build/classes
java -cp ".;../../src/lib/*" hodltree.login
```

### Aplikasi Web

1. Pastikan **Apache** dan **MySQL** di XAMPP/WAMP sudah aktif
2. Buka browser → `http://localhost/hodltree/`
3. Pelanggan dapat mengisi form reservasi meja secara online

---

## 📁 Struktur Proyek

```
apps-implementasi-reservasi-desktop-main/
│
├── src/
│   ├── hodltree/
│   │   ├── login.java              # Form login admin (entry point)
│   │   ├── loading.java            # Loading screen animasi
│   │   ├── HOME.java               # Frame utama — semua fitur admin
│   │   ├── koneksi.java            # Kelas koneksi ke MySQL
│   │   ├── LabelDay.java           # Komponen label hari
│   │   └── LabelTime.java          # Komponen label jam
│   ├── image/                      # Aset gambar (logo, dll.)
│   └── lib/                        # Library JAR dependensi
│       ├── mysql-connector-java-5.1.49.jar
│       ├── itextpdf-5.3.5.jar
│       ├── KControls-2.0.jar
│       ├── jcalendar-1.4.jar
│       ├── AbsoluteLayout.jar
│       └── timingframework-core-5.5.0.jar
│
├── App_Web_reservasionly_hodltree/
│   └── App_Web_reservasionly_hodltree/
│       ├── index.php               # Halaman form reservasi pelanggan
│       ├── process_booking.php     # Proses & simpan booking ke database
│       ├── check_date.php          # Cek ketersediaan tanggal (AJAX)
│       └── logo.png                # Logo cafe
│
├── build/                          # Output kompilasi (.class files)
├── nbproject/                      # Konfigurasi NetBeans (project.xml, dll.)
├── build.xml                       # Ant build script
└── manifest.mf                     # Manifest JAR
```

---

## 🏪 Informasi Cafe

**Hodltree Coffee & Chill**
📍 Jl. Alternatif GOR Pemda, Nanggewer Mekar, Kec. Cibinong, Kabupaten Bogor, Jawa Barat 16912

---

> ⭐ Jika proyek ini bermanfaat, jangan lupa berikan bintang pada repositori ini!
