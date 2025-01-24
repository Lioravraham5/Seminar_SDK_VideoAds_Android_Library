# VideoAdsLib Library
VideoAdsLib is a easy-to-use library designed for integrating video advertisements into Android applications. It simplifies the process of configuring, displaying, and managing video ads

## Architecture Overview
The project is designed as a comprehensive video advertisement platform consisting of four main components: VideoAdsLib, API Flask Server, MongoDB Database, and an Application. Together, these components provide the infrastructure for uploading, managing, and delivering video advertisements to client applications.

<img src="https://github.com/user-attachments/assets/7b7afdbb-72dd-4428-b0f8-9f3038891d4b" alt="Architecture Overview" style="width: 75%; height: 75%;">

### VideoAdsLib (Android Library)
- Provides a ready-to-use SDK for developers to integrate video advertisements into their apps.
- Handles ad display logic, interaction tracking (clicks/impressions), and communication with the Flask server to fetch ads and update ads events

### API Flask Server
Acts as the backend, exposing RESTful endpoints to manage advertisements.

**Main Routes:**
- **Upload Ad:** Adds a new ad to the database.
- **Get Random Ad:** Retrieves a random ad for display.
- **Update Ad:** Modifies specific fields of an ad.
- **Upload Ad event:** Records a new ad event in the database after the ad is displayed within the application.
- **Fetch All Ads:** Lists all stored ads.
- **Delete Ad:** Removes an ad by ID

### Database (MongoDB)
- **Ads Collection:** Stores advertisement details such as video links, advertiser links, prices, and tracking metrics (clicks/impressions).
- **Packages Collection:** The Packages Collection stores all ad events for video ads shown in applications using the SDK.

### Application
The application integrates the VideoAdsLib SDK to display video advertisements to users. It fetches ads from the backend, handles ad display within the app, and tracks user interactions such as impressions and clicks.

## VideoAdsLib
### Features
- **Ad Fetching:** The library fetches video ads from a remote server using a <ins>Retrofit API</ins>.
- **Ad Display:** Video ads are shown in a full-screen dialog using <ins>ExoPlayer</ins> for playback.
- **Event Tracking:** The library tracks key user interactions, like ad views and clicks, and sends this data back to the server via Retrofit for analytics
- **Video Ad Customization:** Developers can configure the ad's behavior, such as the delay before showing the close button and whether the ad should be muted by default through customizable    parameters when initializing the ad.

### Setup
Step 1. Add it in your root `build.gradle` at the end of repositories:
```
allprojects {
    repositories {
        // other repositories
        maven { url = uri("https://jitpack.io" )}
    }
}
```

Step 2. Add the dependency:
```
dependencies {
  implementation("com.github.Lioravraham5:Seminar_SDK_VideoAds_Android_Library:1.0.0")
}
```

Step 3: In the `build.gradle` (app-level) file, set the `compileSdk` to 35:
        
Updating to compileSdk 35 is required because several dependencies (like androidx.activity and media3-exoplayer) need it to compile. Using
```
android {
    compileSdk = 35
}
```

### Usage:

1) **Initialize VideoAdsManager:**
```
VideoAdsManager videoAdsManager = new VideoAdsManager("your.package.name");
```

2) **Set Close Button Delay (Optional):** default - 5 seconds delay
```
videoAdsManager.setCloseButtonDelay(6000); // 6-seconds delay
```

3) **Set Ad Completion Listener (Optional):** Set a listener to handle Ad completion
```
videoAdsManager.setAdClosedCallback(() -> {
    // Your code when the ad is closed
    
});
```

4) **Display an Ad:**
```
videoAdsManager.startVideoAd(fragmentManager, muteAd); // Pass 'true' to mute ads or false to unmute ad
```
 - `fragmentManager`: The `FragmentManager` instance used to display the ad as a dialog.
 - `muteAd`: A boolean flag to indicate whether the ad should play muted
     - `true`: The ad will be muted.
     - `false`: The ad will play with sound.       


[![](https://jitpack.io/v/Lioravraham5/Seminar_SDK_VideoAds_Android_Library.svg)](https://jitpack.io/#Lioravraham5/Seminar_SDK_VideoAds_Android_Library)
