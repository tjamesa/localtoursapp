<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
    $latitude = $_POST['latitude'];
	$longitude = $_POST['longitude'];
    $name = $_POST['name'];
    $description = $_POST['description'];

    require_once('dbConnect.php');

	$sql = "INSERT INTO spots (latitude,longitude,name,description) VALUES ('$latitude','$longitude','$name','$description')";
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