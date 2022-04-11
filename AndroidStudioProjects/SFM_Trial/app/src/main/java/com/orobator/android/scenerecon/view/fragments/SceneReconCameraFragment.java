package com.orobator.android.scenerecon.view.fragments;import android.annotation.SuppressLint;import android.app.Fragment;import android.graphics.Rect;import android.hardware.Camera;import android.os.Build;import android.os.Bundle;import android.os.Environment;import android.util.Log;import android.view.LayoutInflater;import android.view.SurfaceHolder;import android.view.SurfaceView;import android.view.View;import android.view.ViewGroup;import android.widget.Button;import com.orobator.android.scenerecon.R;import com.orobator.android.scenerecon.view.customviews.HorizontalOffsetView;import java.io.File;import java.io.FileOutputStream;import java.io.IOException;import java.util.ArrayList;import java.util.List;/** * Fragment used to take pictures with HorizontalOffsetView */public class SceneReconCameraFragment extends Fragment {    private static final String TAG = "SceneReconCameraFragment";    private Camera mCamera;    private SurfaceView mSurfaceView;    private HorizontalOffsetView mHorizontalOffsetView;    private View mProgressContainer;    private String mSceneName = null;    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {        @Override        public void onPictureTaken(byte[] data, Camera camera) {            // Create a file name            String filename = mSceneName + mHorizontalOffsetView.getCurrentRotations() + ".jpg";            File pictureFile = getOutputMediaFile(filename);            // Save the jpeg data to disk            FileOutputStream outputStream = null;            boolean success = true;            try {                outputStream = new FileOutputStream(pictureFile);                outputStream.write(data);            } catch (Exception e) {                Log.e(TAG, "Error writing to file " + filename, e);                success = false;            } finally {                try {                    if (outputStream != null) {                        outputStream.close();                    }                } catch (Exception e) {                    Log.e(TAG, "Error closing to file " + filename, e);                    success = false;                }            }            if (success) {                Log.i(TAG, "JPEG saved at " + filename);            }            // We're done saving the picture, so get rid of the progress            // indicator            mProgressContainer.setVisibility(View.INVISIBLE);            if (mCamera != null) {                // Camera stops preview when taking picture                mCamera.startPreview();            }        }    };    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {        @Override        public void onShutter() {            // Display the progress indicator            mProgressContainer.setVisibility(View.VISIBLE);        }    };    /**     * A simple algorithm to get the largest size available. For a more robust     * version, see CameraPreview.java in the ApiDemos sample app from Android     */    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes) {        Camera.Size bestSize = sizes.get(0);        int largestArea = bestSize.width * bestSize.height;        for (Camera.Size s : sizes) {            int area = s.width * s.height;            if (area > largestArea) {                bestSize = s;                largestArea = area;            }        }        return bestSize;    }    /**     * Creates a media file in the {@code Environment.DIRECTORY_PICTURES}     * directory. The directory is persistent and available to other     * applications like gallery.     *     * @return A file object pointing to the newly created file.     */    public File getOutputMediaFile(String filename) {        // To be safe, you should check that the SDCard is mounted        // using Environment.getExternalStorageState() before doing this.        if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {            return null;        }        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(                Environment.DIRECTORY_PICTURES), "Scene Recon" + File.separator + mSceneName);        // This location works best if you want the created images to be shared        // between applications and persist after your app has been uninstalled.        // Create the storage directory if it does not exist        if (!mediaStorageDir.exists()) {            if (!mediaStorageDir.mkdirs()) {                Log.e(TAG, "failed to create directory");                return null;            }        }        // Create a media file name        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + filename);        return mediaFile;    }    @Override    public void onActivityCreated(Bundle savedInstanceState) {        super.onActivityCreated(savedInstanceState);        mSceneName = getActivity().getIntent().getStringExtra(SetupFragment.SCENE_NAME);    }    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {        View view = inflater.inflate(R.layout.fragment_camera_view, parent, false);        final Button takePictureButton = (Button) view.findViewById(R.id.take_picture_button);        takePictureButton.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                // take the picture                // indicate the picture angle has been taken                if (mCamera != null) {                    mCamera.takePicture(mShutterCallback, null, mPictureCallback);                }            }        });        mHorizontalOffsetView = (HorizontalOffsetView) view.findViewById(R.id.camera_horizontalOffSetView);        mHorizontalOffsetView.registerSensorListener();        mHorizontalOffsetView.registerEventListener(new HorizontalOffsetView.EventListener() {            @Override            public void onAligned() {                takePictureButton.setEnabled(true);            }            @Override            public void onUnAligned(double offset) {                // Don't allow users to take crooked photos                takePictureButton.setEnabled(false);            }        });        mSurfaceView = (SurfaceView) view.findViewById(R.id.camera_surfaceView);        SurfaceHolder holder = mSurfaceView.getHolder();        assert holder != null;        holder.addCallback(new SurfaceHolder.Callback() {            @Override            public void surfaceCreated(SurfaceHolder holder) {                // Tell the camera to use this surface as its preview area                try {                    if (mCamera != null) {                        mCamera.setPreviewDisplay(holder);                    }                } catch (IOException ioe) {                    Log.e(TAG, "Error setting up preview display", ioe);                }            }            @Override            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {                if (mCamera == null) return;                // The surface has changed size; update the camera preview size                Camera.Parameters parameters = mCamera.getParameters();                Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes());                parameters.setPreviewSize(s.width, s.height);                s = getBestSupportedSize(parameters.getSupportedPictureSizes());                parameters.setPictureSize(s.width, s.height);                mCamera.setParameters(parameters);                try {                    mCamera.startPreview();                } catch (Exception e) {                    Log.e(TAG, "Could not start preview", e);                    mCamera.release();                    mCamera = null;                }            }            @Override            public void surfaceDestroyed(SurfaceHolder holder) {                // Can no longer display on this surface, so stop the preview.                if (mCamera != null) {                    mCamera.stopPreview();                }            }        });        mProgressContainer = view.findViewById(R.id.scene_recon_camera_progressContainer);        mProgressContainer.setVisibility(View.INVISIBLE);        return view;    }    @Override    public void onPause() {        super.onPause();        mHorizontalOffsetView.unregisterSensorListener();        // Make sure to release the camera to save resources        if (mCamera != null) {            mCamera.release();            mCamera = null;        }    }    @SuppressLint("NewApi")    @Override    public void onResume() {        super.onResume();        mHorizontalOffsetView.registerSensorListener();        int firstCameraAvailableID = 0;        mCamera = Camera.open(firstCameraAvailableID);        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {            mCamera.enableShutterSound(true);        }        Camera.Parameters parameters = mCamera.getParameters();        List<String> focusModes = parameters.getSupportedFocusModes();        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);            if (parameters.getMaxNumFocusAreas() > 0) { // check that focus areas are supported                List<Camera.Area> focusAreas = new ArrayList<Camera.Area>();                Rect areaRect = new Rect(-100, -100, 100, 100); // specify an area in center of image                focusAreas.add(new Camera.Area(areaRect, 750)); // weight = 75%                parameters.setFocusAreas(focusAreas);            }        }        if (parameters.getMaxNumMeteringAreas() > 0) {            Rect areaRect = new Rect(-100, -100, 100, 100);            List<Camera.Area> meterAreas = new ArrayList<Camera.Area>();            meterAreas.add(new Camera.Area(areaRect, 750));            parameters.setMeteringAreas(meterAreas);        }        mCamera.setParameters(parameters);    }}