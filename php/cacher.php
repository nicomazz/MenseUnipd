<?php
	// define the path and name of cached file
	$cachefile = 'cached-files/cached_esu.json';
	// define how long we want to keep the file in seconds. I set mine to 5 hours.
	$cachetime = 60*5; // 5 minuti
	// Check if the cached file is still fresh. If it is, serve it up and exit.
	if (file_exists($cachefile) && time() - $cachetime < filemtime($cachefile)) {
		include($cachefile);
		exit;
	}
	// if there is either no file OR the file to too old, render the page and capture the HTML.
	ob_start();
	//$request_content = file_get_contents("http://mobile.esupd.gov.it/api/reiservice.svc/canteens?lang=it");

	//echo "proviamoci!";
	//echo $request_content;
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_URL, 
	    "http://mobile.esupd.gov.it/api/reiservice.svc/canteens?lang=it"
	);
	$content = curl_exec($ch);

	echo $content;

	// We're done! Save the cached content to a file
	$fp = fopen($cachefile, 'w');
	fwrite($fp, $content);
	fclose($fp);
	// finally send browser output
	ob_end_flush();
?>