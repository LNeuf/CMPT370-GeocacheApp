package com.cmpt370_geocacheapp;

public class Main {
    public static void main(String[] args) {
        CacheDatabase database = new CacheDatabase();
        PhysicalCacheBuilder builder = new PhysicalCacheBuilder(database);
        builder.buildCache("Engineering Building Cache", "Ashton", 1);
        builder.buildPhysicalCache(14.03,18.02,"Easy", "Flat");

        database.getPhysicalCache(1).addComment(new CacheComment("Hello", new User("Ashton", "pizzapops123",1), 1));
        System.out.println(database.getPhysicalCache(1).readComment(1).getCommentBody());
    }
}