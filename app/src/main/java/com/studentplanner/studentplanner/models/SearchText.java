package com.studentplanner.studentplanner.models;

public class SearchText{
    private String searchText;

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public SearchText(String searchText) {
        this.searchText = searchText;
    }
    public SearchText(){

    }

    public String getSearchText() {
        return searchText;
    }
}
