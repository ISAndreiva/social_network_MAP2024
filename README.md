My project for APM at UBB. <br>
This was how I learned Java and JavaFX and how to integrate high-level langauges with database solutions, in particular here I used PostgreSQL.

Some images from how it looks, while making it I decided to make it look as goofy as possible so the "graphic design is my passion" meme was the base of all UI/UX decisions:
![](doc/sn-login.png)<br>
On loading the application presents the login page which also allows signing up

![](doc/sn-loggedin.png)<br>
Once logged in the user can see their friends list, search for new friends, view their own profile or logout.

![](doc/friend-requests-notification.png)<br>
If the user has any pending incoming friend requests the friend requests button appears as a notification and upon pressing it the user is presnted with a list of friend requests which they can either accept or deny

![](doc/self-profile-and-update.png)<br>
Each profile has an assciated image which is stored in the database and can be uploaded from the profile update page

![](doc/profile-friend.png)<br>
The profile page of friends allows the user to open the conversation window, for users which are not friends of the user it instead allows to send a friend request.

![](doc/message.png)<br>
The conversation page, allows message replies and updates in real time.