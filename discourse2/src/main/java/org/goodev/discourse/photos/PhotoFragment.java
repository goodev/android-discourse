package org.goodev.discourse.photos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;
import com.lamerman.FileDialog;
import com.lamerman.SelectionMode;

import org.goodev.discourse.App;
import org.goodev.discourse.BuildConfig;
import org.goodev.discourse.R;
import org.goodev.discourse.photos.NetworkPhotoView.OnSetImageBitmap;
import org.goodev.discourse.utils.Tools;
import org.goodev.discourse.utils.Utils;

import java.io.File;
import java.lang.ref.WeakReference;

public class PhotoFragment extends Fragment implements OnSetImageBitmap, OnClickListener, AdListener {
    private static final int REQUEST_CODE = 6384;
    NetworkPhotoView mPhotoView;
    ShareActionProvider mShareActionProvider;
    ShareTask mShareTask;
    SetAsTask mSetAsTask;
    DownloadTask mDownloadTask;
    AdRequest mAdRequest;
    // private String mTitle;
    // private String mDes;
    private String mUrl;
    private String mTempFilePath;
    private AdView mAdView;
    private View mCloseAdView;

    public PhotoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mUrl = args.getString(Utils.EXTRA_URL);
        // mTitle = args.getString(Utils.EXTRA_TITLE);
        // mDes = args.getString(Utils.EXTRA_MSG);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo_layout, container, false);
        mCloseAdView = rootView.findViewById(R.id.title);
        mCloseAdView.setOnClickListener(this);
        // TextView des = (TextView) rootView.findViewById(R.id.des);
        // des.setText(mDes);
        mAdView = (AdView) rootView.findViewById(R.id.adView);
        mAdView.setAdListener(this);
        NetworkPhotoView photoView = (NetworkPhotoView) rootView.findViewById(R.id.photo);
        photoView.setOnSetImageBitmap(this);
        photoView.setErrorImageResId(R.drawable.ic_error);
        photoView.setDefaultImageResId(R.drawable.ic_default);
        photoView.setImageUrl(mUrl, App.getImageLoader());
        mPhotoView = photoView;
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // getActivity().setTitle(mTitle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo, menu);
        // Locate MenuItem with ShareActionProvider
        // MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
        // mShareActionProvider = (ShareActionProvider)
        // item.getActionProvider();

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            if (mShareTask == null) {
                mShareTask = new ShareTask(this, getActivity(), getString(R.string.share_dialog_title));
                mShareTask.execute();
            }
        } else if (item.getItemId() == R.id.action_set_as) {
            if (mSetAsTask == null) {
                mSetAsTask = new SetAsTask(this, getActivity(), getString(R.string.set_as));
                mSetAsTask.execute();
            }
        } else if (item.getItemId() == R.id.action_download) {
            if (mDownloadTask == null) {
                mDownloadTask = new DownloadTask(this, getActivity(), getString(R.string.downlaod));
                mDownloadTask.execute();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void chooserDir() {
        Intent intent = new Intent(getActivity(), FileDialog.class);
        intent.putExtra(FileDialog.START_PATH, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());

        // can user select directories or not
        intent.putExtra(FileDialog.CAN_SELECT_DIR, true);
        intent.putExtra(FileDialog.SELECTION_MODE, SelectionMode.MODE_CREATE);
        // alternatively you can set file filter
        // intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "png" });

        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
                        boolean remember = data.getBooleanExtra(FileDialog.CHECKBOX, false);
                        if (remember) {
                            Tools.setDownloadRemember(getActivity());
                        }
                        Tools.setDownloadFolder(getActivity(), filePath);
                        if (!TextUtils.isEmpty(mTempFilePath)) {
                            if (Tools.copyFile(mTempFilePath, filePath)) {
                                Toast.makeText(getActivity(), R.string.download_success, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), R.string.download_failed, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onImageBitmap(int width, int height) {
        if (width > height && (Tools.isShowBannerAd(getActivity()) || BuildConfig.DEBUG)) {
            if (mAdRequest == null) {
                mAdRequest = new AdRequest();
            }
            mAdView.setVisibility(View.VISIBLE);
            mAdView.loadAd(mAdRequest);
            mCloseAdView.setVisibility(View.VISIBLE);
        } else {
            mCloseAdView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        mAdView.setVisibility(View.GONE);
        mCloseAdView.setVisibility(View.GONE);
    }

    @Override
    public void onDismissScreen(Ad arg0) {

    }

    @Override
    public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
        mCloseAdView.setVisibility(View.GONE);
        mAdView.setVisibility(View.GONE);
    }

    @Override
    public void onLeaveApplication(Ad arg0) {

    }

    @Override
    public void onPresentScreen(Ad arg0) {

    }

    @Override
    public void onReceiveAd(Ad arg0) {
        mAdView.setVisibility(View.VISIBLE);
    }

    class DownloadTask extends PhotoTask {
        public DownloadTask(PhotoFragment fragment, Activity activity, String msg) {
            super(fragment, activity, msg);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            PhotoFragment photo = mPhotoFragment.get();
            Activity activity = mActivity.get();
            if (!TextUtils.isEmpty(result) && photo != null && activity != null) {
                mTempFilePath = result;
                if (!Tools.isRememberDownload(activity)) {
                    photo.chooserDir();
                } else {
                    String dir = Tools.getDownloadFolder(activity);
                    if (Tools.copyFile(result, dir)) {
                        Toast.makeText(activity, R.string.download_success, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity, R.string.download_failed, Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(activity, R.string.download_failed, Toast.LENGTH_LONG).show();
            }
            photo.mDownloadTask = null;
        }
    }

    class PhotoTask extends AsyncTask<Void, Void, String> {
        final WeakReference<PhotoFragment> mPhotoFragment;
        final WeakReference<Activity> mActivity;
        final ProgressDialog mDialog;

        public PhotoTask(PhotoFragment fragment, Activity activity, String msg) {
            mPhotoFragment = new WeakReference<PhotoFragment>(fragment);
            mActivity = new WeakReference<Activity>(activity);
            mDialog = new ProgressDialog(activity);
            mDialog.setMessage(msg);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            PhotoFragment photo = mPhotoFragment.get();
            if (photo == null) {
                return null;
            }
            Drawable drawable = photo.mPhotoView.getDrawable();
            if (drawable instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                String file = Tools.writeBitmapToCacheDir(bitmap);
                return file;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

    class SetAsTask extends PhotoTask {
        public SetAsTask(PhotoFragment fragment, Activity activity, String msg) {
            super(fragment, activity, msg);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            PhotoFragment photo = mPhotoFragment.get();
            Activity activity = mActivity.get();
            if (!TextUtils.isEmpty(result) && photo != null && activity != null) {
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                // intent.setType("image/*");
                intent.setDataAndType(Uri.fromFile(new File(result)), "image/*");
                intent.putExtra("mimeType", intent.getType());
                activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.set_as)));
            } else {
                Toast.makeText(activity, R.string.share_failed, Toast.LENGTH_LONG).show();
            }
            photo.mSetAsTask = null;
        }
    }

    class ShareTask extends PhotoTask {

        public ShareTask(PhotoFragment fragment, Activity activity, String msg) {
            super(fragment, activity, msg);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            PhotoFragment photo = mPhotoFragment.get();
            Activity activity = mActivity.get();
            if (!TextUtils.isEmpty(result) && photo != null && activity != null) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                // emailIntent.setType("text/plain");
                // emailIntent.putExtra(Intent.EXTRA_SUBJECT, photo.mTitle);
                // emailIntent.putExtra(Intent.EXTRA_TITLE, photo.mTitle);
                // emailIntent.putExtra(Intent.EXTRA_TEXT, photo.mDes);
                emailIntent.setType("image/*");
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(result)));
                activity.startActivity(emailIntent);
            } else {
                Toast.makeText(activity, R.string.share_failed, Toast.LENGTH_LONG).show();
            }
            photo.mShareTask = null;
        }
    }
}
