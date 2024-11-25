// FETCH PARA CREATE A USER
export async function createUsuario(usuarioDTO) {
  try {
    const response = await fetch("/api/usuarios", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(usuarioDTO),
      credentials: "include", // Adicionando credenciais (cookies)
    });

    if (!response.ok) {
      throw new Error("Erro em criando usuario: " + response.statusText);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Erro em criando usuario:", error);
    throw error;
  }
}

// FETCH FOR GET USER BY ID
export async function getUsuarioById(id) {
  try {
    const response = await fetch(`/api/usuarios/${id}`, {
      method: "GET",
      credentials: "include",
    });

    if (!response.ok) {
      throw new Error("Erro pegar usuario por id: " + response.statusText);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Erro pegar usuario por id:", error);
    throw error;
  }
}

// FETCH FOR GET ALL USERS
export async function getAllUsuarios() {
  try {
    const response = await fetch(`/api/usuarios`, {
      method: "GET",
      credentials: "include",
    });

    if (!response.ok) {
      throw new Error("Erro em pegar todos usuarios: " + response.statusText);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Erro em pegar todos usuarios:", error);
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
      credentials: "include",
    });

    if (!response.ok) {
      throw new Error("Erro em update usuario:" + response.statusText);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Erro em update usuario:", error);
    throw error;
  }
}

// FETCH FOR DELETE USER
export async function deleteUsuario(id) {
  try {
    const response = await fetch(`/api/usuarios/${id}`, {
      method: "DELETE",
      credentials: "include",
    });

    if (!response.ok) {
      throw new Error("Erro em delete usuario: " + response.statusText);
    }

  } catch (error) {
    console.error("Erro em delete usuario:", error);
    throw error;
  }
}

// FETCH FOR SUCCESS MESSAGE
export async function getUserSuccess() {
  try {
    const response = await fetch("/api/usuarios/user", {
      method: "GET",
      credentials: "include"
    });

    if (!response.ok) {
      throw new Error("Erro em verificar token usuario:" + response.statusText);
    }

    const data = await response.text();
    return data;
  } catch (error) {
    console.error("Erro em verificar token usuario:", error);
    throw error;
  }
}
