package com.cmpt370_geocacheapp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import android.content.Context;

import androidx.room.*;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.*;
import org.junit.runner.RunWith;

import com.cmpt370_geocacheapp.database.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {
    private UserDao userDao;
    private GeocacheDao geocacheDao;
    private AppDatabase db;
    private CommentDao commentDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        geocacheDao = db.geocacheDao();
        userDao = db.userDao();
        commentDao = db.commentDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    /* asserts that:
        - all basic CRUD operations work with the geocaches
          - should generalize to the other types as well
     */
    @Test
    public void testCRUD() {
        Geocache c = new Geocache();
        c.latitude = 2F;
        c.longitude = 3F;

        // insert it twice, should auto-generate unique ids 1 & 2
        c.id = 0L;
        geocacheDao.insertAll(c);
        geocacheDao.insertAll(c);
        List<Geocache> queryResult = geocacheDao.getAll();
        // is it in the database?
        assertThat(queryResult.size(), equalTo(2));
        assertThat(queryResult.get(0).longitude, equalTo(3F));
        // did they get assigned ids?
        assertThat(queryResult.get(0).id, equalTo(1L));
        assertThat(queryResult.get(1).id, equalTo(2L));

        // update the 2nd copy with a new latitude
        c.id = 2;
        c.latitude = 90F;
        geocacheDao.updateAll(c);
        queryResult = geocacheDao.getAll();
        // did its latitude get updated?
        assertThat(queryResult.get(1).latitude, equalTo(90F));

        // delete the 2nd copy
        geocacheDao.deleteAll(c);
        queryResult = geocacheDao.getAll();
        // has it been deleted?
        assertThat(queryResult.size(), equalTo(1));
        assertThat(queryResult.get(0).latitude, equalTo(2F));
    }

    /* asserts that:
    - the many-to-one associations are preserved between
      comments-per-user, and comments-per-geocache
      - should generalize to ratings-per-user, and ratings-per-geocache as well
 */
    @Test
    public void testManyToOne() {
        // the user
        User u = new User();
        u.username = "fungi2000";
        u.password = "abc123";
        userDao.insertAll(u);
        u = userDao.getAll().get(0);

        // the geocache
        Geocache g = new Geocache();
        g.longitude = 180F;
        g.latitude = -90F;
        geocacheDao.insertAll(g);
        g = geocacheDao.getAll().get(0);

        // this comment belongs to: same geocache, same username
        Comment c1 = new Comment();
        c1.contents = "comment 1 fungi2000";
        c1.userUsername = u.username;
        c1.geocacheId = g.id;
        commentDao.insertAll(c1);

        // this comment belongs to: a different geocache
        Comment c2 = new Comment();
        c2.contents = "comment 2 fungi2000";
        c2.userUsername = u.username;
        c2.geocacheId = g.id + 1;
        commentDao.insertAll(c2);

        // this comment belongs to: a different username
        Comment c3 = new Comment();
        c3.contents = "hi my name is mario_fan123";
        c3.userUsername = "mario_fan123";
        c3.geocacheId = g.id;
        commentDao.insertAll(c3);

        // this comment belongs to: neither. different username and geocache.
        Comment c4 = new Comment();
        c4.contents = "random comment";
        c4.userUsername = "random username";
        c4.geocacheId = g.id + 1;
        commentDao.insertAll(c4);

        // check that all 4 comments inserted
        List<Comment> commentQuery = commentDao.getAll();
        assertThat(commentQuery.size(), equalTo(4));

        // user fungi2000 has exactly two comments: c1, c2
        List<Comment> fungi2000Comments = UserDao.getComments(userDao, u);
        assertThat(fungi2000Comments.size(), equalTo(2));
        assertThat(containsComments(fungi2000Comments, c1, c2), equalTo(true));

        // the geocache has exactly two comments: c1, c3
        List<Comment> geocacheComments = GeocacheDao.getComments(geocacheDao, g);
        assertThat(fungi2000Comments.size(), equalTo(2));
        assertThat(containsComments(geocacheComments, c1, c3), equalTo(true));

    }

    private boolean containsComments(List<Comment> comments, Comment... keys) {
        for (Comment k : keys) {
            if (!containsComment(comments, k))
                return false;
        }
        return true;
    }

    private boolean containsComment(List<Comment> comments, Comment key) {
        for (Comment c : comments) {
            if (    c.contents.equals(key.contents) &&
                    c.geocacheId == key.geocacheId &&
                    c.userUsername.equals(key.userUsername)
            )
                return true;
        }
        return false;
    }
}

