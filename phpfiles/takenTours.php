<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
    $idusers = $_POST['idusers'];

    require_once('dbConnect.php');

    $sql = "SELECT * FROM takentours WHERE idusers = '$idusers'";


    if($result = mysqli_query($con,$sql)){
		if(mysqli_num_rows($result) <= 0) {
			echo "No tours found";
		}
		else {
			$rows = array();
			while($row = mysqli_fetch_assoc($result)) {
				$sql2 = "SELECT * FROM tours WHERE idtours = '" . $row['idtours'] . "'";
				if($result2 = mysqli_query($con,$sql2)){
					while($row2 = mysqli_fetch_assoc($result2)) {
						$rows[] = $row2;
					}
				}
			}
			echo json_encode($rows);
		}
    }
}else{
echo 'error';
}
?>