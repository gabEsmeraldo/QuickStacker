// src/views/FormulasView.tsx
import { useEffect, useState, type FormEvent } from "react";
import { apiGet, apiPost, apiPut, apiDelete } from "../services/api";
import CrudTable from "../components/CrudTable";

interface Formula {
  id: number;
  produtoId: number;
  descricao: string;
}

export default function FormulasView() {
  const [formulas, setFormulas] = useState<Formula[]>([]);
  const [form, setForm] = useState({
    produtoId: "",
    descricao: "",
  });
  const [editingId, setEditingId] = useState<number | null>(null);

  useEffect(() => {
    async function load() {
      try {
        const data: Formula[] = await apiGet("/api/formulas");
        setFormulas(data);
      } catch (err) {
        console.error("Erro ao carregar fórmulas", err);
      }
    }
    load();
  }, []);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    const payload = {
      produtoId: Number(form.produtoId),
      descricao: form.descricao,
      // TODO: alinhar com Formula.java (campos reais)
    };

    try {
      if (editingId) {
        const updated: Formula = await apiPut(
          `/api/formulas/${editingId}`,
          payload
        );
        setFormulas((prev) =>
          prev.map((f) => (f.id === editingId ? updated : f))
        );
        setEditingId(null);
      } else {
        const created: Formula = await apiPost("/api/formulas", payload);
        setFormulas((prev) => [...prev, created]);
      }

      setForm({ produtoId: "", descricao: "" });
    } catch (err) {
      console.error("Erro ao salvar fórmula", err);
    }
  };

  const handleEdit = (row: any) => {
    setEditingId(row.id);
    setForm({
      produtoId: String(row["Produto"] ?? row.produtoId ?? ""),
      descricao: row["Descrição"] ?? row.descricao ?? "",
    });
  };

  const handleDelete = async (id: number) => {
    try {
      await apiDelete(`/api/formulas/${id}`);
      setFormulas((prev) => prev.filter((f) => f.id !== id));
    } catch (err) {
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
                <label>ID do Produto</label>
                <input
                  value={form.produtoId}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, produtoId: e.target.value }))
                  }
                  placeholder="Ex.: 1"
                  required
                />
              </div>

              <div className="form-row" style={{ gridColumn: "1 / -1" }}>
                <label>Descrição / Composição</label>
                <textarea
                  value={form.descricao}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, descricao: e.target.value }))
                  }
                  placeholder="Ex.: 50% base X, 30% essência Y, 20% aditivo Z"
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

        <div className="table-wrapper">
          {formulas.length === 0 ? (
            <div className="empty-state">Nenhuma fórmula cadastrada.</div>
          ) : (
            <CrudTable
              data={formulas.map((f) => ({
                id: f.id,
                Produto: f.produtoId,
                Descrição: f.descricao,
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
