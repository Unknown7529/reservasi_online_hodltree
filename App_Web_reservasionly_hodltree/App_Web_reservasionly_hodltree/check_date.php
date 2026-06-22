<?php
// Konfigurasi database
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "hodltree";

// Membuat koneksi ke database
$conn = new mysqli($servername, $username, $password, $dbname);

// Memeriksa koneksi
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Mendapatkan tanggal dari permintaan AJAX
$date = $_POST['date'];

// Query untuk menghitung jumlah booking pada tanggal tertentu
$sql = "SELECT COUNT(*) AS total_bookings FROM bookings WHERE date = '$date'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    if ($row['total_bookings'] >= 10) {
        echo 'full'; // Tanggal penuh
    } else {
        echo 'available'; // Tanggal tersedia
    }
} else {
    echo 'available'; // Tidak ada booking pada tanggal tersebut
}

// Menutup koneksi
$conn->close();
?>
