package com.google.hemmah.domain.repository;

public interface HelpCallRepository {
    void makeCall(String token);
    void answerCall();
}
