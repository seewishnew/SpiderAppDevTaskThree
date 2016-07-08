# SpiderAppDevTaskThree
##Synopsis

This app gets movie data from OMDbAPI.com in the form of JSON string and stores the data locally in the android device till the time the user deletes it. All the information about the movie, including the movie poster, comes from the website.

##Tests

The user can add new items to the database by searching for it in the search bar. The search bar edit text is initially hidden and can be made to come into view by clicking the search button on the top right corner of the screen. The search button has a toggle functionality and automatically changes icon indicating it's current function.

Upon pressing the search button in the keyboard, or pressing enter in the keyboard, the user is taken to another activity which displays results that match the search string entered from the existing database, if matches are found. Otherwise, it says, Couldn't find in the database, and an indeterminate progressbar pops up, while the information is retrieved from the server in the background. 

If the response is true and a movie was found, it is displayed in the grid view. Otherwise, if no movie was found, a toast message pops up and says the same. The user can try again, and the previous entry will remain in the search bar in order to conserve effort to type the entire search string again.

If there was an error, it says so, and the user has to try again. Again, the previous search string will remain in the search bar, and it will be easier for the user to try again.

In both cases, when the information is queried from the database or when the information is retrieved from the server, the user, upon long-clicking the entry, can see a pop up menu.

If the information was queried from the database, then the user can either choose to view details or to delete the entry. 

If the information was retrieved from the server, the user can either choose to view full details or to add the entry to the database.

Similarly, the delete and add options will be available in the details activity as well, in the hidden options menu.

Upon addition or deletion, the user comes back to the main activity where the updated information will be displayed.

## Note

The images are all retrieved from online each time it is requested. The size of the images will vary and hence there is no way to view it bigger. The current view mode maintains the aspect ratio.

And getting the info about the images will take up some data, though incredibly an small amount. In case it is still downloading, a download symbol is displayed in its place. If an error occured during the image download, the standard error image dowload is displayed. 

