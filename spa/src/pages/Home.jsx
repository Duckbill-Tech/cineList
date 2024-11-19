import React, { useState, useEffect } from "react";
import { v4 } from "uuid";
import { useSearchParams } from "react-router-dom";
import NavBar from "../components/NavBar";
import AddMovie from "../components/AddMovie";
import Movies from "../components/Movies";
import MoviesSeen from "../components/MoviesSeen";
import Katerine from "../components/Katerine";
import Nathalie from "../components/Nathalie";
import { fetchMovies, addMovie, deleteMovie } from "../apis/apiHome"; // Importando as funções de apiHome.js

function Home() {
  useEffect(() => {
    document.body.classList.add("bg-black", "text-white");
    return () => {
      document.body.classList.remove("bg-black", "text-white");
    };
  }, []);

  const [searchParams] = useSearchParams();
  const email = searchParams.get("email");

  const [movies, setMovies] = useState([]); // Estado de filmes agora vem da API
  const [moviesSeen, setMoviesSeen] = useState([]);

  // Carrega filmes da API quando o componente for montado
  useEffect(() => {
    fetchMovies()
      .then((data) => setMovies(data))
      .catch((error) => console.error("Erro ao carregar filmes:", error));
  }, []);

  const onMovieClick = (movieId) => {
    const updatedMovies = movies.filter((movie) => movie.id !== movieId);
    const movieToMove = movies.find((movie) => movie.id === movieId);
    if (movieToMove) {
      setMoviesSeen([
        ...moviesSeen,
        { ...movieToMove, comment: "", rating: 0 },
      ]);
    }
    setMovies(updatedMovies);
  };

  const onAddMovieSubmit = (title) => {
    const newMovie = { id: v4(), title, inSeen: false };
    addMovie(newMovie)
      .then((addedMovie) => {
        setMovies([...movies, addedMovie]); // Adiciona o novo filme à lista
      })
      .catch((error) => console.error("Erro ao adicionar filme:", error));
  };

  const onDeleteMovieClick = (movieId) => {
    deleteMovie(movieId)
      .then(() => {
        setMovies(movies.filter((movie) => movie.id !== movieId)); // Remove o filme localmente
      })
      .catch((error) => console.error("Erro ao excluir filme:", error));
  };

  const onDeleteMovieSeenClick = (movieId) => {
    setMoviesSeen(moviesSeen.filter((movie) => movie.id !== movieId));
  };

  const onClearAllMovies = () => setMovies([]);
  const onClearAllMoviesSeen = () => setMoviesSeen([]);

  return (
    <div className="w-screen bg-black flex flex-col">
      <NavBar />

      <div className="w-full max-w-7xl mx-auto bg-black flex justify-center p-6 flex-grow">
        <div className="w-full space-y-6">
          <div id="cinelist" className="text-center">
            <p className="text-white mb-2">Bem vinda/o, {email}</p>
            <p className="w-full gap-6 mx-auto text-white p-4 mt-10 mb-10 max-w-4xl">
              Com o CINELIST, você pode criar sua lista de filmes perfeita! Em
              um único lugar é possível adicionar, gerenciar e acompanhar seus
              filmes favoritos de maneira simples e intuitiva. Organize sua
              lista de filmes a assistir e confira as obras já vistas,
              adicionando comentários e classificando-os conforme sua opinião.
            </p>
          </div>

          <div className="max-w-lg mx-auto mt-10 mb-10">
            <AddMovie onAddMovieSubmit={onAddMovieSubmit} />
          </div>

          <div
            id="listas"
            className="w-full flex max-w-4xl mx-auto gap-6 mt-10 mb-20  justify-center"
          >
            <div className="w-full max-w-md">
              <Movies
                movies={movies}
                onMovieClick={onMovieClick}
                onDeleteMovieClick={onDeleteMovieClick}
                onClearAllMovies={onClearAllMovies}
              />
            </div>
            <div className="w-full max-w-md">
              <MoviesSeen
                moviesSeen={moviesSeen}
                onDeleteMovieSeenClick={onDeleteMovieSeenClick}
                onClearAllMoviesSeen={onClearAllMoviesSeen}
              />
            </div>
          </div>
          <section id="sobreNos" className="mt-32 text-center">
            <div className="w-full max-w-4xl mx-auto px-4">
              <h1 className="text-amber-500 tracking-widest text-lg mb-10 text-left">
                SOBRE NÓS
              </h1>
              <div className="w-full flex max-w-4xl mx-auto gap-6 justify-center">
                <div className="flex mt-4 justify-center gap-4">
                  <div className="flex items-center space-x-2 w-1/2">
                    <img
                      src="./static/sapiens-avatar.png"
                      alt="Avatar Katerine"
                      className="w-32 h-32 rounded-full"
                    />
                    <Katerine />
                  </div>
                  <div className="flex items-center space-x-2 w-1/2">
                    <img
                      src="./static/sapiens-avatar-2.png"
                      alt="Avatar Nathalie"
                      className="w-32 h-32 rounded-full"
                    />
                    <Nathalie />
                  </div>
                </div>
              </div>
            </div>
          </section>
        </div>
      </div>
    </div>
  );
}

export default Home;
