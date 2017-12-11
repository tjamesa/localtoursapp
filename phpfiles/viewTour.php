<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
    $idtours = $_POST['idtours'];

    require_once('dbConnect.php');

    $sql = "SELECT * FROM tours WHERE idtours = '$idtours'";


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