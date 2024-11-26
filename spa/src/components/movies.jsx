import { TrashIcon, CheckCircleIcon } from "lucide-react";
import { updateFilme } from "../../service/FilmeService";

function Movies({
  movies,
  onMovieClick,
  onDeleteMovieClick,
  onClearAllMovies,
}) {
  const onCompleteMovieClick = async (movieId) => {
    try {
      const updates = {
        completedAt: new Date().toISOString(),
        titulo: movies.find((movie) => movie.id === movieId)?.titulo,
      };

      const updatedMovie = await updateFilme(movieId, updates);
      console.log("Filme atualizado com sucesso:", updatedMovie);

      onMovieClick(movieId);
    } catch (error) {
      console.error("Erro ao marcar um filme como assistido:", error);
      alert("Erro ao marcar um filme como assistido. Por favor tente de novo.");
    }
  };

  return (
    <div className="relative flex flex-col rounded-lg bg-amber-400 shadow-lg border border-black p-4">
      <h1
        className="text-white text-xs text-left mb-2 font-josefin-sans tracking-widest"
        aria-live="polite"
      >
      PARA ASSISTIR
      </h1>
      <nav className="flex flex-col gap-2">
        {movies.map((movie) => (
          <div
            key={movie.id}
            className="flex items-center w-full rounded-md p-2 pl-3 bg-white bg-opacity-90 hover:bg-gray-300 hover:bg-opacity-30 transition-all"
          >
            <span className="flex-grow text-black">{movie.titulo}</span>
            <button
              onClick={() => onCompleteMovieClick(movie.id)}
              className="rounded-md border border-transparent p-2 text-green-600 hover:bg-green-100 focus:bg-green-100 active:bg-green-200"
              aria-label={`Marcar "${movie.titulo}" como assistido`}
            >
              <CheckCircleIcon className="w-5 h-5" aria-hidden="true" />
            </button>
            <button
              onClick={(e) => {
                e.stopPropagation();
                onDeleteMovieClick(movie.id);
              }}
              className="rounded-md border border-transparent p-2 border-amber-500 text-amber-500 hover:bg-red-100 focus:bg-red-100 active:bg-red-200"
              aria-label={`Remover "${movie.titulo}" da lista`}
            >
              <TrashIcon className="w-5 h-5" aria-hidden="true" />
            </button>
          </div>
        ))}
      </nav>
      <button
        onClick={onClearAllMovies}
        className="mt-4 text-white hover:text-white text-sm underline"
        aria-label="Limpar toda a lista de filmes para assistir"
      >
        Limpar tudo
      </button>
    </div>
  );
}

export default Movies;