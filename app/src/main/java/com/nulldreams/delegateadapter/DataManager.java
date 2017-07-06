package com.nulldreams.delegateadapter;

import android.os.Handler;

import com.nulldreams.delegateadapter.model.Status;
import com.nulldreams.delegateadapter.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by gaoyunfei on 2017/6/29.
 */

public class DataManager {

    private static DataManager sManager = new DataManager();

    public static DataManager getInstance () {
        return sManager;
    }

    private User[] users = {
            new User(R.drawable.img1, "Jack", "Jack slow f**k"),
            new User(R.drawable.img2, "The Beatles", "Yesterday"),
            new User(R.drawable.img3, "Michael Jackson", "Earth Song"),
            new User(R.drawable.img4, "蒼井 そら", "复仇者之死")
    };

    private String[] contents = {
        "New Sugar deal negotiated with Mexico is a very good one for both Mexico and the U.S. Had no deal for many years which hurt U.S. badly.",
        "Every one of us is special! Watch the trailer debut here.",
        "Font Map, an #AI Experiment that uses machine learning to help designers see relationships across 750 web fonts.",
        "I'm that actor in some of the movies you liked and some you didn't. Sometimes I'm in pretty good shape, other times I'm not. Hey, you gotta live, you know?"
    };

    private DataManager () {

    }

    public void getStatus (final OnStatusListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Status> statuses = new ArrayList<Status>();
                int size = new Random().nextInt(5) + 1;
                for (int i = 0; i < size; i++) {
                    Status status = new Status(users[i % users.length], contents[i % contents.length]);
                    statuses.add(status);
                }
                listener.onGet(statuses);
            }
        }, 5000);
    }

    public User getMe () {
        return new User(R.drawable.me, "boybeak", "对于未来的爱，我还是非常精彩");
    }

    public interface OnStatusListener {
        public void onGet (List<Status> statuses);
    }

}
