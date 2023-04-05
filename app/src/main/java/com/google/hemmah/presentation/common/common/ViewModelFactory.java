//package com.google.hemmah.presentation.common.common;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.ViewModel;
//import androidx.lifecycle.ViewModelProvider;
//
//import java.util.Map;
//
//import javax.inject.Inject;
//import javax.inject.Provider;
//
//public class ViewModelFactory implements ViewModelProvider.Factory {
//        private final Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModelMap;
//
//        @Inject
//        public ViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModelMap) {
//            this.viewModelMap = viewModelMap;
//        }
//
//        @NonNull
//        @Override
//        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//            Provider<? extends ViewModel> viewModelProvider = viewModelMap.get(modelClass);
//            if (viewModelProvider == null) {
//                throw new IllegalArgumentException("Unknown ViewModel class " + modelClass);
//            }
//            return (T) viewModelProvider.get();
//        }
//    }
//
