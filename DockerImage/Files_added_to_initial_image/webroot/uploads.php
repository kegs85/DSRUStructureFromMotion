<?php
session_start();
 
$message = ''; 
if (isset($_POST['uploadBtn']) && $_POST['uploadBtn'] == 'Upload')
{
	if (isset($_FILES['uploadedFile']) && count($_FILES['uploadedFile']['name']) > 1)
  {
	$countfiles = count($_FILES['uploadedFile']['name']);
	$foldername = md5(time());
    $uploadFileDir = '../data/webfiles/' . $foldername . '/images/';
    if (!mkdir($uploadFileDir, 0777, true)) {
        die('Failed to create directories...');
    }
    for($i=0;$i<$countfiles;$i++){
        // get details of the uploaded file
        $fileTmpPath = $_FILES['uploadedFile']['tmp_name'][$i];
        $fileName = $_FILES['uploadedFile']['name'][$i];
        $fileSize = $_FILES['uploadedFile']['size'][$i];
        $fileType = $_FILES['uploadedFile']['type'][$i];
        $fileNameCmps = explode(".", $fileName);
        $fileExtension = strtolower(end($fileNameCmps));
 
        // sanitize file-name
        $newFileName = md5(time() . $fileName) . '.' . $fileExtension;
 
        // check if file has one of the following extensions
        $allowedfileExtensions = array('jpeg','jpg', 'gif', 'png', 'zip', 'txt', 'xls', 'doc');
 
        if (in_array($fileExtension, $allowedfileExtensions))
        {
               
          $dest_path = $uploadFileDir . $newFileName;
 
          if(move_uploaded_file($fileTmpPath, $dest_path)) 
          {
            $message ='File is successfully uploaded.';
          }
          else
          {
            $message = 'There was some error moving the file to upload directory. Please make sure the upload directory is writable by web server.';
          }
        }
        else
        {
          $message = 'Upload failed. Allowed file types: ' . implode(',', $allowedfileExtensions);
	}
    }
    $old_path = getcwd();
    chdir('../scripts/');
    $output = shell_exec('./processFiles ' . $foldername);
    chdir($old_path);
    echo '<p>Thanks for processing your image, you can download the final ply from the link below.<br>
	<a href="downloads/' . $foldername . '.ply" download>Download your file here</a></p>
	<p>I recommend you download Meshlab (<a href="https://www.meshlab.net/">https://www.meshlab.net/</a>) and open the ply file from there to perform measurements.</p>
	<p>To start a new download (<a href="index.php">Return to the index page here</a>)</p>
	<h3>Basic Meshlab Steps</h3>
	<p>To perform measurements in MeshLab:</p>
	<ol>
        <li>Make sure you have the measurement of your marker on at least one dimension - we will call this the [Known Dimension]</li>
        <li>Select File> Import Mesh and import the ply file you downloaded  above</li>
        <li>Use the Measure tool (tape measure) to measure the diameter of the marker used (coin in my case) we will call this the  [Marker Dimension]<br>Note: this must be the same dimension you have physically measured on the marker in step 1</li>
        <li>Use the Measure tool (tape measure) to measure the length of the object you wish to measure [Unknown Dimension] - we will call this the [Ratio]</li>
        <li>Divide the [Marker Dimension] by the [Known Dimension], then divide the [Unknown Dimension] by the [Ratio] to get the actual measurement of your unknown dimension (in the same units as your [Marker Dimension])</li>
        <li>Repeat steps 4 and 5 for any other measurement required</li>
	</ol>
<span style="border:2px solid red; border-radius:7px; padding: 10px;margin: 10px;">You can download a sample project <a href="sample_project.zip">here</a>, complete with original files, reference measurements and the output ply file.</span>';
  }
  else
  {
	  $message = 'There is some error in the file upload. Please check the following error.<br>';
	  echo $message;
   // $message .= 'Error:' . $_FILES['uploadedFile']['error'];
  }
}
$_SESSION['message'] = $message;
