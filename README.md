
# JamOut

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Social app that allows users to build profiles based around their music interests and then be matched with users who have similar interests. 

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Music/Social
- **Mobile:** A lot of music listening happens on the go so a mobile app gives users the capability to update their music interests whenever and wherever.
- **Story:** Allow users to express interest in songs, albums, artists and find others who like similar content.
- **Market:** Anyony who listens to music. The app is meant to help people find others who share similar music tastes and get recommendations based on that.
- **Habit:** Users can update their music interests any time they discover a new artist/song/album. They can also get song recommendations whenever.
- **Scope:** Basic app would involve user to build a profile with their favorite songs, albums, and artists and view other user profiles. The app could be expanded to have more features like posting music reviews.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Users can make a profile
* Users can login
* Users can edit their profile to add new music interests
* Users cans search for other users
* Users can follow other users
* User can see suggested users based on people on the app who have similar interests (determined by similar favorited songs and genres of music)
* Users can post about new songs they find


**Optional Nice-to-have Stories**

* Users can can create lists of songs
* Users can get recommended songs based on what other users with similar taste like

### 2. Screen Archetypes

* Login Screen
   * User can login   
* Profile Screen
   * Users can see their current favorites
   * Users can view who they follow
   * User can logout
* Edit Profile Screen
    * User can update their favorites
* Main Feed
    * User can view posts from people they are following
* Search Screen
    * User can search for other users
    * User can search for songs
    * User can follow/unfollow users

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Profile Screen
* Main Feed
* Share a Song
* Search Screen

**Flow Navigation** (Screen to Screen)

* Login Screen
   * Profile Screen
* Profile Screen
   * Edit Profile Screen
* Search Screen
    * Song Detail Screen
* Main Screen
    * Profile Detail Screen
    * Song Detail Screen

## Wireframes
[Add picture of your hand sketched wireframes in this section]
![](https://i.imgur.com/5mdzloK.jpg)
![](https://i.imgur.com/mOieLF6.jpg)
![](https://i.imgur.com/zLVQw5V.jpg)
![](https://i.imgur.com/0IkRmoy.jpg)




### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 

### Models



#### User
| Property | Type     | Description |
| -------- | -------- | -------- |
| favorite     |Array of song Arrays, album arrays, and artist arrays|Contains information for favorites of the user    |
| username    |String    |The users username   |
| profilePicture     | File     | User's profile picture     |
| followers    | Array     | Array of pointers to users that follow the user    |
| following    | Array    | Array of pointers to users that the user is following     |

#### Song
| Property | Type     | Description |
| -------- | -------- | -------- |
| songName    |String    |Name of song  |
| albumName   |String   |Name of album song is on   |
| artistName   |String     |Name of artist   |
| albumCover   |Image     |Image of album song is on   |


#### Album
| Property | Type     | Description |
| -------- | -------- | -------- |
| albumName   |String   |Name of album  |
| artistName   |String     |Name of artist   |
| albumCover   |Image     |Image of the album cover|

#### Artist
| Property | Type     | Description |
| -------- | -------- | -------- |
| artistName   |String     |Name of artist   |
| artistPicture   |File   |Image of artist  |





### Networking
- Profile Screen
    - (Get) Retrieve all current favorite songs
    - (Get) Retrieve all current favorite artists
    - (Get) Retrieve all current favorite albums
    - (Get) Retrieve all current profiles following user
    - (Get) Retrieve all profiles user is currently following
    - (Create/Post) Change username
    - (Create/Post) Change profile picture
- Search Screen
    - (Get) Retrieve all search results matching search input
    - (Create/Post) Favorite a new song
    - (Create/Post) Favorite a new album
    - (Create/Post) Favorite a new artist
    - (Create/Post) Follow a new user
- Recommended Screen
    - (Create/Post) Favorite a new song
    - (Create/Post) Favorite a new album
    - (Create/Post) Favorite a new artist
