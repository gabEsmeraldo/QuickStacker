// src/services/api.ts
const API_BASE_URL = "https://quickstacker-production.up.railway.app";

async function handleResponse(res: Response) {
  if (!res.ok) {
    let msg = "Erro inesperado da API";

    try {
      const data = await res.json();
      msg = data.message || data.error || msg;
    } catch (e) {
      // sem body
    }

    throw new Error(msg);
  }

  if (res.status === 204) return null;

  return res.json();
}

export async function apiGet(path: string) {
  const res = await fetch(`${API_BASE_URL}${path}`, {
    // credentials: "include" - Uncomment when implementing authentication
    // Note: Backend must use specific origins (not "*") when credentials are enabled
  });
  return handleResponse(res);
}

export async function apiPost(path: string, body: any) {
  const res = await fetch(`${API_BASE_URL}${path}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
    // credentials: "include" - Uncomment when implementing authentication
  });
  return handleResponse(res);
}

export async function apiPut(path: string, body: any) {
  const res = await fetch(`${API_BASE_URL}${path}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
    // credentials: "include" - Uncomment when implementing authentication
  });
  return handleResponse(res);
}

export async function apiDelete(path: string) {
  const res = await fetch(`${API_BASE_URL}${path}`, {
    method: "DELETE",
    // credentials: "include" - Uncomment when implementing authentication
  });
  return handleResponse(res);
}
