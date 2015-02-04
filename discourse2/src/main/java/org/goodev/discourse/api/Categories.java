package org.goodev.discourse.api;

import android.util.SparseArray;

import org.goodev.discourse.api.data.Category;
import org.goodev.discourse.api.data.Topic;
import org.goodev.discourse.api.data.User;

import java.util.ArrayList;
import java.util.HashMap;

public class Categories {

    public static final int VIEW_TYPE_TOPIC_ITEM = 0;
    public static final int VIEW_TYPE_CATEGORY_HEADER = 1;
    public static final int VIEW_TYPE_CATEGORY_FOOTER = 2;
    public static final int VIEW_TYPE_LOADING = 3;
    private final HashMap<Long, ArrayList<Topic>> mTopics = new HashMap<Long, ArrayList<Topic>>();
    private final ArrayList<Category> mCategories = new ArrayList<Category>();
    private final HashMap<Long, User> mUsers = new HashMap<Long, User>();
    private final SparseArray<Category> mSparseCategories = new SparseArray<Category>();
    private CategoryList mCategoryList;

    public CategoryList getCategoryList() {
        return mCategoryList;
    }

    public void setCategoryList(CategoryList data) {
        mCategoryList = data;
    }

    public void putCategory(Category c) {
        mCategories.add(c);
    }

    public ArrayList<Category> getCategories() {
        return mCategories;
    }

    public void putUser(User data) {
        mUsers.put(data.id, data);
    }

    public User getUser(Long id) {
        return mUsers.get(id);
    }

    public void putTopic(Long categoryId, Topic data) {
        ArrayList<Topic> topics = mTopics.get(categoryId);
        if (topics == null) {
            topics = new ArrayList<Topic>();
            mTopics.put(categoryId, topics);
        }
        topics.add(data);

    }

    public ArrayList<Topic> getTopic(Long categoryId) {
        return mTopics.get(categoryId);
    }

    public int getSize() {
        mSparseCategories.clear();
        final ArrayList<Category> categories = mCategories;
        if (categories.size() == 0) {
            return 0;
        }
        int size = 0;
        int index = 0;
        final HashMap<Long, ArrayList<Topic>> topicsList = mTopics;
        for (Category c : categories) {
            mSparseCategories.append(size + index, c);
            ArrayList<Topic> topics = topicsList.get(c.id);
            if (topics != null) {
                size += topics.size();
            }
            mSparseCategories.append(size + index + 1, new Category(c, true));
            // category header and footer
            size += 2;
        }
        return size;
    }

    public int getItemType(int position) {
        final ArrayList<Category> categories = mCategories;
        if (categories.size() == 0) {
            return VIEW_TYPE_LOADING;
        }
        Category cat = mSparseCategories.get(position);
        if (cat != null) {
            return cat.isFooter ? VIEW_TYPE_CATEGORY_FOOTER : VIEW_TYPE_CATEGORY_HEADER;
        }

        return VIEW_TYPE_TOPIC_ITEM;

    }

    public Object getItem(int position) {
        final ArrayList<Category> categories = mCategories;
        if (categories.size() == 0) {
            return null;
        }
        Category cat = mSparseCategories.get(position);
        if (cat != null) {
            return cat;
        }

        int size = 0;
        final HashMap<Long, ArrayList<Topic>> topicsList = mTopics;
        for (Category c : categories) {
            size += 1;
            ArrayList<Topic> topics = topicsList.get(c.id);
            if (topics != null) {
                if (size + topics.size() >= position + 1) {
                    Topic t = topics.get(position - size);
                    return t;
                }
                size += topics.size();
            }
            // category header and footer
            size += 1;
        }
        return null;

    }

    public void clear() {
        mCategories.clear();
        mCategoryList = null;
        mSparseCategories.clear();
        mTopics.clear();
        mUsers.clear();
    }

    public static class CategoryList {
        public boolean can_create_topic;
        public boolean can_create_category;
        public String draft;
        public String draft_key;
        public long draft_sequence;
    }

}
