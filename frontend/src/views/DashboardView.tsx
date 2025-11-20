export default function DashboardView() {
  return (
    <>
      <header className="view-header">
        <div className="view-header-title">
          <h1>Visão Geral da Operação</h1>
          <p>Status atual de estoque, produção e consumo.</p>
        </div>
      </header>

      <section className="dashboard-grid">
        <div className="dashboard-card">
          <span className="dashboard-card-title">Total em estoque</span>
          <span className="dashboard-card-value">12.480 un</span>
          <div className="dashboard-status-row">
            <div className="dashboard-status">+4.1% último mês</div>
          </div>
        </div>

        <div className="dashboard-card">
          <span className="dashboard-card-title">Materiais críticos</span>
          <span className="dashboard-card-value">5 itens</span>
          <div className="dashboard-status-row">
            <div className="dashboard-status" style={{ color: "#ff4b4b" }}>
              ação imediata
            </div>
          </div>
        </div>

        <div className="dashboard-card">
          <span className="dashboard-card-title">Produção ativa</span>
          <span className="dashboard-card-value">3 lotes</span>
          <div className="dashboard-status-row">
            <div className="dashboard-status">lote #2025-11</div>
          </div>
        </div>
      </section>

      <section className="page-grid">
        <div className="chart-box">Gráfico: Fluxo de produção (em breve)</div>
        <div className="chart-box">Gráfico: Consumo de matéria-prima</div>
      </section>
    </>
  );
}
