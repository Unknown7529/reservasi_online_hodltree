<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hodltree Restaurant - Reservation</title>
    <link rel="icon" href="assets/logo.png">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }

        .reservation-form {
            background-color: #ffffff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .navbar {
            background-color: #343a40;
        }

        .navbar-brand img {
            height: 50px;
        }

        .footer {
            background-color: #343a40;
            color: #ffffff;
            padding: 20px 0;
        }

        #dateFeedback {
            font-size: 0.875rem;
        }
    </style>
</head>

<body>

    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand" href="#">
                <img src="logo.png" alt="Hodltree Logo">
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="#menu">Menu</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#venue">Venue</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link btn btn-primary text-white" href="#book">Book Now</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Reservation Form -->
    <div class="container my-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="reservation-form">
                    <h2 class="text-center mb-4">Make a Reservation</h2>
                    <form id="reservationForm" method="post" action="process_booking.php">
                        <div class="mb-3">
                            <label for="is_member" class="form-label">Are you a member?</label>
                            <select class="form-select" id="is_member" name="is_member" required>
                                <option value="no">No</option>
                                <option value="yes">Yes</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="name" class="form-label">Name</label>
                            <input type="text" class="form-control" id="name" name="name" required>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="date" class="form-label">Date</label>
                                <input type="date" class="form-control" id="date" name="date" required>
                                <div id="dateFeedback" class="text-danger mt-1"></div>
                            </div>
                            <div class="col-md-6">
                                <label for="time" class="form-label">Time</label>
                                <input type="time" class="form-control" id="time" name="time" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="people" class="form-label">Number of People</label>
                            <input type="number" class="form-control" id="people" name="people" required>
                        </div>
                        <div class="mb-3">
                            <label for="whatsapp_number" class="form-label">Whatsapp Number</label>
                            <input type="text" class="form-control" id="whatsapp_number" name="whatsapp_number" required>
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" name="email">
                        </div>
                        <button type="submit" id="reserveButton" class="btn btn-primary w-100">Reserve a Table</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="fullBookingModal" tabindex="-1" aria-labelledby="fullBookingModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="fullBookingModalLabel">Booking Full</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    Booking pada tanggal tersebut sudah penuh, silahkan ganti tanggal booking untuk melanjutkan reservasi.
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">OK</button>
                </div>
            </div>
        </div>
    </div>

    <footer class="footer text-center">
        <div class="container">
            <p>© 2025 Hodltree - All rights reserved.</p>
            <p>Email: <a href="mailto:info@Hodltree.com" class="text-white">info@Hodltree.com</a></p>
            <p>Location: Jl. Alternatif Gor Pemda, Cibinong</p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function () {
            $('#is_member').change(function () {
                if ($(this).val() === 'yes') {
                    let whatsappNumber = prompt("Please enter your Whatsapp Number:");
                    if (whatsappNumber) {
                        $.ajax({
                            url: 'get_member.php',
                            type: 'GET',
                            data: { whatsapp_number: whatsappNumber },
                            success: function (data) {
                                let member = JSON.parse(data);
                                if (member) {
                                    $('#name').val(member.nama).attr('readonly', true);
                                    $('#whatsapp_number').val(member.no_wa).attr('readonly', true);
                                } else {
                                    alert("Member not found!");
                                }
                            },
                            error: function () {
                                alert("Error fetching member data.");
                            }
                        });
                    }
                } else {
                    $('#name').val('').removeAttr('readonly');
                    $('#whatsapp_number').val('').removeAttr('readonly');
                }
            });

            $('#date').on('change', function () {
                let selectedDate = $(this).val();
                if (selectedDate) {
                    $.ajax({
                        url: 'check_date.php',
                        type: 'POST',
                        data: { date: selectedDate },
                        success: function (response) {
                            if (response === 'full') {
                                $('#dateFeedback').text('Mohon maaf reservasi pada tanggal tersebut sudah penuh (full booked)');
                                $('#reserveButton').prop('disabled', true);
                                $('#fullBookingModal').modal('show');
                            } else {
                                $('#dateFeedback').text('');
                                $('#reserveButton').prop('disabled', false);
                            }
                        },
                        error: function () {
                            alert('Error checking date availability.');
                        }
                    });
                }
            });
        });
    </script>
</body>

</html>
