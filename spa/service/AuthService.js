// FETCH PARA LOGIN
export async function login(email, senha) {
  try {
    const response = await fetch("/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, senha }),
      credentials: "include", // Adicionando credenciais (cookies)
    });

    if (!response.ok) {
      throw new Error("Erro ao fazer login: " + response.statusText);
    }

    const data = await response.json();
    return data;

  } catch (error) {
    console.error("Erro ao fazer login:", error);
  }
}

// FETCH PARA REGISTER
export async function register(nome, email, cpf, senha) {
  try {
    const response = await fetch("/api/auth/register", { 
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ nome, email, cpf, senha }),
      credentials: "include",
    });

    if (!response.ok) {
      throw new Error("Erro durante o registro: " + response.statusText);
    }

    const data = await response.json();
    return data;
    
  } catch (error) {
    console.error("Erro ao fazer login:", error);
  }
}


// FETCH PARA RESET PASSWORD
export async function resetPassword(newPassword) {
  try {
    const response = await fetch(`/api/auth/reset-password`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ newPassword }),
      credentials: "include",
    });

    if (!response.ok) {
      throw new Error("Erro ao redefinir senha: " + response.statusText);
    }

    const data = await response.json();
    return data;
    
  } catch (error) {
    console.error("Erro ao redefinir senha:", error);
    throw error;
  }
}