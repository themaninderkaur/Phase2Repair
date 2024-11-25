# Social Media App

## Instructions
In order to compile the project, you need to type javac into the terminal followed by the java file name and .java. An example of this is javac TestFile.java. After you enter this line, the driver will be compiled and ready to run. Next, you must type java followed by the same file name to execute the program. An example of this is java TestFile. Following this, the program will run and you should see the input screen in the terminal below the line you just entered. Please follow the menus and input what you'd like to execute based on the program's offerings.

## Contributions
Maninder - entire database.java and the test cases. Worked on the blcokedUser methods and assisted in the server creation

Emily - created User.java and Friends, directMessage methods for the interface. Also did the signup and login in server.

Justin - Created the interfaces for User.java

Adi - created and implemented logic for Server.java and client.java

## Classes
User- The user class holds all the contructers and methods related to the user. This includes, creating a user, logging in, viewing your own user. The User object will also have multiple attributes including:
- user_id: Unique identifier for each user.
- username: Unique username for the user.
- pass: Password for user authentication.
- email: Unique email address for the user.
- bio: A short string (up to 150 charecters) for user expression.
- created_at: Timestamp of when the user account was created.
- message_id: Unique identifier for each message.
- sender_id: ID of the user sending the message.
- receiver_id: ID of the user receiving the message.
- content: The text content of the message.
- created_at: Timestamp of when the message was sent.
- is_read: Boolean indicating whether the message has been read.

## Class: User.java
### Methods (that have been implemented):
User() - Instantiates a user class
- username is set to ""
- password is set to ""

User(String username, String password, String email) 
- username set to username
- password set to password
- email set to email
- user count increased by one

getPassword() - gets the password from the user

setPassword() - sets the password from the user

getUsername() - gets the user for the user

setUsername() - sets the user for the user

boolean login() - returns true if login works, false if login doesn't. allows user three attempts to login by matching whatever inputted username with the password. if either username doesn't exist or password doesn't match, system allows user to know.

void signUp() - allows a new user to sign up with specific username & password requirements. if successfuly signed up, user/password combo will be save into the 'accountInfo.txt' file.

boolean findUser() - a private method to find the user by going through the file accountInfo.txt using the scanner class.

sendMessage (User sender, User recipient, String message); - sends a message from sender to recipetent. returns true if successful, false if not

deleteMessage(String message, User username); - deletes specified message if found

blockUser(User sender, String username); - has username added to sender's blocked list
** returns true if done, false if not.

restricted(boolean restricted) - sets restricted to parameter.

addFriend(String user, String friendUser); - adds a friend based on the username inputted
** returns true if done, false if not

blockFriend(String user, String blockedUser) - blocks a friend based on the username inputted
** returns true if done, false if not

removeFriend(String user, String friendUser) - removes a friend based on the username inputted
** returns true if done, false if not

## Database.java
We have created a database.java which will only be implemnted once. The 2D array database file table works like an excel spreadsheet. Each column is assigned a variable value (user_id, profile_pic, etc.), and each row corresponds with each of the user's created and currenly created within our social media platform. We have created example implmentations within the data files to showcase how varibles will be pushed. Creation of the database was made by java 2D arraylists, where the testcases were also ran on a backup database. 

