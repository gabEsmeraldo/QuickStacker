import { useState } from "react";
import { useAuth } from "../context/AuthContext";

export default function LoginPage() {
  const { login, testModeEnabled, setTestModeEnabled } = useAuth();
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [loading, setLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState("");

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setErrorMsg("");

    try {
      await login(email, senha);
    } catch (err: any) {
      setErrorMsg("Credenciais inválidas. Tente novamente.");
    }

    setLoading(false);
  };

  return (
    <div className="login-root">
      <div className="login-card">
        <div className="login-header">
          <div className="login-logo">QS</div>
          <h1>QuickStacker</h1>
          <p>Controle industrial com precisão e segurança</p>
        </div>

        <form onSubmit={handleLogin} className="login-form">
          {errorMsg && <div className="login-error">{errorMsg}</div>}

          {/* Test Mode Toggle */}
          <div
            style={{
              display: "flex",
              alignItems: "center",
              justifyContent: "space-between",
              padding: "0.75rem",
              marginBottom: "1rem",
              background: "rgba(255,255,255,0.05)",
              borderRadius: "8px",
              border: "1px solid rgba(255,255,255,0.1)",
            }}
          >
            <label
              style={{
                margin: 0,
                cursor: "pointer",
                display: "flex",
                alignItems: "center",
                gap: "0.5rem",
              }}
            >
              <input
                type="checkbox"
                checked={testModeEnabled}
                onChange={(e) => setTestModeEnabled(e.target.checked)}
                style={{ cursor: "pointer" }}
              />
              <span
                style={{ fontSize: "0.9rem", color: "rgba(255,255,255,0.8)" }}
              >
                Modo de Teste (pula login)
              </span>
            </label>
          </div>

          <label>Email</label>
          <input
            type="email"
            placeholder="seuemail@empresa.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required={!testModeEnabled}
            disabled={testModeEnabled}
          />

          <label>Senha</label>
          <input
            type="password"
            placeholder="•••••••••"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            required={!testModeEnabled}
            disabled={testModeEnabled}
          />

          <button
            type="submit"
            className="btn-primary"
            disabled={loading || testModeEnabled}
          >
            {testModeEnabled
              ? "Modo de Teste Ativo"
              : loading
              ? "Entrando..."
              : "Acessar Painel"}
          </button>
        </form>

        <footer className="login-footer">
          <span>© {new Date().getFullYear()} QuickStacker</span>
        </footer>
      </div>
    </div>
  );
}
