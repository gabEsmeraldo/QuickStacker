// src/views/FormulasView.tsx
import { useEffect, useState, type FormEvent } from "react";
import { apiGet, apiPost, apiPut, apiDelete } from "../services/api";
import CrudTable from "../components/CrudTable";

interface Formula {
  idFormula: number;
  descricaoModoPreparo: string | null;
  produto: {
    idProduto: number;
    nome: string;
  };
}

export default function FormulasView() {
  const [formulas, setFormulas] = useState<Formula[]>([]);
  const [produtos, setProdutos] = useState<Array<{ idProduto: number; nome: string }>>([]);
  const [form, setForm] = useState({
    produtoId: "",
    descricaoModoPreparo: "",
  });
  const [editingId, setEditingId] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function load() {
      setLoading(true);
      setError(null);
      try {
        const [formulasData, produtosData] = await Promise.all([
          apiGet("/api/formulas"),
          apiGet("/api/produtos")
        ]);
        setFormulas(formulasData);
        setProdutos(produtosData);
      } catch (err: any) {
        const errorMsg = err?.message || "Erro ao carregar fórmulas";
        setError(errorMsg);
        console.error("Erro ao carregar fórmulas", err);
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!form.produtoId) {
      setError("Selecione um produto");
      return;
    }

    const payload = {
      descricaoModoPreparo: form.descricaoModoPreparo,
      produto: {
        idProduto: parseInt(form.produtoId)
      }
    };

    try {
      if (editingId) {
        const updated: Formula = await apiPut(
          `/api/formulas/${editingId}`,
          payload
        );
        setFormulas((prev) =>
          prev.map((f) => (f.idFormula === editingId ? updated : f))
        );
        setEditingId(null);
      } else {
        const created: Formula = await apiPost("/api/formulas", payload);
        setFormulas((prev) => [...prev, created]);
      }

      setForm({ produtoId: "", descricaoModoPreparo: "" });
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao salvar fórmula";
      setError(errorMsg);
      console.error("Erro ao salvar fórmula", err);
    }
  };

  const handleEdit = (row: any) => {
    const id = row.id ?? row.idFormula;
    setEditingId(id);
    setForm({
      produtoId: row.produto?.idProduto ? String(row.produto.idProduto) : "",
      descricaoModoPreparo: row["Descrição"] ?? row.descricaoModoPreparo ?? "",
    });
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Tem certeza que deseja excluir esta fórmula?")) return;
    
    setError(null);
    try {
      await apiDelete(`/api/formulas/${id}`);
      setFormulas((prev) => prev.filter((f) => f.idFormula !== id));
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao excluir fórmula";
      setError(errorMsg);
      console.error("Erro ao excluir fórmula", err);
    }
  };

  return (
    <>
      <header className="view-header">
        <div className="view-header-title">
          <h1>Fórmulas</h1>
          <p>Registre composições e vínculos com produtos.</p>
        </div>
      </header>

      <div className="page-grid">
        <section className="card">
          <div className="card-header">
            <h2>{editingId ? "Editar fórmula" : "Nova fórmula"}</h2>
            <span className="card-subtitle">
              Associe a fórmula a um produto e descreva a composição.
            </span>
          </div>

          <form onSubmit={handleSubmit}>
            <div className="form-grid">
              <div className="form-row">
                <label>Produto</label>
                <select
                  value={form.produtoId}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, produtoId: e.target.value }))
                  }
                  required
                >
                  <option value="">Selecione um produto</option>
                  {produtos.map((p) => (
                    <option key={p.idProduto} value={p.idProduto}>
                      {p.nome}
                    </option>
                  ))}
                </select>
              </div>

              <div className="form-row" style={{ gridColumn: "1 / -1" }}>
                <label>Descrição do Modo de Preparo</label>
                <textarea
                  value={form.descricaoModoPreparo}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, descricaoModoPreparo: e.target.value }))
                  }
                  placeholder="Ex.: 50% oleo de girassol, 50% agua, 1 frasco 60ml, 1 tampa conta gotas"
                  style={{
                    background: "rgba(15,15,20,0.85)",
                    borderRadius: "10px",
                    border: "1px solid rgba(255,255,255,0.07)",
                    padding: "0.8rem",
                    minHeight: "120px",
                    color: "#fff",
                  }}
                />
              </div>
            </div>

            <div className="form-actions">
              <button className="btn btn-primary">
                {editingId ? "Salvar alterações" : "Cadastrar fórmula"}
              </button>
            </div>
          </form>
        </section>

        <section className="card">
          <div className="card-header">
            <h3>Resumo</h3>
          </div>
          <div className="stats-row" style={{ gridTemplateColumns: "1fr" }}>
            <div className="stat-card">
              <div className="stat-label">Total de fórmulas</div>
              <div className="stat-value">{formulas.length}</div>
            </div>
          </div>
        </section>
      </div>

      <section className="card" style={{ marginTop: "1rem" }}>
        <div className="card-header">
          <h2>Fórmulas cadastradas</h2>
        </div>

        {error && (
          <div className="error-message" style={{ 
            padding: "1rem", 
            margin: "1rem 0", 
            background: "#fee", 
            border: "1px solid #fcc", 
            borderRadius: "4px",
            color: "#c33"
          }}>
            {error}
          </div>
        )}

        <div className="table-wrapper">
          {loading ? (
            <div className="empty-state">Carregando...</div>
          ) : formulas.length === 0 ? (
            <div className="empty-state">Nenhuma fórmula cadastrada.</div>
          ) : (
            <CrudTable
              data={formulas.map((f) => ({
                id: f.idFormula,
                idFormula: f.idFormula,
                Produto: f.produto?.nome ?? "-",
                "Descrição do Modo de Preparo": f.descricaoModoPreparo ?? "-",
              }))}
              onEdit={handleEdit}
              onDelete={handleDelete}
            />
          )}
        </div>
      </section>
    </>
  );
}
