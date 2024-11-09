import { useState } from "react";
import AddMovie from "./components/addMovie";
import Movies from "./components/Movies";
import MoviesSeen from "./components/MoviesSeen";

function App() {
  const [movies, setMovies] = useState([
    {
      id: 1,
      title: "filme 1",
      inSeen: false,
    },
    {
      id: 2,
      title: "filme 2",
      inSeen: false,
    },
    {
      id: 3,
      title: "filme 3",
      inSeen: false,
    },
  ]);

  const [moviesSeen, setMoviesSeen] = useState([]);

  function onMovieClick(movieId) {
    const newMovies = movies.filter((movie) => movie.id !== movieId);
    const movieToMove = movies.find((movie) => movie.id === movieId);
    if (movieToMove) {
      movieToMove.inSeen = true;
      setMoviesSeen([...moviesSeen, movieToMove]);
    }
    setMovies(newMovies);
  }

  function onDeleteMovieClick(movieId) {
    const newMovies = movies.filter((movie) => movie.id !== movieId);
    setMovies(newMovies);
  }

  function onDeleteMovieSeenClick(movieId) {
    const newMoviesSeen = moviesSeen.filter((movie) => movie.id !== movieId);
    setMoviesSeen(newMoviesSeen);
  }

  return (
    <div className="w-screen h-screen bg-black flex justify-center p-6">
      <div className="w-[800px]">
        <h1 className="text-3xl font-bold text-center text-white">CINELIST</h1>
        <p className="text-center text-white p-11">
          Lorem ipsum dolor sit amet consectetur adipisicing elit. Repellat
          ducimus corrupti sint, magni iste error nihil ipsum nobis aperiam ea
          laudantium facilis quisquam voluptatibus ratione? Voluptatibus illum
          perferendis officiis facere.
        </p>
        <AddMovie />
        <div className="">
          <Movies
            movies={movies}
            onMovieClick={onMovieClick}
            onDeleteMovieClick={onDeleteMovieClick}
          />
          <MoviesSeen
            moviesSeen={moviesSeen}
            onDeleteMovieSeenClick={onDeleteMovieSeenClick}
          />
        </div>
        <h1 className="text-white">SOBRE NÓS</h1>
        <div>
          <img src="" alt="Avatar" class="w-16 h-16 rounded-full"></img>

          <img src="" alt="Avatar" class="w-16 h-16 rounded-full"></img>
        </div>
      </div>
    </div>
  );
}

export default App;
