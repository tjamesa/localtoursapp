<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
    $idspots = $_POST['idspots'];

    require_once('dbConnect.php');

    $sql = "SELECT * FROM spots WHERE idspots = '$idspots'";


    if($result = mysqli_query($con,$sql)){
		if(mysqli_num_rows($result) <= 0) {
			echo "No spots found";
		}
		else {
			$row = mysqli_fetch_assoc($result);
			echo json_encode($row);
		}
    }
}else{
echo 'error';
}
?>