package org.goodev.discourse.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.goodev.discourse.ActivityUtils;
import org.goodev.discourse.App;
import org.goodev.discourse.R;
import org.goodev.discourse.api.Api;
import org.goodev.discourse.api.data.Post;
import org.goodev.discourse.api.data.Topic;
import org.goodev.discourse.contentprovider.Provider;
import org.goodev.discourse.ui.dialog.ConfirmDialogFragment;
import org.goodev.discourse.ui.dialog.ConfirmDialogFragment.ConfirmListener;
import org.goodev.discourse.ui.dialog.PreviewDialogFragment;
import org.goodev.discourse.utils.HttpRequest;
import org.goodev.discourse.utils.L;
import org.goodev.discourse.utils.Utils;
import org.goodev.widget.AddLinkFragment;
import org.goodev.widget.AddLinkFragment.OnAddLinkListener;
import org.goodev.widget.Selection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Stack;

import static org.goodev.discourse.database.tables.CategoriesTable.COLOR;
import static org.goodev.discourse.database.tables.CategoriesTable.DESCRIPTION;
import static org.goodev.discourse.database.tables.CategoriesTable.DESCRIPTION_EXCERPT;
import static org.goodev.discourse.database.tables.CategoriesTable.ID;
import static org.goodev.discourse.database.tables.CategoriesTable.NAME;
import static org.goodev.discourse.database.tables.CategoriesTable.SLUG;
import static org.goodev.discourse.database.tables.CategoriesTable.TEXT_COLOR;
import static org.goodev.discourse.database.tables.CategoriesTable.TOPIC_COUNT;
import static org.goodev.discourse.database.tables.CategoriesTable.UID;

/**
 * 标题不得少于15个字符 帖子内容不得少于20个字符
 */
public class EditorFragment extends Fragment implements LoaderCallbacks<Cursor>, ConfirmListener, OnClickListener, OnAddLinkListener {

    private static final int TITLE_MIN_LENGTH = 5;
    private static final int CONTENT_MIN_LENGTH = 5;
    private static final int LOADER_ID_CATEGORY = 0;
    private static final String[] CATEGORY_COLUMNS = new String[]{ID, UID, NAME, COLOR, TEXT_COLOR, SLUG, TOPIC_COUNT, DESCRIPTION, DESCRIPTION_EXCERPT};
    private static final int INDEX_UID = 1;
    private static final int INDEX_NAME = 2;
    private static final int INDEX_COLOR = 3;
    private static final int INDEX_TEXT_COLOR = 4;
    private static final int INDEX_SLUG = 5;
    private static final int INDEX_TOPIC_COUNT = 6;
    /**
     * 引用整个帖子 full:为true， 否则为 full:false
     */
    private static final String QUOTE_POST = "[quote=\"%s, post:%d, topic:%d, full:true\"]\n%s\n[/quote]\n";
    private static final char[] BOLD1 = {'*', '*'};
    private static final char[] BOLD22 = {'*', '*', '*', '*'};
    private static final String KEY_LINK = "key_s1";
    private static final String KEY_UPLOAD = "key_s2";
    private static final String KEY_MD_ACTION_ID = "key_s3";
    private static final String KEY_SECTION = "key_s4";
    private static final String KEY_HIS_TXT = "key_s5";
    private static final String KEY_HIS_SELECTION = "key_s6";
    private static final String KEY_HIS_ACTIONS = "key_s7";
    private static final String DIVIDER = "\n\n----------\n";
    private static final String ENTER = "\n";
    private static final char ENTER_CHAR = '\n';
    private static final char CODE_CHAR = '`';
    private static final String CODE = "`";
    private static final String CODE2 = "``";
    private static final String QUOTE_START = "\n> ";
    private static final String QUOTE_START2 = "\n\n> ";
    private static final String QUOTE = "> ";
    private static final String NUMBER_SIGN = "#";
    private static final String ASTERISK = "*";
    private static final String ASTERISK2 = "**";
    private static final String ASTERISK4 = "****";
    private static final String NUM_LIST = "\n %d. ";
    private static final String BULLETED_LIST = "\n - ";
    private static final String SB = "[";
    private static final String SB2 = "]";
    private static final String MARKDOWN_IMG = "![%s](%s)";
    private final TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            clearHistory();
        }
    };
    private TextView mPostTitle;
    private EditText mTitleET;
    private Spinner mCategoriesSpinner;
    private EditText mContentET;
    private String mTitle;
    private String mContent;
    private int mCategoryIndex;
    private CursorAdapter mCategoryAdapter;
    private Topic mTopic;
    private int mPostNum = -1;
    private String mUsername;
    private boolean mIsEditPost;
    private boolean mIsPrivateMsg;
    private long mPostId = -1;
    private PostTask mPostTask;
    private ProgressFragment mProgressFragment;
    // ------------------------ Markdown 按钮功能实现
    private View mQuoteView;
    private String mPostRaw;
    private int mListIndex;
    /**
     * 记录历史编辑内容
     */
    private Stack<Editable> mHistories = new Stack<Editable>();
    /**
     * 记录历史光标位置
     */
    private Stack<Selection> mHistoriesSelection = new Stack<Selection>();
    private Stack<Integer> mHistoriyActions = new Stack<Integer>();
    /**
     * 记录最近一次操作：加粗、斜体等...
     */
    private int mLastAction;
    private Selection mLastSelection;
    private Selection mLinkSelection;
    private Selection mUploadSelection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mCategoryAdapter = new CategoryAdapter(getActivity(), null);
        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(Utils.EXTRA_TITLE, null);
            mContent = savedInstanceState.getString(Utils.EXTRA_MSG, null);
            mCategoryIndex = savedInstanceState.getInt(Utils.EXTRA_CAT_INDEX, 0);

            restoreMdStatus(savedInstanceState);
        }

        Bundle args = getArguments();
        mTopic = (Topic) args.getSerializable(Utils.EXTRA_OBJ);
        mPostNum = args.getInt(Utils.EXTRA_NUMBER, -1);
        mUsername = args.getString(Utils.EXTRA_NAME);
        mIsEditPost = args.getBoolean(Utils.EXTRA_IS_EDIT_POST, false);
        mIsPrivateMsg = args.getBoolean(Utils.EXTRA_IS_PRIVATE_MSG, false);
        mPostId = args.getLong(Utils.EXTRA_ID);
        if (mIsEditPost) {
        }
    }

    private boolean isReplyTopic() {
        return mTopic != null && mPostNum == -1;
    }

    private boolean isReplyPost() {
        return mPostNum > -1 && mTopic != null;
    }

    private boolean isNewTopic() {
        return mTopic == null && mPostNum == -1;
    }

    private boolean isEditPost() {
        return mIsEditPost;
    }

    private boolean isPrivateMsg() {
        return mIsPrivateMsg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editor, container, false);
        mPostTitle = (TextView) view.findViewById(R.id.post_title);
        mTitleET = (EditText) view.findViewById(R.id.edit_title);
        mTitleET.setText(mTitle);
        mContentET = (EditText) view.findViewById(R.id.edit_content);
        mContentET.setText(mContent);
        mCategoriesSpinner = (Spinner) view.findViewById(R.id.edit_categories_spinner);
        mCategoriesSpinner.setAdapter(mCategoryAdapter);

        setupMarkdownViews(view);
        if (isPrivateMsg()) {
            mCategoriesSpinner.setVisibility(View.GONE);
            mPostTitle.setText(getString(R.string.send_private_msg_title, mUsername));
        } else if (isNewTopic()) {
            mPostTitle.setVisibility(View.GONE);
        } else if (isReplyTopic()) {
            mTitleET.setVisibility(View.GONE);
            mCategoriesSpinner.setVisibility(View.GONE);
            mPostTitle.setText(getString(R.string.reply_topic_title, mTopic.title));
        } else if (isReplyPost()) {
            mTitleET.setVisibility(View.GONE);
            mCategoriesSpinner.setVisibility(View.GONE);
            mPostTitle.setText(getString(R.string.reply_post_title, mPostNum, mUsername));
        } else if (isEditPost()) {
            mTitleET.setVisibility(View.GONE);
            mCategoriesSpinner.setVisibility(View.GONE);
            mPostTitle.setText(getString(R.string.edit_post_title, mPostNum, mUsername));
            mContentET.setText(R.string.loading_post_text);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isNewTopic()) {
            getLoaderManager().initLoader(LOADER_ID_CATEGORY, null, this);
        } else if (isEditPost()) {
            loadingPostData();
        }
        setTitle();
    }

    private void setTitle() {
        int titleId = R.string.title_activity_editor;
        if (isPrivateMsg()) {
            titleId = R.string.private_msg_title;
        } else if (isNewTopic()) {
            titleId = R.string.title_activity_editor;
        } else if (isEditPost()) {
            titleId = R.string.title_activity_edit_post;
        } else if (isReplyPost()) {
            titleId = R.string.title_activity_reply_post;
        } else if (isReplyTopic()) {
            titleId = R.string.title_activity_reply_topic;
        }

        getActivity().setTitle(titleId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String title = mTitleET.getText().toString();
        if (!TextUtils.isEmpty(title)) {
            outState.putString(Utils.EXTRA_TITLE, title);
        }
        String content = mContentET.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            outState.putString(Utils.EXTRA_MSG, content);
        }
        int index = mCategoriesSpinner.getSelectedItemPosition();
        outState.putInt(Utils.EXTRA_CAT_INDEX, index);

        if (mLinkSelection != null) {
            outState.putSerializable(KEY_LINK, mLinkSelection);
        }
        if (mUploadSelection != null) {
            outState.putSerializable(KEY_UPLOAD, mUploadSelection);
        }
        if (mLastSelection != null) {
            outState.putSerializable(KEY_SECTION, mLastSelection);
        }
        if (!mHistories.isEmpty()) {
            outState.putSerializable(KEY_HIS_TXT, mHistories);
        }
        if (!mHistoriesSelection.isEmpty()) {
            outState.putSerializable(KEY_HIS_SELECTION, mHistoriesSelection);
        }
        if (!mHistoriyActions.isEmpty()) {
            outState.putSerializable(KEY_HIS_ACTIONS, mHistoriyActions);
        }
        outState.putInt(KEY_MD_ACTION_ID, mLastAction);

    }

    private void restoreMdStatus(Bundle state) {
        mLinkSelection = (Selection) state.getSerializable(KEY_LINK);
        mUploadSelection = (Selection) state.getSerializable(KEY_UPLOAD);
        mLastSelection = (Selection) state.getSerializable(KEY_SECTION);
        if (state.containsKey(KEY_HIS_ACTIONS)) {
            mHistoriyActions = (Stack<Integer>) state.getSerializable(KEY_HIS_ACTIONS);
        }
        if (state.containsKey(KEY_HIS_TXT)) {
            mHistories = (Stack<Editable>) state.getSerializable(KEY_HIS_TXT);
        }
        if (state.containsKey(KEY_HIS_SELECTION)) {
            mHistoriesSelection = (Stack<Selection>) state.getSerializable(KEY_HIS_SELECTION);
        }
        mLastAction = state.getInt(KEY_MD_ACTION_ID);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_editor, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_preview:
                preview();
                return true;
            case R.id.action_discard:
                discard();
                return true;
            case R.id.action_send:
                saveTopic();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void discard() {
        String msg = getString(R.string.discard_post_msg);
        ConfirmDialogFragment f = ConfirmDialogFragment.newInstance(null, msg);
        f.show(getChildFragmentManager(), "discard_confirm");
    }

    private void saveTopic() {
        if (isEditPost()) {
            saveEditPost();
            return;
        }
        boolean hasTitle = isNewTopic() || isPrivateMsg();
        String title = null;
        boolean titleError = false;
        boolean contentError = false;
        if (hasTitle) {
            title = mTitleET.getText().toString().trim();
            if (title.length() < TITLE_MIN_LENGTH) {
                mTitleET.setError(getResources().getString(R.string.e_title_length_error));
                titleError = true;
            } else {
                mTitleET.setError(null);
            }
        }
        String content = mContentET.getText().toString().trim();
        if (content.length() < CONTENT_MIN_LENGTH) {
            mContentET.setError(getResources().getString(R.string.e_conent_length_error));
            contentError = true;
        } else {
            mContentET.setError(null);
        }
        if (contentError || titleError) {
            return;
        }
        long catId = -1;
        long tid = -1;
        String username = null;
        if (isPrivateMsg()) {
            username = mUsername;
            mPostTask = new PostTask(title, content, Api.ARCHETYPE_PRIVATE_MSG, username);
            mPostTask.execute();
            return;
        } else if (isNewTopic()) {
            Cursor c = (Cursor) mCategoriesSpinner.getSelectedItem();
            if (c != null) {
                catId = c.getLong(INDEX_UID);
            }
        } else {
            tid = mTopic.id;
            catId = mTopic.category_id;
        }

        int postNumber = isReplyPost() ? mPostNum : -1;

        mPostTask = new PostTask(catId, title, content, tid, postNumber);
        mPostTask.execute();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Provider.CATEGORIES_CONTENT_URI, CATEGORY_COLUMNS, null, null, UID + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCategoryAdapter.swapCursor(data);
        if (mCategoryIndex > 0 && mCategoryIndex < mCategoryAdapter.getCount()) {
            mCategoriesSpinner.setSelection(mCategoryIndex);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void loadingPostData() {
        new GetPostTask().execute();
    }

    private void showActonBarProgress() {
        Activity a = getActivity();
        if (a != null) {
            a.setProgressBarIndeterminateVisibility(Boolean.TRUE);
        }
    }

    private void hideActonBarProgress() {
        Activity a = getActivity();
        if (a != null) {
            a.setProgressBarIndeterminateVisibility(Boolean.FALSE);
        }
    }

    private void saveEditPost() {
        String content = mContentET.getText().toString().trim();
        boolean contentError = false;
        if (content.length() < CONTENT_MIN_LENGTH) {
            mContentET.setError(getResources().getString(R.string.e_conent_length_error));
            contentError = true;
        } else {
            mContentET.setError(null);
        }
        if (contentError) {
            return;
        }
        new EditPostTask(content).execute();
    }

    public void finish(String result) {
        Activity a = getActivity();
        if (a == null) {
            return;
        }
        Intent data = new Intent();
        data.putExtra(Utils.EXTRA_MSG, result);
        a.setResult(Activity.RESULT_OK, data);
        a.finish();
    }

    public void editPostError() {
        int res = R.string.saving_post_failure;
        Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfirmClicked() {
        getActivity().finish();
    }

    private void setupMarkdownViews(View view) {
        mQuoteView = view.findViewById(R.id.e_quote_post);
        mQuoteView.setOnClickListener(this);

        view.findViewById(R.id.e_bold).setOnClickListener(this);
        view.findViewById(R.id.e_code).setOnClickListener(this);
        view.findViewById(R.id.e_divider).setOnClickListener(this);
        view.findViewById(R.id.e_header).setOnClickListener(this);
        view.findViewById(R.id.e_italic).setOnClickListener(this);
        view.findViewById(R.id.e_link).setOnClickListener(this);
        view.findViewById(R.id.e_list_ol).setOnClickListener(this);
        view.findViewById(R.id.e_list_ul).setOnClickListener(this);
        view.findViewById(R.id.e_upload).setOnClickListener(this);
        view.findViewById(R.id.e_quote).setOnClickListener(this);
        L.i(".");
        if (!isEditPost() && !isNewTopic()) {
            L.i("...");
            mQuoteView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        if (!mContentET.isFocused()) {
            mContentET.requestFocus();
        }
        mContentET.removeTextChangedListener(mTextWatcher);
        final int id = v.getId();
        if (id != R.id.e_header && id != R.id.e_quote_post && mLastAction == id && getContentSelection().equals(mLastSelection)) {
            undo();
            return;
        }

        pushToStack(id);
        switch (id) {
            case R.id.e_quote_post:
                markdownQuotePost();
                break;
            case R.id.e_link:
                markdownLink();
                break;
            case R.id.e_upload:
                markdownUpload();
                break;
            case R.id.e_quote:
                markdownQuote();
                break;
            case R.id.e_bold:
                markdownBold();
                break;
            case R.id.e_italic:
                markdownItalic();
                break;
            case R.id.e_code:
                markdownCode();
                break;
            case R.id.e_list_ol:
                if (mLastAction == R.id.e_list_ol) {
                    mListIndex++;
                } else {
                    mListIndex = 1;
                }
                markdownNumberedList();
                break;
            case R.id.e_list_ul:
                markdownBulletedList();
                break;
            case R.id.e_header:
                markdownHeader();
                break;
            case R.id.e_divider:
                markdownDivider();
                break;

        }
        mLastAction = id;
        mLastSelection = getContentSelection();
        mContentET.addTextChangedListener(mTextWatcher);
    }

    private void markdownQuotePost() {
        if (mPostRaw == null) {
            new GetPostTask() {

                @Override
                protected void onPostExecute(String result) {
                    hideActonBarProgress();
                    try {
                        JSONObject obj = new JSONObject(result);
                        Post p = Api.getPost(obj);
                        mPostRaw = p.raw;
                        quotePost(mPostRaw);
                    } catch (JSONException e) {
                        int res = R.string.get_post_content_failure;
                        Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
                    }
                }

            }.execute();
        } else {
            quotePost(mPostRaw);
        }
    }

    private void quotePost(String raw) {

        final EditText et = mContentET;
        Editable editable = et.getText();
        int start = et.getSelectionStart();
        editable.insert(start, String.format(QUOTE_POST, mUsername, mPostNum, mTopic.id, mPostRaw));
    }

    private void markdownDivider() {
        final EditText et = mContentET;
        Editable editable = et.getText();
        int start = et.getSelectionStart();
        int diff = DIVIDER.length();
        if (et.hasSelection()) {
            int end = et.getSelectionEnd();
            editable.replace(start, end, DIVIDER);
            et.setSelection(start + diff);
        } else {
            editable.insert(start, DIVIDER);
            et.setSelection(start + diff);
        }
    }

    private void markdownHeader() {
        final EditText et = mContentET;
        Editable editable = et.getText();
        int start = et.getSelectionStart();
        int diff = NUMBER_SIGN.length();
        if (et.hasSelection()) {
            int end = et.getSelectionEnd();
            editable.insert(start, NUMBER_SIGN);
            et.setSelection(start + diff, end + diff);
        } else {
            editable.insert(start, NUMBER_SIGN);
            et.setSelection(start + diff);
        }
    }

    private void markdownBulletedList() {
        final EditText et = mContentET;
        Editable editable = et.getText();
        int start = et.getSelectionStart();
        int diff = BULLETED_LIST.length();
        if (et.hasSelection()) {
            int end = et.getSelectionEnd();
            editable.insert(end, ENTER);
            editable.insert(start, BULLETED_LIST);
            et.setSelection(start + diff, end + diff);
        } else {
            editable.insert(start, BULLETED_LIST);
            et.setSelection(start + diff);
        }
    }

    private void markdownNumberedList() {
        final EditText et = mContentET;
        Editable editable = et.getText();
        int start = et.getSelectionStart();
        String list = String.format(NUM_LIST, mListIndex);
        int diff = list.length();
        if (et.hasSelection()) {
            int end = et.getSelectionEnd();
            editable.insert(end, ENTER);
            editable.insert(start, list);
            et.setSelection(start + diff, end + diff);
        } else {
            editable.insert(start, list);
            et.setSelection(start + diff);
        }
    }

    private void markdownCode() {
        final EditText et = mContentET;
        Editable editable = et.getText();
        int start = et.getSelectionStart();
        if (et.hasSelection()) {
            int end = et.getSelectionEnd();
            editable.insert(end, CODE);
            editable.insert(start, CODE);
            et.setSelection(start + 1, end + 1);
        } else {
            editable.insert(start, CODE2);
            et.setSelection(start + 1);
        }
    }

    private void pushToStack(int id) {
        Editable editable = mContentET.getText();
        mHistories.push(new SpannableStringBuilder(editable));
        L.d(mContentET.getText().toString());
        mHistoriesSelection.push(getContentSelection());
        mHistoriyActions.push(mLastAction);
    }

    private void undo() {
        if (mHistoriyActions.isEmpty()) {
            return;
        }
        mLastAction = mHistoriyActions.pop();
        mLastSelection = mHistoriesSelection.pop();
        Editable editable = mHistories.pop();
        L.d(editable.toString());
        mContentET.setText(editable.toString());
        mContentET.setSelection(mLastSelection.start, mLastSelection.end);

    }

    private Selection getContentSelection() {
        return new Selection(mContentET);
    }

    protected void clearHistory() {
        mHistories.clear();
        mHistoriesSelection.clear();
        mHistoriyActions.clear();
        if (mLastAction != R.id.e_list_ol) {
            mLastAction = 0;
        }
        mLastSelection = null;
    }

    private void markdownBold() {
        final EditText et = mContentET;
        Editable editable = et.getText();
        if (et.hasSelection()) {
            int start = et.getSelectionStart();
            int end = et.getSelectionEnd();
            editable.insert(end, ASTERISK2);
            editable.insert(start, ASTERISK2);
            et.setSelection(start + 2, end + 2);
        } else {
            int start = et.getSelectionStart();
            editable.insert(start, ASTERISK4);
            et.setSelection(start + 2);
        }
    }

    private void markdownItalic() {
        final EditText et = mContentET;
        Editable editable = et.getText();
        if (et.hasSelection()) {
            int start = et.getSelectionStart();
            int end = et.getSelectionEnd();
            editable.insert(end, ASTERISK);
            editable.insert(start, ASTERISK);
            et.setSelection(start + 1, end + 1);
        } else {
            int start = et.getSelectionStart();
            editable.insert(start, ASTERISK2);
            et.setSelection(start + 1);
        }
    }

    private void markdownQuote() {
        final EditText et = mContentET;
        Editable editable = et.getText();
        int start = et.getSelectionStart();
        String quoteStart = null;
        if (start == 0 || editable.charAt(start - 1) == ENTER_CHAR) {
            quoteStart = QUOTE_START;
        } else {
            quoteStart = QUOTE_START2;
        }
        int diff = quoteStart.length();
        if (et.hasSelection()) {
            int end = et.getSelectionEnd();
            editable.insert(end, ENTER);
            editable.insert(start, quoteStart);
            et.setSelection(start + diff, end + diff);
        } else {
            editable.insert(start, quoteStart);
            et.setSelection(start + diff);
        }
    }

    private void markdownLink() {
        final EditText et = mContentET;
        Editable editable = et.getText();
        mLinkSelection = new Selection(et);
        CharSequence des = null;
        if (!mLinkSelection.isEmpty()) {
            des = editable.subSequence(mLinkSelection.start, mLinkSelection.end);
        }
        AddLinkFragment.newInstance(des).show(getChildFragmentManager(), "add_link_tag");
    }

    private void insertMarkdownLink(String des, String url) {
        final EditText et = mContentET;
        Editable editable = et.getText();
        int start = et.getSelectionStart();
        int desL = des.length();
        int sbL = 1;
        // 当弹出对话框的时候， 文本的选择状态会消失。
        if (mLinkSelection.isEmpty()) {
            editable.insert(start, SB + des + SB2 + "(" + url + ")");
        } else {
            start = mLinkSelection.start;
            int end = mLinkSelection.end;
            editable.replace(start, end, SB + des + SB2 + "(" + url + ")");
            // editable.insert(start, SB);
            // editable.insert(start + desL + sbL, SB2);
            // editable.insert(start + desL + sbL + sbL, "(" + url + ")");
            et.setSelection(start + sbL, start + sbL + desL);
        }
    }

    private void markdownUpload() {
        final EditText et = mContentET;
        Editable editable = et.getText();
        mUploadSelection = new Selection(et);
        CharSequence des = null;
        if (!mUploadSelection.isEmpty()) {
            des = editable.subSequence(mUploadSelection.start, mUploadSelection.end);
        }
        AddLinkFragment.newInstance(des, AddLinkFragment.MD_IMG).show(getChildFragmentManager(), "add_link_tag");
    }

    private void insertMarkdownPicture(String des, String url) {
        final EditText et = mContentET;
        Editable editable = et.getText();
        int start = et.getSelectionStart();
        int desL = des.length();
        int sbL = 1;
        // 当弹出对话框的时候， 文本的选择状态会消失。
        if (mUploadSelection.isEmpty()) {
            editable.insert(start, String.format(MARKDOWN_IMG, des, url));
        } else {
            start = mUploadSelection.start;
            int end = mUploadSelection.end;
            editable.replace(start, end, String.format(MARKDOWN_IMG, des, url));
            // editable.insert(start, SB);
            // editable.insert(start + desL + sbL, SB2);
            // editable.insert(start + desL + sbL + sbL, "(" + url + ")");
            et.setSelection(start + sbL, start + sbL + desL);
        }
    }

    @Override
    public void add(int type, String des, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        boolean isLink = type == AddLinkFragment.MD_URL;
        if (isLink && TextUtils.isEmpty(des)) {
            des = url;
        }

        if (isLink) {
            insertMarkdownLink(des, url);
        } else if (type == AddLinkFragment.MD_IMG) {
            insertMarkdownPicture(des, url);
        }
    }

    // TODO --------------- priview
    private void preview() {
        PreviewDialogFragment.newInstance(mContentET.getText().toString()).show(getChildFragmentManager(), "preview_tag");
    }

    class PostTask extends AsyncTask<Void, Void, String> {

        private final static String ARCHETYPE = "archetype";
        private final static String CATEGORY = "category";
        private final static String RAW = "raw";
        private final static String TITLE = "title";
        private final static String TOPIC_ID = "topic_id";
        private final static String TARGET_USERNAMES = "target_usernames";
        private final static String REPLY_TO_POST_NUMBER = "reply_to_post_number";

        private final long mCategoryId;
        private final String mTitle;
        private final String mContent;
        private final String mArchetype;
        private final long mReplyId;
        private final int mPostNum;
        private String mUsername;

        public PostTask(long categoryId, String title, String content) {
            this(categoryId, title, content, Api.ARCHETYPE_REGULAR, -1, -1);
        }

        public PostTask(long categoryId, String title, String content, long replyId, int postNum) {
            this(categoryId, title, content, Api.ARCHETYPE_REGULAR, replyId, postNum);
        }

        public PostTask(String title, String content, String archetype, String username) {
            this(-1, title, content, archetype, -1, -1);
            mUsername = username;
        }

        public PostTask(long categoryId, String title, String content, String archetype, long replyId, int postNum) {
            L.i("posts %d title %s content %s", categoryId, title, content);
            this.mCategoryId = categoryId;
            this.mTitle = title;
            this.mContent = content;
            this.mArchetype = archetype;
            this.mReplyId = replyId;
            mPostNum = postNum;
        }

        @Override
        protected String doInBackground(Void... params) {
            L.d("%s -- %s ", mArchetype, mUsername);
            try {
                String url = App.getSiteUrl() + Api.POSTS;
                HttpRequest hr = HttpRequest.post(url);
                HttpURLConnection connection = hr.getConnection();
                if (App.isLogin()) {
                    App.getCookieManager().setCookies(connection);
                }
                hr.form(ARCHETYPE, mArchetype).form(RAW, mContent);
                if (mCategoryId != -1) {
                    hr.form(CATEGORY, String.valueOf(mCategoryId));
                }
                if (!TextUtils.isEmpty(mTitle)) {
                    hr.form(TITLE, mTitle);
                }
                if (mReplyId != -1) {
                    hr.form(TOPIC_ID, String.valueOf(mReplyId));
                }
                if (mPostNum != -1) {
                    hr.form(REPLY_TO_POST_NUMBER, String.valueOf(mPostNum));
                }
                if (mUsername != null) {
                    hr.form(TARGET_USERNAMES, mUsername);
                }
                int code = hr.code();
                L.i("%s star code %d ", url, code);
                String body = hr.body();
                L.i(body);
                return body;
            } catch (Exception e) {
                L.e(e);
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            L.i("posts topic 1");
            mProgressFragment = new ProgressFragment();
            Bundle args = new Bundle();
            args.putString(Utils.EXTRA_MSG, getString(R.string.sending_post));
            mProgressFragment.setArguments(args);
            mProgressFragment.show(getFragmentManager(), "progress");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            L.i("posts topic 2");
            getFragmentManager().beginTransaction().remove(mProgressFragment).commit();
            mProgressFragment = null;

            long topicId = -1;
            String topicSlug = null;
            try {
                JSONObject topic = new JSONObject(result);
                if (topic.has(Api.K_errors)) {
                    try {
                        JSONArray errors = topic.getJSONArray(Api.K_errors);
                        Toast.makeText(getActivity(), errors.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if (topic.has(Api.K_topic_id)) {
                    topicId = topic.getLong(Api.K_topic_id);
                }
                if (topic.has(Api.K_topic_slug)) {
                    topicSlug = topic.getString(Api.K_topic_slug);
                }

                // {"errors":["Title is too short (minimum is 15 characters)","Title is invalid; try to be a little more descriptive"]}

                ActivityUtils.startTopicActivity(getActivity(), topicSlug, topicId);
                getActivity().finish();
            } catch (Exception e) {
                Toast.makeText(getActivity(), R.string.create_post_error, Toast.LENGTH_LONG).show();
                L.e(e, "create posts error: " + result);
            }
        }
    }

    class CategoryAdapter extends CursorAdapter {
        LayoutInflater mLayoutInflater;
        Context mContext;

        public CategoryAdapter(Context context, Cursor c) {
            super(context, c, false);
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return mLayoutInflater.inflate(R.layout.editor_category_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView category = (TextView) view.findViewById(R.id.category_name);
            String name = cursor.getString(INDEX_NAME);
            category.setText(name);
            // String bgColor = cursor.getString(INDEX_COLOR);
            // String textColor = cursor.getString(INDEX_TEXT_COLOR);
            // Utils.setCategoryView(category, name, bgColor, textColor);
        }

        public void bindDropDownView(View view, Context context, Cursor cursor) {
            TextView category = (TextView) view.findViewById(R.id.category_name);
            TextView topicCount = (TextView) view.findViewById(R.id.category_topic_count);
            String name = cursor.getString(INDEX_NAME);
            String bgColor = cursor.getString(INDEX_COLOR);
            String textColor = cursor.getString(INDEX_TEXT_COLOR);
            Utils.setCategoryView(category, name, bgColor, textColor);
            long count = cursor.getLong(INDEX_TOPIC_COUNT);
            topicCount.setText(mContext.getString(R.string.editor_category_count, count));
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if (mDataValid) {
                mCursor.moveToPosition(position);
                View v;
                if (convertView == null) {
                    v = newDropDownView(mContext, mCursor, parent);
                } else {
                    v = convertView;
                }
                bindDropDownView(v, mContext, mCursor);
                return v;
            } else {
                return null;
            }
        }

        @Override
        public View newDropDownView(Context context, Cursor cursor, ViewGroup parent) {
            return mLayoutInflater.inflate(R.layout.editor_category_dropdown_item, parent, false);
        }

    }

    class GetPostTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showActonBarProgress();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String url = App.getSiteUrl() + String.format(Api.GET_POST, mPostId);
                HttpRequest hr = HttpRequest.get(url);
                HttpURLConnection connection = hr.getConnection();
                if (App.isLogin()) {
                    App.getCookieManager().setCookies(connection);
                }
                int code = hr.code();
                L.i("%s star code %d ", url, code);
                String body = hr.body();
                L.i(body);
                return body;
            } catch (Exception e) {
                L.e(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideActonBarProgress();
            try {
                JSONObject obj = new JSONObject(result);
                Post p = Api.getPost(obj);
                mContentET.setText(p.raw);
            } catch (JSONException e) {
                int res = R.string.get_post_content_failure;
                Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
            }
        }

    }

    class EditPostTask extends AsyncTask<Void, Void, String> {
        private static final String RAW_PARAM = "post[raw]";
        private final String mRaw;

        public EditPostTask(String raw) {
            mRaw = raw;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String url = App.getSiteUrl() + String.format(Api.GET_POST, mPostId);
                HttpRequest hr = HttpRequest.put(url);
                HttpURLConnection connection = hr.getConnection();
                if (App.isLogin()) {
                    App.getCookieManager().setCookies(connection);
                }
                hr.form(RAW_PARAM, mRaw);
                int code = hr.code();
                L.i("%s star code %d ", url, code);
                String body = hr.body();
                return body;
            } catch (Exception e) {
                L.e(e);
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            L.i("posts topic 1");
            mProgressFragment = new ProgressFragment();
            Bundle args = new Bundle();
            args.putString(Utils.EXTRA_MSG, getString(R.string.sending_post));
            mProgressFragment.setArguments(args);
            mProgressFragment.show(getFragmentManager(), "progress");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            L.i("posts topic 2");
            getFragmentManager().beginTransaction().remove(mProgressFragment).commit();
            mProgressFragment = null;
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.has(Api.K_errors)) {
                    try {
                        JSONArray errors = obj.getJSONArray(Api.K_errors);
                        Toast.makeText(getActivity(), errors.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if (obj.has(Api.K_post)) {
                    finish(result);
                } else {
                    editPostError();
                }
            } catch (JSONException e) {
                editPostError();
            }
        }

    }
}
