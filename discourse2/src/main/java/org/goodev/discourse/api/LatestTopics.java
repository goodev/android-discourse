package org.goodev.discourse.api;

import org.goodev.discourse.api.data.Category;
import org.goodev.discourse.api.data.Topic;
import org.goodev.discourse.api.data.TopicPoster;
import org.goodev.discourse.api.data.User;

import java.util.ArrayList;
import java.util.HashMap;

public class LatestTopics {
    private final HashMap<Long, ArrayList<TopicPoster>> mPosters = new HashMap<Long, ArrayList<TopicPoster>>();

    /**
     * topic's posters info, index is the topic id
     */
    // private final SparseArrayCompat<ArrayList<Poster>> mPosters = new SparseArrayCompat<ArrayList<Poster>>();
    /**
     * categories, the index is the category id
     */
    // private final SparseArrayCompat<Category> mCategories = new SparseArrayCompat<LatestTopics.Category>();
    private final HashMap<Long, Category> mCategories = new HashMap<Long, Category>();
    private final HashMap<Long, User> mUsers = new HashMap<Long, User>();
    private final HashMap<Long, Topic> mTopics = new HashMap<Long, Topic>();
    private final ArrayList<Topic> mTopicsData = new ArrayList<Topic>();
    /**
     * users , the inex is the user id
     */
    // private final SparseArrayCompat<User> mUsers = new SparseArrayCompat<LatestTopics.User>();
    private TopicList mTopicList;

    public LatestTopics() {

    }

    public LatestTopics(LatestTopics lt) {
        mTopicList = lt.mTopicList;
        mCategories.putAll(lt.mCategories);
        mPosters.putAll(lt.mPosters);
        mTopics.putAll(lt.mTopics);
        mTopicsData.addAll(lt.mTopicsData);
        mUsers.putAll(lt.mUsers);
    }

    public Topic getTopic(int index) {
        return mTopicsData.get(index);
    }

    public void putPoster(Long topicId, TopicPoster p) {
        ArrayList<TopicPoster> posters = mPosters.get(topicId);
        if (posters == null) {
            posters = new ArrayList<TopicPoster>();
            mPosters.put(topicId, posters);
        }
        posters.add(p);
    }

    public ArrayList<TopicPoster> getPosters(Long id) {
        return mPosters.get(id);
    }

    public void putCategory(Category c) {
        mCategories.put(c.id, c);
    }

    public Category getCategory(Long id) {
        return mCategories.get(id);
    }

    public void putUser(User data) {
        mUsers.put(data.id, data);
    }

    public User getUser(Long id) {
        return mUsers.get(id);
    }

    public void putTopic(Topic data) {
        mTopicsData.add(data);
        mTopics.put(data.id, data);
    }

    public Topic getTopic(Long id) {
        return mTopics.get(id);
    }

    public int getTopicSize() {
        return mTopicsData.size();
    }

    public TopicList getTopicList() {
        return mTopicList;
    }

    public void setTopicList(TopicList data) {
        mTopicList = data;
    }

    public void addAll(LatestTopics lt) {
        mTopicList = lt.mTopicList;
        mCategories.putAll(lt.mCategories);
        mPosters.putAll(lt.mPosters);
        mTopics.putAll(lt.mTopics);
        mTopicsData.addAll(lt.mTopicsData);
        mUsers.putAll(lt.mUsers);
    }

    public void clear() {
        mTopicList = null;
        mCategories.clear();
        mPosters.clear();
        mTopics.clear();
        mTopicsData.clear();
        mUsers.clear();
    }

    public static class TopicList {
        public boolean can_create_topic;
        public String more_topics_url;
        public String draft;
        public String draft_key;
        public long draft_sequence;
    }

}
