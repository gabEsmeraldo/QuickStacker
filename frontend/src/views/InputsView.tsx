import { useEffect, useState, type FormEvent } from "react";
import { apiGet, apiPost, apiPut, apiDelete } from "../services/api";
import CrudTable from "../components/CrudTable";

interface Insumo {
  idInsumo: number;
  nome: string;
  custoUnitario: number | null;
}

export default function InputsView() {
  const [inputs, setInputs] = useState<Insumo[]>([]);
  const [form, setForm] = useState({
    nome: "",
    custoUnitario: "",
  });
  const [editingId, setEditingId] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function load() {
      setLoading(true);
      setError(null);
      try {
        const data: Insumo[] = await apiGet("/api/insumos");
        setInputs(data);
      } catch (err: any) {
        const errorMsg = err?.message || "Erro ao carregar insumos";
        setError(errorMsg);
        console.error("Erro ao carregar insumos", err);
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);

    const payload = {
      nome: form.nome,
      custoUnitario: form.custoUnitario ? parseFloat(form.custoUnitario) : null,
    };

    try {
      if (editingId) {
        const updated: Insumo = await apiPut(
          `/api/insumos/${editingId}`,
          payload
        );
        setInputs((prev) =>
          prev.map((i) => (i.idInsumo === editingId ? updated : i))
        );
        setEditingId(null);
      } else {
        const created: Insumo = await apiPost("/api/insumos", payload);
        setInputs((prev) => [...prev, created]);
      }

      setForm({ nome: "", custoUnitario: "" });
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao salvar insumo";
      setError(errorMsg);
      console.error("Erro ao salvar insumo", err);
    }
  };

  const handleEdit = (row: any) => {
    const id = row.id ?? row.idInsumo;
    setEditingId(id);
    setForm({
      nome: row.Nome ?? row.nome ?? "",
      custoUnitario: row.custoUnitario ? String(row.custoUnitario) : "",
    });
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Tem certeza que deseja excluir este insumo?")) return;
    
    setError(null);
    try {
      await apiDelete(`/api/insumos/${id}`);
      setInputs((prev) => prev.filter((i) => i.idInsumo !== id));
    } catch (err: any) {
      const errorMsg = err?.message || "Erro ao excluir insumo";
      setError(errorMsg);
      console.error("Erro ao excluir insumo", err);
    }
  };

  return (
    <>
      <header className="view-header">
        <div className="view-header-title">
          <h1>Insumos</h1>
          <p>Itens auxiliares utilizados nos processos produtivos.</p>
        </div>
      </header>

      <div className="page-grid">
        <section className="card">
          <div className="card-header">
            <h2>{editingId ? "Editar insumo" : "Novo insumo"}</h2>
            <span className="card-subtitle">
              Cadastre itens complementares da linha produtiva.
            </span>
          </div>

          <form onSubmit={handleSubmit}>
            <div className="form-grid">
              <div className="form-row">
                <label>Nome do insumo</label>
                <input
                  value={form.nome}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, nome: e.target.value }))
                  }
                  placeholder="Ex.: Detergente industrial"
                  required
                />
              </div>

              <div className="form-row">
                <label>Custo Unitário</label>
                <input
                  type="number"
                  step="0.0001"
                  min={0}
                  value={form.custoUnitario}
                  onChange={(e) =>
                    setForm((f) => ({ ...f, custoUnitario: e.target.value }))
                  }
                  placeholder="Ex.: 2.50"
                />
              </div>
            </div>

            <div className="form-actions">
              <button className="btn btn-primary">
                {editingId ? "Salvar alterações" : "Cadastrar insumo"}
              </button>
            </div>
          </form>
        </section>

        <section className="card">
          <div className="card-header">
            <h3>Resumo rápido</h3>
          </div>

          <div className="stats-row" style={{ gridTemplateColumns: "1fr" }}>
            <div className="stat-card">
              <div className="stat-label">Total de insumos</div>
              <div className="stat-value">{inputs.length}</div>
            </div>
          </div>

        </section>
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

      <section className="card" style={{ marginTop: "1rem" }}>
        <div className="card-header">
          <h2>Lista de insumos</h2>
        </div>

        <div className="table-wrapper">
          {loading ? (
            <div className="empty-state">Carregando...</div>
          ) : inputs.length === 0 ? (
            <div className="empty-state">Nenhum item cadastrado.</div>
          ) : (
            <CrudTable
              data={inputs.map((i) => ({
                id: i.idInsumo,
                idInsumo: i.idInsumo,
                Nome: i.nome,
                "Custo Unitário": i.custoUnitario ?? "-",
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
