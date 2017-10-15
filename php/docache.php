<?php
require_once __DIR__ . '/cacher_utils.php';
	
	// Check if the cached file is still fresh. If it is, serve it up and exit.
	//if (file_exists($cachefile) && time() - $cachetime < filemtime($cachefile)) {
		//include($cachefile);
		//files already present and updated
		//exit;
	//}
	ignore_user_abort(true);
	set_time_limit(30);
	
	ob_start();
	// do initial processing here
	echo "caching in progress!"; // send the response
	header('Connection: close');
	header('Content-Length: '.ob_get_length());
	ob_end_flush();
	ob_flush();
	flush();


	$ch = curl_init();
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_URL, 
	    "http://mobile.esupd.gov.it/api/reiservice.svc/canteens?lang=it"
	);
	$content = curl_exec($ch);

	// We're done! Save the cached content to a file
	$fp = fopen($cachefile, 'w');
	fwrite($fp, $content);
	fclose($fp);
	// finally send browser output
?>