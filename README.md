#SIGNAL MAP

## INTRODUCTION

As the modern era proceeds, people inevitably tend to keep their smartphones with them all the time, which gives any application to reach out to the masses very easily and impact their lives. Majority of the people who are engaged in some kind of business tend to travel a lot which makes them go in and out of the area in the reach of their corresponding network providers. Because of this, there are constant glitches and cut offs during an ongoing conversation on the phone. The only way to eradicate this constant problem is to help them find a reliable network and that’s what we aim at doing. There have been a lot of cases at airports, railway stations, during travels or even at home where people inadvertently go out of the reachable zone and hence the connection goes down. By the help of our application they could know where exactly they got to be when they want a completely uninterrupted conversation.

The similar problem persists with the Wi-Fi signals perhaps because of the inability of the router to reach to a wider area or due to the various obstacles that come in the way. This leads to interruption in various online facilities and most importantly while making important transactions over the internet. In order to deal with such a scenario the user needs to figure out the location where he/she needs to be to carry on with a reliable connection.

The Google Maps API for Android provides developers with the means to create apps with localization functionality. Version 2 of the Maps API was released at the end of 2012 and it introduced a range of new features including 3D, improved caching, and vector tiles. The proposed app will use Google Maps for Android V2 in conjunction with the Google Places API. The app will present a map to the user, mark their current location and nearby network strength depending on which carrier the user is on, and will update when the user checks in.

Android gives your applications access to the location services supported by the device through classes in the android.location package. The central component of the location framework is the LocationManagersystem service, which provides APIs to determine location and bearing of the underlying device.

## TECHNICAL OVERVIEW

- We developed the android application using Android SDK in Eclipse.
- All the carrier, cellular signal, wifi signal, battery, GPS location data were obtained using androids' built in functions.
- In order to visualize the signal map, we used Google Maps with the use of Google Maps API from Google.
- We used MySQL database to host the data on the server and in case of low battery, we downloaded the data to the users device and store it in SQLite DB.
- We made following pages (Activity) for the application:
  Splash Activity:
    This is the first screen that will show up with just a little animation to inform the user of the app being initialized on the user’s     device.
  Option Screen:
    Option screen is the activity class to which the user is redirected automatically as the timer in the ‘splash activity’ class is         timed out. This activity presents users with the options to either select ‘Wifi’ module or ‘Cellular’ module.
    It just makes use of two buttons and their ‘setOnClickListener’class to redirect accordingly.
  Main Activity:
    It is the activity class that is responsible to handle everything that goes on into the cellular module which ranges from fetching       network data from the device to that of integrating and interacting with the Google Maps.
  MainActivity_WiFi:
    This activity is quite similar to main activity and performs mostly the same functionalities but with specific focus on WiFi rather      than cellular. It is the activity class that is responsible to handle everything that goes on into the WiFi module which ranges from     fetching WiFi data from the device to that of integrating and interacting with the Google Maps.
    As mentioned earlier, the process of fetching data from My SQL database is being done with the help of JSON (Javascript Object           Notation). The json data that is coming from the php files is

  
