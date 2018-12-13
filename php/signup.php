<?
 $mysqli = mysqli_connect( "localhost", "ppmj789", "whdeo1027" ,"ppmj789");
 if(!$mysqli){
   die('could not connect'.mysql_error());
 }else{
   $sql = "INSERT INTO UserDB (UserID, Password, Name)";
   // $sql = $sql."VALUES ('ppp','sss','ssss')";
   // $UserID = $_POST['UserID'];
   // $Password = $_POST['Password'];
   // $Name = $_POST['Name'];

   $sql = $sql."VALUES('$_GET[UserID]','$_GET[Password]','$_GET[Name]')";

   $result = mysqli_query($mysqli,$sql);

   if($result){
     echo "회원 가입 성공";
   }else{
     echo "회원 가입 실패 아이디가 중복됩니다.";
   }

 }
 header('Content-Type: text/html; charset=utf-8');
 header('Content-Type: application/json; charset=utf8');
 mysqli_close($mysqli);
?>
