# ChatterBoxAndroid

ChatterBox is an android Chat Application built in Java. 

## Features of Chatterbox: 
1. User Login and Registration using Firebase Authentication.
2. Options for Account Settings including changing current status and profile picture with a crop feature before uploading.
3. Ability to view all the current users of the app.
4. Sending requests for chat to other users
5. Accepting or rejecting requests received from users.
6. Chat exchange

## Technical Details:
ChatterBox uses Firebase Authentication for managing users in an efficient and secure manner. We have implemented user registration and login using the Firebase Authentication Email/Password sign-in method. We have built our custom user interface that utilizes different firebase authentication functions to authenticate and authorize the user according to the current user state. To track the current state of the Firebase Auth instance we have used different listeners. Through these listeners, the user will be prompted to create a new account if they haven’t registered yet. In case they already have an account, they will be asked to sign-in. If the user is already signed-in they will be taken to their account directly, where they can chat with friends added to their account. 
A Firebase User has a fixed set of basic properties which are stored in the project's user database. We cannot add other properties to the Firebase User object directly; instead, we can store the additional properties in our Firebase Realtime Database. For ChatterBox, we had to store some more details of our registered users such as profile pictures, thumbnails, status and friend requests sent and received by the users. For storing these details we have used Firebase Realtime Database.
We are also storing the messages exchanged between users in the Firebase Realtime Database.

