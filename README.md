# Tika
A more sophisticated TODO list

# Description
-------------
Tika allows a user gain extra support from friends to accomplish a collection of set aside goals and this prototype demos the finished product.
Simulation of a list of tasks and activities, stored locally on the android device. This would usually be done on a remote server preferably using firebase.
The initial data displayed within the RecyclerView was prepopulated using a Callback function in the RoomDatabase.
The preferred architecture chosen was MVVM, and Kotlin Coroutines was used to switch between threads whenever the SQLite database was queried.

# Other notable features to be incorporated
-------------------------------------------
1. Add a repository to conceal remote and local storage.
2. Create a schema for storing data on the cloud using firebase.
3. Implement the Task and Support fragments.
4. Tests with Junit, Espresso and Mockito.
5. Use a DI library (Dagger2)
6. Use a SharedPreference file to locally store the users data
