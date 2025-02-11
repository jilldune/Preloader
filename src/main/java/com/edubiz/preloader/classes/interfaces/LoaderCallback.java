package com.edubiz.preloader.classes.interfaces;

import javafx.scene.Parent;

@FunctionalInterface
public interface LoaderCallback {
    Parent createLoaderUI();
}