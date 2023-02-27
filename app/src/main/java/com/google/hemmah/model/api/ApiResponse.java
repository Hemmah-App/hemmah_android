package com.google.hemmah.model.api;

import com.google.hemmah.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiResponse {
    private String timeStamp;
    private int statusCode;
    private String status;
    private String reason;
    private String message;
    private String developerMessage;
    private data data;
    //    private JsonObject data;

//    public <T> T getData(Class<T> clazz){
//        return new GsonBuilder().create().fromJson(this.data,clazz);
//    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class data{
        private User user;
        private String token;
    }

}

