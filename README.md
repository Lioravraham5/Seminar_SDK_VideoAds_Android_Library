# VideoAdsLib Library
VideoAdsLib is a easy-to-use library designed for integrating video advertisements into Android applications. It simplifies the process of configuring, displaying, and managing video ads

## Architecture Overview
The project is designed as a comprehensive video advertisement platform consisting of four main components: VideoAdsLib, API Flask Server, MongoDB Database, and an Application. Together, these components provide the infrastructure for uploading, managing, and delivering video advertisements to client applications.

add a scratch

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
Ads Collection: Stores advertisement details such as video links, advertiser links, prices, and tracking metrics (clicks/impressions).
Packages Collection: The Packages Collection stores all ad events for video ads shown in applications using the SDK.

### Application
The application integrates the VideoAdsLib SDK to display video advertisements to users. It fetches ads from the backend, handles ad display within the app, and tracks user interactions such as impressions and clicks.

[![](https://jitpack.io/v/Lioravraham5/Seminar_SDK_VideoAds_Android_Library.svg)](https://jitpack.io/#Lioravraham5/Seminar_SDK_VideoAds_Android_Library)
