<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
    $idusers = $_POST['idusers'];
    $idtours = $_POST['idtours'];

    require_once('dbConnect.php');

	
	$sql = "SELECT * FROM takentours WHERE idusers = '$idusers' AND idtours = '$idtours'";
	if($result = mysqli_query($con,$sql)){
		if(mysqli_num_rows($result)> 0) {
			echo "Tour completed again!";
		}
		else {
			$sql2 = "INSERT INTO takentours (idusers,idtours) VALUES ('$idusers','$idtours')";
			if(mysqli_query($con,$sql2)){
				echo "Tour completed!";
			}else{
				echo "Could not complete tour";
			}
		}
	}
	
}else{
echo 'error';
}
?>