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

// Mendapatkan data dari form
$name = $_POST['name'];
$date = $_POST['date'];
$time = $_POST['time'];
$people = $_POST['people'];
$whatsapp_number = $_POST['whatsapp_number'];
$email = $_POST['email'];

// Validasi apakah tanggal masih tersedia
$sql_check_date = "SELECT COUNT(*) AS total_bookings FROM bookings WHERE date = '$date'";
$result_check_date = $conn->query($sql_check_date);
$row_check_date = $result_check_date->fetch_assoc();

if ($row_check_date['total_bookings'] >= 10) {
    echo "<script>
            alert('Booking full for this day! Please select another date.');
            window.location.href = 'index.php';
          </script>";
    exit();
}

// Menyimpan data ke tabel bookings
$sql = "INSERT INTO bookings (name, date, time, people, whatsapp_number, email)
        VALUES ('$name', '$date', '$time', $people, '$whatsapp_number', '$email')";

if ($conn->query($sql) === TRUE) {
    $booking_id = $conn->insert_id;
    echo "<script>
            alert('Reservation successful! Your Booking ID is: $booking_id.');
            window.location.href = 'index.php';
          </script>";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

// Menutup koneksi
$conn->close();
?>
