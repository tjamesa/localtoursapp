<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
    $idusers = $_POST['idusers'];
    $idspots = $_POST['idspots'];

    require_once('dbConnect.php');

	
	$sql = "SELECT * FROM visitedspots WHERE idusers = '$idusers' AND idspots = '$idspots'";
	if($result = mysqli_query($con,$sql)){
		if(mysqli_num_rows($result)> 0) {
			echo "You have already visited this spot.";
		}
		else {
			$sql2 = "INSERT INTO visitedspots (idusers,idspots) VALUES ('$idusers','$idspots')";
			if(mysqli_query($con,$sql2)){
				echo "Successfully Visited";
			}else{
				echo "Could not mark as visited";
			}
		}
	}
	
}else{
echo 'error';
}
?>