<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "hodltree";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$whatsapp_number = $_GET['whatsapp_number'];
$sql = "SELECT nama, no_wa FROM pelanggan WHERE no_wa='$whatsapp_number'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $member = $result->fetch_assoc();
    echo json_encode($member);
} else {
    echo json_encode(null);
}

$conn->close();
?>
