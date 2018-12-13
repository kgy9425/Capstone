<?
 $mysqli = mysqli_connect( "localhost", "ppmj789", "whdeo1027" ,"ppmj789");
 if(!$mysqli){
   die('could not connect'.mysql_error());
 }else{

   $sql = "INSERT INTO DiseaseRecordDB (UserID, DiseaseCode, Date)";
   // $sql = $sql."VALUES ('ppp','sss','ssss')";
   // $UserID = $_POST['UserID'];
   // $Password = $_POST['Password'];
   // $Name = $_POST['Name'];

   $sql = $sql."VALUES('$_GET[UserID]','$_GET[DiseaseCode]','$_GET[Date]')";

   $result = mysqli_query($mysqli,$sql);

   if($result){
     echo "질병 기록 성공";
   }else{
     echo "질병 기록 실패.";
   }

 }
 header('Content-Type: text/html; charset=utf-8');
 header('Content-Type: application/json; charset=utf8');
 mysqli_close($mysqli);
?>
