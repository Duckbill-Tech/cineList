const BASE_URL = "http://localhost:5173/login";

/**
 * Realiza a autenticação de um usuário.
 * @param {string} email - Email do usuário.
 * @param {string} senha - Senha do usuário.
 * @returns {Promise<object>} - Resposta do servidor contendo os dados do usuário ou um erro.
 */
export async function loginUser(email, senha) {
  try {
    const response = await fetch(`${BASE_URL}/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, senha }),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || "Erro ao realizar login.");
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Erro ao realizar login:", error);
    throw error;
  }
}
