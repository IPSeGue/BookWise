package com.mobdeve.s12.bookwise;

public class Reviews {
    private String  comment;
    private int rating;

    public Reviews (String comment, int rating){
        this.comment = comment;
        this.rating = rating;
    }

    public String getComment(){ return comment;}

    public int getRating(){ return rating;}
}
