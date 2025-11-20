import { useState, type FormEvent } from "react";
import { useAuth } from "../context/AuthContext";

export default function AuthPage() {
  const [mode, setMode] = useState<"login" | "register">("login");
  const { login, register } = useAuth();

  const submitLogin = (e: FormEvent) => {
    e.preventDefault();
    const form = e.currentTarget as HTMLFormElement;
    login(
      (form.email as unknown as HTMLInputElement).value,
      (form.password as unknown as HTMLInputElement).value
    );
  };

  const submitRegister = (e: FormEvent) => {
    e.preventDefault();
    const form = e.currentTarget as HTMLFormElement;
    register(
      (form.name as unknown as HTMLInputElement).value,
      (form.email as unknown as HTMLInputElement).value,
      (form.password as unknown as HTMLInputElement).value
    );
  };

  return (
    <div className="login-wrapper">
      <aside className="login-left">
        <div className="login-left-top">
          <div className="header-brand">
            <div className="header-logo">QS</div>
            <div className="header-title">
              <span>QuickStacker</span>
              <span>Gestão industrial avançada</span>
            </div>
          </div>

          <p className="login-tag">GESTÃO • PRODUÇÃO • ESTOQUE</p>

          <h1 className="login-title">
            Controle total da sua operação industrial em um único painel.
          </h1>

          <p className="login-subtitle">
            Agilidade, disciplina e rastreabilidade para empresas que precisam
            de precisão absoluta em estoque e produção.
          </p>

          <div className="login-stats">
            <div className="login-stat-card">
              <div className="login-stat-value">99.97%</div>
              <div className="login-stat-label">Disponibilidade</div>
            </div>
            <div className="login-stat-card">
              <div className="login-stat-value">+43%</div>
              <div className="login-stat-label">Eficiência operacional</div>
            </div>
            <div className="login-stat-card">
              <div className="login-stat-value">24/7</div>
              <div className="login-stat-label">Monitoramento ativo</div>
            </div>
          </div>

          <div className="login-trust">
            Confiado por equipes de produção, PCP, logística e controle de
            qualidade.
          </div>
        </div>

        <div className="login-footer">
          Suporte • Política de Privacidade • Termos de Uso
        </div>
      </aside>

      <section className="login-right">
        <div className="login-card">
          <div className="login-toggle">
            <button
              className={mode === "login" ? "active" : ""}
              onClick={() => setMode("login")}
            >
              Entrar
            </button>
            <button
              className={mode === "register" ? "active" : ""}
              onClick={() => setMode("register")}
            >
              Criar conta
            </button>
          </div>

          {mode === "login" ? (
            <>
              <h1>Acessar plataforma</h1>
              <p>Digite suas credenciais corporativas.</p>

              <form onSubmit={submitLogin}>
                <div className="login-row">
                  <label htmlFor="email">E-mail</label>
                  <input id="email" name="email" type="email" required />
                </div>

                <div className="login-row">
                  <label htmlFor="password">Senha</label>
                  <input
                    id="password"
                    name="password"
                    type="password"
                    required
                  />
                </div>

                <div className="login-submit">
                  <button className="btn btn-primary">Entrar</button>
                </div>
              </form>

              <div className="login-cta">
                Não possui conta?
                <button onClick={() => setMode("register")}>Criar agora</button>
              </div>
            </>
          ) : (
            <>
              <h1>Criar conta</h1>
              <p>Preencha seus dados iniciais.</p>

              <form onSubmit={submitRegister}>
                <div className="login-row">
                  <label htmlFor="name">Nome completo</label>
                  <input id="name" name="name" required />
                </div>

                <div className="login-row">
                  <label htmlFor="email">E-mail</label>
                  <input id="email" name="email" type="email" required />
                </div>

                <div className="login-row">
                  <label htmlFor="password">Senha</label>
                  <input
                    id="password"
                    name="password"
                    type="password"
                    required
                  />
                </div>

                <div className="login-submit">
                  <button className="btn btn-primary">Criar acesso</button>
                </div>
              </form>

              <div className="login-cta">
                Já é cadastrado?
                <button onClick={() => setMode("login")}>Entrar</button>
              </div>
            </>
          )}
        </div>
      </section>
    </div>
  );
}
