const BASE_URL = "http://localhost:5173/registro"; // URL base da sua API

/**
 * Registra um novo usuário na aplicação.
 * @param {string} CPF - CPF do usuário.
 * @param {string} email - Email do usuário.
 * @param {string} senha - Senha do usuário.
 * @returns {Promise<object>} - Dados retornados pela API após o registro.
 */
export async function registerUser(CPF, email, senha) {
  try {
    const response = await fetch(`${BASE_URL}/registro`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ CPF, email, senha }),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || "Erro ao registrar usuário.");
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Erro ao registrar usuário:", error);
    throw error;
  }
}
