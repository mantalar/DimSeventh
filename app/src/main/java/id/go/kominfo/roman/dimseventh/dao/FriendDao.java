package id.go.kominfo.roman.dimseventh.dao;

import java.util.List;

import id.go.kominfo.roman.dimseventh.model.Friend;

public interface FriendDao {
    long insert(Friend friend);
    void update(Friend friend);
    void delete(int id);

    Friend getAFriendById(int id);
    List<Friend> getAllFriends();
}
