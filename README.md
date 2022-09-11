# AppFactoryWIthPersistence
This app represents a test task for Android developers by AppsFactory.

# Project Description
This app implementation seeks to demostrate the use of best practices, tools and architecture that plaques Android development today. It is implemented 
to showcase how the MVVM architecture can be leveraged along with best practices for developing Android applications. It also demontrates the use of 
popular libraries such as Retrofit, Pagin 3, LiveData, ROOM, Coroutines and other jetpack components and patterns across various layers.

# Features of the APP

- The Home/Favorite page displaying Albums stored locally on the device
- A details page that allows the user to see the details of a particular album (artist, tracks, duration, etc)
- A Find an artist search page that allows the user to search for artists with search results displayed in the form of a list. 
- Clicking on a search result item opens another screen view showing the top albums of the Artist clicked on from the Search page.
The list of albums is displayed in the form of a grid.
- From the Albums page. each item can be saved or deleted from local storage by tapping on the favorites icon associated with each item. Also, taping on an album from this screen opens up the details page and automatically saves this album locally.
- From the Favorites page, the user can also delete the album from local storage by tapping on the favorites icon. If an item is removed, the list if adjected.

## Tools and Libraries Used

- [Kotlin](https://developer.android.com/kotlin) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - Executing code asynchronously. Coroutines is a rich library for coroutines developed by JetBrains. It contains a number of high-level coroutine-enabled primitives 
that this guide covers, including launch, async and others
- [Room](https://developer.android.com/jetpack/androidx/releases/room) - Room Persistence Library on Android
- [Retrofit](https://github.com/square/retrofit) - A type-safe HTTP client for Android and Java.
- [Paging 3](https://developer.android.com/jetpack/androidx/releases/paging) - Library helps you load and display small chunks of data at a time.
Loading partial data on demand reduces usage of network bandwidth and system resources
- [MVVM](https://github.com/square/retrofit)
- [Hilt](https://developer.android.com/jetpack/androidx/releases/hilt) - Built on Dagger for Dependency Injection in Android.
- [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation) - To manage inApp navigations between screens
- [Glide](https://github.com/bumptech/glide) - An image loading and caching library for Android focused on smooth scrolling.
- [databinding](https://developer.android.com/jetpack/androidx/releases/databinding) - Declaratively bind observable data to UI elements.
- [livedata](https://developer.android.com/topic/libraries/architecture/livedata)- Build data objects that notify views when the underlying database changes.
- [OkHttp](https://developer.android.com/jetpack/androidx/releases/databinding) - An HTTP+HTTP/2 client for Android and Java applications
- [gson](https://github.com/google/gson) -  to deserialize JSON to data Objects
