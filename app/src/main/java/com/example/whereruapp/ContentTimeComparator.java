package com.example.whereruapp;

import java.util.Comparator;

class ContentTimeComparator implements Comparator<Content> {
    @Override
    public int compare(Content c1, Content c2) {
        return (int) (c1.getPostTime() - c2.getPostTime());
    }
}
