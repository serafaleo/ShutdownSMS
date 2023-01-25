# ShutdownSMS
Simple Android app that listen to a predefined SMS message and shuts down the device when it arrives. REQUIRES ROOT! This is the very first Android app I wrote, and I'm not fluent in Java, so there is probably some things I could have done better. It is not to the extent of this README file to teach you how to root your device. You should do it by yourself at your own risk. All I can do is give you a hint: search for Magisk!

## Download
You can grab an APK at the Releases section of this repostitory: https://github.com/serafaleo/ShutdownSMS/releases

## How to use it
After installing the app, you have to open it to grant root privileges and allow read SMS. It begins in the OFF state and the default SMS shutdown message is randomly generated so that each device has a unique message, avoiding one being able to shutdown other device. It is recommended that you change this message to an easier to remember one.

## Currently supported languages
- English
- Portuguese

## TODO
- Implement PIN and/or biometric lock to open the app, making it harder for someone to turn it off or change the shutdown message.
