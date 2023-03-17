# Administrative Interface


Backend:

Tests:
On all of the requests and their proper entry to the database
Requests:
-SimpleRequest
	Tests are done that mimic a simple request. The data fields are two strings, a message and a title. It is ensured that this class is called properly and it can be added to the database. It functions properly
-SimpleComRequest
	SimpleRequest but simpler. Rather than sending in two strings as a json object as the request, it only does one. There is only a comment to obtain rather than a title and message
-StructuredResponse
	Tests and ensures that a StructuredResponse is correctly created and utilized. Checks that structuredresponse object is adding its fields properly 
-DataRow 
	Tests to ensure that a DataRow object constructor is made properly and remains constant throughout its use

ALL OF THE BACKEND TESTS WORK PROPERLY

Frontend:

Utilizes the testing library from react to test the functionalities of the system. 
In addition,
- AddLike
	Tests that clicking the like button adds a like to a post
- AddDislike
	Tests that clicking the like button adds a dislike to a post
- AddPost
	Ensures that adding a post properly adds the post to the table
- AddComment
	Ensures that adding a comment properly adds a comment to a given post
- ClearForm
	Ensures that when clear form is clicked there are no values in the textbox fields 
- ShowElements
	Ensures that all elements are properly shown

ALL FRONTEND TESTS FUNCTION

Mobile:

Utilizes Dart
- DataRecieved
	Tests that the mobile app can correctly recieve the necessary data to display messages
- GoogleOAuth
	Ensures that mobile app can correctly connect to google ouath
- JSON Parsing
	Ensures that app can effectively parse the data from the given JSON files

Utilizes Flutter Widget Tests
- CounterAt0
	Ensures that counter starts at 0 in flutter app
- Increments
	Ensures that the counter increments when selected

ALL MOBILE TESTS FUNCTIONING

Admin:
	Tests the database queries to ensure proper use
- getComment
	Ensures that the getComment call works properly by comparing the expected outcome to that recieved by the query
-addLike
	Checks to make sure that you can add a Like to a post and the like count will increment
-addDislike
	Checks to make sure that you can add a dislike to a post and the dislike count will increment
-updateComment
	Ensures that a comment that has been updated does so propely
-getProfile
	Ensures that the expected profile is the one recieved

ALL ADMIN TESTS ARE FUNCTIONAL



