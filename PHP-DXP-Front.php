<?php
//echo "<b> <center>You're viewing the current status of Tycoon DXP across all ten servers. This is live data. </b><br/><br/>";
$servername = "";
$username = ";
$password = "";
$dbname = "";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}

$sql = "SELECT * FROM serverdata";
$result = $conn->query($sql);

//require('index.html'); // HTML,CSS,JS ETC design code

echo '<table class="table table-bordered">
  <thead>
    <tr>
      <th scope="col"> TT Server ID</th>
      <th scope="col">DXP Status</th>
      <th scope="col">DXP Info</th>
      <th scope="col">Server Info</th>
    </tr>
  </thead>';
if ($result->num_rows > 0) {
  // output data of each row
  while($row = $result->fetch_assoc()) {
if ($row['expired'] == 0) {
  $dxp = $row['dxp'];
  $dxp_array = explode (",", $dxp); 
   if ($dxp_array[0] == "[true") {
echo ' <tr>
      <th scope="row">'.$row["serverid"].'</th>
      <td>'.$dxp_array[0].'</td>
      <td>'.floor($dxp_array[2]/60000).'</td>
      <td>'.$dxp_array[1].'</td>
    </tr>';
   } else {
echo '<tr>
      <th scope="row">'.$row["serverid"].'</th>
      <td><p style="color:red">No DXP Active.</p></td>
      <td> </td>
      <td>'.str_replace('"', "", $row['uptime']).'</td>
    </tr>';
  } 
} else {

}
  }
} else {
 // echo "0 results";
}
echo '</table>';
echo "<b> This is live data. If a server does not show it is offline.</b>";
$conn->close();
?>
