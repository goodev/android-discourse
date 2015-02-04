package org.goodev.discourse.api;

import org.goodev.discourse.api.data.Post;
import org.goodev.discourse.api.data.Topic;
import org.goodev.discourse.api.data.TopicDetails;
import org.goodev.discourse.api.data.User;

import java.util.ArrayList;

/**
 * 单个topic 数据 GET /t/4116/posts.json?post_ids[]=15557&post_ids[]=15558&post_ids[]=15560
 */
public class TopicStream {
    public Topic mTopic;
    public Long[] mPostStreams;
    public ArrayList<Post> mPosts = new ArrayList<Post>();
    public TopicDetails mTopicDetails;

    public TopicStream() {

    }

    public TopicStream(TopicStream ts) {
        mTopic = new Topic(ts.mTopic);
        if (ts.mPostStreams != null) {
            mPostStreams = new Long[ts.mPostStreams.length];
            System.arraycopy(ts.mPostStreams, 0, mPostStreams, 0, mPostStreams.length);
        }

        mPosts = new ArrayList<Post>(ts.mPosts);
        if (ts.mTopicDetails != null) {
            mTopicDetails = new TopicDetails(ts.mTopicDetails);
        }
    }

    public int getTopicLinksSize() {
        if (mTopicDetails == null) {
            return 0;
        }
        if (mTopicDetails.links == null) {
            return 0;
        }
        return mTopicDetails.links.length;
    }

    public void setPost(int index, Post data) {
        if (index >= mPosts.size()) {
            return;
        }
        mPosts.set(index, data);
    }

    public int getPosterSize() {
        if (mTopicDetails == null) {
            return 0;
        }
        if (mTopicDetails.participants == null) {
            return 0;
        }
        return mTopicDetails.participants.length;
    }

    public User[] getPosters() {
        if (mTopicDetails == null) {
            return null;
        }
        return mTopicDetails.participants;
    }

    public void setPostSreams(Long[] stream) {
        mPostStreams = stream;
    }

    public void addPosts(ArrayList<Post> posts) {
        mPosts.addAll(posts);
    }

    public void addAll(TopicStream data) {
        if (data == null) {
            return;
        }
        if (data.mPostStreams != null) {
            mPostStreams = data.mPostStreams;
        }
        if (data.mTopicDetails != null) {
            mTopicDetails = data.mTopicDetails;
        }
        mPosts.addAll(data.mPosts);
    }

    public void clear() {
        mPostStreams = null;
        mPosts.clear();
    }

    public int getSize() {
        return mPosts.size();
    }

    public Post getPost(int position) {
        return mPosts.get(position);
    }

}
