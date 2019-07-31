GeoQuiz
===========================
Simple Android app to test users on their knowledge of geography

Note: This application is used as a teaching tool in *Big Nerd Ranch Guide to Android Programming.*

# Usage
The application is self-explanatory as far as user interface goes. A true/false question is displayed. 
Users have the option of choosing 'true' or 'false'. After selection, a toast will appear indicating if their choice was correct.
After all questions are answered, a toast will appear showing the user's score.

## Cheating
To add a twist, users have the ability to cheat. 
The cheat button brings them to a second page, where they are asked if they are sure they want to cheat.
If the user accepts, the answer is shown. However, when they go back to answer the question, a toast will appear condemning them for cheating.
They will not receive any points.

# Development
Overall, this is a fairly simple application. The Views used are fundamental and the functions mostly uncomplicated. 
Where things got tricky is when the second activity for cheating was introduced.
At that point, communication between activities was added to the list of things to worry about. 
The correct answer had to be sent from the main activity to the secondary one, and whether or not the user had cheated had to be sent back.
This was doubly complicated when exploits like rotating the screen had to be addressed.

## Rotation Loopholes
Rotating one's screen actually destroys the activity and creates a new one. 
This means that all previous data is lost.
This in turn means that an user can cheat to view an answer, then rotate his screen, and the application would be none the wiser.
The way to close these loopholes is in saving this data into a Bundle called savedInstanceState that persists through activity destruction
and reloading it on re-creation of the activity.