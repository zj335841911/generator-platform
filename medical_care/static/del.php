<?php
$res = @unlink(SixteenToStr($_POST['pic']));
echo json_encode(array('status'=>$res));
function SixteenToStr($str){  
    $len    = strlen($str);    
    $newstr = '';   
    if ($len % 2 == 0) {
        for($i = 0; $i < $len; $i = $i+2){
            $bin = $str[$i].$str[$i+1];
            $newstr .= pack("H*", $bin);  
        }
    } 
    return $newstr;
}