package com.cmpt370_geocacheapp.model;

public class Main {
    /*
    * This main class is just for further testing to determine if the builder methods for the builder design patterns work.
    */
    public static void main(String[] args) {
        CacheDatabase database = new CacheDatabase();
        PhysicalCacheBuilder builder = new PhysicalCacheBuilder(database);
        User author = new User("Ashton", "pizzapops123",1);
        builder.buildCache("Engineering Building Cache", author, 1);
        builder.buildPhysicalCache(14.03,18.02,1, 1, 2);

        database.getPhysicalCache(1).addComment(new CacheComment("Hello", author,1));
        System.out.println(database.getPhysicalCache(1).readComment(1).getCommentBody());
    }
}