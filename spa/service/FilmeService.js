// FETCH FOR GET ALL FILMES
export async function getAllFilmes() {
  try {
    const response = await fetch(`http://localhost:8081/api/filmes`, {
      method: "GET",
      credentials: "include",
    });

    if (!response.ok) {
      throw new Error("Error fetching filmes: " + response.statusText);
    }

    const data = await response.json(); 
    return data;
  } catch (error) {
    console.error("Error fetching filmes:", error);
    throw error;
  }
}

// FETCH FOR CREATE A FILME
export async function createFilme(filmeDTO, usuarioId) {
  try {
    const response = await fetch(`http://localhost:8081/api/filmes?usuarioId=${usuarioId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(filmeDTO),
      credentials: "include", 
    });

    if (!response.ok) {
      throw new Error("Error creating filme: " + response.statusText);
    }

    const data = await response.json();
    console.debug("Server response:", data);
    return data;
  } catch (error) {
    console.error("Error creating filme:", error);
    throw error;
  }
}

// FETCH FOR GET FILME BY ID
export async function getFilmeById(id) {
  try {
    const response = await fetch(`http://localhost:8081/api/filmes/${id}`, {
      method: "GET",
      credentials: "include", // Adicionando credenciais (cookies)
    });

    if (!response.ok) {
      throw new Error("Error fetching filme: " + response.statusText);
    }

    const data = await response.json(); // Convert the response to JSON
    return data;
  } catch (error) {
    console.error("Error fetching filme:", error);
    throw error;
  }
}

// FETCH FOR UPDATE FILME
export async function updateFilme(id, filmeDTO) {
  try {
    const response = await fetch(`http://localhost:8081/api/filmes/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(filmeDTO),
      credentials: "include", // Adicionando credenciais (cookies)
    });

    if (!response.ok) {
      throw new Error("Error updating filme: " + response.statusText);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error updating filme:", error);
    throw error;
  }
}


// FETCH FOR DELETE FILME
export async function deleteFilme(id) {
  try {
    const response = await fetch(`http://localhost:8081/api/filmes/${id}`, {
      method: "DELETE",
      credentials: "include", // Adicionando credenciais (cookies)
    });

    if (!response.ok) {
      throw new Error("Error deleting filme: " + response.statusText);
    }
  } catch (error) {
    console.error("Error deleting filme:", error);
    throw error;
  }
}