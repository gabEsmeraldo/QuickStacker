import { useState } from "react";
import { useAuth } from "../context/AuthContext";

export default function LoginPage() {
  const { login } = useAuth(); // login(email, senha)
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

          <label>Email</label>
          <input
            type="email"
            placeholder="seuemail@empresa.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <label>Senha</label>
          <input
            type="password"
            placeholder="•••••••••"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            required
          />

          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? "Entrando..." : "Acessar Painel"}
          </button>
        </form>

        <footer className="login-footer">
          <span>© {new Date().getFullYear()} QuickStacker</span>
        </footer>
      </div>
    </div>
  );
}
