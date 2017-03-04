package com.example.dell.moviesapp;

/**
 * Created by DELL on 27-Feb-17.
 */

public class Utilities {

    public static String genreName (double id) {
        String name = "";

        if (id == 28){
            name = "Action";
        } else if (id == 12) {
            name = "Adventure";
        } else if (id == 16) {
            name = "Animation";
        } else if (id == 35) {
            name = "Comedy";
        } else if (id == 80) {
            name = "Crime";
        } else if (id == 99) {
            name = "Documentary";
        } else if (id == 18) {
            name = "Drama";
        } else if (id == 10751) {
            name = "Family";
        } else if (id == 14) {
            name = "Fantasy";
        } else if (id == 36) {
            name = "History";
        } else if (id == 27) {
            name = "Horror";
        } else if (id == 10402) {
            name = "Music";
        } else if (id == 9648) {
            name = "Mystery";
        } else if (id == 10749) {
            name = "Romance";
        } else if (id == 878) {
            name = "Science Fiction";
        } else if (id == 10770) {
            name = "TV Movie";
        } else if (id == 53) {
            name = "Thriller";
        } else if (id == 10752) {
            name = "War";
        } else if (id == 37) {
            name = "Western";
        }
        return name;
    }

    public static String getLanguage (String lang) {
        String name = "";

        if (lang.equals("en")) {
            name = "English";
        } else if (lang.equals("ja")) {
            name = "Japanese";
        } else if (lang.equals("ar")) {
            name = "Arabic";
        } else if (lang.equals("zh")) {
            name = "Chinese";
        } else if (lang.equals("fr")) {
            name = "French";
        } else if (lang.equals("de")) {
            name = "German";
        } else if (lang.equals("hi")) {
            name = "Hindi";
        } else if (lang.equals("it")) {
            name = "Italian";
        } else if (lang.equals("es")) {
            name = "Spanish";
        }
        return name;
    }
}
