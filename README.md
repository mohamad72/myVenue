**Notes:**  
1. I couldn't find any parameter (like page number) for implementing pagination so I downloaded all item in first api call and then in the View Model I send a chunk of data to view with each scroll  
2. In Android Q we need background location permission. but in this app cause of we don't want get new data when app is in background so we skip this permission
  

**Libraries:**  
•	Room: for saving venue and searched location(lat & lng) and join this table with requestId Field and then wrote instrumented test.    
•	Retrofit  
•	Hilt  
•	Timber  
•	Work manager  
•	Easy permission : for location permission  
•	Kotlin Coroutines  

**Features (BDD):**  
**1.**  
**Given:**  
internet is Enable  
**When:**  
user open app  
**Then:**  
get data from foursquare Api with special location & save data to local  
& show data to user  
 
**2.**  
**Given:**  
data exist for current user's location in local  
**When:**  
user open app  
**Then:**  
& show data to user  
  
**3.**  
**Given:**  
app is open  
**When:**  
user changed his location  
**Then:**  
update data  
  
**4.**  
**Given:**  
list is in the end  
**When:**  
user scroll  
**Then:**  
get new data and list become bigger  
  
**5.**  
**Given:**  
app open  
**When:**  
user press on a venue item  
**Then:**  
open detail venue page  
  
**6.**  
**Given:**  
app is open  
**When:**  
net suddenly enabled  
**Then:**  
update list  
  
**7.**  
**Given:**  
net is disabled  
& there is no data for user location in local  
**When:**  
user open app  
**Then:**  
show internet is offline page  
  
**8.**  
**Given:**  
there is data in the local  
**When:**  
data was old  
**Then:**  
delete old data  
  
**9.**  
**Given:**  
signal is weak  
or we don't know the location  
**When:**  
user open app  
**Then:**  
use last location as user's current location  
  

