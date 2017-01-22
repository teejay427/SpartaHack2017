<!DOCTYPE html>
<html>
<head>
	<title>Purely Connection</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<?php

#http://wiki.hashphp.org/PDO_Tutorial_for_MySQL_Developers

#Fill in the variables with appropriate info
$db = new PDO('HOSTNAME;dbname=DBNAME;charset=utf8mb4', 'USERNAME', 'PASSWORD');

$lat = $_POST['lat'];
$lon = $_POST['lon'];
$query = "SELECT * FROM crash_data WHERE lat LIKE '$lat%' AND lon LIKE '$lon%'";

function getData($db, $query){
	foreach($db->query($query) as $row){
		echo "$row[0],$row[2],$row[3];";
	}
}

try{
	getData($db, $query);
} catch(PDOException $ex){
	echo "An Error occured!";
}


?>
</body>
</html>
