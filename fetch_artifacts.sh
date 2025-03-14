#!/bin/sh

# Manually fetch and edit lwk repository

REPO=https://github.com/Blockstream/lwk-rn
TAG=0.9.0

ANDROID_URL=$REPO/releases/download/$TAG/lwk-android-artifact.zip
wget $ANDROID_URL
unzip -o lwk-android-artifact.zip -d android
rm -rf lwk-android-artifact.zip

IOS_URL=$REPO/releases/download/$TAG/lwk-ios-artifact.zip
wget $IOS_URL
unzip -o lwk-ios-artifact.zip -d LwkRnFramework.xcframework
rm -rf lwk-ios-artifact.zip
