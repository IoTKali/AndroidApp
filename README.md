# Karport Android App
## Description
Karport is a project of smart parking using Internet of Things. The first isuee it tackles is the lack of knowledge of available spaces. There are a lot of solutions that tried a spot by spot aproach, but due to the cost it becomes unfeasible. So karport uses a zone aproeach, allowing for a good estimate with only a fraction of hardware.

The app is dessigned using the new Material design API.

## Android Activities

### Splash Activity
Shows a brief sight of the logo. Then proceeds to the next activity.
### Pre-Login Activity.
Shows a screen with two options. Sign in, and Sign up. 
### Sign Up Activity
Has input fields for name, Email, Password, a date picker for birthdate, options for legal gender and condition ("pregnant", "handicaped", "none").
Posts to the server and creates a user.
### Vehicle Register Activity
Has input fields for plates, Brand, Model, Year. Posts the new car to the server.
### Sign In Activity
Has input fields for Email, password. Verifies the user validity.
### Main Menu Activity
A simple and streamlined interface. This is important, since it needs to distract the user the least possible, as they may be behind the wheel when using the app.
### Profile Activity
Shows the user information. And the registered vehicles information.
### Find Spot Activity
Using the amount of cars currently registerd by the server. It will suggest a parking zone based on color. Also has the option to select a particular Zone, and get the most suitable options based on preference.
### Save Location Activity
Saves the location where the vehicle is parked, using google maps API.
### Find Vehicle Activity
This activity uses the previously saved location, and shows the position on a map, as well as the user's current position. This allows an easy search of a parked car, it also works everywhere. 
