<h1>Android Nanodegree Popular Movies - Part 2</h1>

<b>Project Overview</b>
<p>Welcome back to Popular Movies! In this Second and Final Part, I added additional functionality to the app I built in Part 1.</p>
<p>Recall from Part 1, we developed a User Interface that presented the user with a grid of movie posters, allowed users to change sort order, 
and presented the Movie Details of a selected movie.</p>
<p>These additional features include:
<ul>
  <li>Allowing the user play and share trailers</li>
    <ul>
      <li>in the YouTube app, or</li>
      <li>in a web browser</li>
    </ul>
  <li>Allowing the user to read reviews on a selected movie</li>
  <li>Allowing the user to mark a movie as a favorite in the Movie Details by tapping on the heart</li>
  <li>Allowing the user to not only sort through the Popular and Top-Rated Movies, but also show a list of their Favorite Movies</li>
  <li>Include a better user experience by including the following:</li>
    <ul>
      <li>Pull down to refresh Popular or Top-Rated Movie. If this calls fails, the cached movies will continue to be displayed</li>
      <li>Error handling to be able to better idenitify the issue</li>
      <li>Ensuring there are Movie Trailers before displaying the Share or Watch Trailer buttons</li>
      <li>Offline access to your list of Favorite Movies and their details.</li>
    </ul>
  <li>Finally, creating a database and content provider to store the Movie Details for the Favorite Movie.</li> 
</ul>
</p>
<p><b>How to run the Popular Movies app</b></p>
<p>The Popular Movies app connects to theMovieDB server which displays movie information. To be able to run this app, you will 
need to sign up for a free theMoviesDB api key at https://www.themoviedb.org/documentation/api. Once you have recieved this key, you will 
need to copy and paste your key into the gradle.properties and replace "Insert API Key here" with your key.</p>
<ul>
  <li>Example: API_KEY="Insert API Key here"</li>
</ul>
<p>By completing this, the Popular Movies app will be able to display the most Popular Movies!</p>
<b>Below are screenshots from the app Popular Movies - Part 2:</b>
<p><em>Menu Sort Criteria</em></p>
<p>
<img src="https://github.com/AndreaWolff/AndroidNanoDegreePopularMoviesPart2/blob/master/images/PopularMovies.png" height="450" width="250">
<img src="https://github.com/AndreaWolff/AndroidNanoDegreePopularMoviesPart2/blob/master/images/Top-Rated-Movies.png" height="450" width="250">
<img src="https://github.com/AndreaWolff/AndroidNanoDegreePopularMoviesPart2/blob/master/images/Favorited-Movie-List.png" height="450" width="250">
</p>
<p><em>Movie Details</em></p>
<p>
<img src="https://github.com/AndreaWolff/AndroidNanoDegreePopularMoviesPart2/blob/master/images/Not-Favorited-Movie.png" height="450" width="250">
<img src="https://github.com/AndreaWolff/AndroidNanoDegreePopularMoviesPart2/blob/master/images/Movie_favorited.png" height="450" width="250">
<img src="https://github.com/AndreaWolff/AndroidNanoDegreePopularMoviesPart2/blob/master/images/Share-Movie-Trailer.png" height="450" width="250">
</p>
<p><em>No Movie Trailers and Error Handling</em></p>
<p>
<img src="https://github.com/AndreaWolff/AndroidNanoDegreePopularMoviesPart2/blob/master/images/No-Trailers-Or-Reviews.png" height="450" width="250">
<img src="https://github.com/AndreaWolff/AndroidNanoDegreePopularMoviesPart2/blob/master/images/No-Favorites.png" height="450" width="250">
<img src="https://github.com/AndreaWolff/AndroidNanoDegreePopularMoviesPart2/blob/master/images/Error-Handling.png" height="450" width="250">
</p>
<p><em>Offline Mode</em></p>
<p>
<img src="https://github.com/AndreaWolff/AndroidNanoDegreePopularMoviesPart2/blob/master/images/Offline-Mode-For-Favorites.png" height="450" width="250">
</p>
<p><em>Movie Database</em></p>
<p>
<img src="https://github.com/AndreaWolff/AndroidNanoDegreePopularMoviesPart2/blob/master/images/Screen%20Shot%202018-04-06%20at%208.27.09%20PM.png" height="350" width="700">
<img src="https://github.com/AndreaWolff/AndroidNanoDegreePopularMoviesPart2/blob/master/images/Screen%20Shot%202018-04-06%20at%208.28.30%20PM.png" height="200" width="900">
</p>
<p><b>What I learned after Part 2?</b></p>
<ul>
  <li>How to fetch and display User Reviews and Movie Trailer from the Internet</li>
  <li>How to create an SQLite database and Content Provider</li>
  <li>How to use Data Binding to bind UI components to an Activity, to help remove boilerplate code</li>
  <li>How to set up and run a small set of Unit Tests</li>
  <li>How to achieve a good user experience by including:</li>
    <ul>
      <li>Pull to refresh Movies</li>
      <li>Caching Movies from the Server</li>
      <li>Allowing the user to access their Favorite Movies offline</li>
      <li>Error handling to help inform the user of any issues</li>
    </ul>
</ul>
