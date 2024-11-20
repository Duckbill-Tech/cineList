// FETCH FOR CREATE A USER
export async function createUsuario(usuarioDTO) {
  try {
    const response = await fetch("/api/usuarios", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(usuarioDTO),
    });

    if (!response.ok) {
      throw new Error("Error creating usuario: " + response.statusText);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error creating usuario:", error);
    throw error;
  }
}

// FETCH FOR GET USER BY ID
export async function getUsuarioById(id) {
  try {
    const response = await fetch(`/api/usuarios/${id}`, {
      method: "GET",
    });

    if (!response.ok) {
      throw new Error("Error fetching usuario: " + response.statusText);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching usuario:", error);
    throw error;
  }
}

// FETCH FOR GET ALL USERS
export async function getAllUsuarios() {
  try {
    const response = await fetch("/api/usuarios", {
      method: "GET",
    });

    if (!response.ok) {
      throw new Error("Error fetching usuarios: " + response.statusText);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching usuarios:", error);
    throw error;
  }
}

// FETCH FOR UPDATE USER
export async function updateUsuario(id, usuarioDTO) {
  try {
    const response = await fetch(`/api/usuarios/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(usuarioDTO),
    });

    if (!response.ok) {
      throw new Error("Error updating usuario: " + response.statusText);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error updating usuario:", error);
    throw error;
  }
}

// FETCH FOR DELETE USER
export async function deleteUsuario(id) {
  try {
    const response = await fetch(`/api/usuarios/${id}`, {
      method: "DELETE",
    });

    if (!response.ok) {
      throw new Error("Error deleting usuario: " + response.statusText);
    }
  } catch (error) {
    console.error("Error deleting usuario:", error);
    throw error;
  }
}

// FETCH FOR SUCCESS MESSAGE
export async function getUserSuccess() {
  try {
    const response = await fetch("/api/usuarios/user", {
      method: "GET",
    });

    if (!response.ok) {
      throw new Error("Error fetching success message: " + response.statusText);
    }

    const data = await response.text();
    return data;
  } catch (error) {
    console.error("Error fetching success message:", error);
    throw error;
  }
}