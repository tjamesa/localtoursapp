<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
    $username = $_POST['username'];
    $password = $_POST['password'];

    require_once('dbConnect.php');

    $sql = "SELECT * FROM users WHERE username = '$username' AND password = '$password'";


    if($result = mysqli_query($con,$sql)){
		if(mysqli_num_rows($result) <= 0) {
			echo "Invalid Username and Password Combination";
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
