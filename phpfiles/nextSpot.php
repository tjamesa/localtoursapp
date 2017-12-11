<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
    $idtours = $_POST['idtours'];
	$spotIndex = $_POST['spotIndex'];
	$spotIndex++;
	
    require_once('dbConnect.php');

    $sql = "SELECT * FROM tours WHERE idtours = '$idtours'";


    if($result = mysqli_query($con,$sql)){
		if(mysqli_num_rows($result) <= 0) {
			echo "No tours found";
		}
		else {
			$row = mysqli_fetch_assoc($result);
			$spotString = "spot" . $spotIndex;
			$sql2 = "SELECT * FROM spots WHERE idspots = '" . $row[$spotString] . "'";
			if($result2 = mysqli_query($con,$sql2)){
				if(mysqli_num_rows($result) <= 0) {
					echo "No spots found";
				}
				else{
					$row2 = mysqli_fetch_assoc($result2);
					echo json_encode($row2);
				}
			}
		}
    }
}else{
echo 'error';
}
?>