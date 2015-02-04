package org.goodev.discourse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import org.goodev.discourse.api.data.Topic;
import org.goodev.discourse.ui.EditorFragment;
import org.goodev.discourse.ui.dialog.ConfirmDialogFragment;
import org.goodev.discourse.ui.dialog.ConfirmDialogFragment.ConfirmListener;
import org.goodev.discourse.utils.Utils;

public class EditorActivity extends FragmentActivity implements ConfirmListener {

    private static final String FRAGMENT_TAG = "editor_frag";
    EditorFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_editor);
        if (savedInstanceState == null) {
            mFragment = new EditorFragment();
            Bundle args = new Bundle();
            Intent intent = getIntent();
            Topic t = (Topic) intent.getSerializableExtra(Utils.EXTRA_OBJ);
            int number = intent.getIntExtra(Utils.EXTRA_NUMBER, -1);
            String username = intent.getStringExtra(Utils.EXTRA_NAME);
            if (t != null) {
                args.putSerializable(Utils.EXTRA_OBJ, t);
            }
            if (number != -1) {
                args.putInt(Utils.EXTRA_NUMBER, number);
            }
            if (username != null) {
                args.putString(Utils.EXTRA_NAME, username);
            }
            long id = intent.getLongExtra(Utils.EXTRA_ID, -1);
            if (id != -1) {
                args.putLong(Utils.EXTRA_ID, id);
            }
            boolean isEditPost = intent.getBooleanExtra(Utils.EXTRA_IS_EDIT_POST, false);
            if (isEditPost) {
                args.putBoolean(Utils.EXTRA_IS_EDIT_POST, true);
                if (id == -1) {
                    Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            boolean isPrivateMsg = intent.getBooleanExtra(Utils.EXTRA_IS_PRIVATE_MSG, false);
            if (isPrivateMsg) {
                args.putBoolean(Utils.EXTRA_IS_PRIVATE_MSG, true);
            }
            mFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mFragment, FRAGMENT_TAG).commit();
        } else {
            mFragment = (EditorFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        }
        setupActionBar();
    }

    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                finish();
                // NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_discard:
                discard();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editor, menu);
        return true;
    }

    private void discard() {
        String msg = getString(R.string.discard_post_msg);
        ConfirmDialogFragment f = ConfirmDialogFragment.newInstance(null, msg);
        f.show(getSupportFragmentManager(), "discard_confirm");
    }

    @Override
    public void onBackPressed() {
        discard();
    }

    @Override
    public void onConfirmClicked() {
        finish();
    }
}
