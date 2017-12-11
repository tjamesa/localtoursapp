<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
	$name = $_POST['name'];
    $spot1 = $_POST['spot1'];
	$spot2 = $_POST['spot2'];
    $spot3 = $_POST['spot3'];
	$spot4 = $_POST['spot4'];
	$spot5 = $_POST['spot5'];
	$spot6 = $_POST['spot6'];
	$spot7 = $_POST['spot7'];
	$spot8 = $_POST['spot8'];
	$spot9 = $_POST['spot9'];
	$spot10 = $_POST['spot10'];
	

    require_once('dbConnect.php');

	$sql = "INSERT INTO tours (name,spot1,spot2,spot3,spot4,spot5,spot6,spot7,spot8,spot9,spot10) VALUES ('$name','$spot1','$spot2','$spot3','$spot4','$spot5','$spot6','$spot7','$spot8','$spot9','$spot10')";
	if(mysqli_query($con,$sql)){
		echo "Successfully Uploaded";
	}
	else {
		echo "Could not upload";
	}
	
}else{
echo 'error';
}
?>