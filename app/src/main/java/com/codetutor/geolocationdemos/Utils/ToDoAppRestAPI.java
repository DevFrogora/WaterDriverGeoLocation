package com.codetutor.geolocationdemos.Utils;

public interface ToDoAppRestAPI {

    String baseLocalHostUrl = "http://192.168.179.131:8080/dotolist/webapi";
    String baseRemoteUrl = "http://todolistmobileapp-env.ap-south-1.elasticbeanstalk.com/webapi";

    String registerAuthor="/authors";

    String login = registerAuthor+"/login";
}
