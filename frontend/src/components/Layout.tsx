import { type ReactNode, useState } from "react";
import { useAuth } from "../context/AuthContext";

interface LayoutProps {
  children: (view: string) => ReactNode;
}

export default function Layout({ children }: LayoutProps) {
  const { user, logout } = useAuth();
  const [view, setView] = useState("dashboard");

  const navItems = [
    { id: "dashboard", label: "Visão Geral", icon: "📊" },
    { id: "products", label: "Produtos", icon: "📦" },
    { id: "materials", label: "Matérias-primas", icon: "🧱" },
    { id: "inputs", label: "Insumos", icon: "⚙️" },
    { id: "formulas", label: "Fórmulas", icon: "🧪" },
    { id: "settings", label: "Configurações", icon: "⚙" },
  ];

  return (
    <div className="app-root">
      <header className="app-header">
        <div className="header-brand">
          <div className="header-logo">QS</div>
          <div className="header-title">
            <span>QuickStacker</span>
            <span>Painel Industrial</span>
          </div>
        </div>

        <div className="header-right">
          <span style={{ opacity: 0.8 }}>{user?.email}</span>
          <button className="btn-outline" onClick={logout}>Sair</button>
        </div>
      </header>

      <div className="app-layout">
        <aside className="sidebar">
          <div className="sidebar-section-title">Navegação</div>

          <nav className="sidebar-nav">
            {navItems.map((item) => (
              <button
                key={item.id}
                className={`sidebar-btn ${view === item.id ? "active" : ""}`}
                onClick={() => setView(item.id)}
              >
                <span className="sidebar-icon">{item.icon}</span>
                <span className="sidebar-text">{item.label}</span>
              </button>
            ))}
          </nav>

          <div className="sidebar-footer">
            <button className="sidebar-logout" onClick={logout}>
              <span>Sair da Conta</span>
              <span>⟶</span>
            </button>
          </div>
        </aside>

        <main className="main">
          {children(view)}
        </main>
      </div>
    </div>
  );
}
