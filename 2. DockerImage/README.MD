# DSRUStructureFromMotion - 2. DockerImage
Details about the docker image component.

This docker image was built from https://github.com/mapillary/OpenSfM using the Dockerfile.

Implementing the project without Dockerfile was similarly problematic due to incompatibilities in the repo configuration and the system in use, using Apple Silicone (MacBook Pro M1).

Due to the changes in some of the components, there were some issues in creating the image with the contained dockerfile and several components needed to be rolled back to previous versions.  I also had a goal to make this project more user accessible, so PHP has also been installed, and some web components have been added to allow a user accessible, so the deployed package has a web start script and processing has been automated.

Note - the Files_added_to_initial_image directory contains all of the additional scripts and web files added to the initially built docker image.  These get added to the /source/OpenSfM/ directory (working directory that the image loads to).
You do not need to add these if you use the image supplied, however, if you build from scratch using the OpenSFM dockerfile, you will need to add these.

DOCKER_IMAGE:
kegs85/dsruproject:opensfm

HOW_TO_USE
* This process can be used to create a dense point cloud from a series of 2D images.
* These images should be taken from left to right or right to left across the front of an image, not vertically.

1) Deploy the docker image to a container
  a. make sure the container maps a port on your computer - you'll need to remember this port for Step 3
2) SSH to the container and execute "./startweb"
3) Open a browser and navigate to localhost:port_from_step_1a
4) Use the web UI to select the images you wish to process
5) Click Upload
wait for the system to process the files - you can watch progress on the SSH window if you like
6) Once complete, you can download your completed dense point cloud
7) I suggest downloading MeshLAB and opening the ply with it
8) Measure your reference marker (e.g. coin) and use it to scale the other measurements in the point cloud
