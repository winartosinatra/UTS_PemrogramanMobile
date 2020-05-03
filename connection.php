<?php
$conn = mysqli_connect('localhost','root','','db_phone') or die(mysqli_error());
if ($conn) {
	echo "Berhasil";
}
?>