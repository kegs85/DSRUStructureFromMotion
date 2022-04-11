<?php
session_start(); 
?>
<!DOCTYPE html>
<html>
<head>
  <title>PHP File Upload</title>
</head>
<body>
  <?php
    if (isset($_SESSION['message']) && $_SESSION['message'])
    {
      echo '<p class="notification">'.$_SESSION['message'].'</p>';
      unset($_SESSION['message']);
    }
  ?>
  <h1>2D to 3D Image Renderer</h1>
  <p>This form allows you to submit photos to be processed into 3D mesh, which can then be used to peform measurements in 3D.<br>It's recommended that you have:</p>
  <ul>
    <li>At least 8 images of the object in question</li>
    <li>All images taken in portrait mode, panning in a 'left to right' or 'right to left' formation across the subject and marker</li>
    <li>A marker object to give you scale - I have used an Australian $1 coin as it is accessible and easily measured</li>
    <li>A plain or consistent background that is distinguished from the subject matter - shadows may cause noise in the 3D render</li>
  </ul>
        <span style="border:2px solid red; border-radius:7px; padding: 10px;margin: 10px;">You can download a sample project <a href="sample_project.zip">here</a>, complete with original files, reference measurements and the output ply file.</span>
  <h2>Uploading Files:</h2>
  <form  method="POST" action="uploads.php" enctype="multipart/form-data" onsubmit="document.getElementById('submitbutton').setAttribute('style','display:none');document.getElementById('uploadMsg').setAttribute('style','display:block')">
    <div class="upload-wrapper">
      <p class="file-name">Click Browse to choose your files...</p>
      <p><label for="file-upload">Browse <input type="file" multiple id="file-upload" name="uploadedFile[]"></label></p>
    </div>
    <br>
    <input type="submit" id="submitbutton" name="uploadBtn" value="Upload" />
  </form>
<p id="uploadMsg" style="display:none;">Your upload is in progress...<br>it will take ~60s after clicking upload before your output file is displayed.</p>
</body>
</html>
