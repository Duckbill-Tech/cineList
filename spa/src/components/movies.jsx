import { TrashIcon } from "lucide-react";
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
      console.log("Atualizar o filme com sucesso:", updatedMovie);
  
      onMovieClick(movieId);
    } catch (error) {
      console.error("Erro ao marcar um filme como assistido:", error);
      alert("Erro ao marcar um filme como assistido. Por favor tente de novo.");
    }
  };

  return (
    <div
      className="border border-amber-500 rounded-md p-4"
      role="region"
      aria-label="Filmes para assistir"
    >
      <h1
        className="text-white text-xs text-left mb-2 font-josefin-sans tracking-widest"
        aria-live="polite"
      >
        PARA ASSISTIR
      </h1>
      <ul className="space-y-4 bg-amber-500 p-2 rounded-md font-josefin-slab text-s">
        {movies.map((movie) => (
          <li key={movie.id} className="flex gap-2">
            <button
              className="w-full text-left flex text-black bg-white p-2 rounded-md"
              onClick={() => onCompleteMovieClick(movie.id)}
              aria-label={`Marcar "${movie.titulo}" como assistido`}
            >
              {movie.titulo}
            </button>
            <button
              onClick={() => onDeleteMovieClick(movie.id)}
              className="text-black bg-white p-2 rounded-md"
              aria-label={`Remover "${movie.titulo}" da lista`}
            >
              <TrashIcon aria-hidden="true" />
            </button>
          </li>
        ))}
      </ul>
      <button
        onClick={onClearAllMovies}
        className="hover:text-black text-xs text-amber-500 py-2 font-josefin-sans"
        aria-label="Limpar toda a lista de filmes para assistir"
      >
        Limpar Tudo
      </button>
    </div>
  );
}

export default Movies;