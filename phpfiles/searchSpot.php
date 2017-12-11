<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
    $name = $_POST['name'];

    require_once('dbConnect.php');

    $sql = "SELECT * FROM spots WHERE name like '%" . $name . "%'";


    if($result = mysqli_query($con,$sql)){
		if(mysqli_num_rows($result) <= 0) {
			echo "No spots found";
		}
		else {
			$rows = array();
			while($row = mysqli_fetch_assoc($result)) {
				$rows[] = $row;
			}
			echo json_encode($rows);
		}
    }
}else{
echo 'error';
}
?>
