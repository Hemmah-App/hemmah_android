package com.google.hemmah.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.hemmah.domain.model.User;

public interface UserController {
    default void sendUserIntent(Context context, Class<?> cls, User user) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.USER_INTENT_TAG, user);
        context.startActivity(intent);
    }
    default User receiveUserIntent(Activity activity){
        Bundle bundle = activity.getIntent().getExtras();
        return bundle.getParcelable(Constants.USER_INTENT_TAG);
    }

}
