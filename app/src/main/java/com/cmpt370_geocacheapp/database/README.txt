- database is stored locally on the android phone, for now

- 4 types: Geocache, User, Comment, RatingReview

- I am treating users as uniquely identifiable by their username,
  which can be changed

- I only added minimal fields required to do the testing.
  its easy to add more, just edit one of the 4 @Entity classes

- see DatabaseTest in the java/com/cmpt370_geocacheapp (androidTest)
  to see how to read/write/update/delete stuff. really easy to use

- Lastly, in order to use this in the actual app, I think you need
  to create a seperate thread from the main app for 
  accessing the database
