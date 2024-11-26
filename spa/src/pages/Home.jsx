import React, { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import NavBar from "../components/NavBar";
import AddMovie from "../components/AddMovie";
import Movies from "../components/Movies";
import MoviesSeen from "../components/MoviesSeen";
import Katerine from "../components/Katerine";
import Nathalie from "../components/Nathalie";
import { getAllFilmes, createFilme, deleteFilme }from "../../service/FilmeService";


function Home() {
  useEffect(() => {
    document.body.classList.add("bg-black", "text-white");
    return () => {
      document.body.classList.remove("bg-black", "text-white");
    };
  }, []);

  const [searchParams] = useSearchParams();
  const nome = searchParams.get("nome");

  const [movies, setMovies] = useState([]);
  const [moviesSeen, setMoviesSeen] = useState([]);

  useEffect(() => {
    const fetchMovies = async () => {
      try {
        const filmes = await getAllFilmes();
  
        const filmesNaoCompletados = filmes.filter((filme) => !filme.completedAt);
        const filmesCompletados = filmes.filter((filme) => filme.completedAt);
  
        setMovies(filmesNaoCompletados);
        setMoviesSeen(filmesCompletados);
      } catch (error) {
        console.error("Erro ao buscar filmes:", error);
      }
    };
  
    fetchMovies();
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

  const onAddMovieSubmit = async (titulo) => {
    try {
      const newMovie = await createFilme({ titulo });
      setMovies((prevMovies) => [...prevMovies, newMovie]);
    } catch (error) {
      console.error("Erro ao adicionar filme:", error);
    }
  };
  
  const onDeleteMovieClick = async (movieId) => {
    try {
      await deleteFilme(movieId);
  
      setMovies((prevMovies) => prevMovies.filter((movie) => movie.id !== movieId));
    } catch (error) {
      console.error("Erro ao deletar o filme:", error);
      alert("Não foi possível deletar o filme. Tente novamente.");
    }
  };
  
  const onDeleteMovieSeenClick = async (movieId) => {
    try {
      await deleteFilme(movieId);

      setMoviesSeen((prevMoviesSeen) => prevMoviesSeen.filter((movie) => movie.id !== movieId));
    } catch (error) {
      console.error("Erro ao deletar o filme assistido:", error);
      alert("Não foi possível deletar o filme assistido. Tente novamente.");
    }
  };

  const onClearAllMovies = async () => {
    try {
      await Promise.all(movies.map((movie) => deleteFilme(movie.id)));
      setMovies([]);
    } catch (error) {
      console.error("Erro ao limpar a lista de filmes:", error);
      alert("Não foi possível limpar a lista. Tente novamente.");
    }
  };
  
  const onClearAllMoviesSeen = async () => {
    try {
      await Promise.all(moviesSeen.map((movie) => deleteFilme(movie.id)));

      setMoviesSeen([]);
    } catch (error) {
      console.error("Erro ao limpar a lista de filmes assistidos:", error);
      alert("Não foi possível limpar a lista de filmes assistidos. Tente novamente.");
    }
  };

  return (
    <div className="w-screen bg-black flex flex-col">
      <NavBar />

      <div className="w-full max-w-7xl mx-auto bg-black flex justify-center p-6 flex-grow">
        <div className="w-full space-y-6">
          <div id="cinelist" className="text-center">
            <p className="text-white mb-2 font-josefin-sans text-base">Bem vinda/o! {nome}! </p>
            <p className="w-full gap-6 mx-auto text-white p-4 mt-10 mb-10 max-w-4xl font-josefin-sans leading-relaxed text-base">
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
            className="w-full flex max-w-4xl mx-auto gap-6 mt-10 mb-20 justify-center"
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
                setMoviesSeen={setMoviesSeen}
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
                      src="./assets/sapiens-avatar.png"
                      alt="Avatar Katerine"
                      className="w-32 h-32 rounded-full"
                    />
                    <Katerine />
                  </div>
                  <div className="flex items-center space-x-2 w-1/2">
                    <img
                      src="./assets/sapiens-avatar-2.png"
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