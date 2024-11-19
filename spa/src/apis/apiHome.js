const API_URL = "http://localhost:5173/home";

export const fetchMovies = async () => {
  const response = await fetch(API_URL);
  const data = await response.json();
  return data;
};

export const addMovie = async (newMovie) => {
  const response = await fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(newMovie),
  });
  return await response.json();
};

export const deleteMovie = async (movieId) => {
  await fetch(`${API_URL}/${movieId}`, {
    method: "DELETE",
  });
};
