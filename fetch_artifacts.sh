#!/bin/sh

# Manually fetch and edit lwk repository

REPO=https://github.com/Blockstream/lwk-rn
TAG=0.9.1

ANDROID_URL=$REPO/releases/download/$TAG/lwk-android-artifact.zip
curl -L $ANDROID_URL --output lwk-android-artifact.zip
unzip -o lwk-android-artifact.zip -d android
rm -rf lwk-android-artifact.zip

IOS_URL=$REPO/releases/download/$TAG/lwk-ios-artifact.zip
curl -L $IOS_URL --output lwk-ios-artifact.zip
unzip -o lwk-ios-artifact.zip -d LwkRnFramework.xcframework
rm -rf lwk-ios-artifact.zip
