<?php
ini_set('display_errors', false);
$return = array(
	'pic'    => 'http://www.baidu.com/img/bd_logo1.png',
	'name'   => '上传失败!',
	'path'   => '/',
	'status' => false
);
if ($_FILES['file']['error'] == 0) {
	$ext      = pathinfo($_FILES['file']['name'], PATHINFO_EXTENSION); 
	//$filePath = 'Images/'.Date('Y').'/'.Date('m').'/';
	$filePath = 'images/gallery/';
	$return['path'] = $filePath;
	$fileName = uniqid( mt_rand(11111,99999)).'.'.$ext;
	if (!is_dir($filePath)) {
		mkdir($filePath, 0700, true);
	}
	$filePath = $filePath.$fileName;  
	$r     = move_uploaded_file($_FILES['file']['tmp_name'], $filePath);
	if ($r == true) {
		$return['pic']    = dirname($_SERVER['HTTP_REFERER']);
		$return['pic']   .= '/' . $filePath;
		$return['name']   = $fileName;
		$return['del']    = StrToSixteen($filePath);
		$return['status'] = true;
	}
} elseif ($_FILES['file']['error'] == 1) {
	$return['name'] = '上传的文件超过了 php.ini 中 upload_max_filesize 选项限制的值';
} elseif ($_FILES['file']['error'] == 2) {
	$return['name'] = '上传文件的大小超过了 HTML 表单中 MAX_FILE_SIZE 选项指定的值';
} elseif ($_FILES['file']['error'] == 3) {
	$return['name'] = '网速太差了 文件只有部分被上传';
} elseif ($_FILES['file']['error'] == 4) {
	$return['name'] = '上传文件出错!';
}
echo json_encode($return);
function StrToSixteen($str){
    $arr = str_split($str); 
    foreach($arr as &$v){ 
        $temp = unpack('H*', $v); 
        $v = $temp[1];  
        unset($temp);
    }
    return implode('',$arr);
}
?>
